package br.edu.femass.controller;

import br.edu.femass.dao.ProdutoDao;
import br.edu.femass.model.Tenis;
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

public class ProdutoController implements Initializable {
    private final ProdutoDao produtoDao = new ProdutoDao();

    @FXML
    private ListView<Tenis> LstProdutos;

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
    private TextField TxtEstoque;

    @FXML
    private TextField TxtId;

    private void limparTela() {
        TxtEstoque.setText("");
        TxtNome.setText("");
        TxtId.setText("");
    }
    private void habilitarInterface(Boolean incluir) {
        TxtNome.setDisable(!incluir);
        BtnGravar.setDisable(!incluir);
        BtnCancelar.setDisable(!incluir);
        BtnExcluir.setDisable(incluir);
        BtnIncluir.setDisable(incluir);
        LstProdutos.setDisable(incluir);
    }

    private void exibirProduto() {
        Tenis tenis = LstProdutos.getSelectionModel().getSelectedItem();
        if (tenis ==null) return;
        TxtNome.setText(tenis.getNome());
        TxtEstoque.setText(String.valueOf(tenis.getEstoque()));
        TxtId.setText(String.valueOf(tenis.getId()));
    }

    @FXML
    private void LstProdutos_MouseClicked(MouseEvent evento) {exibirProduto();}

    @FXML
    private void LstProdutos_KeyPressed(KeyEvent evento) {
        exibirProduto();
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
        Tenis tenis = LstProdutos.getSelectionModel().getSelectedItem();

        if (tenis ==null) return;

        try {
            produtoDao.excluir(tenis);
        } catch (Exception e) {
            e.printStackTrace();
        }

        atualizarLista();

    }

    @FXML
    private void BtnAlterar_Action(ActionEvent evento){
        Tenis tenis = LstProdutos.getSelectionModel().getSelectedItem();

        if (tenis ==null) return;
        habilitarInterface(true);
        BtnGravar.setText("Alterar");
        TxtNome.requestFocus();

    }
    @FXML
    private void BtnGravar_Action(ActionEvent evento) throws Exception {
        if (!TxtNome.getText().isEmpty()) {
            Tenis tenis = new Tenis();
            tenis.setNome(TxtNome.getText());

            if (Objects.equals(BtnGravar.getText(), "Gravar")) {
                try {
                    produtoDao.gravar(tenis);
                } catch (Exception e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setContentText(e.getMessage());
                    errorAlert.show();
                    return;
                }
            } else {
                try {
                    tenis.setId(LstProdutos.getSelectionModel().getSelectedItem().getId());
                    tenis.setNome(TxtNome.getText());
                    produtoDao.alterar(tenis);
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
        List<Tenis> tenis;
        try {
            tenis = produtoDao.listar();
        } catch (Exception e) {
            tenis = new ArrayList<>();
        }
        ObservableList<Tenis> produtosOb = FXCollections.observableArrayList(tenis);
        LstProdutos.setItems(produtosOb);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {atualizarLista();}
}
