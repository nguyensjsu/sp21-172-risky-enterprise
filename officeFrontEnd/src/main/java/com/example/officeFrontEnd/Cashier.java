package com.example.officeFrontEnd;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Cashier {
    private String cashierId;
    private String cashierPassword;

    public String getCashierId() {return cashierId;}
    public String getCashierPassword(){return cashierPassword;}

}
