package com.progetto.gymmanager.view.fx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.logging.*;

public abstract class SceneManager {
    private static Stage primaryStage;
    private static final Logger logger = Logger.getLogger(SceneManager.class.getName());

    private SceneManager() { // Obbligato a farlo per bypassare il code smell di SonarCloud
        throw new IllegalStateException("Questa è una utility class!");
    }

    public static void setStage(Stage stage) {
        primaryStage = stage;
    }

    public static void changeScene(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/com/progetto/gymmanager/fx/" + fxml));
            Parent root = loader.load();

            if (primaryStage.getScene() == null) {
                primaryStage.setScene(new Scene(root));
            } else {
                primaryStage.getScene().setRoot(root);
            }

            primaryStage.setMaximized(true);
            primaryStage.show();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error changing the scene", e);
        }
    }
}