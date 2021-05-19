package com.example.officeFrontEnd;

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

public class EmployeeRepositoryUserDetailsService implements UserDetailsService{

    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeRepositoryUserDetailsService(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
        throws UsernameNotFoundException {
     Employee employee = employeeRepository.findByUsername(username);
      System.out.print("==================>" + employee.getUsername());
      if (employee != null) {
        return employee;
      }
      throw new UsernameNotFoundException(
                      "User '" + username + "' not found");
    }
}
