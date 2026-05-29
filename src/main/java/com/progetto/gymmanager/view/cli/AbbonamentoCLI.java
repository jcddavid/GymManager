package com.progetto.gymmanager.view.cli;

import com.progetto.gymmanager.controller.GestioneAbbonamentoController;
import com.progetto.gymmanager.engineering.singleton.SessioneAttuale;
import java.util.Scanner;

public class AbbonamentoCLI {
    private final Scanner scanner;
    private final GestioneAbbonamentoController controller = new GestioneAbbonamentoController();

    public AbbonamentoCLI(Scanner scanner) { this.scanner = scanner; }

    public void start() {
        String nick = SessioneAttuale.getInstance().getCurrentUser().getNickname();
        try {
            System.out.println("\n--- GESTIONE ABBONAMENTO ---");
            System.out.println("Stato Attuale: " + controller.getStatoAbbonamento(nick));

            System.out.println("1. Rinnova Trimestrale (€99.99)");
            System.out.println("2. Rinnova Annuale (€349.99)");
            System.out.println("0. Indietro");
            System.out.print("Scelta: ");
            String c = scanner.nextLine();

            if ("1".equals(c)) controller.rinnovaAbbonamento(nick, 3);
            else if ("2".equals(c)) controller.rinnovaAbbonamento(nick, 12);

            if ("1".equals(c) || "2".equals(c)) System.out.println(">>> Abbonamento rinnovato con successo!");
        } catch (Exception e) {
            System.out.println("[ERRORE] " + e.getMessage());
        }
    }
}