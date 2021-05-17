package com.example.officeFrontEnd;




public class officeModel {
    private boolean registered;
    private String casierId;
    private String reward;
    private String customerId;

    public officeModel () {
        registered = false;
        reward = "";
        customerId = "111";
    }
    public void setCashierId (String cashierId){
        this.casierId = cashierId;
    }

    public String getCashierId() {
        return casierId;
    }

    public void setRegistered(boolean b){
        registered = b;
    }

    public void setReward(String newReward){
        reward = newReward;
    }

    public boolean isRegistered() {
        return registered;
    }

    public String reward(){
        return reward;
    }

    public boolean checkCustomerId(String customerId){
        // if check customerid passed 
        if(customerId.equals(this.customerId)){
            return true;
        }
        // reward = customerid.reward
        return false;

        //else return false;
    }

    public String getCustomerId(){
        return customerId;
    }
    
}
