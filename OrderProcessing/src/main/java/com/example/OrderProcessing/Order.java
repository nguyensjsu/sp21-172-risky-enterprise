package com.example.springstarbucksapi;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Table;


@Data
@Entity
@Table(name = "CUSTOMER_ORDER")
public class Order {
    
    //private @Id @GeneratedValue Long Id;

    private @Id Long regId;
    private Drink drink;
    private Milk milk;
    private CoffeeSize size;
    private double price;
    private OrderStatus status;

    public Order(){}


    public Order(String drink, String milk, String size, double price){
        System.out.println(drink.toUpperCase());
        this.drink = Drink.valueOf(drink.toUpperCase());
        this.milk = Milk.valueOf(milk.toUpperCase());
        this.size = CoffeeSize.valueOf(size.toUpperCase());
        this.price = price;
    }

    
}
