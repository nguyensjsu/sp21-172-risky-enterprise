package com.example.CustomerFrontEnd;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;


@Configuration
public class MVCConfig implements WebMvcConfigurer {

  public void addViewControllers(ViewControllerRegistry registry) {
    //egistry.addViewController("").setViewName("home");
    registry.addViewController("/").setViewName("login");
    registry.addViewController("/login").setViewName("login");
  }

}