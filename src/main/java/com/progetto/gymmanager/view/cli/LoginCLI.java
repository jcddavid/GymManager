package com.progetto.gymmanager.view.cli;

import com.progetto.gymmanager.bean.CredentialsBean;
import com.progetto.gymmanager.bean.RegistrationBean;
import com.progetto.gymmanager.bean.UserBean;
import com.progetto.gymmanager.controller.LoginController;

import java.util.Scanner;

public class LoginCLI {
    private final Scanner scanner;
    private final LoginController loginController = new LoginController();

    public LoginCLI(Scanner scanner) {
        this.scanner = scanner;
    }

    public void start() {
        String choice = "";
        while (!choice.equals("3")) {
            System.out.println("\n--- MENU ACCESSO ---");
            System.out.println("1. Accedi");
            System.out.println("2. Registrati");
            System.out.println("3. Esci dal sistema");
            System.out.print("Scelta: ");

            choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    eseguiLogin();
                    break;
                case "2":
                    eseguiRegistrazione();
                    break;
                case "3":
                    System.out.println("Chiusura in corso... Arrivederci!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Scelta non valida. Riprova.");
            }
        }
    }

    private void eseguiLogin() {
        System.out.print("Nickname: ");
        String nick = scanner.nextLine();
        System.out.print("Password: ");
        String pass = scanner.nextLine();

        try {
            CredentialsBean creds = new CredentialsBean(nick, pass);
            UserBean user = loginController.login(creds);
            System.out.println("\n>>> Login effettuato con successo!");

            // Smista alla dashboard passando il ruolo
            new HomeCLI(scanner, user.getRuolo()).start();

        } catch (Exception e) {
            System.out.println("\n[ERRORE] " + e.getMessage());
        }
    }

    private void eseguiRegistrazione() {
        try {
            RegistrationBean bean = new RegistrationBean();
            System.out.print("Nickname: "); bean.setNickname(scanner.nextLine());
            System.out.print("Email: "); bean.setEmail(scanner.nextLine());
            System.out.print("Password: "); bean.setPassword(scanner.nextLine());
            System.out.print("Indirizzo: "); bean.setIndirizzo(scanner.nextLine());
            System.out.print("Telefono: "); bean.setTelefono(scanner.nextLine());
            System.out.print("Data Nascita (YYYY-MM-DD): "); bean.setDataNascita(scanner.nextLine());

            // Forza il ruolo base
            bean.setRuolo("ABBONATO");

            loginController.registraUtente(bean);
            System.out.println("\n>>> Registrazione completata! Ora puoi effettuare il login.");
        } catch (Exception e) {
            System.out.println("\n[ERRORE] " + e.getMessage());
        }
    }
}