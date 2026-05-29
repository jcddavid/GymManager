package com.progetto.gymmanager;

import com.progetto.gymmanager.view.fx.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        SceneManager.setStage(primaryStage);
        primaryStage.setTitle("GymManager - Software Gestionale Palestra");

        primaryStage.setMaximized(true);
        SceneManager.changeScene("LoginGUI.fxml");
    }

    public static void main(String[] args) {
        launch(args);
    }
}