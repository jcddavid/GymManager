package com.progetto.gymmanager.view.fx;

import com.progetto.gymmanager.controller.GestioneProfiloController;
import com.progetto.gymmanager.controller.NotificheController;
import com.progetto.gymmanager.engineering.exception.AbbonamentoException;
import com.progetto.gymmanager.engineering.observer.Observer;
import com.progetto.gymmanager.engineering.singleton.NotificationManager;
import com.progetto.gymmanager.engineering.singleton.SessioneAttuale;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class HomeGUIController implements Observer {

    @FXML private Label welcomeLabel;
    @FXML private Label badgeNotifiche;
    @FXML private Button btnCreaScheda;
    @FXML private Button btnCorsiAbbonato;
    @FXML private Button btnGestioneCorsi;
    @FXML private Button btnAbbonamento;
    @FXML private Button btnQr;
    @FXML private Button btnAmministratore;

    private final NotificheController notificheController = new NotificheController();

    @FXML
    public void initialize() {
        NotificationManager.getInstance().attach(this);

        if (SessioneAttuale.getInstance().getCurrentUser() != null) {
            String nick = SessioneAttuale.getInstance().getCurrentUser().getNickname();
            String ruolo = SessioneAttuale.getInstance().getCurrentUser().getRuolo().name();
            welcomeLabel.setText("Benvenuto, " + nick + " [" + ruolo + "]");

            if ("ISTRUTTORE".equals(ruolo)) {
                btnCreaScheda.setVisible(true);
                btnCreaScheda.setManaged(true);
                btnGestioneCorsi.setVisible(true);
                btnGestioneCorsi.setManaged(true);
                btnCorsiAbbonato.setVisible(false);
                btnCorsiAbbonato.setManaged(false);
                if (btnQr != null) {
                    btnQr.setVisible(true);
                    btnQr.setManaged(true);
                }
            } else if ("ABBONATO".equals(ruolo)) {
                btnAbbonamento.setVisible(true);
                btnAbbonamento.setManaged(true);
                if (btnQr != null) {
                    btnQr.setVisible(true);
                    btnQr.setManaged(true);
                }
            } else if ("AMMINISTRATORE".equals(ruolo)) {
                if (btnAmministratore != null) {
                    btnAmministratore.setVisible(true);
                    btnAmministratore.setManaged(true);
                }
                btnCorsiAbbonato.setVisible(false);
                btnCorsiAbbonato.setManaged(false);
            }
            aggiornaBadge();
        }
    }

    @Override
    public void update() {
        Platform.runLater(this::aggiornaBadge);
    }

    private void aggiornaBadge() {
        if (SessioneAttuale.getInstance().getCurrentUser() == null) return;
        String nick = SessioneAttuale.getInstance().getCurrentUser().getNickname();
        String ruolo = SessioneAttuale.getInstance().getCurrentUser().getRuolo().name();

        int count = notificheController.contaNotificheNonLette(nick, ruolo);
        if (count > 0) {
            badgeNotifiche.setText(String.valueOf(count));
            badgeNotifiche.setVisible(true);
        } else {
            badgeNotifiche.setVisible(false);
        }
    }

    @FXML protected void goToNotifiche() { SceneManager.changeScene("NotificheGUI.fxml"); }
    @FXML protected void goToGestioneCorsi() { SceneManager.changeScene("CorsiIstruttoreGUI.fxml"); }
    @FXML protected void goToCorsi() { SceneManager.changeScene("CorsiGUI.fxml"); }
    @FXML protected void goToShop() { SceneManager.changeScene("ShopGUI.fxml"); }
    @FXML protected void goToScheda() { SceneManager.changeScene("SchedaGUI.fxml"); }
    @FXML protected void goToVisita() { SceneManager.changeScene("VisitaMedicaGUI.fxml"); }
    @FXML protected void goToCreaScheda() { SceneManager.changeScene("CreaSchedaGUI.fxml"); }
    @FXML protected void goToAbbonamento() { SceneManager.changeScene("AbbonamentoGUI.fxml"); }
    @FXML protected void goToProfilo() { SceneManager.changeScene("ProfiloGUI.fxml"); }
    @FXML protected void goToAmministratore() { SceneManager.changeScene("AmministratoreGUI.fxml"); }

    @FXML
    protected void handleQrCode() {
        try {
            GestioneProfiloController ctrl = new GestioneProfiloController();
            String token = ctrl.generaQrCodeAccesso();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/progetto/gymmanager/fx/QrCodeGUI.fxml"));
            Parent root = loader.load();
            QrCodeGUIController qrCtrl = loader.getController();
            qrCtrl.setToken(token);

            btnQr.getScene().setRoot(root);
        } catch (AbbonamentoException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
            alert.setHeaderText(null);
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Errore imprevisto nel caricamento del QR Code.");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

    @FXML
    protected void handleLogout() {
        NotificationManager.getInstance().detach(this);
        SessioneAttuale.getInstance().logout();
        SceneManager.changeScene("LoginGUI.fxml");
    }
}