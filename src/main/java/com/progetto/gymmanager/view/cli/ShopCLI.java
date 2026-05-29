package com.progetto.gymmanager.view.cli;

import com.progetto.gymmanager.bean.ProdottoShopBean;
import com.progetto.gymmanager.controller.ShopController;
import com.progetto.gymmanager.engineering.singleton.SessioneAttuale;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ShopCLI {
    private final Scanner scanner;
    private final ShopController controller = new ShopController();
    private final List<ProdottoShopBean> carrello = new ArrayList<>();

    public ShopCLI(Scanner scanner) {
        this.scanner = scanner;
    }

    public void start() {
        boolean nelloShop = true;

        while (nelloShop) {
            System.out.println("\n--- SHOP GYMMANAGER ---");
            List<ProdottoShopBean> catalogo = controller.getProdottiDisponibili();

            for (int i = 0; i < catalogo.size(); i++) {
                System.out.println((i + 1) + ". " + catalogo.get(i).getNome() + " - €" + catalogo.get(i).getPrezzo());
            }

            System.out.println("C. Vai al Carrello e Paga (" + carrello.size() + " elementi)");
            System.out.println("0. Torna alla Home");
            System.out.print("Scelta: ");

            String choice = scanner.nextLine();

            if ("0".equals(choice)) {
                nelloShop = false;
            } else if ("C".equalsIgnoreCase(choice)) {
                paga();
            } else {
                try {
                    int index = Integer.parseInt(choice) - 1;
                    if (index >= 0 && index < catalogo.size()) {
                        carrello.add(catalogo.get(index));
                        System.out.println(">>> Aggiunto al carrello: " + catalogo.get(index).getNome());
                    } else {
                        System.out.println("Prodotto non trovato.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Input non valido.");
                }
            }
        }
    }

    private void paga() {
        if (carrello.isEmpty()) {
            System.out.println("\n[AVVISO] Il carrello è vuoto!");
            return;
        }

        double totale = controller.calcolaTotale(carrello, "");
        System.out.println("\nTotale provvisorio: €" + String.format("%.2f", totale));
        System.out.print("Hai un codice sconto? (Premi INVI per saltare): ");
        String codice = scanner.nextLine();

        try {
            String nick = SessioneAttuale.getInstance().getCurrentUser().getNickname();
            controller.finalizzaAcquisto(carrello, nick, codice);

            System.out.println("\n>>> Acquisto completato con successo! Transazione elaborata.");
            carrello.clear(); // Svuota carrello dopo l'acquisto
        } catch (Exception e) {
            System.out.println("\n[ERRORE DI ACQUISTO] " + e.getMessage());
        }
    }
}