package com.example.CashierApp;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Order extends Payload {
    private int regId;
    private String drink;
    private String size;
    private String milk;
    private String status;
    private double price;
    private int id;
    private String cardnumber;
    private String rewardnumber;
}
