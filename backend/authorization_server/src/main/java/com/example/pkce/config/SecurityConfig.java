package com.example.pkce.config;

import com.example.pkce.handlers.CustomAuthenticationHandler;
import com.example.pkce.models.EmailUser;
import com.example.pkce.services.UserDetailsManagerImp;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jackson2.CoreJackson2Module;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.client.jackson2.OAuth2ClientJackson2Module;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.jackson2.WebJackson2Module;
import org.springframework.security.web.jackson2.WebServletJackson2Module;
import org.springframework.security.web.server.jackson2.WebServerJackson2Module;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Import(OAuth2AuthorizationServerConfiguration.class)
public class SecurityConfig {

  private static final String[] UNAUTHORIZED_URLS = {
    "/",
    "/login",
    "/login-error",
    "/logout",
    "/oauth2/**",
    "/user/**",
    "/elastic/**",
    "/files/**",
    "/auth/**",
  };

  @Autowired private CustomAuthenticationHandler handler;
  @Autowired private Environment env;

  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
      throws Exception {

    OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
    http.oauth2Login().loginPage("/login").permitAll().successHandler(handler);
    http.cors();

    return http.formLogin().and().build();
  }

  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeRequests(
            authorizeRequests ->
                authorizeRequests
                    .antMatchers(UNAUTHORIZED_URLS)
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .formLogin()
        .loginPage("/login")
        .failureUrl("/login-error")
        .and()
        .oauth2Login()
        .loginPage("/login")
        .successHandler(handler);

    http.cors();

    return http.build();
  }

  @Bean
  public UserDetailsService users() {
    return new UserDetailsManagerImp();
  }


  @Bean
  public PasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public ClientRegistrationRepository clientRegistrationRepository() {
    return new InMemoryClientRegistrationRepository(facebookClient(), googleClient());
  }

  private ClientRegistration facebookClient() {
    return CommonOAuth2Provider.FACEBOOK
        .getBuilder("facebook")
        .clientId( env.getProperty("app.facebook.client.id"))
        .clientSecret( env.getProperty("app.facebook.client.secret"))
        .redirectUri(String.format("%s/login/oauth2/code/facebook", env.getProperty("app.url")))
        .build();
  }

  private ClientRegistration googleClient() {
    return CommonOAuth2Provider.GOOGLE
        .getBuilder("google")
        .clientId( env.getProperty("app.google.client.id"))
        .clientSecret( env.getProperty("app.google.client.secret"))
        .redirectUri(String.format("%s/login/oauth2/code/google", env.getProperty("app.url")))
        .build();
  }


}
