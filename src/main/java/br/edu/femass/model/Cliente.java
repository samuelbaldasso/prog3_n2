package br.edu.femass.model;

import lombok.Data;

@Data
public class Cliente {
    private Long id;
    private String nome;
    private String cpf;

    @Override
    public String toString() {return this.getNome();}
}
