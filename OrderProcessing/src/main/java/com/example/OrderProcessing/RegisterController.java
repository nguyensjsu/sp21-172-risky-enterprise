package com.example.OrderProcessing;

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

import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;



@RestController
public class RegisterController {

    private final OrderRepository orderRepository;

    @Value("${paymentprocessing.serverport}")private String serverPort;
    @Value("${paymentprocessing.apihost}") private String apiHost;

    RegisterController(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }
    
    @GetMapping("/ping")
    Message newMessage() {
        Message healthCheck = new Message();
        healthCheck.setMessage("Starbucks API version 1.0 alive!");
        return healthCheck;
    }

    @GetMapping("/order/register/{regId}")
    List<Order> allActiveOrdersRegID(@PathVariable Long regId){
        // ArrayList<Long> ids = new ArrayList<>();
        // ids.add(regId);

        List<Order> orders = orderRepository.findAllByRegId(regId);
        List<Order> activeOrders = orders.stream().filter(order -> order.getStatus().equals(OrderStatus.READY_FOR_PAYMENT)).collect(Collectors.toList());

        return activeOrders;
    }

    @PostMapping("/order/register/{regId}")
    Order addOrder(@PathVariable Long regId, @RequestBody Order order){
        order.setRegId(regId);
        order.setStatus(OrderStatus.READY_FOR_PAYMENT);

        orderRepository.save(order);
        return order;
    }

    @PostMapping("/order/register/{regid}/pay/reward/{customerId}")
    Message makePaymentRewards(@PathVariable Long regid, @PathVariable String customerId){

        List<Order> allOrdersReadyForPayment = allActiveOrdersRegID(regid);
        double amount = 0;
        for(Order order: allOrdersReadyForPayment){
            amount+=order.getPrice();
        }

        String url = "http://" + apiHost + ":" + serverPort + "/pay/reward/" + customerId + "/amount/" + amount;

        PaymentProcessingAPI api = new PaymentProcessingAPI();
        api.setHost(apiHost);

        PostResponse res = api.sendPost(url);
        System.out.print("Result =>" + res.getResponse().substring(8, 15));
        if(res.getCode() == 200 && res.getResponse().substring(8, 15).equals("SUCCESS")){
            // set all ready for payment orders are payed
            for(Order order: allOrdersReadyForPayment){
                order.setStatus(OrderStatus.PAYED);
                orderRepository.save(order);
            }  

            return new Message("SUCCESS");
        }
        else{
            return new Message("FAILURE");
        }        
    }


    @PostMapping("/order/register/{regid}/pay/card/{cardnum}")
    Message makePaymentCard(@PathVariable Long regid, @PathVariable String cardnum){

        List<Order> allOrdersReadyForPayment = allActiveOrdersRegID(regid);
        double amount = 0;
        for(Order order: allOrdersReadyForPayment){
            amount+=order.getPrice();
        }

        if(amount-0.0 <= 0.1){
            return new Message("SUCCESS");
        }

        String url = "http://" + apiHost + ":" + serverPort + "/pay/card/" + cardnum + "/amount/" + amount;

        PaymentProcessingAPI api = new PaymentProcessingAPI();
        api.setHost(apiHost);

        PostResponse res = api.sendPost(url);
        System.out.print("Result =>" + res.getResponse());
        if(res.getCode() == 200 && res.getResponse().substring(8, 15).equals("SUCEESS")){
            // set all ready for payment orders are payed
            for(Order order: allOrdersReadyForPayment){
                order.setStatus(OrderStatus.PAYED);
                orderRepository.save(order);
            }  

            return new Message("SUCCESS");
        }
        else{
            return new Message("FAILURE");
        }        
    }


    @DeleteMapping("/order/register/{regId}")
        Message deleteOrders(@PathVariable Long regId){
            
            List<Order> orders = orderRepository.findAllByRegId(regId);
            List<Order> deleteOrders = orders.stream().filter(order -> order.getStatus().equals(OrderStatus.READY_FOR_PAYMENT)).collect(Collectors.toList());


            orderRepository.deleteAll(deleteOrders);

            Message status = new Message("All READY_FOR_PAYMENT order deleted from" + regId);

            return status;
    }
    
    @DeleteMapping("/orders")
        Message deleteAllOrders(){
            orderRepository.deleteAll();
            Message status = new Message("deleted All orders" );
            return status;
    }


}