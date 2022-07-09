package br.edu.femass.controller;

import br.edu.femass.dao.CompraDao;
import br.edu.femass.dao.FornecedorDao;
import br.edu.femass.dao.ProdutoDao;
import br.edu.femass.model.Compra;
import br.edu.femass.model.Fornecedor;
import br.edu.femass.model.ItemCompra;
import br.edu.femass.model.Produto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CompraController implements Initializable {
    private final CompraDao compraDao = new CompraDao();
    private final ProdutoDao produtoDao = new ProdutoDao();
    private final FornecedorDao fornecedorDao = new FornecedorDao();
    Compra compra = new Compra();

    @FXML
    private ListView<ItemCompra> LstItemCompra;

    @FXML
    private Button BtnIncluir;

    @FXML
    private Button BtnCancelar;

    @FXML
    private Button BtnGravar;

    @FXML
    private ComboBox<Produto> CboProduto;

    @FXML
    private ComboBox<Fornecedor> CboUsuario;

    @FXML
    private TextField TxtQtd;

    @FXML
    private TextField TxtValor;

    @FXML
    private TextField TxtTotal;

    private void limparTela() {
        TxtQtd.setText("");
        TxtValor.setText("");
        CboUsuario.setValue(null);
        CboProduto.setValue(null);
    }

    private void habilitarInterface(Boolean incluir) {
        BtnGravar.setDisable(!incluir);
        BtnCancelar.setDisable(!incluir);
        CboUsuario.setDisable(!incluir);
    }

    @FXML
    private void BtnIncluir_Action(ActionEvent evento) {
        ItemCompra itemCompra = new ItemCompra();
        itemCompra.setProduto(CboProduto.getValue());
        itemCompra.setQtd(Integer.parseInt(TxtQtd.getText()));
        itemCompra.setPrecoCompra(Float.parseFloat(TxtValor.getText()));

        compra.getItensComprados().add(itemCompra);
        compra.setValorTotal(compra.getValorTotal() + (itemCompra.getPrecoCompra() * itemCompra.getQtd()));
        TxtTotal.setText(compra.getValorTotal().toString());

        List <ItemCompra> itens = compra.getItensComprados();
        ObservableList <ItemCompra> itenscompradosOb = FXCollections.observableArrayList(itens);
        LstItemCompra.setItems(itenscompradosOb);

        atualizarLista();
        habilitarInterface(true);
        limparTela();
        CboProduto.requestFocus();
    }

    @FXML
    private void BtnGravar_Action (ActionEvent evento) throws Exception {
        compra.setFornecedor(CboUsuario.getValue());

        try{
            compraDao.gravar(compra);
        } catch (Exception e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText(e.getMessage());
            errorAlert.show();
            return;
        }

        compra = new Compra();
        List <ItemCompra> itens = compra.getItensComprados();
        ObservableList <ItemCompra> itenscompradosOb = FXCollections.observableArrayList(itens);
        LstItemCompra.setItems(itenscompradosOb);

        atualizarLista();
        TxtTotal.setText("");
        habilitarInterface(false);

    }

    @FXML
    private void BtnCancelar_Action(ActionEvent evento) {
        compra = new Compra();
        List <ItemCompra> itens = compra.getItensComprados();
        ObservableList <ItemCompra> itenscompradosOb = FXCollections.observableArrayList(itens);
        LstItemCompra.setItems(itenscompradosOb);

        habilitarInterface(false);
        TxtTotal.setText("");
    }

    private void atualizarLista() {
        List <Produto> produtos;
        List <Fornecedor> fornecedores;

        try {
            fornecedores = fornecedorDao.listar();
            produtos = produtoDao.listar();
        } catch (Exception e) {
            produtos = new ArrayList<>();
            fornecedores = new ArrayList<>();
        }

        ObservableList<Produto> produtoOb = FXCollections.observableArrayList(produtos);
        ObservableList<Fornecedor> fornecedorOb = FXCollections.observableArrayList(fornecedores);
        CboProduto.setItems(produtoOb);
        CboUsuario.setItems(fornecedorOb);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        atualizarLista();
    }

}
