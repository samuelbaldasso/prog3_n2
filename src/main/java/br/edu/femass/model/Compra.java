package br.edu.femass.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class Compra {

    private Long id;
    private Date data;
    private Float valorTotal = 0F;
    private List<ItemCompra> itensComprados = new ArrayList<>();
    private Fornecedor fornecedor;

    @Override
    public String toString() {return "Total: " + this.getValorTotal() + "; Data Compra: " + this.getData();}
}
