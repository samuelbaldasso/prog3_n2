package br.edu.femass.model;

import lombok.Data;


@Data
public class ItemVenda {

    private Long id;
    private int qtd;
    private Float PrecoVenda;
    private Produto produto;

    @Override
    public String toString() {return this.produto.getNome() + "; Preço da compra: " + this.getPrecoVenda() +
            ", quantidade: " + this.getQtd();}

}
