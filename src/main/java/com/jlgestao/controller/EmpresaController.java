package com.jlgestao.controller;

import com.jlgestao.model.Empresa;
import com.jlgestao.repository.EmpresaRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EmpresaController {

    private final EmpresaRepository empresaRepository;

    public EmpresaController(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    @GetMapping("/teste")
    public String teste() {
        return "JL Gestão funcionando";
    }

    @GetMapping("/empresas")
    public List<Empresa> listarEmpresas() {
        return empresaRepository.findAll();
    }
    @PostMapping("/empresas")
    public Empresa cadastrarEmpresa(@RequestBody Empresa empresa) {
        return empresaRepository.save(empresa);
    }
}