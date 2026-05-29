package com.progetto.gymmanager.view.cli;

import java.util.Scanner;

public class MainCLI {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("  BENVENUTO IN GYMMANAGER (Versione CLI) ");
        System.out.println("=========================================");

        LoginCLI loginCLI = new LoginCLI(scanner);
        loginCLI.start();
    }
}