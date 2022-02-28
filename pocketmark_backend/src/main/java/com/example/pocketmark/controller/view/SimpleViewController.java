package com.example.pocketmark.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SimpleViewController {
    
    @GetMapping("/")
    public String home(){
        return "home.html";
    }
}
