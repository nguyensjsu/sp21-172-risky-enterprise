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
public class appController {

    officeModel office = new officeModel();
    Login login = new Login();

    @GetMapping 
    public String getAction (@ModelAttribute("command") Cashier command, Model model) {
        log.info("start");
       return "cashierLogin";
    }


    @GetMapping ("/officePage")
    public String getOfficeAction (@ModelAttribute("command") Customer command, Model model) {
        
        log.info("get office page");

        String customerId = command.getCustomerId();
        if (office.checkCustomerId(customerId)){

            command.setReward(office.reward());
            command.setReward(command.getNewReward());

            return "officePage";
        } else {
            return "officePage";
        }
    }
    
    @PostMapping 
    public String postAction(@Valid @ModelAttribute("command") Cashier command,
                  Error errors, Model model, HttpServletRequest request) {
        
        log.info("check login");
               
        String id = command.getCashierId();
        String password = command.getCashierPassword();
        
        if (login.check(id, password)){

            office.setRegistered(true);
            office.setCashierId(id);

            return "officePage";
        } else {
            return "cashierLogin";
        }      

    }; 

    

    @PostMapping ("/officePage") 
    public String officeAction(@Valid @ModelAttribute("command") Customer command,
        @RequestParam(value="action", required=true) String action,
        Error errors, Model model, HttpServletRequest request) throws Exception {
        
        log.info("check customerId");    

        String customerId = command.getCustomerId();
        
        
        if (office.checkCustomerId(customerId)){

            office.setReward(command.getNewReward());
        }

        return "officePage";
        
    };

    
}
