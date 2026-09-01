package com.jlgestao.repository;

import com.jlgestao.model.Despesa;
import com.jlgestao.model.StatusDespesa;
import org.springframework.data.jpa.repository.JpaRepository;


import java.time.LocalDate;
import java.util.List;

public interface DespesaRepository extends JpaRepository<Despesa, Long> {

    List<Despesa> findByStatus(StatusDespesa status);

    List<Despesa> findByCategoriaId(Long categoriaId);

    List<Despesa> findByDataBetween(LocalDate inicio, LocalDate fim);

}