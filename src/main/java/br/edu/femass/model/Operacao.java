package br.edu.femass.model;

import lombok.Data;

@Data
public class Operacao {

    private TipoOperacao tipo;

    private Float valor;
}
