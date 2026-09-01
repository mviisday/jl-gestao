package com.jlgestao.model;

import java.math.BigDecimal;

public class ResumoFinanceiro {

    private BigDecimal totalEntradas;
    private BigDecimal totalDespesas;
    private BigDecimal resultado;

    public ResumoFinanceiro(BigDecimal totalEntradas,
                            BigDecimal totalDespesas,
                            BigDecimal resultado) {

        this.totalEntradas = totalEntradas;
        this.totalDespesas = totalDespesas;
        this.resultado = resultado;
    }

    public BigDecimal getTotalEntradas() {
        return totalEntradas;
    }

    public BigDecimal getTotalDespesas() {
        return totalDespesas;
    }

    public BigDecimal getResultado() {
        return resultado;
    }
}