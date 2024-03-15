package com.ipcb.ad.microservicoia.Repositories;

import com.ipcb.ad.microservicoia.Models.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepositorioTarefa extends JpaRepository<Tarefa,Long> {
    Optional<List<Tarefa>> findAllByUserId(Integer userId);

    Optional<Tarefa> findTarefaByIdAndUserId(Long id, Integer userId);
}
