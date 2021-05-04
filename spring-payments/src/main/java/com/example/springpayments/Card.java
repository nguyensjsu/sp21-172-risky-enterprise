
package com.example.springpayments;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Id;




import lombok.Data;



@Entity
@Table(name="CARDS")
@Data public class Card {

    @ManyToOne
    @JoinColumn(name="customer_id", nullable=false)
    private Customer customer;

    @Id
    @Column(name = "cardNumber")
    private String cardNumber;

    private String cardCode;
    private double balance;
    private boolean activated;

    public Card(){};

    public Card(String cardNumber, String cardCode, double balance, boolean activated){
        this.cardNumber = cardNumber;
        this.cardCode = cardCode;
        this.balance = balance;
        this.activated = activated;
    }

    public void setCustomer(Customer customer){
        this.customer = customer;
    }

    public Long getCustomer(){
        if(this.customer!= null){
            return this.customer.getId();
        }
        else{
            return null;
        }
    }

}
