package com.ipcb.ad.microservicogestao.Proxy;

import com.ipcb.ad.microservicogestao.Models.PredictionDTO;
import com.ipcb.ad.microservicogestao.Models.TarefaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@FeignClient(value="MICROSERVICO-IA")
public interface ProxyMicroservicoIA {
    @GetMapping("/tarefas/user/{userid}")
    Optional<List<TarefaDTO>> getTarefas(@PathVariable("userid") Integer userId);

    @PostMapping(value="/tarefas", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    TarefaDTO postTarefa(@RequestPart("imagem") MultipartFile imagem, @RequestParam Integer userId);
}
