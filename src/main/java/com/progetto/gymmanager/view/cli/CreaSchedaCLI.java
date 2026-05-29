package com.progetto.gymmanager.view.cli;

import com.progetto.gymmanager.bean.EsercizioBean;
import com.progetto.gymmanager.bean.SchedaAllenamentoBean;
import com.progetto.gymmanager.controller.SchedaAllenamentoController;
import com.progetto.gymmanager.engineering.singleton.SessioneAttuale;
import java.util.List;
import java.util.Scanner;

public class CreaSchedaCLI {
    private final Scanner scanner;
    private final SchedaAllenamentoController controller = new SchedaAllenamentoController();

    public CreaSchedaCLI(Scanner scanner) { this.scanner = scanner; }

    public void start() {
        try {
            System.out.println("\n--- RICHIESTE SCHEDE PENDENTI ---");
            List<SchedaAllenamentoBean> reqs = controller.ottieniRichiestePendenti();
            if (reqs.isEmpty()) { System.out.println("Nessuna richiesta."); return; }

            for (int i = 0; i < reqs.size(); i++) {
                System.out.println((i+1) + ". Atleta: " + reqs.get(i).getNicknameAtleta() + " | Scheda: " + reqs.get(i).getNomeScheda() + " | Note: " + reqs.get(i).getNoteRichiesta());
            }

            System.out.print("Seleziona richiesta da compilare (0 per uscire): ");
            int sel = Integer.parseInt(scanner.nextLine()) - 1;
            if (sel < 0) return;

            SchedaAllenamentoBean bean = reqs.get(sel);
            bean.setNicknameIstruttore(SessioneAttuale.getInstance().getCurrentUser().getNickname());

            while(true) {
                System.out.print("\nAggiungi Esercizio (Nome) [Lascia vuoto e premi INVIO per terminare e salvare]: ");
                String nomeEs = scanner.nextLine();
                if (nomeEs.trim().isEmpty()) break;

                EsercizioBean eb = new EsercizioBean();
                eb.setNome(nomeEs);
                System.out.print("Serie: "); eb.setSerie(Integer.parseInt(scanner.nextLine()));
                System.out.print("Reps: "); eb.setRipetizioni(Integer.parseInt(scanner.nextLine()));
                System.out.print("Carico: "); eb.setCarico(scanner.nextLine());
                bean.addEsercizio(eb);
            }

            controller.compilaScheda(bean);
            System.out.println(">>> Scheda compilata e salvata con successo!");
        } catch (Exception e) {
            System.out.println("[ERRORE] " + e.getMessage());
        }
    }
}