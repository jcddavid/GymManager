package com.progetto.gymmanager.view.cli;

import com.progetto.gymmanager.controller.GestioneCorsiController;
import com.progetto.gymmanager.engineering.singleton.SessioneAttuale;
import java.util.List;
import java.util.Scanner;

public class CorsiIstruttoreCLI {
    private final Scanner scanner;
    private final GestioneCorsiController controller = new GestioneCorsiController();

    public CorsiIstruttoreCLI(Scanner scanner) { this.scanner = scanner; }

    public void start() {
        String nick = SessioneAttuale.getInstance().getCurrentUser().getNickname();
        try {
            System.out.println("\n--- GESTIONE CORSI ISTRUTTORE ---");
            System.out.println("1. Crea Nuovo Corso");
            System.out.println("2. Gestisci Richieste Pendenti");
            System.out.println("0. Indietro");
            System.out.print("Scelta: ");
            String c = scanner.nextLine();

            if ("1".equals(c)) {
                System.out.print("Nome Corso: "); String nome = scanner.nextLine();
                System.out.print("Data Corso (YYYY-MM-DD): "); String data = scanner.nextLine();
                controller.creaCorso(nome, data, nick);
                System.out.println(">>> Corso creato!");
            } else if ("2".equals(c)) {
                List<String> richieste = controller.getRichiestePendenti();
                for (int i = 0; i < richieste.size(); i++) System.out.println((i+1) + ". " + richieste.get(i));
                if (richieste.isEmpty()) { System.out.println("Nessuna richiesta."); return; }

                System.out.print("Seleziona richiesta: "); int sel = Integer.parseInt(scanner.nextLine()) - 1;
                System.out.print("Approvare? (S/N): "); boolean app = scanner.nextLine().equalsIgnoreCase("S");
                controller.gestisciRichiesta(richieste.get(sel), app);
                System.out.println(">>> Richiesta gestita.");
            }
        } catch (Exception e) {
            System.out.println("[ERRORE] " + e.getMessage());
        }
    }
}