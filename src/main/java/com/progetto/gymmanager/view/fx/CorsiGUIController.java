package com.progetto.gymmanager.view.fx;

import com.progetto.gymmanager.bean.CorsoBean;
import com.progetto.gymmanager.controller.GestioneCorsiController;
import com.progetto.gymmanager.engineering.exception.CorsoException;
import com.progetto.gymmanager.engineering.singleton.SessioneAttuale;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CorsiGUIController {
    @FXML private ListView<String> corsiListView;
    private final GestioneCorsiController controller = new GestioneCorsiController();

    @FXML
    public void initialize() {
        caricaCorsi();
    }

    private void caricaCorsi() {
        corsiListView.getItems().clear();
        try {
            for (CorsoBean c : controller.getCorsiDisponibili()) {
                corsiListView.getItems().add(c.getNome() + " - " + c.getData() + " (Istruttore: " + c.getIstruttore() + ")");
            }
        } catch (CorsoException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    @FXML
    protected void prenotaCorso() {
        String selezionato = corsiListView.getSelectionModel().getSelectedItem();
        if (selezionato != null) {
            String nomeCorso = selezionato.split(" - ")[0].trim();
            String nickname = SessioneAttuale.getInstance().getCurrentUser().getNickname();
            try {
                controller.richiediIscrizione(nomeCorso, nickname);
                new Alert(Alert.AlertType.INFORMATION, "Richiesta di iscrizione inviata all'istruttore!").showAndWait();
            } catch (CorsoException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Seleziona un corso dalla lista.").showAndWait();
        }
    }

    @FXML
    protected void disdiciPrenotazione() {
        String selezionato = corsiListView.getSelectionModel().getSelectedItem();
        if (selezionato != null) {
            String nomeCorso = selezionato.split(" - ")[0].trim();
            String nickname = SessioneAttuale.getInstance().getCurrentUser().getNickname();
            try {
                controller.disiscriviUtente(nomeCorso, nickname);
                new Alert(Alert.AlertType.INFORMATION, "Iscrizione annullata con successo.").showAndWait();
            } catch (CorsoException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Seleziona un corso dalla lista.").showAndWait();
        }
    }

    @FXML protected void tornaHome() { SceneManager.changeScene("HomeGUI.fxml"); }
}