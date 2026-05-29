package com.progetto.gymmanager.view.fx;

import com.progetto.gymmanager.controller.NotificheController;
import com.progetto.gymmanager.engineering.singleton.NotificationManager;
import com.progetto.gymmanager.engineering.singleton.SessioneAttuale;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import java.util.List;

public class NotificheGUIController {
    @FXML private ListView<String> notificheListView;
    private final NotificheController controller = new NotificheController();

    @FXML
    public void initialize() {
        String nick = SessioneAttuale.getInstance().getCurrentUser().getNickname();
        String ruolo = SessioneAttuale.getInstance().getCurrentUser().getRuolo().name();

        List<String> messaggi = controller.getNotificheUtente(nick, ruolo);
        if (messaggi.isEmpty()) {
            notificheListView.getItems().add("Nessuna nuova notifica.");
        } else {
            notificheListView.getItems().addAll(messaggi);
        }
        NotificationManager.getInstance().triggerNotifica();
    }

    @FXML protected void tornaHome() { SceneManager.changeScene("HomeGUI.fxml"); }
}