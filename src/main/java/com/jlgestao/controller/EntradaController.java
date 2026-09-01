package com.jlgestao.controller;

import com.jlgestao.model.Empresa;
import com.jlgestao.model.Entrada;
import com.jlgestao.repository.EmpresaRepository;
import com.jlgestao.repository.EntradaRepository;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;
import java.util.Optional;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.math.BigDecimal;

@RestController
public class EntradaController {

    private final EntradaRepository entradaRepository;
    private final EmpresaRepository empresaRepository;

    public EntradaController(EntradaRepository entradaRepository,
                             EmpresaRepository empresaRepository) {
        this.entradaRepository = entradaRepository;
        this.empresaRepository = empresaRepository;
    }

    @GetMapping("/entradas")
    public List<Entrada> listarEntradas() {
        return entradaRepository.findAll();
    }

    @GetMapping("/entradas/{id}")
    public Entrada buscarEntradaPorId(@PathVariable Long id) {

        Optional<Entrada> entradaEncontrada =
                entradaRepository.findById(id);

        if (entradaEncontrada.isPresent()) {
            return entradaEncontrada.get();
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Entrada não encontrada com o ID: " + id
        );
    }

    @PostMapping("/entradas")
    public Entrada cadastrarEntrada(@Valid @RequestBody Entrada entrada) {

        Long empresaId = entrada.getEmpresa().getId();

        Optional<Empresa> empresaEncontrada =
                empresaRepository.findById(empresaId);

        if (empresaEncontrada.isPresent()) {
            entrada.setEmpresa(empresaEncontrada.get());
            return entradaRepository.save(entrada);
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Empresa não encontrada com o ID: " + empresaId
        );
    }

    @PutMapping("/entradas/{id}")
    public Entrada atualizarEntrada(@PathVariable Long id,
                                    @RequestBody Entrada novosDados) {

        Optional<Entrada> entradaEncontrada =
                entradaRepository.findById(id);

        if (entradaEncontrada.isPresent()) {

            Entrada entrada = entradaEncontrada.get();

            entrada.setValor(novosDados.getValor());
            entrada.setData(novosDados.getData());
            entrada.setFormaPagamento(novosDados.getFormaPagamento());
            entrada.setDescricao(novosDados.getDescricao());

            return entradaRepository.save(entrada);
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Entrada não encontrada com o ID: " + id
        );
    }

    @DeleteMapping("/entradas/{id}")
    public void deletarEntrada(@PathVariable Long id) {

        Optional<Entrada> entradaEncontrada =
                entradaRepository.findById(id);

        if (entradaEncontrada.isPresent()) {
            Entrada entrada = entradaEncontrada.get();
            entradaRepository.delete(entrada);
            return;
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Entrada não encontrada com o ID: " + id
        );
    }
    @GetMapping("/entradas/periodo")
    public List<Entrada> listarEntradasPorPeriodo(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {

        return entradaRepository.findByDataBetween(inicio, fim);
    }
    @GetMapping("/entradas/total")
    public BigDecimal calcularTotalEntradasPorPeriodo(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {

        List<Entrada> entradas =
                entradaRepository.findByDataBetween(inicio, fim);

        BigDecimal total = BigDecimal.ZERO;

        for (Entrada entrada : entradas) {
            total = total.add(entrada.getValor());
        }

        return total;
    }
    }
