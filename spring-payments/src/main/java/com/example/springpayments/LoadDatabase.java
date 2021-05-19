package com.example.springpayments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;


@Configuration
class LoadDatabase {

  private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

  @Profile("dev")
  @Bean
  @Transactional
  CommandLineRunner initDatabase(CustomerRepository customerRepository) {

    return args -> {
    //   cardRepository.save(new Card("498498082", "425", 20.0, false, ""));
    //   cardRepository.save(new Card("627131848", "547", 20.0, false, ""));

    //   cardRepository.findAll().forEach(card -> log.info("Preloaded " + card));


        Customer c1 = new Customer();
        c1.setFirstName("firstName");
        c1.setLastName("lastName");
        c1.setAddress("address");
        c1.setUsername("userName");
        c1.setPassword("password");
        c1.setCity("city");
        c1.setState("CA");
        c1.setZip("94105");
        c1.setPhoneNumber("9162675317");
        c1.setEmail("test@cybs.com");
        c1.setRewards(10);
        customerRepository.save(c1);
    
      customerRepository.findAll().forEach(customer -> log.info("Preloaded" + customer));
    };
  }
}