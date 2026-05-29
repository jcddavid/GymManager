package com.progetto.gymmanager;

import com.progetto.gymmanager.view.fx.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class AppLauncher extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        SceneManager.setStage(stage);
        SceneManager.changeScene("LoginGUI.fxml");
        stage.setTitle("GymManager ISPW");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}