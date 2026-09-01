package com.jlgestao.controller;

import com.jlgestao.model.CategoriaDespesa;
import com.jlgestao.repository.CategoriaDespesaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
public class CategoriaDespesaController {

    private final CategoriaDespesaRepository categoriaDespesaRepository;

    public CategoriaDespesaController(
            CategoriaDespesaRepository categoriaDespesaRepository) {

        this.categoriaDespesaRepository = categoriaDespesaRepository;
    }

    @GetMapping("/categorias-despesa")
    public List<CategoriaDespesa> listarCategorias() {
        return categoriaDespesaRepository.findAll();
    }

    @GetMapping("/categorias-despesa/{id}")
    public CategoriaDespesa buscarCategoriaPorId(
            @PathVariable Long id) {

        Optional<CategoriaDespesa> categoriaEncontrada =
                categoriaDespesaRepository.findById(id);

        if (categoriaEncontrada.isPresent()) {
            return categoriaEncontrada.get();
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Categoria não encontrada com o ID: " + id
        );
    }

    @PostMapping("/categorias-despesa")
    public CategoriaDespesa cadastrarCategoria(
            @RequestBody CategoriaDespesa categoria) {

        return categoriaDespesaRepository.save(categoria);
    }

    @PutMapping("/categorias-despesa/{id}")
    public CategoriaDespesa atualizarCategoria(
            @PathVariable Long id,
            @RequestBody CategoriaDespesa novosDados) {

        Optional<CategoriaDespesa> categoriaEncontrada =
                categoriaDespesaRepository.findById(id);

        if (categoriaEncontrada.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Categoria não encontrada com o ID: " + id
            );
        }

        CategoriaDespesa categoria =
                categoriaEncontrada.get();

        categoria.setNome(novosDados.getNome());

        return categoriaDespesaRepository.save(categoria);
    }

    @DeleteMapping("/categorias-despesa/{id}")
    public void deletarCategoria(
            @PathVariable Long id) {

        Optional<CategoriaDespesa> categoriaEncontrada =
                categoriaDespesaRepository.findById(id);

        if (categoriaEncontrada.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Categoria não encontrada com o ID: " + id
            );
        }

        categoriaDespesaRepository.delete(
                categoriaEncontrada.get()
        );
    }
}