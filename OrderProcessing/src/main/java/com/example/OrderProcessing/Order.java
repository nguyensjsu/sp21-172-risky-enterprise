package com.example.OrderProcessing;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;


import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Table;


@Data
@Entity
@Table(name = "CUSTOMER_ORDER")
public class Order {
    
    private @Id @GeneratedValue Long Id;

    private Long regId;
    private Drink drink;
    private Milk milk;
    private CoffeeSize size;
    private double price;
    private OrderStatus status;

    public Order(){}


    public Order(Long regId, String drink, String milk, String size, double price){
        this.regId = regId;
        this.drink = Drink.valueOf(drink.toUpperCase());
        this.milk = Milk.valueOf(milk.toUpperCase());
        this.size = CoffeeSize.valueOf(size.toUpperCase());
        this.price = price;
    }

    
}
