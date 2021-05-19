package com.example.CustomerFrontEnd;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.stereotype.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;


import org.springframework.web.server.ResponseStatusException;

import org.springframework.dao.EmptyResultDataAccessException;

import org.springframework.web.bind.annotation.GetMapping; 
import org.springframework.web.bind.annotation.RequestParam; 

import org.springframework.beans.factory.annotation.Value;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import java.io.*;

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.*;

import org.springframework.ui.Model;
import org.springframework.validation.Errors;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.validation.ObjectError;

import org.springframework.web.bind.annotation.ModelAttribute;


import javax.validation.Valid;


@Controller
@RequestMapping("/home")
public class HomeController {

    private Map<String, String> monthToNum;
    private Map<String, String> cardTypeToDigits;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CardRepository cardRepository;




    @Value("${paymentprocessing.serverport}")private String serverPort;
    @Value("${paymentprocessing.apihost}") private String apiHost;
    @Value("${paymentprocessing.apikey}") private String apiKey;

    
    HomeController(){
        this.monthToNum= new HashMap<>();
        monthToNum.put("january","01");
        monthToNum.put("february", "02");
        monthToNum.put("march", "03");
        monthToNum.put("april", "04");
        monthToNum.put("may","05");
        monthToNum.put("june", "06");
        monthToNum.put("july","07");
        monthToNum.put("august", "08");
        monthToNum.put("september","09");
        monthToNum.put("october", "10");
        monthToNum.put("november", "11");
        monthToNum.put("december", "12");

        this.cardTypeToDigits = new HashMap<>();
        cardTypeToDigits.put("4", "001");// visa
        cardTypeToDigits.put("3", "003");// americanExpress
        cardTypeToDigits.put("6", "004");// Discover
        cardTypeToDigits.put("5", "002"); // mastercard
    }

    @GetMapping
    public String getHome(@Valid @ModelAttribute("form") CardForm form, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        Customer customer =  customerRepository.findByUsername(currentPrincipalName);
        model.addAttribute("name", customer.getUsername());
        model.addAttribute("reward", customer.getRewards());
        model.addAttribute("customerid", customer.getId());


        List<Card> cards = customer.getCards();
        
        model.addAttribute("cards", cards);

        return "home";

    }


    @PostMapping
    public String postAction( @Valid @ModelAttribute("form") CardForm form, Errors errors, Model model ){
        /* validate Form */
        CheckRequiredFeilds(errors, form);
        FieldValidation(errors, form );

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        Customer customer =  customerRepository.findByUsername(currentPrincipalName);
        model.addAttribute("name", customer.getUsername());
        model.addAttribute("reward", customer.getRewards());
        model.addAttribute("customerid", customer.getId());

        List<Card> cards = customer.getCards();
        
        if (errors.hasErrors()) {
            List<Message> messages = new ArrayList<>();
            for(ObjectError objectError: errors.getGlobalErrors()){
                messages.add(new Message(objectError.getDefaultMessage()));
            }
            model.addAttribute( "messages", messages);
            model.addAttribute("cards", cards);
            return "home";
        }

        

        Card card = new Card();
        card.setCustomer(customer);
        card.setActivated(true);
        card.setCardNumber(form.getCardnum().replace("-", ""));
        card.setCardType(cardTypeToDigits.get(form.getCardnum().substring(0,1)));
        card.setCvv(form.getCardcvv());
        card.setExpMonth(monthToNum.get(form.getCardexpmon().toLowerCase()));
        card.setExpYear(form.getCardexpyear());

        cardRepository.save(card);

        // display the newly added 
        cards.add(card);
        model.addAttribute("cards", cards);
        model.addAttribute("message", new Message("CARD succeffully uploaded"));
        return "home";

    }

    public void CheckRequiredFeilds(Errors errors, CardForm form){
        

        if(form.getCardnum() == null || form.getCardnum().length() == 0){
            errors.reject("8", "Credit Number not provided");
        }
        
        if(form.getCardexpyear() == null || form.getCardexpyear().length() == 0){
            errors.reject("9", "ExpYear not provided");
        }

        if(form.getCardcvv().length() == 0){
            errors.reject("10", "cvv not provided");
        }

      

    }


    public void FieldValidation(Errors errors, CardForm form){

       

        if(!this.monthToNum.containsKey(form.getCardexpmon().toLowerCase())){
            errors.reject("19", "Not Valid Month");
        }

    
        if(! form.getCardnum().matches("\\d{4}-\\d{4}-\\d{4}-\\d{4}")){
            errors.reject("14", "Credit Number not valid Format");
        }
        
       
 
        if(! form.getCardexpyear().matches("\\d{4}")){
            errors.reject("16", "Not valid Year format");
        }

        if( !form.getCardcvv().matches("\\d{3}")){
            errors.reject("17", "Not CVV Year format");
        }

        String regex = "^(?:(?<visa>4[0-9]{12}(?:[0-9]{3})?)|" +
        "(?<mastercard>5[1-5][0-9]{14})|" +
        "(?<discover>6(?:011|5[0-9]{2})[0-9]{12})|" +
        "(?<amex>3[47][0-9]{13})|" +
        "(?<jcb>(?:2131|1800|35[0-9]{3})[0-9]{11}))$";
 

        if(! form.getCardnum().replace("-", "").matches(regex)){
            errors.reject("20", "Only Visa, mastercard, discover, american Express are accepted");
        }


        
    }

}
