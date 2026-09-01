package com.jlgestao.controller;

import com.jlgestao.model.CategoriaDespesa;
import com.jlgestao.model.Despesa;
import com.jlgestao.model.Empresa;
import com.jlgestao.model.StatusDespesa;
import com.jlgestao.repository.CategoriaDespesaRepository;
import com.jlgestao.repository.DespesaRepository;
import com.jlgestao.repository.EmpresaRepository;

import jakarta.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
public class DespesaController {

    private final DespesaRepository despesaRepository;
    private final EmpresaRepository empresaRepository;
    private final CategoriaDespesaRepository categoriaDespesaRepository;

    public DespesaController(
            DespesaRepository despesaRepository,
            EmpresaRepository empresaRepository,
            CategoriaDespesaRepository categoriaDespesaRepository) {

        this.despesaRepository = despesaRepository;
        this.empresaRepository = empresaRepository;
        this.categoriaDespesaRepository = categoriaDespesaRepository;
    }

    @GetMapping("/despesas")
    public List<Despesa> listarDespesas() {
        return despesaRepository.findAll();
    }

    @GetMapping("/despesas/{id}")
    public Despesa buscarDespesaPorId(@PathVariable Long id) {

        Optional<Despesa> despesaEncontrada =
                despesaRepository.findById(id);

        if (despesaEncontrada.isPresent()) {
            return despesaEncontrada.get();
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Despesa não encontrada com o ID: " + id
        );
    }

    @PostMapping("/despesas")
    public Despesa cadastrarDespesa(
            @Valid @RequestBody Despesa despesa) {

        Long empresaId = despesa.getEmpresa().getId();
        Long categoriaId = despesa.getCategoria().getId();

        Optional<Empresa> empresaEncontrada =
                empresaRepository.findById(empresaId);

        Optional<CategoriaDespesa> categoriaEncontrada =
                categoriaDespesaRepository.findById(categoriaId);

        if (empresaEncontrada.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Empresa não encontrada com o ID: " + empresaId
            );
        }

        if (categoriaEncontrada.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Categoria não encontrada com o ID: " + categoriaId
            );
        }

        despesa.setEmpresa(empresaEncontrada.get());
        despesa.setCategoria(categoriaEncontrada.get());

        return despesaRepository.save(despesa);
    }

    @PutMapping("/despesas/{id}")
    public Despesa atualizarDespesa(
            @PathVariable Long id,
            @Valid @RequestBody Despesa novosDados) {

        Optional<Despesa> despesaEncontrada =
                despesaRepository.findById(id);

        if (despesaEncontrada.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Despesa não encontrada com o ID: " + id
            );
        }

        Long empresaId = novosDados.getEmpresa().getId();
        Long categoriaId = novosDados.getCategoria().getId();

        Optional<Empresa> empresaEncontrada =
                empresaRepository.findById(empresaId);

        Optional<CategoriaDespesa> categoriaEncontrada =
                categoriaDespesaRepository.findById(categoriaId);

        if (empresaEncontrada.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Empresa não encontrada com o ID: " + empresaId
            );
        }

        if (categoriaEncontrada.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Categoria não encontrada com o ID: " + categoriaId
            );
        }

        Despesa despesa = despesaEncontrada.get();

        despesa.setValor(novosDados.getValor());
        despesa.setData(novosDados.getData());
        despesa.setFormaPagamento(novosDados.getFormaPagamento());
        despesa.setDescricao(novosDados.getDescricao());
        despesa.setStatus(novosDados.getStatus());
        despesa.setEmpresa(empresaEncontrada.get());
        despesa.setCategoria(categoriaEncontrada.get());

        return despesaRepository.save(despesa);
    }

    @DeleteMapping("/despesas/{id}")
    public void deletarDespesa(@PathVariable Long id) {

        Optional<Despesa> despesaEncontrada =
                despesaRepository.findById(id);

        if (despesaEncontrada.isPresent()) {
            Despesa despesa = despesaEncontrada.get();
            despesaRepository.delete(despesa);
            return;
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Despesa não encontrada com o ID: " + id
        );
    }

    @GetMapping("/despesas/categoria/{categoriaId}")
    public List<Despesa> listarDespesasPorCategoria(
            @PathVariable Long categoriaId) {

        return despesaRepository.findByCategoriaId(categoriaId);
    }

    @GetMapping("/despesas/periodo")
    public List<Despesa> listarDespesasPorPeriodo(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate inicio,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fim) {

        return despesaRepository.findByDataBetween(inicio, fim);
    }

    @GetMapping("/despesas/total")
    public BigDecimal calcularTotalPorPeriodo(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate inicio,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fim) {

        List<Despesa> despesas =
                despesaRepository.findByDataBetween(inicio, fim);

        BigDecimal total = BigDecimal.ZERO;

        for (Despesa despesa : despesas) {
            total = total.add(despesa.getValor());
        }

        return total;
    }

    @GetMapping("/despesas/status/{status}")
    public List<Despesa> listarDespesasPorStatus(
            @PathVariable StatusDespesa status) {

        return despesaRepository.findByStatus(status);
    }
}