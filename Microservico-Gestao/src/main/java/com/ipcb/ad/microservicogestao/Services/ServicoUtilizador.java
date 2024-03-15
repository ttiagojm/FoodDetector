package com.ipcb.ad.microservicogestao.Services;

import com.ipcb.ad.microservicogestao.Models.Role;
import com.ipcb.ad.microservicogestao.Models.Utilizador;
import com.ipcb.ad.microservicogestao.Repositories.RepositorioUtilizador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServicoUtilizador {

    private final RepositorioUtilizador repositorioUtilizador;
    @Qualifier("codificador.bcrypt")
    private final BCryptPasswordEncoder encoder;
    @Autowired
    private ServicoUtilizador(RepositorioUtilizador repositorioUtilizador,
                              BCryptPasswordEncoder encoder){
        this.repositorioUtilizador = repositorioUtilizador;
        this.encoder = encoder;
    }
    public void registarUser(Utilizador utilizador){

        utilizador.setRoles(List.of(new Role("STANDARD")));

        String passwdHash = encoder.encode(utilizador.getPassword());
        utilizador.setPassword(passwdHash);
        repositorioUtilizador.save(utilizador);

    }

    public Utilizador getUtilizador(Principal principal){
        Optional<Utilizador> utilizadorOptional = repositorioUtilizador.findByNome(principal.getName());
        return utilizadorOptional.orElse(new Utilizador());
    }

    public List<Utilizador> getUtilizadores(){
        return repositorioUtilizador.findAll();
    }

    public void removerUtilizadores(List<Integer> userIds) {
        for (int userId : userIds) {
            Optional<Utilizador> utilizadorOptional = repositorioUtilizador.findById(userId);
            utilizadorOptional.ifPresent(utilizador -> repositorioUtilizador.deleteById(utilizador.getId()));
        }
    }

}
