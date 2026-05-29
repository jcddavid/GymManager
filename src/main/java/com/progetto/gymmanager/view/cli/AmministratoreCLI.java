package com.progetto.gymmanager.view.cli;

import com.progetto.gymmanager.controller.AmministratoreController;
import java.util.Scanner;

public class AmministratoreCLI {
    private final Scanner scanner;
    private final AmministratoreController controller = new AmministratoreController();

    public AmministratoreCLI(Scanner scanner) { this.scanner = scanner; }

    public void start() {
        while(true) {
            System.out.println("\n--- PANNELLO AMMINISTRATORE ---");
            System.out.println("1. Visualizza Istruttori");
            System.out.println("2. Aggiungi Prodotto Shop");
            System.out.println("3. Ordina Rifornimento Magazzino");
            System.out.println("4. Invia Comunicazione Broadcast");
            System.out.println("0. Indietro");
            System.out.print("Scelta: ");
            String c = scanner.nextLine();

            try {
                switch(c) {
                    case "1":
                        controller.getListaIstruttori().forEach(System.out::println);
                        break;
                    case "2":
                        System.out.print("Nome Prodotto: "); String nome = scanner.nextLine();
                        System.out.print("Prezzo: "); double prezzo = Double.parseDouble(scanner.nextLine());
                        controller.aggiungiProdotto(nome, prezzo);
                        System.out.println(">>> Prodotto aggiunto al DB!");
                        break;
                    case "3":
                        System.out.print("Fornitore: "); String forn = scanner.nextLine();
                        System.out.print("Prodotto: "); String prod = scanner.nextLine();
                        System.out.print("Quantità: "); int qty = Integer.parseInt(scanner.nextLine());
                        controller.ordinaRifornimento(forn, prod, qty);
                        System.out.println(">>> Ordine inoltrato e notificato.");
                        break;
                    case "4":
                        System.out.print("Target (TUTTI/ABBONATI/ISTRUTTORI): "); String target = scanner.nextLine().toUpperCase();
                        System.out.print("Messaggio: "); String msg = scanner.nextLine();
                        controller.inviaComunicazione(target, msg);
                        System.out.println(">>> Comunicazione Push inviata!");
                        break;
                    case "0": return;
                    default: System.out.println("Scelta non valida.");
                }
            } catch (Exception e) {
                System.out.println("[ERRORE ADMIN] " + e.getMessage());
            }
        }
    }
}