package br.edu.femass.model;


import lombok.Data;

@Data
public class Tenis {
    private Long id;
    private String nome;
    private Float precoVenda = 0F;
    public static int estoque = 0;

    @Override
    public String toString(){return this.getNome() + ", estoque: " + this.estoque;}

    public Boolean checkEstoque (Integer value) {
        return this.estoque >= value;
    }

    public Boolean checkPreco (Float value) {
        return this.getPrecoVenda() > value;
    }

}