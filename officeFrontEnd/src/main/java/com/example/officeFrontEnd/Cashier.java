package com.example.officeFrontEnd;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Cashier {
    private String cashierId;
    private String cashierPassword;
}
