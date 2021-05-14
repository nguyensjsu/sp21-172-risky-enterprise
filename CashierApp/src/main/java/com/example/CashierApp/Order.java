package com.example.CashierApp;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Order {
    private String drink;
    private String size;
    private String milk;
    private String message;
    private String price;

    //public int calculatePrice(){
    //    return 0;
    //}
}
