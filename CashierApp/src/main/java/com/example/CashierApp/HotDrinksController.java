package com.example.CashierApp;
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
@RequestMapping("/hotdrinks")
public class HotDrinksController {
    @GetMapping
    public String getAction(@ModelAttribute("command") Order command, Model model) {
        log.info("Hot Drinks Page");
        return "hotdrinks";

    }

    @PostMapping
    public String postAction(@Valid @ModelAttribute("command") Order command,
        Errors errors, Model model, HttpServletRequest request) {
    
        log.info(command.getMilk());
        log.info(command.getSize());
        log.info(command.getMessage());
        log.info(command.getDrink());
                                
    
        return "hotdrinks";

    }
}
