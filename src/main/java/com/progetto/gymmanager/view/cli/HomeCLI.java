package com.progetto.gymmanager.view.cli;

import com.progetto.gymmanager.controller.NotificheController;
import com.progetto.gymmanager.engineering.singleton.SessioneAttuale;
import java.util.Scanner;

public class HomeCLI {
    private final Scanner scanner;
    private final String ruolo;
    private final String nickname;
    private final NotificheController notificheController = new NotificheController();

    private static final String ABBONATO = "ABBONATO";
    private static final String ISTRUTTORE = "ISTRUTTORE";
    private static final String AMMINISTRATORE = "AMMINISTRATORE";

    public HomeCLI(Scanner scanner, String ruolo) {
        this.scanner = scanner;
        this.ruolo = ruolo.toUpperCase();
        this.nickname = SessioneAttuale.getInstance().getCurrentUser().getNickname();
    }

    public void start() {
        boolean running = true;
        while (running) {
            int notifiche = notificheController.contaNotificheNonLette(nickname, ruolo);
            stampaMenu(notifiche);
            String choice = scanner.nextLine().toUpperCase();
            running = gestisciScelta(choice);
        }
    }

    private void stampaMenu(int notifiche) {
        System.out.println("\n=== HOME GYMMANAGER ===");
        System.out.println("Benvenuto, " + nickname + " [" + ruolo + "]");
        System.out.println("N. Notifiche [" + notifiche + " da leggere]");

        System.out.println("1. Il Mio Profilo & QR Accesso");

        if (ruolo.equals(ABBONATO)) {
            System.out.println("2. Gestione Abbonamento");
            System.out.println("3. Prenotazione Corsi");
            System.out.println("4. Le Mie Schede di Allenamento");
            System.out.println("5. Prenota Visita Medica");
        } else if (ruolo.equals(ISTRUTTORE)) {
            System.out.println("2. Area Istruttore: Gestione Corsi");
            System.out.println("3. Area Istruttore: Crea Schede");
        } else if (ruolo.equals(AMMINISTRATORE)) {
            System.out.println("2. Pannello Amministratore");
        }

        System.out.println("S. Shop Integrato");
        System.out.println("0. Logout");
        System.out.print("Scelta: ");
    }

    private boolean gestisciScelta(String choice) {
        switch (choice) {
            case "N":
                new NotificheCLI(scanner).start();
                break;
            case "1":
                new ProfiloCLI(scanner).start();
                break;
            case "S":
                new ShopCLI(scanner).start();
                break;
            case "0":
                SessioneAttuale.getInstance().logout();
                return false;
            case "2":
                if (ruolo.equals(ABBONATO)) new AbbonamentoCLI(scanner).start();
                else if (ruolo.equals(ISTRUTTORE)) new CorsiIstruttoreCLI(scanner).start();
                else if (ruolo.equals(AMMINISTRATORE)) new AmministratoreCLI(scanner).start();
                break;
            case "3":
                if (ruolo.equals(ABBONATO)) new CorsiCLI(scanner).start();
                else if (ruolo.equals(ISTRUTTORE)) new CreaSchedaCLI(scanner).start();
                break;
            case "4":
                if (ruolo.equals(ABBONATO)) new SchedaCLI(scanner).start();
                break;
            case "5":
                if (ruolo.equals(ABBONATO)) new VisitaMedicaCLI(scanner).start();
                break;
            default:
                System.out.println("Scelta non valida.");
        }
        return true;
    }
}