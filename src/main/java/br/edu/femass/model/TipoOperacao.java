package br.edu.femass.model;

public enum TipoOperacao {

    VENDA("Venda"),
    COMPRA("Compra");

    private String nome;

    TipoOperacao(String nome) {
        this.nome = nome;
    }
}
