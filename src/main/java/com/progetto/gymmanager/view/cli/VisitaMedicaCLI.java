package com.progetto.gymmanager.view.cli;

import com.progetto.gymmanager.controller.PrenotazioneVisitaController;
import com.progetto.gymmanager.engineering.singleton.SessioneAttuale;
import java.util.List;
import java.util.Scanner;

public class VisitaMedicaCLI {
    private final Scanner scanner;
    private final PrenotazioneVisitaController controller = new PrenotazioneVisitaController();

    public VisitaMedicaCLI(Scanner scanner) { this.scanner = scanner; }

    public void start() {
        String nick = SessioneAttuale.getInstance().getCurrentUser().getNickname();
        System.out.println("\n--- VISITA MEDICA INTERNA ---");
        boolean idoneo = controller.verificaIdoneitaInCorso(nick);
        System.out.println("Stato Certificato: " + (idoneo ? "VALIDO" : "ASSENTE O SCADUTO"));

        System.out.print("Inserisci data desiderata (YYYY-MM-DD) o 0 per uscire: ");
        String data = scanner.nextLine();
        if ("0".equals(data)) return;

        try {
            List<String> orari = controller.getOrariDisponibili(data);
            System.out.println("Orari disponibili:");
            for (int i = 0; i < orari.size(); i++) System.out.println((i+1) + ". " + orari.get(i));

            System.out.print("Seleziona orario (1-" + orari.size() + "): ");
            int sc = Integer.parseInt(scanner.nextLine()) - 1;

            controller.prenotaVisita(data, orari.get(sc), nick);
            System.out.println(">>> Visita prenotata con successo!");
        } catch (Exception e) {
            System.out.println("[ERRORE] " + e.getMessage());
        }
    }
}