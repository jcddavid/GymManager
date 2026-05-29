package com.progetto.gymmanager.view.cli;

import com.progetto.gymmanager.controller.NotificheController;
import com.progetto.gymmanager.engineering.singleton.SessioneAttuale;
import java.util.List;
import java.util.Scanner;

public class NotificheCLI {
    private final Scanner scanner;
    private final NotificheController controller = new NotificheController();

    public NotificheCLI(Scanner scanner) {
        this.scanner = scanner;
    }

    public void start() {
        String nick = SessioneAttuale.getInstance().getCurrentUser().getNickname();
        String ruolo = SessioneAttuale.getInstance().getCurrentUser().getRuolo().name();

        System.out.println("\n--- LE TUE NOTIFICHE ---");
        List<String> messaggi = controller.getNotificheUtente(nick, ruolo);

        if (messaggi.isEmpty()) {
            System.out.println("Nessuna nuova notifica.");
        } else {
            for (String m : messaggi) {
                System.out.println("- " + m);
            }
        }

        System.out.println("Premi INVIO per tornare indietro.");
        scanner.nextLine();
    }
}