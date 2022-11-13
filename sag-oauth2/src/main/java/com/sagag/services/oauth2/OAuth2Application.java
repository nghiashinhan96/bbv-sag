package com.sagag.services.oauth2;

import com.sagag.eshop.repo.config.RepoConfiguration;
import com.sagag.services.oauth2.config.OAuth2Configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@Import(value = { OAuth2Configuration.class, RepoConfiguration.class })
@ComponentScan(value = "com.sagag")
public class OAuth2Application extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(OAuth2Application.class, args);
  }
}
