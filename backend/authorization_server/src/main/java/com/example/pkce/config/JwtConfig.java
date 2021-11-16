package com.example.pkce.config;

import com.example.pkce.entities.User;
import com.example.pkce.models.EmailUser;
import com.example.pkce.repositories.RoleRepository;
import com.example.pkce.repositories.RoleTemplateRepository;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.server.authorization.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Configuration
public class JwtConfig {

  @Autowired private RoleRepository roleRepository;

  @Autowired private RoleTemplateRepository roleTemplateRepository;

    @Autowired
    private Environment env;

  @Bean
  OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {

    return context -> {
      User user = ((EmailUser) context.getPrincipal().getPrincipal()).getUser();

        //Find all roles of a user within specific client
      List<String> authoritiesList = new ArrayList<>();

      roleRepository
          .findAllByUserIdAndClientId(user.getId(), context.getRegisteredClient().getClientId())
          .forEach(
              role -> {
                roleTemplateRepository
                    .findById(role.getRoleTemplateId())
                    .ifPresent(
                        roleTemplate1 -> {
                          authoritiesList.add("ROLE_" + roleTemplate1.getRole());
                        });
              });

      context.getClaims().claim("authorities", authoritiesList);
      context.getClaims().subject(String.valueOf(user.getId())).claim("email", user.getEmail());
    };
  }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = getKeyPair();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    @Bean
    public ProviderSettings providerSettings() {
        ProviderSettings ps = new ProviderSettings();
        ps = ps.issuer(env.getProperty("app.issuer"));
        ps = ps.jwkSetEndpoint("/certs");
        return ps;
    }

    private RSAKey getKeyPair() {
        KeyPair keyPair = generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        return new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    private KeyPair generateKeyPair() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }
}
