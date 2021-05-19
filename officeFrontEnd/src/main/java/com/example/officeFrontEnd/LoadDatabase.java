package com.example.officeFrontEnd;

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

  @Bean
  CommandLineRunner initDatabase(EmployeeRepository employeeRepository) {

    return args -> {
    
    Employee e1 = new Employee();
    e1.setUsername("e1");
    e1.setPassword(passwordEncoder.encode("e1"));

    employeeRepository.save(e1);
      employeeRepository.findAll().forEach(employee -> log.info("Preloaded" + employee));
    };
  }
}
