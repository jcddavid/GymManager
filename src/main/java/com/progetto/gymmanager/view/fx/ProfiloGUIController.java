package com.progetto.gymmanager.view.fx;

import com.progetto.gymmanager.bean.RegistrationBean;
import com.progetto.gymmanager.controller.GestioneProfiloController;
import com.progetto.gymmanager.engineering.singleton.SessioneAttuale;
import com.progetto.gymmanager.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;

public class ProfiloGUIController {
    @FXML private TextField emailField;
    @FXML private TextField indirizzoField;
    @FXML private TextField telefonoField;
    @FXML private DatePicker nascitaPicker;
    private final GestioneProfiloController controller = new GestioneProfiloController();

    @FXML
    public void initialize() {
        User u = SessioneAttuale.getInstance().getCurrentUser();
        emailField.setText(u.getEmail());
        indirizzoField.setText(u.getIndirizzo());
        telefonoField.setText(u.getTelefono());
        if (u.getDataNascita() != null && !u.getDataNascita().isEmpty()) {
            nascitaPicker.setValue(LocalDate.parse(u.getDataNascita()));
        }
    }

    @FXML
    protected void handleSalva() {
        try {
            RegistrationBean bean = new RegistrationBean();
            bean.setEmail(emailField.getText());
            bean.setIndirizzo(indirizzoField.getText());
            bean.setTelefono(telefonoField.getText());
            bean.setDataNascita(nascitaPicker.getValue() != null ? nascitaPicker.getValue().toString() : "");

            controller.aggiornaDatiUtente(bean);
            new Alert(Alert.AlertType.INFORMATION, "Profilo aggiornato con successo!").showAndWait();
            tornaHome();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    @FXML protected void tornaHome() { SceneManager.changeScene("HomeGUI.fxml"); }
}