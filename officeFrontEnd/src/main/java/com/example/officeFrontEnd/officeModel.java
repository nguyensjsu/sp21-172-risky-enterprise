package com.example.officeFrontEnd;




public class officeModel {
    private boolean registered;
    private String reward;
    private String customerId;

    public officeModel () {
        registered = false;
        reward = "";
        customerId = "";
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
            this.customerId = customerId;
        // reward = customerid.reward
        return true;

        //else return false;
    }

    public String getCustomerId(){
        return customerId;
    }
    
}
