package com.jlgestao.controller;

import com.jlgestao.model.Despesa;
import com.jlgestao.model.Entrada;
import com.jlgestao.repository.DespesaRepository;
import com.jlgestao.repository.EntradaRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.jlgestao.model.ResumoFinanceiro;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
public class ResultadoController {

    private final EntradaRepository entradaRepository;
    private final DespesaRepository despesaRepository;

    public ResultadoController(EntradaRepository entradaRepository,
                               DespesaRepository despesaRepository) {

        this.entradaRepository = entradaRepository;
        this.despesaRepository = despesaRepository;
    }
    @GetMapping("/resultado")
    public ResumoFinanceiro calcularResultado(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {

        List<Entrada> entradas =
                entradaRepository.findByDataBetween(inicio, fim);

        List<Despesa> despesas =
                despesaRepository.findByDataBetween(inicio, fim);

        BigDecimal totalEntradas = BigDecimal.ZERO;
        BigDecimal totalDespesas = BigDecimal.ZERO;

        for (Entrada entrada : entradas) {
            totalEntradas = totalEntradas.add(entrada.getValor());
        }

        for (Despesa despesa : despesas) {
            totalDespesas = totalDespesas.add(despesa.getValor());
        }

        BigDecimal resultado =
                totalEntradas.subtract(totalDespesas);

        return new ResumoFinanceiro(
                totalEntradas,
                totalDespesas,
                resultado
        );
    }
}