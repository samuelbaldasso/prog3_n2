package br.edu.femass.model;


import lombok.Data;

@Data
public class Produto {
    private Long id;
    private String nome;
    private Float precoVenda = 0F;
    private int estoque = 0;

    @Override
    public String toString(){return this.getNome() + ", estoque: " + this.getEstoque();}

    public Boolean checkEstoque (Integer value) {
        return this.getEstoque() >= value;
    }

    public Boolean checkPreco (Float value) {
        return this.getPrecoVenda() > value;
    }

}