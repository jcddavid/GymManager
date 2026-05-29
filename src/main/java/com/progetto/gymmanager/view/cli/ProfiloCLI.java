package com.progetto.gymmanager.view.cli;

import com.progetto.gymmanager.bean.RegistrationBean;
import com.progetto.gymmanager.controller.GestioneProfiloController;
import com.progetto.gymmanager.engineering.singleton.SessioneAttuale;
import com.progetto.gymmanager.model.User;

import java.util.Scanner;

public class ProfiloCLI {
    private final Scanner scanner;
    private final GestioneProfiloController controller = new GestioneProfiloController();

    public ProfiloCLI(Scanner scanner) { this.scanner = scanner; }

    public void start() {
        System.out.println("\n--- PROFILO UTENTE ---");
        System.out.println("1. Modifica Dati Personali");
        System.out.println("2. Genera QR Code (Token) Accesso");
        System.out.println("0. Indietro");
        System.out.print("Scelta: ");

        String c = scanner.nextLine();
        if ("1".equals(c)) {
            try {
                User u = SessioneAttuale.getInstance().getCurrentUser();
                RegistrationBean bean = new RegistrationBean();
                System.out.print("Email [" + u.getEmail() + "]: "); bean.setEmail(scanner.nextLine());
                System.out.print("Indirizzo [" + u.getIndirizzo() + "]: "); bean.setIndirizzo(scanner.nextLine());
                System.out.print("Telefono [" + u.getTelefono() + "]: "); bean.setTelefono(scanner.nextLine());
                System.out.print("Data Nascita [" + u.getDataNascita() + "]: "); bean.setDataNascita(scanner.nextLine());

                controller.aggiornaDatiUtente(bean);
                System.out.println(">>> Dati aggiornati con successo.");
            } catch (Exception e) {
                System.out.println("[ERRORE] " + e.getMessage());
            }
        } else if ("2".equals(c)) {
            try {
                System.out.println("\n[QR CODE SIMULATO - IL TUO TOKEN DI ACCESSO]");
                System.out.println(">>> " + controller.generaQrCodeAccesso() + " <<<");
            } catch (Exception e) {
                System.out.println("[ERRORE] " + e.getMessage());
            }
        }
    }
}