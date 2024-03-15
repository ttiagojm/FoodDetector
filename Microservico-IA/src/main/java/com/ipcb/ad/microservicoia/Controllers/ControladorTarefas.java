package com.ipcb.ad.microservicoia.Controllers;

import com.ipcb.ad.microservicoia.Models.Tarefa;
import com.ipcb.ad.microservicoia.Services.ServicoTarefas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
public class ControladorTarefas {

    private final ServicoTarefas servicoTarefas;

    private ControladorTarefas(ServicoTarefas servicoTarefas){
        this.servicoTarefas = servicoTarefas;
    }

    @GetMapping("/tarefas/user/{userid}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<List<Tarefa>> getTarefas(@PathVariable("userid") Integer userId){
       return servicoTarefas.obterTarefas(userId);
    }

    @GetMapping("/tarefas/user/{userid}/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Tarefa getTarefa(@PathVariable("userid") Integer userId, @PathVariable("id") Long id){
        return servicoTarefas.obterTarefa(userId, id);
    }

    @PostMapping(value="/tarefas", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Tarefa postTarefa(@RequestPart("imagem") MultipartFile imagem, @RequestParam Integer userId){
        return servicoTarefas.criarTarefa(userId, imagem);
    }
}
