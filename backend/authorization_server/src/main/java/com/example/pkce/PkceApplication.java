package com.example.pkce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PkceApplication {

  public static void main(String[] args) {
    SpringApplication.run(PkceApplication.class, args);
  }
}
