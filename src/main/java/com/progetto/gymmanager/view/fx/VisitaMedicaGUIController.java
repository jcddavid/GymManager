package com.progetto.gymmanager.view.fx;

import com.progetto.gymmanager.controller.PrenotazioneVisitaController;
import com.progetto.gymmanager.engineering.exception.PrenotazioneException;
import com.progetto.gymmanager.engineering.singleton.SessioneAttuale;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

public class VisitaMedicaGUIController {

    @FXML private Label statoIdoneitaLabel;
    @FXML private DatePicker visitaDatePicker;
    @FXML private ComboBox<String> orarioComboBox;

    private final PrenotazioneVisitaController controller = new PrenotazioneVisitaController();

    @FXML
    public void initialize() {
        String nickname = SessioneAttuale.getInstance().getCurrentUser().getNickname();
        boolean idoneo = controller.verificaIdoneitaInCorso(nickname);
        if (idoneo) {
            statoIdoneitaLabel.setText("Stato Certificato: Valido");
            statoIdoneitaLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #11caa0;");
        } else {
            statoIdoneitaLabel.setText("Stato Certificato: Assente o Scaduto");
            statoIdoneitaLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #ff4444;");
        }
    }

    @FXML
    protected void handleCaricaOrari() {
        if (visitaDatePicker.getValue() != null) {
            try {
                orarioComboBox.getItems().clear();
                orarioComboBox.getItems().addAll(controller.getOrariDisponibili(visitaDatePicker.getValue().toString()));
            } catch (PrenotazioneException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
            }
        }
    }

    @FXML
    protected void prenotaVisita() {
        if (visitaDatePicker.getValue() == null || orarioComboBox.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "Seleziona giorno e orario prima di confermare.").showAndWait();
            return;
        }
        String data = visitaDatePicker.getValue().toString();
        String orario = orarioComboBox.getValue();
        String nickname = SessioneAttuale.getInstance().getCurrentUser().getNickname();

        try {
            controller.prenotaVisita(data, orario, nickname);
            new Alert(Alert.AlertType.INFORMATION, "Visita medica prenotata con successo!").showAndWait();
            visitaDatePicker.setValue(null);
            orarioComboBox.getItems().clear();
            initialize();
        } catch (PrenotazioneException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    @FXML protected void tornaHome() { SceneManager.changeScene("HomeGUI.fxml"); }
}