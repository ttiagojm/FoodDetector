package com.ipcb.ad.microservicogestao;

import com.ipcb.ad.microservicogestao.Models.Role;
import com.ipcb.ad.microservicogestao.Models.Utilizador;
import com.ipcb.ad.microservicogestao.Repositories.RepositorioUtilizador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;

@EnableFeignClients
@SpringBootApplication
public class MicroservicoGestaoApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(MicroservicoGestaoApplication.class, args);
    }

    @Autowired
    RepositorioUtilizador repositorioUtilizador;

    @Qualifier("codificador.bcrypt")
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        try {
            Utilizador utilizador1 = new Utilizador("goncalo", "goncalo@ipcb.pt", encoder.encode("1"), List.of(new Role("ADMIN")));
            repositorioUtilizador.save(utilizador1);

            Utilizador utilizador2 = new Utilizador("tiago", "tiago@ipcb.pt", encoder.encode("admin"), List.of(new Role("STANDARD")));
            repositorioUtilizador.save(utilizador2);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}