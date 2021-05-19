package com.example.officeFrontEnd;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web
                        .configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web
                    .configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.annotation.Order;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//import org.springframework.security.core.userdetails.
                                        //UserDetailsService;



@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private EmployeeRepositoryUserDetailsService userDetailsService;

    

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
        throws Exception {

    auth
        .userDetailsService(userDetailsService)
        .passwordEncoder(encoder());

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        .antMatchers("/officePage")
        .access("hasRole('ROLE_USER')")        
        .antMatchers("/", "/**").access("permitAll")
        .antMatchers("/h2-console/**").permitAll()        

        .and()
        .formLogin()
        .loginPage("/login")
        .defaultSuccessUrl("/officePage", true)

        .and()
        .logout()
        .logoutSuccessUrl("/login")
        ;

        http.csrf().disable();
        http.headers().frameOptions().disable();
    }


}

