package com.example.CustomerFrontEnd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.annotation.Profile;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.beans.factory.annotation.Autowired;





@Configuration
class LoadDatabase {

  private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

  @Autowired
    private PasswordEncoder passwordEncoder;

  @Profile("dev")
  @Bean
  @Transactional
  CommandLineRunner initDatabase(CustomerRepository customerRepository, CardRepository cardRepository) {

    return args -> {
    //   cardRepository.save(new Card("498498082", "425", 20.0, false, ""));
    //   cardRepository.save(new Card("627131848", "547", 20.0, false, ""));

    //   cardRepository.findAll().forEach(card -> log.info("Preloaded " + card));
        
        

        Customer c1 = new Customer();
        c1.setFirstName("firstName");
        c1.setLastName("lastName");
        c1.setAddress("address");
        c1.setUsername("username");
        c1.setPassword(passwordEncoder.encode("password"));
        c1.setCity("city");
        c1.setState("CA");
        c1.setZip("94105");
        c1.setPhoneNumber("9162675317");
        c1.setEmail("test@cybs.com");
        c1.setRewards(10);
        customerRepository.save(c1);

        Card card1 = new Card();
        card1.setCustomer(c1);
        card1.setCardNumber("4111111111111111");
        card1.setActivated(true);
        card1.setExpYear("2020");
        card1.setExpMonth("09");
        card1.setCardType("001");
        card1.setCvv("040");

        cardRepository.save(card1);


        Card card2 = new Card();
        card2.setCustomer(c1);
        card2.setCardNumber("4111111111111112");
        card2.setActivated(true);
        card2.setExpYear("2020");
        card2.setExpMonth("09");
        card2.setCardType("001");
        card2.setCvv("040");

        cardRepository.save(card2);
    
      customerRepository.findAll().forEach(customer -> log.info("Preloaded" + customer));
    };
  }
}