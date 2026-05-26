package com.lanceraro.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/guilda")
public class TavernaController {

    @GetMapping("/boas-vindas")
    public String darBoasVindas() {
        return "Bem-vindo à Taverna do Lance Raro! O servidor Java está online!";
    }
}
