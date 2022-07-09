package br.edu.femass.controller;

import br.edu.femass.dao.ClienteDao;
import br.edu.femass.model.Cliente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ClienteController implements Initializable {
    private final ClienteDao clienteDao = new ClienteDao();

    @FXML
    private ListView<Cliente> LstClientes;

    @FXML
    private Button BtnIncluir;

    @FXML
    private Button BtnExcluir;

    @FXML
    private Button BtnGravar;

    @FXML
    private Button BtnAlterar;
    @FXML
    private Button BtnCancelar;

    @FXML
    private TextField TxtNome;

    @FXML
    private TextField TxtCpf;

    private void limparTela() {
        TxtCpf.setText("");
        TxtNome.setText("");
    }
    private void habilitarInterface(Boolean incluir) {
        TxtNome.setDisable(!incluir);
        TxtCpf.setDisable(!incluir);
        BtnGravar.setDisable(!incluir);
        BtnCancelar.setDisable(!incluir);
        BtnExcluir.setDisable(incluir);
        BtnIncluir.setDisable(incluir);
        LstClientes.setDisable(incluir);
    }

    private void exibirCliente() {
        Cliente cliente = LstClientes.getSelectionModel().getSelectedItem();
        if (cliente==null) return;
        TxtNome.setText(cliente.getNome());
        TxtCpf.setText(cliente.getCpf());
    }

    @FXML
    private void LstClientes_MouseClicked(MouseEvent evento) {
        exibirCliente();
    }

    @FXML
    private void LstClientes_KeyPressed(KeyEvent evento) {
        exibirCliente();
    }

    @FXML
    private void BtnIncluir_Action(ActionEvent evento) {
        atualizarLista();
        habilitarInterface(true);
        limparTela();
        TxtNome.requestFocus();
    }

    @FXML
    private void BtnExcluir_Action(ActionEvent evento) {
        Cliente cliente = LstClientes.getSelectionModel().getSelectedItem();

        if (cliente==null) return;

        try {
            clienteDao.excluir(cliente);
        } catch (Exception e) {
            e.printStackTrace();
        }

        atualizarLista();

    }

    @FXML
    private void BtnAlterar_Action(ActionEvent evento){
        Cliente cliente = LstClientes.getSelectionModel().getSelectedItem();

        if (cliente==null) return;
        habilitarInterface(true);
        BtnGravar.setText("Alterar");
        TxtNome.requestFocus();

    }
    @FXML
    private void BtnGravar_Action(ActionEvent evento) throws Exception {
        Cliente cliente = new Cliente();
        cliente.setNome(TxtNome.getText());
        cliente.setCpf(TxtCpf.getText());

        if (Objects.equals(BtnGravar.getText(), "Gravar")) {
            try {
                clienteDao.gravar(cliente);
            } catch (Exception e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setContentText(e.getMessage());
                errorAlert.show();
                return;
            }
        }
        else {
            try {
                cliente.setId(LstClientes.getSelectionModel().getSelectedItem().getId());
                cliente.setNome(TxtNome.getText());
                cliente.setCpf(TxtCpf.getText());
                clienteDao.alterar(cliente);
            } catch (Exception e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setContentText(e.getMessage());
                errorAlert.show();
                return;
            }

        }

        atualizarLista();
        habilitarInterface(false);
    }

    @FXML
    private void BtnCancelar_Action(ActionEvent evento) {
        habilitarInterface(false);
    }


    private void atualizarLista() {
        BtnGravar.setText("Gravar");
        List<Cliente> clientes;
        try {
            clientes = clienteDao.listar();
        } catch (Exception e) {
            clientes = new ArrayList<>();
        }
        ObservableList<Cliente> clientesOb = FXCollections.observableArrayList(clientes);
        LstClientes.setItems(clientesOb);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {atualizarLista();}
}
