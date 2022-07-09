package br.edu.femass.model;

import lombok.Data;

@Data
public class Fornecedor {
    private Long id;
    private String nome;
    private String cnpj;


    @Override
    public String toString() {return this.getNome();}
}
