package com.sagag.eshop.repo.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
@ComponentScan(value = "com.sagag")
public class RepoApplication {

  public static void main(String[] args) {
    SpringApplication.run(RepoApplication.class, args);
  }

}
