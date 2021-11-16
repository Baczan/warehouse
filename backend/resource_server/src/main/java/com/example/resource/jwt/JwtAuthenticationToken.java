package com.example.resource.jwt;

import org.springframework.security.oauth2.core.AbstractOAuth2Token;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;

import java.util.Collection;
import java.util.Map;

public class JwtAuthenticationToken extends AbstractOAuth2TokenAuthenticationToken {

  protected JwtAuthenticationToken(AbstractOAuth2Token token) {
    super(token);
  }

  protected JwtAuthenticationToken(AbstractOAuth2Token token, Collection collection) {
    super(token, collection);
  }

  protected JwtAuthenticationToken(AbstractOAuth2Token token, Object principal, Object credentials, Collection collection) {
    super(token, principal, credentials, collection);
    this.setAuthenticated(true);
  }


  @Override
  public Map<String, Object> getTokenAttributes() {
    return null;
  }
}
