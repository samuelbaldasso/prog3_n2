package br.edu.femass.controller;

import br.edu.femass.model.Caixa;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class CaixaController implements Initializable {

    @FXML
    Button BtnFechar;

    @FXML
    Button BtnCancelar;

    @FXML
    TextField TxtData;

    @FXML
    TextField TxtEntrada;

    @FXML
    TextField TxtSaida;

    @FXML
    TextField TxtTotal;

    private void limparTela() {
        TxtData.setText("");
        TxtEntrada.setText("");
        TxtSaida.setText("");
        TxtTotal.setText("");
    }

    @FXML
    private void BtnFecharAction(ActionEvent evento) {
        Caixa caixa = new Caixa();
        caixa.fecharCaixa(TxtData.getText());

        TxtSaida.setText("R$" + String.format(Locale.getDefault(), "%.2f", caixa.getOperacoes().get(0).getValor()));
        TxtEntrada.setText("R$" + String.format(Locale.getDefault(), "%.2f", caixa.getOperacoes().get(1).getValor()));
        if(caixa.getOperacoes().get(0).getValor() > caixa.getOperacoes().get(1).getValor()) {
            TxtTotal.setText("- R$" + String.format(Locale.getDefault(), "%.2f", caixa.getTotal()));
        }
        else {
            TxtTotal.setText("+ R$" + String.format(Locale.getDefault(), "%.2f", caixa.getTotal()));
        }
    }

    @FXML void BtnCancelar_Action(ActionEvent evento) {
        limparTela();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}
}
