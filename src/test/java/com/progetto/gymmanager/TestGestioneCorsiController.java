package com.progetto.gymmanager;

import com.progetto.gymmanager.controller.GestioneCorsiController;
import com.progetto.gymmanager.engineering.exception.CorsoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestGestioneCorsiController {
    // Author: Davide Miele
    private GestioneCorsiController corsiController;

    @BeforeEach
    void setUp() {
        corsiController = new GestioneCorsiController();
    }

    @Test
    void testCreazioneCorsoParametriMancanti() {
        assertThrows(CorsoException.class, () -> {
            corsiController.creaCorso("", "2024-10-10", "IstruttoreMarco");
        }, "Creare un corso senza nome deve lanciare CorsoException.");
    }

    @Test
    void testCreazioneCorsoSuccesso() {
        assertDoesNotThrow(() -> {
            corsiController.creaCorso("Pilates Avanzato", "2024-12-01", "IstruttoreMarco");
        }, "La creazione di un corso con parametri validi deve avere successo.");
    }

    @Test
    void testIscrizioneCorsoSenzaDati() {
        assertThrows(CorsoException.class, () -> {
            corsiController.richiediIscrizione("", "MarioRossi");
        }, "L'iscrizione con nome corso vuoto deve essere bloccata.");
    }
}