package com.example.resource.controllers;

import com.example.resource.dto.AllegroAuthorizationResponseDto;
import com.example.resource.dto.AllegroJwtDto;
import com.example.resource.dto.ConnectionResponseDTO;
import com.example.resource.entities.Connection;
import com.example.resource.helpers.ConnectionHelper;
import com.example.resource.jwt.JwtUser;
import com.example.resource.repositories.ConnectionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.beans.Transient;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("connect")
@PreAuthorize("hasRole('ADMIN')")
public class ConnectionController {

    @Autowired
    private Environment env;

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private ConnectionHelper connectionHelper;

    @PostMapping("/allegro")
    public ResponseEntity<?> connectToAllegro(@RequestParam String code, Authentication authentication) throws JsonProcessingException {


        String url = String.format("/auth/oauth/token?grant_type=authorization_code&code=%s&redirect_uri=%s",
                code,
                env.getProperty("app.allegro.redirect.url")
        );

        try {

            Mono<AllegroAuthorizationResponseDto> response = connectionHelper.allegroWebClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(AllegroAuthorizationResponseDto.class);


            AllegroAuthorizationResponseDto allegroAuthorizationResponseDto = response.block();

            String[] chunks = allegroAuthorizationResponseDto.getAccess_token().split("\\.");

            ObjectMapper mapper = new ObjectMapper();

            String payload = new String(Base64.getDecoder().decode(chunks[1]));

            AllegroJwtDto allegroJwtDto = mapper.readValue(payload, AllegroJwtDto.class);

            Connection connection;

            if(connectionRepository.existsByService("allegro")){

                connection = connectionRepository.getByService("allegro");
                connection.setAccessToken(allegroAuthorizationResponseDto.getAccess_token());
                connection.setRefreshToken(allegroAuthorizationResponseDto.getRefresh_token());
                connection.setUsername(allegroJwtDto.getUser_name());

                connection = connectionRepository.save(connection);
            }else {
                connection = new Connection();

                connection.setService("allegro");

                connection.setAccessToken(allegroAuthorizationResponseDto.getAccess_token());
                connection.setRefreshToken(allegroAuthorizationResponseDto.getRefresh_token());
                connection.setUsername(allegroJwtDto.getUser_name());

                connection = connectionRepository.save(connection);
            }

            return new ResponseEntity<>(connection, HttpStatus.OK);
        }catch (WebClientResponseException e){
            return new ResponseEntity<>("unable to exchange code for token", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/allegro/refresh")
    public ResponseEntity<?> refreshAllegro(){

        return connectionHelper.refreshAllegro();

    }

    @PostMapping("/allegro/disconnect")
    @Transactional
    public ResponseEntity<?> disconnectAllegro(){

        if(!connectionRepository.existsByService("allegro")){
            return new ResponseEntity<>("allegro_not_connected",HttpStatus.BAD_REQUEST);
        }

        connectionRepository.deleteByService("allegro");

        return new ResponseEntity<>("disconnected",HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<?> list(){

        List<ConnectionResponseDTO> connections = connectionRepository.findAll()
                .stream()
                .map(connection ->  new ConnectionResponseDTO(connection.getService(),connection.getUsername()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(connections,HttpStatus.OK);
    }


}
