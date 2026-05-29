package com.progetto.gymmanager.view.cli;

import com.progetto.gymmanager.bean.SchedaAllenamentoBean;
import com.progetto.gymmanager.controller.SchedaAllenamentoController;
import com.progetto.gymmanager.engineering.singleton.SessioneAttuale;
import java.util.Scanner;

public class SchedaCLI {
    private final Scanner scanner;
    private final SchedaAllenamentoController controller = new SchedaAllenamentoController();

    public SchedaCLI(Scanner scanner) { this.scanner = scanner; }

    public void start() {
        String nick = SessioneAttuale.getInstance().getCurrentUser().getNickname();
        try {
            System.out.println("\n--- LE MIE SCHEDE DI ALLENAMENTO ---");
            System.out.println("1. Consulta le tue schede");
            System.out.println("2. Richiedi una NUOVA scheda all'istruttore");
            System.out.println("0. Indietro");
            System.out.print("Scelta: ");
            String c = scanner.nextLine();

            if ("1".equals(c)) {
                for (SchedaAllenamentoBean s : controller.consultaSchedeUtente(nick)) {
                    System.out.println("\nScheda: " + s.getNomeScheda() + " (Istr: " + s.getNicknameIstruttore() + ")");
                    s.getEsercizi().forEach(e ->
                            System.out.println(" - " + e.getNome() + " | " + e.getSerie() + "x" + e.getRipetizioni() + " | Carico: " + e.getCarico()));
                }
            } else if ("2".equals(c)) {
                System.out.print("Nome nuova scheda (es. Massa Estiva): "); String nome = scanner.nextLine();
                System.out.print("Note per istruttore: "); String note = scanner.nextLine();
                controller.richiediScheda(nick, nome, note);
                System.out.println(">>> Richiesta inviata con successo!");
            }
        } catch (Exception e) {
            System.out.println("[ERRORE] " + e.getMessage());
        }
    }
}