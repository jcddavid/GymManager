package com.progetto.gymmanager;

import com.progetto.gymmanager.controller.GestioneAbbonamentoController;
import com.progetto.gymmanager.engineering.exception.AbbonamentoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestGestioneAbbonamentoController {
    //Author: Davide Miele
    private GestioneAbbonamentoController abbonamentoController;

    @BeforeEach
    void setUp() {
        abbonamentoController = new GestioneAbbonamentoController();
    }

    @Test
    void testRinnovoMesiNegativi() {
        assertThrows(AbbonamentoException.class, () -> {
            abbonamentoController.rinnovaAbbonamento("MarioRossi", -3);
        }, "Il rinnovo con mesi negativi o zero deve lanciare AbbonamentoException.");
    }

    @Test
    void testRecuperoStatoAbbonamentoUtenteInesistente() {
        assertThrows(AbbonamentoException.class, () -> {
            abbonamentoController.getStatoAbbonamento(null);
        }, "Un nickname nullo deve generare un errore nel calcolo dell'abbonamento.");
    }
}