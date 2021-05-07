
package com.example.springpayments;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.OneToMany;


import lombok.Data;


@Entity
@Table(name="CUSTOMER")
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String firstName;
    private String lastName;
    private String address;
    private String userName;
    private String password;
    private String city;
    private String state;
    private String zip;
    private String phoneNumber;
    private String email;
    private double rewards;


    @OneToMany(mappedBy="customer", fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Card> cards;
}
