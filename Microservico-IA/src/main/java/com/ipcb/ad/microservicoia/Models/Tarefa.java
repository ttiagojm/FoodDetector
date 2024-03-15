package com.ipcb.ad.microservicoia.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ipcb.ad.microservicoia.Models.Enumerations.EstadoTarefa;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeOuUrlImagem;

    private String hashImagem;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tarefa", fetch = FetchType.EAGER)
    @JsonIgnoreProperties("tarefa")
    private List<Prediction> objetosIdentificados;

    private LocalDateTime inicioTarefa;

    private LocalDateTime fimTarefa;

    private Long duracao;

    @Enumerated(EnumType.STRING)
    private EstadoTarefa estado;

    private Integer userId;

    public Tarefa(String nomeOuUrlImagem, String hashImagem, List<Prediction> objetosIdentificados,
                  LocalDateTime inicioTarefa, LocalDateTime fimTarefa, Long duracao,
                  EstadoTarefa estado, Integer userId) {
        this.nomeOuUrlImagem = nomeOuUrlImagem;
        this.hashImagem = hashImagem;
        this.objetosIdentificados = objetosIdentificados != null ? objetosIdentificados : new ArrayList<>();
        this.inicioTarefa = inicioTarefa;
        this.fimTarefa = fimTarefa;
        this.duracao = duracao;
        this.estado = estado;
        this.userId = userId;
    }
}
