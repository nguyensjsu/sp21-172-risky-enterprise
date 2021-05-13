
package com.example.CustomerFrontEnd;


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

    private boolean activated;
    private String expMonth;
    private String expYear;
    private String cvv;
    private String cardType;

    public Card(){};

    public Card(String cardNumber, boolean activated, String expMonth, String expYear, String cardType, String cvv){
        this.cardNumber = cardNumber;
        this.activated = activated;
        this.expMonth = expMonth;
        this.expYear = expYear;
        this.cvv = cvv;
        this.cardType = cardType;
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
