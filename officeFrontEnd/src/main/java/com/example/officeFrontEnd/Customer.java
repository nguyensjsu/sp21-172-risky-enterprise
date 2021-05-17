package com.example.officeFrontEnd;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Customer {
    private String customerId;
    private String reward;
    private String newReward;
    

    public String getCustomerId(){return customerId;}
}
