package com.example.resource.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class JwtConverter implements Converter<Jwt, AbstractOAuth2TokenAuthenticationToken> {

  private String aud;

  public JwtConverter(String aud) {
    this.aud = aud;
  }

  @Override
  public AbstractOAuth2TokenAuthenticationToken convert(Jwt jwt) {

    if(!jwt.getClaimAsStringList("aud").contains(aud)){
      return new JwtAuthenticationToken(jwt);
    }


    List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
    List<String> authorities = jwt.getClaimAsStringList("authorities");
    authorities.forEach(authority->{
      grantedAuthorities.add(new Authority(authority));
    });

    JwtUser user = new JwtUser(jwt.getSubject(),jwt.getClaimAsString("email"),authorities);
    return new JwtAuthenticationToken(jwt,user,jwt,grantedAuthorities);
  }
}

