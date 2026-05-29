package com.progetto.gymmanager.view.fx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class QrCodeGUIController {
    @FXML private Label tokenLabel;
    @FXML private ImageView qrImageView;

    public void setToken(String token) {
        tokenLabel.setText(token);

        String urlApiQr = "https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=" + token;

        Image qrImage = new Image(urlApiQr, true);
        qrImageView.setImage(qrImage);
    }

    @FXML protected void tornaHome() { SceneManager.changeScene("HomeGUI.fxml"); }
}