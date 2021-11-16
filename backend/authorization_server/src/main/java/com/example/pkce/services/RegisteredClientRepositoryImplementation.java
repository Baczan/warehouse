package com.example.pkce.services;

import com.example.pkce.entities.Client;
import com.example.pkce.exceptions.ClientNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
public class RegisteredClientRepositoryImplementation implements RegisteredClientRepository {

  @Autowired private ClientService clientService;

  @Override
  public void save(RegisteredClient registeredClient) {}

  @Override
  public RegisteredClient findById(String s) {

    try {
      return registeredClientFromClient(clientService.loadClientById(Integer.parseInt(s)));
    } catch (ClientNotFoundException e) {
      return null;
    }

  }

  @Override
  public RegisteredClient findByClientId(String s) {

    try {
      return registeredClientFromClient(clientService.loadClientByClientId(s));
    } catch (ClientNotFoundException e) {
      return null;
    }

  }

  private RegisteredClient registeredClientFromClient(Client client) {

    if (client == null) {
      return null;
    }


    return RegisteredClient.withId(String.valueOf(client.getId()))
        .tokenSettings(
            tokenSettings ->
                tokenSettings
                    .accessTokenTimeToLive(Duration.parse(client.getAccessTokenTimeToLive()))
                    .reuseRefreshTokens(client.isReuseRefreshTokens())
                    .refreshTokenTimeToLive(Duration.parse(client.getRefreshTokenTimeToLive())))
        .clientId(client.getClientId())
        .clientSecret(client.getClientSecret())
        .redirectUris(
            strings -> {
              strings.addAll(client.getRedirectUrl());
            })
        .scope("read")
        .clientSettings(
            clientSettings ->
                clientSettings
                    .requireUserConsent(client.isRequireUserConsent())
                    .requireProofKey(client.isRequireProofKey()))
        .authorizationGrantTypes(
            authorizationGrantTypes -> {
              authorizationGrantTypes.addAll(grantTypes(client));
            })
        .clientAuthenticationMethods(
            clientAuthenticationMethods -> {
              clientAuthenticationMethods.addAll(authenticationMethods(client));
            })
        .build();
  }

  private List<AuthorizationGrantType> grantTypes(Client client) {
    List<AuthorizationGrantType> authorizationGrantTypesList = new ArrayList<>();

    if (client.isAuthorizationGrantTypeAuthorizationCode()) {
      authorizationGrantTypesList.add(AuthorizationGrantType.AUTHORIZATION_CODE);
    }

    if (client.isAuthorizationGrantTypeImplicit()) {
      authorizationGrantTypesList.add(AuthorizationGrantType.IMPLICIT);
    }

    if (client.isAuthorizationGrantTypeRefreshToken()) {
      authorizationGrantTypesList.add(AuthorizationGrantType.REFRESH_TOKEN);
    }

    if (client.isAuthorizationGrantTypePassword()) {
      authorizationGrantTypesList.add(AuthorizationGrantType.PASSWORD);
    }

    if (client.isAuthorizationGrantTypeClientCredentials()) {
      authorizationGrantTypesList.add(AuthorizationGrantType.CLIENT_CREDENTIALS);
    }

    return authorizationGrantTypesList;
  }

  private List<ClientAuthenticationMethod> authenticationMethods(Client client) {
    List<ClientAuthenticationMethod> authenticationMethodList = new ArrayList<>();

    if (client.isAuthenticationMethodBasic()) {
      authenticationMethodList.add(ClientAuthenticationMethod.BASIC);
    }

    if (client.isAuthenticationMethodPost()) {
      authenticationMethodList.add(ClientAuthenticationMethod.POST);
    }

    if (client.isAuthenticationMethodNone()) {
      authenticationMethodList.add(ClientAuthenticationMethod.NONE);
    }

    return authenticationMethodList;
  }
}
