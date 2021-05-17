package com.example.officeFrontEnd;

public class Login {
    private String cashierId = "lisa";
    private String cashierPassword = "123";

    public boolean check (String id, String password){
        
        
        // check into database
        if (cashierId.equals(id) && cashierPassword.equals(password)){
            return true;
        }
        else {
                return false;
        }
        
    }
    
}
