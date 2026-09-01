package com.jlgestao.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "empresa")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    @Column(name = "cnpj", length = 20)
    private String cnpj;

    @Column(name = "telefone", length = 20)
    private String telefone;

    @Column(name = "ativo")
    private Boolean ativo;

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return cnpj;

    }
    public String getTelefone() {
        return telefone;
    }
    public Boolean getAtivo() {
        return ativo;
    }
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
    public void setTelefone(String telefone)  {
        this.telefone = telefone;
    }
    public void setAtivo(Boolean ativo)  {
        this.ativo = ativo;
    }
}