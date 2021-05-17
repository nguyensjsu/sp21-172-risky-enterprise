package com.example.officeFrontEnd;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.util.Optional;
import java.time.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64.Encoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.validation.BeanPropertyBindingResult;

import org.springframework.beans.factory.annotation.Value;

import lombok.extern.slf4j.Slf4j;
import lombok.Getter;
import lombok.Setter;


@Slf4j
@Controller
@RequestMapping("/")
public class officeController {

    officeModel office = new officeModel();

    @GetMapping
    public String getAction (@ModelAttribute("command") Cashier command, Model model) {
        log.info("start");
       return "cashierLogin";
    }


    @PostMapping ("/?cashierId=&cashierPassword=&action=login")
    public String postAction(@Valid @ModelAttribute("command") Cashier command,
                  Error errors, Model model, HttpServletRequest request) {
        
        log.info("check login");
               
        Login login = new Login();
        String id = command.getCashierId();
        String password = command.getCashierPassword();
        
        return "cashierLogin";

    }; 
/*
    @GetMapping("/officePage") 
    public String getOfficeinfo (@ModelAttribute("command") Cashier command, Model model) throws Exception {

        return "officePage";
    }
    */
    

/*
        if(login.check(id, password)){
            // check sucessful  
            office.setRegistered(true);
            return "officeFrontend";
        } else {
            // check unsucessful
            return "cashierLogin";
        }    
        */
        

   

/*
    @PostMapping ("/officePage") 
    public String officeAction(@Valid @ModelAttribute("command") FrontEndEntity command,
        @RequestParam(value="action", required=true) String action,
        Error errors, Model model, HttpServletRequest request) throws Exception {
            
        String customerId = command.getCustomerId();
        
        if (office.checkCustomerId(customerId)){

            office.setReward(command.getNewReward());
            return "officePage"; 
        }else{
            return "officePage";
        }
        
    };
*/
    
}
