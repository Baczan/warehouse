package com.example.resource.helpers;

import com.example.resource.dto.AllegroAuthorizationResponseDto;
import com.example.resource.dto.AllegroJwtDto;
import com.example.resource.entities.Connection;
import com.example.resource.repositories.ConnectionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Base64;

@Service
public class ConnectionHelper {

    @Autowired
    private Environment env;

    @Autowired
    private ConnectionRepository connectionRepository;


    public WebClient allegroWebClient;

    @PostConstruct
    private void setUpWebClients(){

        String authorizationHeaderValue = String.format("%s:%s",
                env.getProperty("app.allegro.client.id"),
                env.getProperty("app.allegro.client.secret"));

        authorizationHeaderValue = "Basic "+Base64.getEncoder().encodeToString(authorizationHeaderValue.getBytes());

        allegroWebClient = WebClient.builder()
                .baseUrl(env.getProperty("app.allegro.authorization.url"))
                .defaultHeader(HttpHeaders.AUTHORIZATION,authorizationHeaderValue).build();

    }


    public ResponseEntity<?> refreshAllegro(){
        if(!connectionRepository.existsByService("allegro")){
            return new ResponseEntity<>("refreshing_error", HttpStatus.BAD_REQUEST);
        }

        Connection connection = connectionRepository.getByService("allegro");

        String url = String.format("/auth/oauth/token?grant_type=refresh_token&refresh_token=%s&redirect_uri=%s",
                connection.getRefreshToken(),
                env.getProperty("app.allegro.redirect.url")
        );

        try {
            Mono<AllegroAuthorizationResponseDto> response = allegroWebClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(AllegroAuthorizationResponseDto.class);

            AllegroAuthorizationResponseDto allegroAuthorizationResponseDto = response.block();

            String[] chunks = allegroAuthorizationResponseDto.getAccess_token().split("\\.");

            ObjectMapper mapper = new ObjectMapper();

            String payload = new String(Base64.getDecoder().decode(chunks[1]));

            AllegroJwtDto allegroJwtDto = mapper.readValue(payload, AllegroJwtDto.class);

            connection.setAccessToken(allegroAuthorizationResponseDto.getAccess_token());
            connection.setRefreshToken(allegroAuthorizationResponseDto.getRefresh_token());
            connection.setUsername(allegroJwtDto.getUser_name());

            connectionRepository.save(connection);

            return new ResponseEntity<>(connection,HttpStatus.OK);

        }catch (WebClientResponseException | JsonProcessingException e){
            System.out.println(e.getMessage());
            connectionRepository.delete(connection);
            return new ResponseEntity<>("refreshing_error",HttpStatus.BAD_REQUEST);
        }
    }

}
