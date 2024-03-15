package com.ipcb.ad.microservicogestao.Repositories;

import com.ipcb.ad.microservicogestao.Models.Utilizador;
import feign.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepositorioUtilizador extends JpaRepository<Utilizador,Integer> {
    public Optional<Utilizador> findByNome(String nome);

    @Modifying
    @Transactional
    @Query("UPDATE Utilizador u SET u.disabled = true WHERE u.id = :userId")
    int bloquearUtilizador(@Param("userId") Integer userId);

}
