package com.example.OrderProcessing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

  private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

  @Bean
  CommandLineRunner initDatabase(CardRepository cardRepository) {

    return args -> {
      cardRepository.save(new Card("498498082", "425", 20.0, false, ""));
      cardRepository.save(new Card("627131848", "547", 20.0, false, ""));

      cardRepository.findAll().forEach(card -> log.info("Preloaded " + card));
    };
  }
}