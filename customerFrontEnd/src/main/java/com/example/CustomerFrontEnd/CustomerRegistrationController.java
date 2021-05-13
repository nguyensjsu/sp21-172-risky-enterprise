package com.example.CustomerFrontEnd;

import javax.validation.Valid;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.beans.factory.annotation.Value;


import org.springframework.ui.Model;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;


import java.util.*;
import org.springframework.validation.ObjectError;

import org.springframework.context.annotation.Bean;

import org.springframework.security.crypto.password.StandardPasswordEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;


import java.lang.*;

@Controller
@RequestMapping("/register")
public class CustomerRegistrationController {

    
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;



    private String[] states;

    public CustomerRegistrationController(CustomerRepository customerRepositor ){
        this.customerRepository =  customerRepositor;

        this.states = new String[]{"AK","AL","AR","AS","AZ","CA","CO","CT","DC","DE","FL","GA","GU","HI","IA","ID","IL","IN","KS","KY","LA","MA","MD","ME","MI","MN","MO","MS","MT","NC","ND","NE","NH","NJ","NM","NV","NY","OH","OK","OR","PA","PR","RI","SC","SD","TN","TX","UT","VA","VI","VT","WA","WI","WV","WY"};
    }

    @GetMapping
    public String registerForm(@ModelAttribute("form") RegistrationForm form, Model model) {
        return "register";
    }

    @PostMapping
    public String processRegistration(@Valid @ModelAttribute("form") RegistrationForm form, Errors errors, Model model, HttpServletRequest request) {
        
        /* validate Form */
        CheckRequiredFeilds(errors, form);
        FieldValidation(errors, form);

        if (errors.hasErrors()) {
            List<Message> messages = new ArrayList<>();
            for(ObjectError objectError: errors.getGlobalErrors()){
                messages.add(new Message(objectError.getDefaultMessage()));
            }
            model.addAttribute( "messages", messages) ;
            return "register";
        }

        
        customerRepository.save(form.toCustomer(passwordEncoder));

        return "redirect:/login";
    }


    public void CheckRequiredFeilds(Errors errors, RegistrationForm form){
        if(form.getFirstName().length() == 0){
            errors.reject("1", "FirstName not provided");
        }

        if(form.getLastName().length() == 0){
            errors.reject("2", "LastName not provided");
        }

        if(form.getStreet().length() == 0){
            errors.reject("3", "Address not provided");
        }

        if(form.getCity().length() == 0){
            errors.reject("4", "City not provided");
        }

        if(form.getState().length() == 0){
            errors.reject("5", "State not provided");
        }

        if(form.getZip().length() == 0){
            errors.reject("6", "ZIP not provided");
        }

        if(form.getPhoneNumber() == null || form.getPhoneNumber().length() == 0){
            errors.reject("7", "Phone Number not provided");
        }

        if(form.getEmail().length() == 0){
            errors.reject("11", "Email not provided");
        }


    }

    public void FieldValidation(Errors errors, RegistrationForm form){

        Set<String> setStates = new HashSet<String>(Arrays.asList(states));
        if(!(setStates.contains(form.getState()))){
            errors.reject("18", "Not valid US state");
        }

        if(!(form.getZip().matches("\\d{5}"))){
            errors.reject("12", "Invalid ZIP Code");
        }

        if(! form.getPhoneNumber().matches("[(]\\d{3}[)] \\d{3}-\\d{4}")){
            errors.reject("13", "Invalid Phone number Format");
        }

    }




}
