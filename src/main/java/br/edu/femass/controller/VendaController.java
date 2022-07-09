package br.edu.femass.controller;

import br.edu.femass.dao.ClienteDao;
import br.edu.femass.dao.ProdutoDao;
import br.edu.femass.dao.VendaDao;
import br.edu.femass.model.Cliente;
import br.edu.femass.model.ItemVenda;
import br.edu.femass.model.Tenis;
import br.edu.femass.model.Venda;
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

public class VendaController implements Initializable {
    private final VendaDao vendaDao = new VendaDao();
    private final ProdutoDao produtoDao = new ProdutoDao();
    private final ClienteDao clienteDao = new ClienteDao();

    Venda venda = new Venda();

    @FXML
    private ListView<ItemVenda> LstItemVenda;

    @FXML
    private Button BtnIncluir;

    @FXML
    private Button BtnCancelar;

    @FXML
    private Button BtnGravar;

    @FXML
    private ComboBox<Tenis> CboProduto;

    @FXML
    private ComboBox<Cliente> CboUsuario;

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
        ItemVenda itemVenda = new ItemVenda();
        itemVenda.setTenis(CboProduto.getValue());
        itemVenda.setQtd(Integer.parseInt(TxtQtd.getText()));
        itemVenda.setPrecoVenda(Float.parseFloat(TxtValor.getText()));

        if(!itemVenda.getTenis().checkEstoque(itemVenda.getQtd())) {return;}

        venda.getItensVendidos().add(itemVenda);
        venda.setValorTotal(venda.getValorTotal() + (itemVenda.getPrecoVenda() * itemVenda.getQtd()));
        TxtTotal.setText(venda.getValorTotal().toString());

        List<ItemVenda> itens = venda.getItensVendidos();
        ObservableList<ItemVenda> itensvendadosOb = FXCollections.observableArrayList(itens);
        LstItemVenda.setItems(itensvendadosOb);

        atualizarLista();
        habilitarInterface(true);
        limparTela();
        CboProduto.requestFocus();
    }

    @FXML
    private void BtnGravar_Action (ActionEvent evento) throws Exception {
        venda.setCliente(CboUsuario.getValue());

        try{
            vendaDao.gravar(venda);
        } catch (Exception e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText(e.getMessage());
            errorAlert.show();
            return;
        }

        venda = new Venda();
        List <ItemVenda> itens = venda.getItensVendidos();
        ObservableList <ItemVenda> itensvendadosOb = FXCollections.observableArrayList(itens);
        LstItemVenda.setItems(itensvendadosOb);

        atualizarLista();
        TxtTotal.setText("");
        habilitarInterface(false);

    }

    @FXML
    private void BtnCancelar_Action(ActionEvent evento) {
        venda = new Venda();
        List <ItemVenda> itens = venda.getItensVendidos();
        ObservableList <ItemVenda> itensvendadosOb = FXCollections.observableArrayList(itens);
        LstItemVenda.setItems(itensvendadosOb);

        habilitarInterface(false);
        TxtTotal.setText("");
    }

    private void atualizarLista() {
        List <Tenis> tenis;
        List <Cliente> clientes;

        try {
            clientes = clienteDao.listar();
            tenis = produtoDao.listar();
        } catch (Exception e) {
            tenis = new ArrayList<>();
            clientes = new ArrayList<>();
        }

        ObservableList<Tenis> tenisOb = FXCollections.observableArrayList(tenis);
        ObservableList<Cliente> clienteOb = FXCollections.observableArrayList(clientes);
        CboProduto.setItems(tenisOb);
        CboUsuario.setItems(clienteOb);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        atualizarLista();
    }

}
