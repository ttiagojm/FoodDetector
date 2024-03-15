package com.ipcb.ad.microservicogestao.Services;

import com.ipcb.ad.microservicogestao.Models.PredictionDTO;
import com.ipcb.ad.microservicogestao.Models.TarefaDTO;
import com.ipcb.ad.microservicogestao.Models.Utilizador;
import com.ipcb.ad.microservicogestao.Proxy.ProxyMicroservicoIA;
import com.ipcb.ad.microservicogestao.Repositories.RepositorioUtilizador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServicoGestao {

    private final RepositorioUtilizador bdutilizadores;
    private final ProxyMicroservicoIA proxyMicroservicoIA;
    @Autowired
    private ServicoGestao(RepositorioUtilizador repositorioUtilizador,
                          ProxyMicroservicoIA proxyMicroservicoIA){
        this.bdutilizadores = repositorioUtilizador;
        this.proxyMicroservicoIA = proxyMicroservicoIA;
    }

    public Optional<List<TarefaDTO>> getTarefas(Principal principal){
        Optional<Utilizador> utilizador = bdutilizadores.findByNome(principal.getName());

        if(utilizador.isPresent())
            return proxyMicroservicoIA.getTarefas(utilizador.get().getId());

        return Optional.of(new ArrayList<>());
    }

    public int bloquearUtilizador(Integer id){
        return bdutilizadores.bloquearUtilizador(id);
    }

    public List<PredictionDTO> getPredictions(MultipartFile file, Integer userId){
        TarefaDTO tarefa = proxyMicroservicoIA.postTarefa(file, userId);
        return tarefa.getObjetosIdentificados();
    }

    public void guardarImagem(MultipartFile file, String fileName){
        try {
            Path path = Paths.get(fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
