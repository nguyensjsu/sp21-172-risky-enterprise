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
@RequestMapping("/")
public class CashierAppController {
    private CyberSourceAPI api = new CyberSourceAPI();
    private String message;

    @GetMapping
    public String getAction(@ModelAttribute("command") Order command, Model model) throws Exception {
        log.info("Application Started");

        OrderResponse response = new OrderResponse();
        response = api.authorize(command, "order/register/5012349", "GetOrder");
        System.out.println("\n\nAuth Response: " + response.toJson());

        message = response.reply;
        if (message.equals("[]"))
            message = "No Orders Placed";

        model.addAttribute("message", message);
        return "order" ;
    }

    @PostMapping
    public String postAction(@Valid @ModelAttribute("command") Order command,
        @RequestParam(value="action", required=true) String action,
        Errors errors, Model model, HttpServletRequest request) throws Exception {
        
        log.info(action);
        if (action.equals("ClearOrder")){
            OrderResponse response = new OrderResponse();
            response = api.authorize(command, "orders", action);
            System.out.println("\n\nAuth Response: " + response.toJson());
            message = "No Orders";
        }
        if (action.equals("PayOrder")){
            if(command.getCardnumber().isEmpty()){
                OrderResponse response = new OrderResponse();
                response = api.authorize(command, "order/register/5012349/pay/reward/" + command.getRewardnumber(), action);
                System.out.println("\n\nAuth Response: " + response.toJson());
                message = "No Orders";
            }
            else{
                OrderResponse response = new OrderResponse();
                response = api.authorize(command, "order/register/5012349/pay/card/" + command.getCardnumber(), action);
                System.out.println("\n\nAuth Response: " + response.toJson());
                message = "No Orders";
            }
        }

        model.addAttribute("message", message);
        return "order";

    }
}