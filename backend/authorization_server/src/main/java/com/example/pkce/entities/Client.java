package com.example.pkce.entities;

import com.example.pkce.models.ClientModel;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "client")
public class Client {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "owner", nullable = false)
  private int owner;

  @Column(name = "client_id", nullable = false, unique = true)
  private String clientId;

  @Column(name = "client_secret", nullable = false)
  private String clientSecret;

  @Column(name = "client_secret_plain", nullable = false)
  private String clientSecretPlain;

  @Column(name = "authentication_method_basic", nullable = false)
  private boolean authenticationMethodBasic = true;

  @Column(name = "authentication_method_post", nullable = false)
  private boolean authenticationMethodPost = false;

  @Column(name = "authentication_method_none", nullable = false)
  private boolean authenticationMethodNone = false;

  @Column(name = "authorization_grant_type_authorization_code", nullable = true)
  private boolean authorizationGrantTypeAuthorizationCode = true;

  @Column(name = "authorization_grant_type_implicit", nullable = false)
  private boolean authorizationGrantTypeImplicit = false;

  @Column(name = "authorization_grant_type_refresh_token", nullable = false)
  private boolean authorizationGrantTypeRefreshToken = false;

  @Column(name = "authorization_grant_type_password", nullable = false)
  private boolean authorizationGrantTypePassword = false;

  @Column(name = "authorization_grant_type_client_credentials", nullable = false)
  private boolean authorizationGrantTypeClientCredentials = false;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "redirect_url", joinColumns = @JoinColumn(name = "client_id"))
  @Column(name = "redirect_url")
  private Collection<String> redirectUrl = Collections.singletonList("http://localhost/");

  @Column(name = "require_user_consent", nullable = false)
  private boolean requireUserConsent = false;

  @Column(name = "require_proof_key", nullable = false)
  private boolean requireProofKey = false;

  @Column(name = "reuse_refresh_tokens", nullable = false)
  private boolean reuseRefreshTokens = false;

  @Column(name = "access_token_time_to_live", nullable = false)
  private String accessTokenTimeToLive = "PT30M";

  @Column(name = "refresh_token_time_to_live", nullable = false)
  private String refreshTokenTimeToLive = "PT168H";

  public Client() {}

  public void setFromClientModel(ClientModel clientModel){

    this.setName(clientModel.getName());

    this.setAuthenticationMethodBasic(clientModel.isAuthenticationMethodBasic());
    this.setAuthenticationMethodPost(clientModel.isAuthenticationMethodPost());
    this.setAuthenticationMethodNone(clientModel.isAuthenticationMethodNone());

    this.setAuthorizationGrantTypeAuthorizationCode(
            clientModel.isAuthorizationGrantTypeAuthorizationCode());
    this.setAuthorizationGrantTypeImplicit(clientModel.isAuthorizationGrantTypeImplicit());
    this.setAuthorizationGrantTypePassword(clientModel.isAuthorizationGrantTypePassword());
    this.setAuthorizationGrantTypeRefreshToken(
            clientModel.isAuthorizationGrantTypeRefreshToken());
    this.setAuthorizationGrantTypeClientCredentials(
            clientModel.isAuthorizationGrantTypeClientCredentials());

    this.setRedirectUrl(clientModel.getRedirectUrls());

    this.setRequireProofKey(clientModel.isRequireProofKey());
    this.setRequireUserConsent(clientModel.isRequireUserConsent());
    this.setReuseRefreshTokens(clientModel.isReuseRefreshToken());

    this.setAccessTokenTimeToLive(clientModel.getAccessTokenTimeToLive());
    this.setRefreshTokenTimeToLive(clientModel.getRefreshTokenTimeToLive());

  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
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

  public String getClientSecretPlain() {
    return clientSecretPlain;
  }

  public void setClientSecretPlain(String clientSecretPlain) {
    this.clientSecretPlain = clientSecretPlain;
  }

  public Collection<String> getRedirectUrl() {
    return redirectUrl;
  }

  public void setRedirectUrl(Collection<String> redirectUrl) {
    this.redirectUrl = redirectUrl;
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

  public boolean isReuseRefreshTokens() {
    return reuseRefreshTokens;
  }

  public void setReuseRefreshTokens(boolean reuseRefreshTokens) {
    this.reuseRefreshTokens = reuseRefreshTokens;
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
}
