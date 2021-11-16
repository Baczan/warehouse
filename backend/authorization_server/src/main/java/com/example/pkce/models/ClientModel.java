package com.example.pkce.models;

import com.example.pkce.entities.Client;

import java.util.Collection;

public class ClientModel {

  private String name;
  private int owner;
  private String clientId;
  private String clientSecret;
  private boolean authenticationMethodBasic;
  private boolean authenticationMethodPost;
  private boolean authenticationMethodNone;
  private boolean authorizationGrantTypeAuthorizationCode;
  private boolean authorizationGrantTypeImplicit;
  private boolean authorizationGrantTypeRefreshToken;
  private boolean authorizationGrantTypePassword;
  private boolean authorizationGrantTypeClientCredentials;
  private Collection<String> redirectUrls;
  private boolean requireUserConsent;
  private boolean requireProofKey;
  private boolean reuseRefreshToken;
  private String accessTokenTimeToLive;
  private String refreshTokenTimeToLive;

  public ClientModel() {}

  public ClientModel(Client client) {
    this.name = client.getName();
    this.owner = client.getOwner();
    this.clientId = client.getClientId();
    this.clientSecret = client.getClientSecretPlain();
    this.authenticationMethodBasic = client.isAuthenticationMethodBasic();
    this.authenticationMethodPost = client.isAuthenticationMethodPost();
    this.authenticationMethodNone = client.isAuthenticationMethodNone();
    this.authorizationGrantTypeAuthorizationCode =
        client.isAuthorizationGrantTypeAuthorizationCode();
    this.authorizationGrantTypeImplicit = client.isAuthorizationGrantTypeImplicit();
    this.authorizationGrantTypeRefreshToken = client.isAuthorizationGrantTypeRefreshToken();
    this.authorizationGrantTypePassword = client.isAuthorizationGrantTypePassword();
    this.authorizationGrantTypeClientCredentials =
        client.isAuthorizationGrantTypeClientCredentials();
    this.redirectUrls = client.getRedirectUrl();
    this.requireProofKey = client.isRequireProofKey();
    this.requireUserConsent = client.isRequireUserConsent();
    this.reuseRefreshToken = client.isReuseRefreshTokens();
    this.accessTokenTimeToLive = client.getAccessTokenTimeToLive();
    this.refreshTokenTimeToLive = client.getRefreshTokenTimeToLive();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getOwner() {
    return owner;
  }

  public void setOwner(int owner) {
    this.owner = owner;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }

  public boolean isAuthenticationMethodBasic() {
    return authenticationMethodBasic;
  }

  public void setAuthenticationMethodBasic(boolean authenticationMethodBasic) {
    this.authenticationMethodBasic = authenticationMethodBasic;
  }

  public boolean isAuthenticationMethodPost() {
    return authenticationMethodPost;
  }

  public void setAuthenticationMethodPost(boolean authenticationMethodPost) {
    this.authenticationMethodPost = authenticationMethodPost;
  }

  public boolean isAuthenticationMethodNone() {
    return authenticationMethodNone;
  }

  public void setAuthenticationMethodNone(boolean authenticationMethodNone) {
    this.authenticationMethodNone = authenticationMethodNone;
  }

  public boolean isAuthorizationGrantTypeAuthorizationCode() {
    return authorizationGrantTypeAuthorizationCode;
  }

  public void setAuthorizationGrantTypeAuthorizationCode(
      boolean authorizationGrantTypeAuthorizationCode) {
    this.authorizationGrantTypeAuthorizationCode = authorizationGrantTypeAuthorizationCode;
  }

  public boolean isAuthorizationGrantTypeImplicit() {
    return authorizationGrantTypeImplicit;
  }

  public void setAuthorizationGrantTypeImplicit(boolean authorizationGrantTypeImplicit) {
    this.authorizationGrantTypeImplicit = authorizationGrantTypeImplicit;
  }

  public boolean isAuthorizationGrantTypeRefreshToken() {
    return authorizationGrantTypeRefreshToken;
  }

  public void setAuthorizationGrantTypeRefreshToken(boolean authorizationGrantTypeRefreshToken) {
    this.authorizationGrantTypeRefreshToken = authorizationGrantTypeRefreshToken;
  }

  public boolean isAuthorizationGrantTypePassword() {
    return authorizationGrantTypePassword;
  }

  public void setAuthorizationGrantTypePassword(boolean authorizationGrantTypePassword) {
    this.authorizationGrantTypePassword = authorizationGrantTypePassword;
  }

  public boolean isAuthorizationGrantTypeClientCredentials() {
    return authorizationGrantTypeClientCredentials;
  }

  public void setAuthorizationGrantTypeClientCredentials(
      boolean authorizationGrantTypeClientCredentials) {
    this.authorizationGrantTypeClientCredentials = authorizationGrantTypeClientCredentials;
  }

  public Collection<String> getRedirectUrls() {
    return redirectUrls;
  }

  public void setRedirectUrls(Collection<String> redirectUrl) {
    this.redirectUrls = redirectUrl;
  }

  public boolean isRequireUserConsent() {
    return requireUserConsent;
  }

  public void setRequireUserConsent(boolean requireUserConsent) {
    this.requireUserConsent = requireUserConsent;
  }

  public boolean isRequireProofKey() {
    return requireProofKey;
  }

  public void setRequireProofKey(boolean requireProofKey) {
    this.requireProofKey = requireProofKey;
  }

  public boolean isReuseRefreshToken() {
    return reuseRefreshToken;
  }

  public void setReuseRefreshToken(boolean reuseRefreshToken) {
    this.reuseRefreshToken = reuseRefreshToken;
  }

  public String getAccessTokenTimeToLive() {
    return accessTokenTimeToLive;
  }

  public void setAccessTokenTimeToLive(String accessTokenTimeToLive) {
    this.accessTokenTimeToLive = accessTokenTimeToLive;
  }

  public String getRefreshTokenTimeToLive() {
    return refreshTokenTimeToLive;
  }

  public void setRefreshTokenTimeToLive(String refreshTokenTimeToLive) {
    this.refreshTokenTimeToLive = refreshTokenTimeToLive;
  }
}
