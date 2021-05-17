package com.example.officeFrontEnd;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Customer {
    private String customerId = "default";
    private String reward = "88";
    private String newReward;
    

    public String getCustomerId(){return customerId;}

    public String getNewReward(){ 
        
        reward = newReward;
        return newReward;
    }
}
