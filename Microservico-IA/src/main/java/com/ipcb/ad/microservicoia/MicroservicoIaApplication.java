package com.ipcb.ad.microservicoia;

import com.ipcb.ad.microservicoia.Models.Enumerations.EstadoTarefa;
import com.ipcb.ad.microservicoia.Models.Prediction;
import com.ipcb.ad.microservicoia.Models.Tarefa;
import com.ipcb.ad.microservicoia.Repositories.RepositorioTarefa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

@EnableFeignClients
@SpringBootApplication
public class MicroservicoIaApplication implements ApplicationRunner {

    @Autowired
    RepositorioTarefa repositorioTarefa;
    public static void main(String[] args) {
        SpringApplication.run(MicroservicoIaApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Tarefa tarefa = new Tarefa(
                "CHOCOLATE0040_png.rf.3b5819556bb221168acae62a6cb7053b.jpg",
                "123456",
                null,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                3600L,
                EstadoTarefa.EM_PROCESSAMENTO,
                1
        );

        tarefa.setObjetosIdentificados(new ArrayList<>());

        tarefa.getObjetosIdentificados().add(new Prediction(0.30f, "Chocolate", 10, 20, 30, 40));
        tarefa.getObjetosIdentificados().add(new Prediction(0.50f, "Chocolate", 10, 20, 30, 40));

        for (Prediction prediction : tarefa.getObjetosIdentificados()) {
            prediction.setTarefa(tarefa);
        }

        repositorioTarefa.save(tarefa);


    }
}
