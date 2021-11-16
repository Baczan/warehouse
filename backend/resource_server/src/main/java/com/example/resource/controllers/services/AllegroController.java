package com.example.resource.controllers.services;

import com.example.resource.dto.AllegroOfferDto;
import com.example.resource.dto.AllegroResponseDescriptionDTO;
import com.example.resource.dto.allegroEvents.AllegroOfferEvent;
import com.example.resource.dto.allegroEvents.AllegroOfferEventsResponse;
import com.example.resource.entities.Connection;
import com.example.resource.entities.Label;
import com.example.resource.entities.LastEvent;
import com.example.resource.formatters.AllegroEventFormatter;
import com.example.resource.helpers.ConnectionHelper;
import com.example.resource.jwt.JwtUser;
import com.example.resource.repositories.ConnectionRepository;
import com.example.resource.repositories.LabelRepository;
import com.example.resource.repositories.LastEventRepository;
import com.example.resource.responses.AllegroOfferResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.*;

@RestController
@RequestMapping("allegro")

public class AllegroController {

    @Autowired
    private Environment env;

    @Autowired
    private ConnectionHelper connectionHelper;

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private LastEventRepository lastEventRepository;

    @Autowired
    private LabelRepository labelRepository;

    private WebClient allegroWebClient;

    private List<String> alreadyChecked = new ArrayList<>();

    private String accessToken;

