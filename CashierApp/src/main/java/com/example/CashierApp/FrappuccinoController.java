package com.example.CashierApp;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;


@Slf4j
@Controller
@RequestMapping("/frappuccino")
public class FrappuccinoController {
    @Value("${orderprocessing.apihost}")private String apiHost;
    @Value("${orderprocessing.apiport}")private String apiPort;


    @GetMapping
    public String getAction(@ModelAttribute("command") Order command, Model model) {
        log.info("Frappuccino Page");
        return "frappuccino";

    }

    @PostMapping
    public String postAction(@Valid @ModelAttribute("command") Order command,
        Errors errors, Model model, HttpServletRequest request) throws Exception {

        CyberSourceAPI api = new CyberSourceAPI(apiHost, apiPort);

        log.info(command.getMilk());
        log.info(command.getSize());
        log.info(command.getDrink());
        
        OrderResponse response = new OrderResponse();
        response = api.authorize(command, "order/register/5012349", "");
        System.out.println("\n\nAuth Response: " + response.toJson());
    
        return "frappuccino";

    }
}
