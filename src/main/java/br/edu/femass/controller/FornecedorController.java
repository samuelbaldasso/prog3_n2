package br.edu.femass.controller;

import br.edu.femass.dao.FornecedorDao;
import br.edu.femass.model.Fornecedor;
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

public class FornecedorController implements Initializable {
    private final FornecedorDao fornecedorDao = new FornecedorDao();

    @FXML
    private ListView<Fornecedor> LstFornecedores;

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
    private TextField TxtCnpj;

    private void limparTela() {
        TxtCnpj.setText("");
        TxtNome.setText("");
    }
    private void habilitarInterface(Boolean incluir) {
        TxtNome.setDisable(!incluir);
        TxtCnpj.setDisable(!incluir);
        BtnGravar.setDisable(!incluir);
        BtnCancelar.setDisable(!incluir);
        BtnExcluir.setDisable(incluir);
        BtnIncluir.setDisable(incluir);
        LstFornecedores.setDisable(incluir);
    }

    private void exibirFornecedor() {
        Fornecedor fornecedor = LstFornecedores.getSelectionModel().getSelectedItem();
        if (fornecedor==null) return;
        TxtNome.setText(fornecedor.getNome());
        TxtCnpj.setText(fornecedor.getCnpj());
    }

    @FXML
    private void LstFornecedores_MouseClicked(MouseEvent evento) {
        exibirFornecedor();
    }

    @FXML
    private void LstFornecedores_KeyPressed(KeyEvent evento) {
        exibirFornecedor();
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
        Fornecedor fornecedor = LstFornecedores.getSelectionModel().getSelectedItem();

        if (fornecedor==null) return;

        try {
            fornecedorDao.excluir(fornecedor);
        } catch (Exception e) {
            e.printStackTrace();
        }

        atualizarLista();

    }

    @FXML
    private void BtnAlterar_Action(ActionEvent evento){
        Fornecedor fornecedor = LstFornecedores.getSelectionModel().getSelectedItem();

        if (fornecedor==null) return;
        habilitarInterface(true);
        BtnGravar.setText("Alterar");
        TxtNome.requestFocus();

    }
    @FXML
    private void BtnGravar_Action(ActionEvent evento) throws Exception {
        if(!TxtNome.getText().isEmpty() && !TxtCnpj.getText().isEmpty()){
            Fornecedor fornecedor = new Fornecedor();
            fornecedor.setNome(TxtNome.getText());
            fornecedor.setCnpj(TxtCnpj.getText());

            if (Objects.equals(BtnGravar.getText(), "Gravar")) {
                try {
                    fornecedorDao.gravar(fornecedor);
                } catch (Exception e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setContentText(e.getMessage());
                    errorAlert.show();
                    return;
                }
            }
            else {
                try {
                    fornecedor.setId(LstFornecedores.getSelectionModel().getSelectedItem().getId());
                    fornecedor.setNome(TxtNome.getText());
                    fornecedor.setCnpj(TxtCnpj.getText());
                    fornecedorDao.alterar(fornecedor);
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
    }

    @FXML
    private void BtnCancelar_Action(ActionEvent evento) {
        habilitarInterface(false);
    }


    private void atualizarLista() {
        BtnGravar.setText("Gravar");
        List<Fornecedor> fornecedores;
        try {
            fornecedores = fornecedorDao.listar();
        } catch (Exception e) {
            fornecedores = new ArrayList<>();
        }
        ObservableList<Fornecedor> fornecedoresOb = FXCollections.observableArrayList(fornecedores);
        LstFornecedores.setItems(fornecedoresOb);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {atualizarLista();}
}
