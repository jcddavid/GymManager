package com.progetto.gymmanager.view.fx;

import com.progetto.gymmanager.bean.CredentialsBean;
import com.progetto.gymmanager.bean.UserBean;
import com.progetto.gymmanager.controller.LoginController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginGUIController {

    @FXML private TextField nicknameField;
    @FXML private PasswordField passwordField;
    private final LoginController loginController = new LoginController();

    @FXML
    protected void handleLogin() {
        try {
            CredentialsBean creds = new CredentialsBean(nicknameField.getText(), passwordField.getText());
            UserBean user = loginController.login(creds);

            // Smistamento scene in base al ruolo
            if ("AMMINISTRATORE".equalsIgnoreCase(user.getRuolo())) {
                SceneManager.changeScene("AmministratoreGUI.fxml");
            } else {
                SceneManager.changeScene("HomeGUI.fxml");
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML protected void goToRegistration() { SceneManager.changeScene("RegistrazioneGUI.fxml"); }
}