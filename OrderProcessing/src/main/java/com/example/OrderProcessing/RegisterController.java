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
import javax.servlet.http.HttpServletRequest;


import org.springframework.web.server.ResponseStatusException;

import org.springframework.dao.EmptyResultDataAccessException;

import org.springframework.web.bind.annotation.GetMapping; 
import org.springframework.web.bind.annotation.RequestParam; 

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;



@RestController
public class RegisterController {

    private final OrderRepository orderRepository;
    private final CardRepository cardRepository;

    RegisterController(OrderRepository orderRepository, CardRepository cardRepository){
        this.orderRepository = orderRepository;
        this.cardRepository = cardRepository;
    }
    
    // @GetMapping("/ping")
    // Message newMessage() {
    //     Message healthCheck = new Message();
    //     healthCheck.setStatus("Starbucks API version 1.0 alive!");
    //     return healthCheck;
    // }

    // @GetMapping("/cards")
    // List<Card> all(){
    //     return cardRepository.findAll();
    // }

    // @PostMapping("/cards")
    // Card newCard(@RequestBody Card card){
    //     return cardRepository.save(card);
    // }

    // @GetMapping("/cards/{num}")
    // Card newCardById(@PathVariable String num, HttpServletResponse response){
    //     System.out.println(" I am " + num);
    //     Card cardFound = cardRepository.findById(num)
    //     .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Error. Card Number not found"));

    //     return cardFound;
    // }


    @GetMapping("/order/register/{regId}")
    List<Order> allActiveOrdersRegID(@PathVariable Long regId){
        ArrayList<Long> ids = new ArrayList<>();
        ids.add(regId);

        List<Order> orders = orderRepository.findAllById(ids);
        List<Order> activeOrders = orders.stream().filter(order -> order.getStatus().equals(OrderStatus.READY_FOR_PAYMENT) || order.getStatus().equals(OrderStatus.PAYED)).collect(Collectors.toList());

        return activeOrders;
    }


    @GetMapping("/orders")
    List<Order> allActiveOrders(){
        List<Order> orders = orderRepository.findAll();
        List<Order> activeOrders = orders.stream().filter(order -> order.getStatus().equals(OrderStatus.READY_FOR_PAYMENT) || order.getStatus().equals(OrderStatus.PAYED)).collect(Collectors.toList());

        return activeOrders;
    }

    @PostMapping("/order/register/{regId}")
    Order addCard(@PathVariable Long regId, @RequestBody Order order){
        order.setRegId(regId);
        order.setTotal(2.289);
        order.setStatus(OrderStatus.READY_FOR_PAYMENT);

        orderRepository.save(order);
        return order;
    }

    @PostMapping("/order/register/{regid}/pay/{cardnum}")
    Card CardById(@PathVariable Long regid, @PathVariable String cardnum){

        Card cardFound = cardRepository.findById(cardnum)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Error. Card Number not found"));

        if(!cardFound.isActivated()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Card not active");
        }

        List<Order> activeOrders = allActiveOrdersRegID(regid);

        double currentCardBalance = cardFound.getBalance();

        for(Order order : activeOrders)
        {
            if(currentCardBalance - order.getTotal() < 0.001){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Error. Insufficient Balance");
            }
            else{
                currentCardBalance-=order.getTotal();
                order.setStatus(OrderStatus.PAYED);
                System.out.print(orderRepository.save(order).toString());
            }
        }

        cardFound.setBalance(currentCardBalance);

        cardRepository.save(cardFound);
        return cardFound;
    }


    @DeleteMapping("/order/register/{regId}")
        Message deleteOrders(@PathVariable Long regId){
            ArrayList<Long> ids = new ArrayList<>();
            ids.add(regId);
    
            List<Order> orders = orderRepository.findAllById(ids);
            List<Order> deleteOrders = orders.stream().filter(order -> order.getStatus().equals(OrderStatus.READY_FOR_PAYMENT)).collect(Collectors.toList());


            orderRepository.deleteAll(deleteOrders);

            Message status = new Message(" All READY_FOR_PAYMENT order deleted from" + regId);

            return status;
    }
    
    @DeleteMapping("/orders")
        Message deleteAllOrders(){
            orderRepository.deleteAll();
            Message status = new Message("deleted All orders" );
            return status;
    }


}
