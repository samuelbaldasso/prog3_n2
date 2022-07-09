package br.edu.femass.model;

import lombok.Data;


@Data
public class ItemVenda {

    private Long id;
    private int qtd;
    private Float PrecoVenda;
    private Tenis tenis;

    @Override
    public String toString() {return this.tenis.getNome() + "; Preço da compra: " + this.getPrecoVenda() +
            ", quantidade: " + this.getQtd();}

}
