package com.example.CustomerFrontEnd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.
                                              UserDetailsService;
import org.springframework.security.core.userdetails.
                                       UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.springframework.security.core.userdetails.
                                        UserDetailsService;

import org.springframework.context.annotation.Configuration;


@Configuration
public class CustomerRepositoryUserDetailsService implements UserDetailsService{

    private CustomerRepository customerRepository;

    @Autowired
    public CustomerRepositoryUserDetailsService(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
        throws UsernameNotFoundException {
      Customer customer = customerRepository.findByUsername(username);
      System.out.print("==================>" + customer.getFirstName());
      if (customer != null) {
        return customer;
      }
      throw new UsernameNotFoundException(
                      "User '" + username + "' not found");
    }

}
