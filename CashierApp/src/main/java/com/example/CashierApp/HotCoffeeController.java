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

@Slf4j
@Controller
@RequestMapping("/hotcoffee")
public class HotCoffeeController {

    private CyberSourceAPI api = new CyberSourceAPI();

    @GetMapping
    public String getAction(@ModelAttribute("command") Order command, Model model) {
        log.info("Hot Coffee Page");
        return "hotcoffee";

    }

    @PostMapping
    public String postAction(@Valid @ModelAttribute("command") Order command,
        Errors errors, Model model, HttpServletRequest request) throws Exception {
        log.info(command.getMilk());
        log.info(command.getSize());
        log.info(command.getDrink());
        
        OrderResponse response = new OrderResponse();
        response = api.authorize(command, "order/register/5012349", "");
        System.out.println("\n\nAuth Response: " + response.toJson());

        return "hotcoffee";

    }
}
