package com.ipcb.ad.microservicogestao.Models;

import com.ipcb.ad.microservicogestao.Models.Enumerations.EstadoTarefa;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

public class TarefaDTO {

    private Long id;

    private String nomeOuUrlImagem;

    private String hashImagem;


    private List<PredictionDTO> objetosIdentificados;

    private LocalDateTime inicioTarefa;

    private LocalDateTime fimTarefa;

    private Long duracao;

    private EstadoTarefa estado;

    private Integer userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeOuUrlImagem() {
        return nomeOuUrlImagem;
    }

    public void setNomeOuUrlImagem(String nomeOuUrlImagem) {
        this.nomeOuUrlImagem = nomeOuUrlImagem;
    }

    public String getHashImagem() {
        return hashImagem;
    }

    public void setHashImagem(String hashImagem) {
        this.hashImagem = hashImagem;
    }

    public List<PredictionDTO> getObjetosIdentificados() {
        return objetosIdentificados;
    }

    public void setObjetosIdentificados(List<PredictionDTO> objetosIdentificados) {
        this.objetosIdentificados = objetosIdentificados;
    }

    public LocalDateTime getInicioTarefa() {
        return inicioTarefa;
    }

    public void setInicioTarefa(LocalDateTime inicioTarefa) {
        this.inicioTarefa = inicioTarefa;
    }

    public LocalDateTime getFimTarefa() {
        return fimTarefa;
    }

    public void setFimTarefa(LocalDateTime fimTarefa) {
        this.fimTarefa = fimTarefa;
    }

    public Long getDuracao() {
        return duracao;
    }

    public void setDuracao(Long duracao) {
        this.duracao = duracao;
    }

    public EstadoTarefa getEstado() {
        return estado;
    }

    public void setEstado(EstadoTarefa estado) {
        this.estado = estado;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
