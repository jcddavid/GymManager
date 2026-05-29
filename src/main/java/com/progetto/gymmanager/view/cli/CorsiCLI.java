package com.progetto.gymmanager.view.cli;

import com.progetto.gymmanager.bean.CorsoBean;
import com.progetto.gymmanager.controller.GestioneCorsiController;
import com.progetto.gymmanager.engineering.singleton.SessioneAttuale;
import java.util.List;
import java.util.Scanner;

public class CorsiCLI {
    private final Scanner scanner;
    private final GestioneCorsiController controller = new GestioneCorsiController();

    public CorsiCLI(Scanner scanner) { this.scanner = scanner; }

    public void start() {
        try {
            List<CorsoBean> corsi = controller.getCorsiDisponibili();
            System.out.println("\n--- PRENOTAZIONE CORSI ---");
            for (int i = 0; i < corsi.size(); i++) {
                System.out.println((i+1) + ". " + corsi.get(i).getNome() + " - " + corsi.get(i).getData() + " (Istr: " + corsi.get(i).getIstruttore() + ")");
            }
            System.out.println("I. Iscriviti a un corso");
            System.out.println("D. Disdici iscrizione");
            System.out.println("0. Indietro");
            System.out.print("Scelta: ");
            String c = scanner.nextLine().toUpperCase();

            if ("0".equals(c)) return;

            System.out.print("Seleziona ID corso: ");
            int id = Integer.parseInt(scanner.nextLine()) - 1;
            String nomeCorso = corsi.get(id).getNome();
            String nick = SessioneAttuale.getInstance().getCurrentUser().getNickname();

            if ("I".equals(c)) {
                controller.richiediIscrizione(nomeCorso, nick);
                System.out.println(">>> Richiesta inviata all'istruttore!");
            } else if ("D".equals(c)) {
                controller.disiscriviUtente(nomeCorso, nick);
                System.out.println(">>> Iscrizione annullata.");
            }
        } catch (Exception e) {
            System.out.println("[ERRORE] " + e.getMessage());
        }
    }
}