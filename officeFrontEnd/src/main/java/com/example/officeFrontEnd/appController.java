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

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import lombok.extern.slf4j.Slf4j;
import lombok.Getter;
import lombok.Setter;


@Slf4j
@Controller
@RequestMapping("/")
public class appController {

    private String tmpCusName;
    private String tmpReward;


    officeModel office = new officeModel();


        @GetMapping ("/officePage")
        public String getOfficeAction (@ModelAttribute("command") Command command, Model model) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentEmployeeName = authentication.getName();
            
            log.info("get office page");
            model.addAttribute("employee", currentEmployeeName);
            
            return "officePage";
            
        }
        
        @PostMapping ("/officePage") 
        public String officeAction(@Valid @ModelAttribute("command") Command command) {
            
            log.info("check customerId");    

            String customerId = command.getCustomerId();
            log.info(customerId);

            return "officePage";
            
        }

    
}
