package com.jack.clientauthority.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/appConfig")
public class AppConfigController {

    @GetMapping
    public String appConfig() {
        return "appConfig";
    }

    @GetMapping("/toMenu")
    public String toMenu() {
        return "menu";
    }
}
