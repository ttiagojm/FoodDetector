package com.ipcb.ad.microservicogestao.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControladorLogin {
    @GetMapping(value = {"/login","/logout",})
    public String getLogin(){
        return "login";
    }

    @GetMapping("/acesso-negado")
    public String mostraAcessoNegado() {
        return "acesso-negado";
    }
}