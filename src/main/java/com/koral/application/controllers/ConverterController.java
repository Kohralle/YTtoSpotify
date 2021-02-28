package com.koral.application.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ConverterController {

    @GetMapping("/converter")

    public String getconverter() {

        return "converter";
    }
}


