package com.progetto.gymmanager.view.fx;

import com.progetto.gymmanager.bean.RegistrationBean;
import com.progetto.gymmanager.controller.LoginController;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RegistrationGUIController {
    @FXML private TextField nickField;
    @FXML private TextField emailField;
    @FXML private TextField indirizzoField;
    @FXML private TextField telefonoField;
    @FXML private PasswordField passField;
    @FXML private DatePicker nascitaPicker;

    private final LoginController loginController = new LoginController();

    @FXML
    protected void handleRegistration() {
        try {
            RegistrationBean bean = new RegistrationBean();
            bean.setNickname(nickField.getText());
            bean.setEmail(emailField.getText());
            bean.setPassword(passField.getText());
            bean.setIndirizzo(indirizzoField.getText());
            bean.setTelefono(telefonoField.getText());
            bean.setDataNascita(nascitaPicker.getValue() != null ? nascitaPicker.getValue().toString() : "");

            // Forza il ruolo di base
            bean.setRuolo("ABBONATO");

            loginController.registraUtente(bean);
            new Alert(Alert.AlertType.INFORMATION, "Registrazione completata con successo!").showAndWait();
            SceneManager.changeScene("LoginGUI.fxml");
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    @FXML protected void goToLogin() { SceneManager.changeScene("LoginGUI.fxml"); }
}