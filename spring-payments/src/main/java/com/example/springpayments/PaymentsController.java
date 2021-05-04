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
    public Message addRewards(@PathVariable Long customerId, @PathVariable int amount) throws ResourceNotFoundException{
        return customerRepository.findById(customerId).map(customer -> {
            customer.setRewards(customer.getRewards()+amount);
            return new Message("$"+customer.getRewards());
        }).orElseThrow(() -> new ResourceNotFoundException("Csutomer do not exist"));
    }


    @PostMapping("/pay/reward/{customerId}/amount/{amount}")
    @ResponseBody
    public Message payWithRewards(@PathVariable Long customerId, @PathVariable int amount) throws ResourceNotFoundException{
        return customerRepository.findById(customerId).map(customer -> {
            if(customer.getRewards() < amount){
                return new Message("INSUFFICIENT BALANCE");
            }else{
                customer.setRewards(customer.getRewards()-amount);
                customerRepository.save(customer);
                return new Message("PAYMENT SUCCESSFULL");
            }
        }).orElseThrow(() -> new ResourceNotFoundException("Csutomer do not exist"));
    }

    @PostMapping("/pay/card/{cardNumber}/amount/{amount}")
    @ResponseBody
    public Message payWithCard(@PathVariable  String cardNumber , @PathVariable int amount) throws ResourceNotFoundException{
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
		auth.cardNumnber = customer.getCreditNumber() ;
		auth.cardExpMonth = monthToNum.get(customer.getExpMonth().toLowerCase());
		auth.cardExpYear = customer.getExpYear(); ;
		auth.cardCVV = customer.getCvv() ;
		auth.cardType = cardTypeToDigits.get(customer.getCreditNumber().substring(0,1)) ;

    //     CyberSourceAPI api = new CyberSourceAPI() ;
	// 	CyberSourceAPI.setHost( apiHost ) ;
	// 	CyberSourceAPI.setKey( merchantKeyId ) ;
	// 	CyberSourceAPI.setSecret(merchantsecretKey ) ;
	// 	CyberSourceAPI.setMerchant( merchantId ) ;

    //     boolean authValid = false ;
	// 	AuthResponse authResponse = new AuthResponse() ;
	// 	System.out.println("\n\nAuth Request: " + auth.toJson() ) ;
	// 	authResponse = api.authorize(auth) ;
	// 	System.out.println("\n\nAuth Response: " + authResponse.toJson() ) ;
	// 	if ( authResponse.status.equals("AUTHORIZED") ) {
	// 		authValid = true ;
	// 	}
    //     else{
    //         model.addAttribute("message", authResponse.getReason());
    //         return "creditcards";
    //     }
		
	// 	boolean captureValid = false ;
	// 	CaptureRequest capture = new CaptureRequest() ;
	// 	CaptureResponse captureResponse = new CaptureResponse() ;
	// 	if ( authValid ) {
	// 		capture.reference = "Order Number: "+orderNum ;
	// 		capture.paymentId = authResponse.id ;
	// 		capture.transactionAmount = "30.00" ;
	// 		capture.transactionCurrency = "USD" ;
	// 		System.out.println("\n\nCapture Request: " + capture.toJson() ) ;
	// 		captureResponse = api.capture(capture) ;
	// 		System.out.println("\n\nCapture Response: " + captureResponse.toJson() ) ;
	// 		if ( captureResponse.status.equals("PENDING") ) {
	// 			captureValid = true ;
	// 		}
    //         else{
    //             model.addAttribute("message", captureResponse.getReason());
    //             return "creditcards";
    //         }

	// 	}

    //     paymentRepository.save(customer);
    //     model.addAttribute( "message", "Thanks for your payment!! Your Order Number is: " + orderNum ) ;

    //     return "creditcards";
    }













    // @PostMapping
    // public String postAction(@Valid @ModelAttribute("customer") Paymentscustomer customer,  
    //                         @RequestParam(value="action", required=true) String action,
    //                         Errors errors, Model model, HttpServletRequest request) {
    
    //     log.info( "Action: " + action ) ;
    //     log.info( "customer: " + customer ) ;

    //     /* validate Form */
    //     CheckRequiredFeilds(errors, customer);
    //     FieldValidation(errors, customer);
     
    //     if (errors.hasErrors()) {
    //         List<Message> messages = new ArrayList<>();
    //         for(ObjectError objectError: errors.getGlobalErrors()){
    //             log.info(objectError.getDefaultMessage());
    //             messages.add(new Message(objectError.getDefaultMessage()));
    //         }
    //         model.addAttribute( "messages", messages) ;
    //         return "creditcards";
    //     }

    //     /* Make payment using cyber source */

    //  String orderNum = String.valueOf(System.currentTimeMillis());

    //     AuthRequest auth = new AuthRequest() ;
	// 	auth.reference = "Order Number: " + orderNum;
	// 	auth.billToFirstName = customer.getFirstname();
	// 	auth.billToLastName = customer.getLastname() ;
	// 	auth.billToAddress = customer.getAddress() ;
	// 	auth.billToCity = customer.getCity() ;
	// 	auth.billToState = customer.getState() ;
	// 	auth.billToZipCode = customer.getZip();
	// 	auth.billToPhone = customer.getPhoneNumber() ;
	// 	auth.billToEmail = customer.getEmail() ;
	// 	auth.transactionAmount = "30.00" ;
	// 	auth.transactionCurrency = "USD" ;
	// 	auth.cardNumnber = customer.getCreditNumber() ;
	// 	auth.cardExpMonth = monthToNum.get(customer.getExpMonth().toLowerCase());
	// 	auth.cardExpYear = customer.getExpYear(); ;
	// 	auth.cardCVV = customer.getCvv() ;
	// 	auth.cardType = cardTypeToDigits.get(customer.getCreditNumber().substring(0,1)) ;

    //     CyberSourceAPI api = new CyberSourceAPI() ;
	// 	CyberSourceAPI.setHost( apiHost ) ;
	// 	CyberSourceAPI.setKey( merchantKeyId ) ;
	// 	CyberSourceAPI.setSecret(merchantsecretKey ) ;
	// 	CyberSourceAPI.setMerchant( merchantId ) ;

    //     boolean authValid = false ;
	// 	AuthResponse authResponse = new AuthResponse() ;
	// 	System.out.println("\n\nAuth Request: " + auth.toJson() ) ;
	// 	authResponse = api.authorize(auth) ;
	// 	System.out.println("\n\nAuth Response: " + authResponse.toJson() ) ;
	// 	if ( authResponse.status.equals("AUTHORIZED") ) {
	// 		authValid = true ;
	// 	}
    //     else{
    //         model.addAttribute("message", authResponse.getReason());
    //         return "creditcards";
    //     }
		
	// 	boolean captureValid = false ;
	// 	CaptureRequest capture = new CaptureRequest() ;
	// 	CaptureResponse captureResponse = new CaptureResponse() ;
	// 	if ( authValid ) {
	// 		capture.reference = "Order Number: "+orderNum ;
	// 		capture.paymentId = authResponse.id ;
	// 		capture.transactionAmount = "30.00" ;
	// 		capture.transactionCurrency = "USD" ;
	// 		System.out.println("\n\nCapture Request: " + capture.toJson() ) ;
	// 		captureResponse = api.capture(capture) ;
	// 		System.out.println("\n\nCapture Response: " + captureResponse.toJson() ) ;
	// 		if ( captureResponse.status.equals("PENDING") ) {
	// 			captureValid = true ;
	// 		}
    //         else{
    //             model.addAttribute("message", captureResponse.getReason());
    //             return "creditcards";
    //         }

	// 	}

    //     paymentRepository.save(customer);
    //     model.addAttribute( "message", "Thanks for your payment!! Your Order Number is: " + orderNum ) ;

    //     return "creditcards";
    // }


    // public void CheckRequiredFeilds(Errors errors, Paymentscustomer customer){
    //     if(customer.getFirstname().length() == 0){
    //         errors.reject("1", "FirstName not provided");
    //     }

    //     if(customer.getLastname().length() == 0){
    //         errors.reject("2", "LastName not provided");
    //     }

    //     if(customer.getAddress().length() == 0){
    //         errors.reject("3", "Address not provided");
    //     }

    //     if(customer.getCity().length() == 0){
    //         errors.reject("4", "City not provided");
    //     }

    //     if(customer.getState().length() == 0){
    //         errors.reject("5", "State not provided");
    //     }

    //     if(customer.getZip().length() == 0){
    //         errors.reject("6", "ZIP not provided");
    //     }

    //     if(customer.getPhoneNumber() == null || customer.getPhoneNumber().length() == 0){
    //         errors.reject("7", "Phone Number not provided");
    //     }

    //     if(customer.getCreditNumber() == null || customer.getCreditNumber().length() == 0){
    //         errors.reject("8", "Credit Number not provided");
    //     }
        
    //     if(customer.getExpYear() == null || customer.getExpYear().length() == 0){
    //         errors.reject("9", "ExpYear not provided");
    //     }

    //     if(customer.getCvv().length() == 0){
    //         errors.reject("10", "cvv not provided");
    //     }

    //     if(customer.getEmail().length() == 0){
    //         errors.reject("11", "Email not provided");
    //     }


    // }


    // public void FieldValidation(Errors errors, Paymentscustomer customer){

    //     Set<String> setStates = new HashSet<String>(Arrays.asList(states));
    //     if(!(setStates.contains(customer.getState()))){
    //         errors.reject("18", "Not valid US state");
    //     }

    //     if(!this.monthToNum.containsKey(customer.getExpMonth().toLowerCase())){
    //         errors.reject("19", "Not Valid Month");
    //     }

    //     if(!(customer.getZip().matches("\\d{5}"))){
    //         errors.reject("12", "Invalid ZIP Code");
    //     }

    //     if(! customer.getPhoneNumber().matches("[(]\\d{3}[)] \\d{3}-\\d{4}")){
    //         errors.reject("13", "Invalid Phone number Format");
    //     }

    //     if(! customer.getCreditNumber().matches("\\d{4}-\\d{4}-\\d{4}-\\d{4}")){
    //         errors.reject("14", "Credit Number not valid Format");
    //     }
        
    //     if(! customer.getEmail().matches("^(.+)@(.+)$")){
    //         errors.reject("15", "Not valid Email format");
    //     }
 
    //     if(! customer.getExpYear().matches("\\d{4}")){
    //         errors.reject("16", "Not valid Year format");
    //     }

    //     if( !customer.getCvv().matches("\\d{3}")){
    //         errors.reject("17", "Not CVV Year format");
    //     }

    //     String regex = "^(?:(?<visa>4[0-9]{12}(?:[0-9]{3})?)|" +
    //     "(?<mastercard>5[1-5][0-9]{14})|" +
    //     "(?<discover>6(?:011|5[0-9]{2})[0-9]{12})|" +
    //     "(?<amex>3[47][0-9]{13})|" +
    //     "(?<jcb>(?:2131|1800|35[0-9]{3})[0-9]{11}))$";
 

    //     if(! customer.getCreditNumber().replace("-", "").matches(regex)){
    //         errors.reject("20", "Only Visa, mastercard, discover, american Express are accepted");
    //     }


        
    // }

}