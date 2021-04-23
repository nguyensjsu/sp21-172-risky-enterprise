
package com.example.OrderProcessing;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.RequiredArgsConstructor;


@Entity
@Data public class Card {
    private  @Id java.lang.String cardNumber;
    private String cardCode;
    private double balance;
    private boolean activated;
    private String status;

    public Card(){};

    public Card(String cardNumber, String cardCode, double balance, boolean activated, String status){
        this.cardNumber = cardNumber;
        this.cardCode = cardCode;
        this.balance = balance;
        this.activated = activated;
        this.status = status;
    }
}
