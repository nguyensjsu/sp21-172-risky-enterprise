package com.example.springstarbucksapi;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.server.ResponseStatusException;

import org.springframework.dao.EmptyResultDataAccessException;

@RestController
public class StarbucksController {
    private final CardRepository cardRepository;

    StarbucksController(CardRepository cardRepository){
        this.cardRepository = cardRepository;
    }
    
    class Message {
        private String status;

        public Message(String string) {
        }

        public String getStatus(){
            return status;
        }

        public void setStatus(String msg){
            status = msg;
        }
    }
    @GetMapping("/ping")
    Message newMessage() {
        Message healthCheck = new Message("Starbucks API version 1.0 alive!");
        return healthCheck;
    }


    @GetMapping("/cards")
    List<Card> all(){
        return cardRepository.findAll();
    }

    @GetMapping("/cards/{num}")
    Card newCardById(@PathVariable String num, HttpServletResponse response){
        System.out.println(" I am " + num);
        Card cardFound = cardRepository.findById(num)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Error. Card Number not found"));

        return cardFound;
    }

    @PostMapping("/cards")
    Card newCard(@RequestBody Card card){
        return cardRepository.save(card);
    }

    @PostMapping("/card/activate/{num}/{code}")
    Card activateCard(@PathVariable String num, @PathVariable String code, HttpServletResponse response){
            Card cardFound = cardRepository.findById(num)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Error. Card Number not found"));            
            if(cardFound.getCardCode().equals(code)){
                cardFound.setActivated(true);
                cardRepository.save(cardFound);
            }
            else{
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error. Card code do not Match");
            }
        
            return cardFound;

    }


    @DeleteMapping("/cards")
        Message deleteAllCards(){
            cardRepository.deleteAll();
            Message status = new Message("deleted All Cards");
            return status;
    }


    

}
