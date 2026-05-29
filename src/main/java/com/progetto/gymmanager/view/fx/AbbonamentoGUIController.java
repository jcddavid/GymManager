package com.progetto.gymmanager.view.fx;

import com.progetto.gymmanager.controller.GestioneAbbonamentoController;
import com.progetto.gymmanager.engineering.exception.AbbonamentoException;
import com.progetto.gymmanager.engineering.singleton.SessioneAttuale;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class AbbonamentoGUIController {
    @FXML private Label statoLabel;
    private final GestioneAbbonamentoController controller = new GestioneAbbonamentoController();

    @FXML
    public void initialize() {
        rinfrescaStato();
    }

    private void rinfrescaStato() {
        try {
            String nickname = SessioneAttuale.getInstance().getCurrentUser().getNickname();
            String stato = controller.getStatoAbbonamento(nickname);
            statoLabel.setText(stato);
            if (stato.equals("SCADUTO")) {
                statoLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #ff4444;");
            } else {
                statoLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #daffde;");
            }
        } catch (AbbonamentoException e) {
            statoLabel.setText("Errore di caricamento: " + e.getMessage());
        }
    }

    @FXML
    protected void rinnovaTrimestrale() {
        processaRinnovo(3, "99.99");
    }

    @FXML
    protected void rinnovaAnnuale() {
        processaRinnovo(12, "349.99");
    }

    private void processaRinnovo(int mesi, String prezzo) {
        try {
            String nickname = SessioneAttuale.getInstance().getCurrentUser().getNickname();
            controller.rinnovaAbbonamento(nickname, mesi);
            new Alert(Alert.AlertType.INFORMATION, "Pagamento di €" + prezzo + " completato. Abbonamento rinnovato!").showAndWait();
            rinfrescaStato();
        } catch (AbbonamentoException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    @FXML protected void tornaHome() { SceneManager.changeScene("HomeGUI.fxml"); }
}