    @PostConstruct
    private void setUpWebClients() {

        allegroWebClient = WebClient.builder()
                .baseUrl(env.getProperty("app.allegro.api.url"))
                .build();

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/offers")
    public ResponseEntity<?> getAllOffers(Authentication authentication) {

        JwtUser user = (JwtUser) authentication.getPrincipal();

        if (!connectionRepository.existsByService("allegro")) {
            return new ResponseEntity<>("allegro-not-connected", HttpStatus.BAD_REQUEST);
        }

        try {

            Connection connection = connectionRepository.getByService("allegro");

            Mono<String> response = allegroWebClient.get()
                    .uri("/sale/offers")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + connection.getAccessToken())
                    .header(HttpHeaders.ACCEPT, "application/vnd.allegro.public.v1+json")
                    .retrieve()
                    .bodyToMono(String.class);

            return new ResponseEntity<>(response.block(), HttpStatus.OK);
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {

                return new ResponseEntity<>("allegro_unauthorized", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/offer")
    public ResponseEntity<?> getOffer(@RequestParam String offerId, Authentication authentication) throws JsonProcessingException {


        if (!connectionRepository.existsByService("allegro")) {
            return new ResponseEntity<>("allegro-not-connected", HttpStatus.BAD_REQUEST);
        }

        try {

            Connection connection = connectionRepository.getByService("allegro");

            Mono<String> response = allegroWebClient.get()
                    .uri("/sale/offers/" + offerId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + connection.getAccessToken())
                    .header(HttpHeaders.ACCEPT, "application/vnd.allegro.public.v1+json")
                    .retrieve()
                    .bodyToMono(String.class);


            ObjectMapper mapper = new ObjectMapper();
            AllegroOfferDto allegroOfferDto = mapper.readValue(response.block(), AllegroOfferDto.class);

            return new ResponseEntity<>(new AllegroOfferResponse(allegroOfferDto), HttpStatus.OK);
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                return new ResponseEntity<>("allegro_unauthorized", HttpStatus.BAD_REQUEST);
            }
            if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
                return new ResponseEntity<>("allegro_forbidden", HttpStatus.BAD_REQUEST);
            }
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return new ResponseEntity<>("allegro_not_found", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    private void setDescription(String id) {

        if (!connectionRepository.existsByService("allegro")) {
            return;
        }

        if(alreadyChecked.contains(id)){
            return;
        }

        alreadyChecked.add(id);

        try {

            Connection connection = connectionRepository.getByService("allegro");

            //Get offer
            Mono<String> response = allegroWebClient.get()
                    .uri("/sale/offers/" + id)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + connection.getAccessToken())
                    .header(HttpHeaders.ACCEPT, "application/vnd.allegro.public.v1+json")
                    .retrieve()
                    .bodyToMono(String.class);



            //Map response to object
            ObjectMapper mapper = new ObjectMapper();
            AllegroResponseDescriptionDTO allegroOfferDto = mapper.readValue(response.block(), AllegroResponseDescriptionDTO.class);

            AtomicBoolean changed = new AtomicBoolean(false);


            allegroOfferDto.getDescription().getSections().forEach(sectionDTO -> {

                sectionDTO.getItems().forEach(itemDTO -> {


                    //Get text items from each section
                    if (itemDTO.getType().equals("TEXT")) {

                        //Parse text items to html and select all the b tags
                        Document document = Jsoup.parse(itemDTO.getContent(), "", Parser.xmlParser());
                        Elements elements = document.select("b");

                        elements.forEach(element -> {

                            //Split b tag to get id and check if it has already assigned warehouse number
                            String text = element.text().split("-")[0];

                            if (text.startsWith("#") && element.text().split("-").length == 1) {


                                //Get id of a label but without the hashtag
                                int labelId = Integer.parseInt(text.substring(1).trim());

                                Optional<Label> labelOptional = labelRepository.findById(labelId);

                                if (labelOptional.isPresent()) {

                                    //If label is found assign to it warehouse number
                                    Label label = labelOptional.get();

                                    if (label.getWarehouseNumberScanned().trim().equals("")) {
                                        element.text(text.trim() + " - " + label.getWarehouseNumber().trim());
                                    } else {
                                        element.text(text.trim() + " - " + label.getWarehouseNumberScanned().trim());
                                    }

                                    changed.set(true);
                                }

                            }
                        });

                        //Update text item
                        itemDTO.setContent(document.html());
                    }


                });

            });


            if(changed.get()){
                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                String json = ow.writeValueAsString(allegroOfferDto);

                connection = connectionRepository.getByService("allegro");

                Mono<String> responsePatch = allegroWebClient.patch()
                        .uri("/sale/product-offers/" + id)
                        .bodyValue(json)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + connection.getAccessToken())
                        .header(HttpHeaders.ACCEPT, "application/vnd.allegro.beta.v3+json")
                        .header(HttpHeaders.CONTENT_TYPE, "application/vnd.allegro.beta.v3+json")
                        .retrieve()
                        .bodyToMono(String.class);

                responsePatch.block();
            }



        } catch (Exception ignored){
        }

    }



    @Scheduled(fixedDelay = 900000)
    public ResponseEntity<?> sync() throws IOException {


        Logger logger = Logger.getLogger("Allegro event logger");
        FileHandler fh;
        logger.setUseParentHandlers(false);


        fh = new FileHandler(env.getProperty("app.log"), true);
        logger.addHandler(fh);
        fh.setFormatter(new AllegroEventFormatter());


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        ResponseEntity<?> responseEntity = this.connectionHelper.refreshAllegro();


        if (responseEntity.getStatusCode() == HttpStatus.OK) {

            Connection connection = connectionRepository.getByService("allegro");
            accessToken=connection.getAccessToken();

            List<AllegroOfferEvent> events = getNewEvents().getOfferEvents();

            while (events.size() > 0) {
                events.forEach(event -> {

                    String log = String.format("%s - %s - %s", event.getOffer().getId(), event.getType(), format.format(event.getOccurredAt()));
                    logger.info(log);

                    if (event.getType().equals("OFFER_ACTIVATED")) {

                        setDescription(event.getOffer().getId());

                    }

                });
                events = getNewEvents().getOfferEvents();


            }

        }
        fh.close();
        alreadyChecked.clear();
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    private AllegroOfferEventsResponse getNewEvents() throws JsonProcessingException {

        try {
            Connection connection = connectionRepository.getByService("allegro");

            Mono<String> response;
            int limit = 100;
            List<LastEvent> lastEventList = lastEventRepository.findAll();

            if (lastEventList.size() > 0) {

                LastEvent lastEvent = lastEventList.get(0);
                response = allegroWebClient.get()
                        .uri("/sale/offer-events?limit=" + limit + "&from=" + lastEvent.getEventId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + connection.getAccessToken())
                        .header(HttpHeaders.ACCEPT, "application/vnd.allegro.public.v1+json")
                        .retrieve()
                        .bodyToMono(String.class);

            } else {

                response = allegroWebClient.get()
                        .uri("/sale/offer-events?limit=" + limit)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + connection.getAccessToken())
                        .header(HttpHeaders.ACCEPT, "application/vnd.allegro.public.v1+json")
                        .retrieve()
                        .bodyToMono(String.class);
            }


            if (response.block() != null) {

                ObjectMapper mapper = new ObjectMapper();
                AllegroOfferEventsResponse allegroOfferEventsResponse = mapper.readValue(response.block(), AllegroOfferEventsResponse.class);

                if (allegroOfferEventsResponse.getOfferEvents().size() > 0) {
                    lastEventRepository.deleteAll();
                    LastEvent lastEvent = new LastEvent();
                    lastEvent.setEventId(allegroOfferEventsResponse.getOfferEvents().get(allegroOfferEventsResponse.getOfferEvents().size() - 1).getId());

                    lastEventRepository.save(lastEvent);
                }


                return allegroOfferEventsResponse;
            }
        }catch (Exception ignored){
        }

        AllegroOfferEventsResponse allegroOfferEventsResponse = new AllegroOfferEventsResponse();
        allegroOfferEventsResponse.setOfferEvents(new ArrayList<>());
        return allegroOfferEventsResponse;
    }

}
