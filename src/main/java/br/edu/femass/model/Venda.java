package br.edu.femass.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class Venda {

    private Long id;

    private Date data;

    private Float valorTotal = 0F;

    private List<ItemVenda> itensVendidos = new ArrayList<>();
    private Cliente cliente;

    @Override
    public String toString() {return "Total: " + this.getValorTotal() + "; Data da Compra: " + this.getData();}
}
