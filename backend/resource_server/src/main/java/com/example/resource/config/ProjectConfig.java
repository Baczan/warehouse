package com.example.resource.config;

import com.example.resource.jwt.JwtConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ProjectConfig implements WebMvcConfigurer {

  @Autowired private Environment env;

  private static final String[] UNAUTHORIZED_URLS = {
          "/parts/search",
          "/newCar/getImage"
  };

  @Bean
  SecurityFilterChain securityFilterChai(HttpSecurity http) throws Exception {
    http.
      authorizeRequests().
            antMatchers(UNAUTHORIZED_URLS)
            .permitAll()
            .anyRequest()
            .authenticated()
    .and().oauth2ResourceServer().jwt().jwtAuthenticationConverter(new JwtConverter(env.getProperty("app.client.id")));
    http.cors();
    http.csrf().disable();
    return http.build();
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**").allowedOrigins("*",env.getProperty("app.cors.mapping")).allowedMethods("GET","POST","DELETE");
  }
}
