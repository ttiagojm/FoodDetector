package com.ipcb.ad.microservicogestao.Controllers;

import com.ipcb.ad.microservicogestao.Models.Utilizador;
import com.ipcb.ad.microservicogestao.Services.ServicoUtilizador;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class ControladorUtilizador {

    private final ServicoUtilizador servicoUtilizador;

    @Autowired
    private ControladorUtilizador(ServicoUtilizador servicoUtilizador){
        this.servicoUtilizador = servicoUtilizador;
    }

    @GetMapping(value = "/formregistar")
    public String getRegistar(@ModelAttribute Utilizador utilizador) {
        return "registar.html";
    }

    @GetMapping(value = "/index")
    public String getIndex(@ModelAttribute Utilizador utilizador) {
        return "index.html";
    }

    @GetMapping("/perfil")
    public String getPerfil(Model model, Principal principal) {
        Utilizador utilizador = servicoUtilizador.getUtilizador(principal);

        model.addAttribute("utilizador", utilizador);

        return "dados-utilizador";
    }

    @PostMapping("/registar")
    public ModelAndView registaUtilizador(@Valid @ModelAttribute Utilizador utilizador,
                                          BindingResult bindingResult){

        ModelAndView modelAndView = new ModelAndView();

        if(bindingResult.hasErrors()){
            modelAndView.addObject(utilizador);
            modelAndView.setViewName("registar.html");
            return modelAndView;
        }

        try {
            servicoUtilizador.registarUser(utilizador);
            modelAndView.addObject(utilizador);
            modelAndView.setViewName("login.html");
            return modelAndView;

        }  catch (Exception e) {
            modelAndView.addObject(utilizador);
            modelAndView.setViewName("registar.html");
            return modelAndView;
        }
    }
}
