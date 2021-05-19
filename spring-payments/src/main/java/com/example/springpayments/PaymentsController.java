package com.example.springpayments;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.util.Optional;
import java.time.*; 
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64.Encoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


import lombok.extern.slf4j.Slf4j;
import com.example.springcybersource.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;
import org.springframework.validation.ObjectError;

import java.lang.*;



@Slf4j
@Controller
@RequestMapping("/")
public class PaymentsController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CardRepository cardRepository;

    @Value("${cybersource.apihost}") private String apiHost ;
    @Value("${cybersource.merchantkeyid}") private String merchantKeyId ;
    @Value("${cybersource.merchantsecretkey}") private String merchantsecretKey ;
    @Value("${cybersource.merchantid}") private String merchantId ;

    PaymentsController(){}


    @GetMapping("/ping")
    @ResponseBody
    public Message ping(){
        Message healthCheck = new Message("Payment Processing API alive v1 !");
        return healthCheck;
    }

    @GetMapping("/cards/{customerId}")
    @ResponseBody
    public List<Card> getCustomerCards(@PathVariable Long customerId)throws ResourceNotFoundException{
        //return cardRepository.findByCustomerId(customerId);
        return customerRepository.findById(customerId).map(customer -> {
            return customer.getCards();
        }).orElseThrow(() -> new ResourceNotFoundException("Csutomer do not exist"));
    }


    @PostMapping("/card/{customerId}")
    @ResponseBody
    public Card createCard(@PathVariable Long customerId, @RequestBody Card card)throws ResourceNotFoundException{
        System.out.print("Card => " + card);
        return customerRepository.findById(customerId).map(customer -> {
            card.setCustomer(customer);
            Card savedCard = cardRepository.save(card);
            System.out.print("Saved card =" + savedCard);
            return savedCard;
        }).orElseThrow(() -> new ResourceNotFoundException("Csutomer do not exist"));
    }


    @PostMapping("/card/activate/{cardNumber}/{customerId}")
    @ResponseBody
    public Card activateCard(@PathVariable String cardNumber, @PathVariable Long customerId)throws ResourceNotFoundException{
        return  cardRepository.findByCardNumberAndCustomerId(cardNumber, customerId).map(card -> {
                card.setActivated(true);
                return cardRepository.save (card);
        }).orElseThrow(() -> new ResourceNotFoundException("Card do not exist"));
    }

    @DeleteMapping("/card/{cardNumber}/{customerId}")
    @ResponseBody
    public Message deleteCard(@PathVariable String cardNumber, @PathVariable Long customerId) throws ResourceNotFoundException{
        return cardRepository.findByCardNumberAndCustomerId(cardNumber, customerId).map(card -> {
            cardRepository.delete(card);
            return new Message("Card Number : " + cardNumber + " Deleted");
        }).orElseThrow(() -> new ResourceNotFoundException("card do not exist"));
    }

    @GetMapping("/reward/{customerId}")
    @ResponseBody
    public Message getRewards(@PathVariable Long customerId) throws ResourceNotFoundException{
        return customerRepository.findById(customerId).map(customer -> {
            return new Message("$"+customer.getRewards());
        }).orElseThrow(() -> new ResourceNotFoundException("Csutomer do not exist"));
    }

    @PostMapping("/reward/{customerId}/add/{amount}")
    @ResponseBody
    public Message addRewards(@PathVariable Long customerId, @PathVariable double amount) throws ResourceNotFoundException{
        return customerRepository.findById(customerId).map(customer -> {
            customer.setRewards(customer.getRewards()+amount);
            customerRepository.save(customer);
            return new Message("$"+customer.getRewards());
        }).orElseThrow(() -> new ResourceNotFoundException("Csutomer do not exist"));
    }


    @PostMapping("/pay/reward/{customerId}/amount/{amount}")
    @ResponseBody
    public Message payWithRewards(@PathVariable Long customerId, @PathVariable double amount) throws ResourceNotFoundException{
        return customerRepository.findById(customerId).map(customer -> {
            if(customer.getRewards() < amount){
                return new Message("INSUFFICIENT BALANCE");
            }else{
                customer.setRewards(customer.getRewards()-amount);
                customerRepository.save(customer);
                return new Message("SUCCESS");
            }
        }).orElseThrow(() -> new ResourceNotFoundException("Csutomer do not exist"));
    }

    @PostMapping("/pay/card/{cardNumber}/amount/{amount}")
    @ResponseBody
    public Message payWithCard(@PathVariable  String cardNumber , @PathVariable String amount) throws ResourceNotFoundException{
        Card card = cardRepository.findById(cardNumber).orElseThrow(() -> new ResourceNotFoundException("Card do not exist"));
        Customer customer = customerRepository.findById(card.getCustomer()).orElseThrow(() -> new ResourceNotFoundException("Customer do not exist"));

        String orderNum = String.valueOf(System.currentTimeMillis());

        AuthRequest auth = new AuthRequest() ;
		auth.reference = "Order Number: " + orderNum;
		auth.billToFirstName = customer.getFirstName();
		auth.billToLastName = customer.getLastName() ;
		auth.billToAddress = customer.getAddress() ;
		auth.billToCity = customer.getCity() ;
		auth.billToState = customer.getState() ;
		auth.billToZipCode = customer.getZip();
		auth.billToPhone = customer.getPhoneNumber() ;
		auth.billToEmail = customer.getEmail() ;
		auth.transactionAmount = amount;
		auth.transactionCurrency = "USD" ;
		auth.cardNumnber = card.getCardNumber() ;
		auth.cardExpMonth = card.getExpMonth();
		auth.cardExpYear = card.getExpYear(); ;
		auth.cardCVV = card.getCvv() ;
		auth.cardType = card.getCardType() ;

        CyberSourceAPI api = new CyberSourceAPI() ;
		CyberSourceAPI.setHost( apiHost ) ;
		CyberSourceAPI.setKey( merchantKeyId ) ;
		CyberSourceAPI.setSecret(merchantsecretKey ) ;
		CyberSourceAPI.setMerchant( merchantId ) ;

        boolean authValid = false ;
		AuthResponse authResponse = new AuthResponse() ;
		System.out.println("\n\nAuth Request: " + auth.toJson() ) ;
		authResponse = api.authorize(auth) ;
		System.out.println("\n\nAuth Response: " + authResponse.toJson() ) ;
		if ( authResponse.status.equals("AUTHORIZED") ) {
			authValid = true ;
		}
        else{
            return new Message(authResponse.getReason());
        }
		
		boolean captureValid = false ;
		CaptureRequest capture = new CaptureRequest() ;
		CaptureResponse captureResponse = new CaptureResponse() ;
		if ( authValid ) {
			capture.reference = "Order Number: "+orderNum ;
			capture.paymentId = authResponse.id ;
			capture.transactionAmount = amount ;
			capture.transactionCurrency = "USD" ;
			System.out.println("\n\nCapture Request: " + capture.toJson() ) ;
			captureResponse = api.capture(capture) ;
			System.out.println("\n\nCapture Response: " + captureResponse.toJson() ) ;
			if ( captureResponse.status.equals("PENDING") ) {
				captureValid = true ;
                return new Message("SUCEESS");
			}
            else{
                return new Message(captureResponse.getReason());
            }

		}
        return new Message("FAILURE");
    }

}