package com.example.CustomerFrontEnd;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Data;
@Data
public class RegistrationForm {

  private String username;
  private String password;
  private String firstName;
  private String lastName;
  private String street;
  private String city;
  private String state;
  private String zip;
  private String phoneNumber;
  private String email;


  public Customer toCustomer(PasswordEncoder encoder) {
    Customer c1 = new Customer();
        c1.setFirstName(firstName);
        c1.setLastName(lastName);
        c1.setAddress(street);
        c1.setUsername(username);
        c1.setPassword(encoder.encode(password));
        c1.setCity(city);
        c1.setState(state);
        c1.setZip(zip);
        c1.setPhoneNumber(phoneNumber.replaceAll("() ", ""));
        c1.setEmail(email);
        
        return c1;
  }

}