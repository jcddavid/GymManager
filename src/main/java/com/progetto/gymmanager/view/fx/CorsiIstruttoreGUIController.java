package com.progetto.gymmanager.view.fx;

import com.progetto.gymmanager.bean.CorsoBean;
import com.progetto.gymmanager.controller.GestioneCorsiController;
import com.progetto.gymmanager.engineering.exception.CorsoException;
import com.progetto.gymmanager.engineering.singleton.SessioneAttuale;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CorsiIstruttoreGUIController {
    @FXML private TextField nomeCorsoField;
    @FXML private DatePicker dataCorsoDatePicker;
    @FXML private ListView<String> richiesteListView;
    @FXML private ListView<String> mieiCorsiListView;
    @FXML private ListView<String> iscrittiListView;

    private final GestioneCorsiController controller = new GestioneCorsiController();

    @FXML
    public void initialize() {
        rinfrescaSchermata();
    }

    private void rinfrescaSchermata() {
        try {
            richiesteListView.getItems().clear();
            richiesteListView.getItems().addAll(controller.getRichiestePendenti());

            mieiCorsiListView.getItems().clear();
            String istruttore = SessioneAttuale.getInstance().getCurrentUser().getNickname();
            for (CorsoBean c : controller.getCorsiPerIstruttore(istruttore)) {
                mieiCorsiListView.getItems().add(c.getNome() + " - " + c.getData());
            }

            iscrittiListView.getItems().clear();
        } catch (CorsoException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    @FXML
    protected void selezionaCorso() {
        String selezionato = mieiCorsiListView.getSelectionModel().getSelectedItem();
        iscrittiListView.getItems().clear();
        if (selezionato != null) {
            try {
                String nomeCorso = selezionato.split(" - ")[0].trim();
                iscrittiListView.getItems().addAll(controller.getIscrittiCorso(nomeCorso));
            } catch (CorsoException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
            }
        }
    }

    @FXML
    protected void creaCorso() {
        if (dataCorsoDatePicker.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "Seleziona una data per il corso.").showAndWait();
            return;
        }
        String nome = nomeCorsoField.getText();
        String data = dataCorsoDatePicker.getValue().toString();
        String istruttore = SessioneAttuale.getInstance().getCurrentUser().getNickname();
        try {
            controller.creaCorso(nome, data, istruttore);
            new Alert(Alert.AlertType.INFORMATION, "Corso creato e memorizzato con successo!").showAndWait();
            nomeCorsoField.clear();
            dataCorsoDatePicker.setValue(null);
            rinfrescaSchermata();
        } catch (CorsoException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    @FXML
    protected void modificaDataCorso() {
        String selezionato = mieiCorsiListView.getSelectionModel().getSelectedItem();
        if (selezionato != null && dataCorsoDatePicker.getValue() != null) {
            String nomeCorso = selezionato.split(" - ")[0].trim();
            String nuovaData = dataCorsoDatePicker.getValue().toString();
            String istruttore = SessioneAttuale.getInstance().getCurrentUser().getNickname();
            try {
                controller.modificaDataCorso(nomeCorso, nuovaData, istruttore);
                new Alert(Alert.AlertType.INFORMATION, "Data modificata in persistenza!").showAndWait();
                dataCorsoDatePicker.setValue(null);
                rinfrescaSchermata();
            } catch (CorsoException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Seleziona un corso dalla lista e imposta una nuova data nel calendario.").showAndWait();
        }
    }

    @FXML
    protected void eliminaCorso() {
        String selezionato = mieiCorsiListView.getSelectionModel().getSelectedItem();
        if (selezionato != null) {
            String nomeCorso = selezionato.split(" - ")[0].trim();
            try {
                controller.eliminaCorso(nomeCorso);
                new Alert(Alert.AlertType.INFORMATION, "Corso rimosso fisicamente. Gli iscritti sono stati notificati.").showAndWait();
                rinfrescaSchermata();
            } catch (CorsoException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Seleziona un corso dall'elenco prima di eliminarlo.").showAndWait();
        }
    }

    @FXML
    protected void approvaRichiesta() { gestisci(true); }

    @FXML
    protected void rifiutaRichiesta() { gestisci(false); }

    private void gestisci(boolean approva) {
        String selezionata = richiesteListView.getSelectionModel().getSelectedItem();
        if (selezionata != null) {
            try {
                controller.gestisciRichiesta(selezionata, approva);
                rinfrescaSchermata();
            } catch (CorsoException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Seleziona una richiesta pendente prima.").showAndWait();
        }
    }

    @FXML protected void tornaHome() { SceneManager.changeScene("HomeGUI.fxml"); }
}