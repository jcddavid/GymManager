package com.progetto.gymmanager.view.fx;

import com.progetto.gymmanager.bean.RegistrationBean;
import com.progetto.gymmanager.controller.AmministratoreController;
import com.progetto.gymmanager.engineering.singleton.SessioneAttuale;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AmministratoreGUIController {

    @FXML private ListView<String> istruttoriListView;
    @FXML private TextField nickIstruttore;
    @FXML private TextField emailIstruttore;
    @FXML private TextField indirizzoIstruttore;
    @FXML private TextField telefonoIstruttore;
    @FXML private PasswordField passIstruttore;
    @FXML private DatePicker nascitaIstruttore;

    @FXML private TextField nomeProdottoField;
    @FXML private TextField prezzoProdottoField;
    @FXML private ListView<String> scontiListView;
    @FXML private TextField codiceScontoField;
    @FXML private TextField percentualeScontoField;
    @FXML private TextField fornitoreField;
    @FXML private TextField prodottoRifornimentoField;
    @FXML private TextField quantitaRifornimentoField;

    @FXML private ComboBox<String> targetComboBox;
    @FXML private TextArea comunicazioneArea;

    private final AmministratoreController controller = new AmministratoreController();

    @FXML
    public void initialize() {
        caricaIstruttori();
        caricaSconti();
    }

    private void caricaIstruttori() {
        istruttoriListView.getItems().clear();
        try {
            istruttoriListView.getItems().addAll(controller.getListaIstruttori());
        } catch (Exception e) {
            mostraAvviso(Alert.AlertType.ERROR, "Errore caricamento istruttori.");
        }
    }

    private void caricaSconti() {
        scontiListView.getItems().clear();
        scontiListView.getItems().addAll(controller.getListaSconti());
    }

    @FXML
    protected void handleCreaIstruttore() {
        try {
            RegistrationBean bean = new RegistrationBean();
            bean.setNickname(nickIstruttore.getText());
            bean.setEmail(emailIstruttore.getText());
            bean.setPassword(passIstruttore.getText());
            bean.setIndirizzo(indirizzoIstruttore.getText());
            bean.setTelefono(telefonoIstruttore.getText());
            bean.setDataNascita(nascitaIstruttore.getValue() != null ? nascitaIstruttore.getValue().toString() : "");

            controller.creaIstruttore(bean);
            mostraAvviso(Alert.AlertType.INFORMATION, "Account Istruttore creato con successo.");
            nickIstruttore.clear(); emailIstruttore.clear(); passIstruttore.clear();
            indirizzoIstruttore.clear(); telefonoIstruttore.clear(); nascitaIstruttore.setValue(null);
            caricaIstruttori();
        } catch (Exception e) {
            mostraAvviso(Alert.AlertType.ERROR, e.getMessage());
        }
    }

    @FXML
    protected void handleEliminaIstruttore() {
        String selezionato = istruttoriListView.getSelectionModel().getSelectedItem();
        if (selezionato != null) {
            String nick = selezionato.split(" - ")[0].trim();
            try {
                controller.eliminaIstruttore(nick);
                mostraAvviso(Alert.AlertType.INFORMATION, "Istruttore licenziato dal sistema.");
                caricaIstruttori();
            } catch (Exception e) {
                mostraAvviso(Alert.AlertType.ERROR, e.getMessage());
            }
        } else {
            mostraAvviso(Alert.AlertType.WARNING, "Seleziona un istruttore dalla lista.");
        }
    }

    @FXML
    protected void handleAggiungiProdotto() {
        try {
            double prezzo = Double.parseDouble(prezzoProdottoField.getText().replace(",", "."));
            controller.aggiungiProdotto(nomeProdottoField.getText(), prezzo);
            mostraAvviso(Alert.AlertType.INFORMATION, "Prodotto inserito e utenti notificati!");
            nomeProdottoField.clear(); prezzoProdottoField.clear();
        } catch (Exception e) {
            mostraAvviso(Alert.AlertType.WARNING, "Controlla i dati inseriti.");
        }
    }

    @FXML
    protected void handleCreaSconto() {
        try {
            int percentuale = Integer.parseInt(percentualeScontoField.getText());
            controller.creaCodiceSconto(codiceScontoField.getText(), percentuale);
            mostraAvviso(Alert.AlertType.INFORMATION, "Sconto attivato!");
            codiceScontoField.clear(); percentualeScontoField.clear();
            caricaSconti();
        } catch (Exception e) {
            mostraAvviso(Alert.AlertType.WARNING, "La percentuale deve essere un numero intero.");
        }
    }

    @FXML
    protected void handleEliminaSconto() {
        String selezionato = scontiListView.getSelectionModel().getSelectedItem();
        if (selezionato != null) {
            String codice = selezionato.split(" ")[0].trim();
            try {
                controller.eliminaCodiceSconto(codice);
                mostraAvviso(Alert.AlertType.INFORMATION, "Codice sconto eliminato.");
                caricaSconti();
            } catch (Exception e) {
                mostraAvviso(Alert.AlertType.ERROR, e.getMessage());
            }
        } else {
            mostraAvviso(Alert.AlertType.WARNING, "Seleziona un codice sconto dalla lista.");
        }
    }

    @FXML
    protected void handleOrdinaRifornimento() {
        try {
            int qty = Integer.parseInt(quantitaRifornimentoField.getText());
            controller.ordinaRifornimento(fornitoreField.getText(), prodottoRifornimentoField.getText(), qty);
            mostraAvviso(Alert.AlertType.INFORMATION, "Ordine di rifornimento inviato a " + fornitoreField.getText());
            fornitoreField.clear(); prodottoRifornimentoField.clear(); quantitaRifornimentoField.clear();
        } catch (Exception e) {
            mostraAvviso(Alert.AlertType.WARNING, "Compila tutti i campi. La quantità deve essere numerica.");
        }
    }

    @FXML
    protected void handleInviaComunicazione() {
        String target = targetComboBox.getValue();
        if (target == null) {
            mostraAvviso(Alert.AlertType.WARNING, "Seleziona i destinatari dal menu a tendina.");
            return;
        }
        try {
            controller.inviaComunicazione(target, comunicazioneArea.getText());
            mostraAvviso(Alert.AlertType.INFORMATION, "Messaggio inoltrato a: " + target);
            comunicazioneArea.clear();
            targetComboBox.getSelectionModel().clearSelection();
        } catch (IllegalArgumentException e) {
            mostraAvviso(Alert.AlertType.WARNING, e.getMessage());
        }
    }

    @FXML
    protected void handleLogout() {
        SessioneAttuale.getInstance().logout();
        SceneManager.changeScene("LoginGUI.fxml");
    }

    private void mostraAvviso(Alert.AlertType tipo, String testo) {
        Alert alert = new Alert(tipo, testo);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}