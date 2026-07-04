package com.progetto.gymmanager;

import com.progetto.gymmanager.controller.PrenotazioneVisitaController;
import com.progetto.gymmanager.engineering.exception.PrenotazioneException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class TestPrenotazioneVisitaController {
    // Author: Davide Miele
    private PrenotazioneVisitaController visitaController;

    @BeforeEach
    void setUp() {
        visitaController = new PrenotazioneVisitaController();
    }

    @Test
    void testPrenotaVisitaDataPassata() {
        String dataPassata = LocalDate.now(ZoneId.systemDefault())().minusDays(5).toString();

        assertThrows(PrenotazioneException.class, () -> {
            visitaController.prenotaVisita(dataPassata, "10:00", "UtenteTest");
        }, "Non si può prenotare una visita nel passato.");
    }

    @Test
    void testPrenotaVisitaDiDomenica() {
        String domenica = "2030-03-17";

        assertThrows(PrenotazioneException.class, () -> {
            visitaController.prenotaVisita(domenica, "10:00", "UtenteTest");
        }, "Le prenotazioni di domenica devono essere bloccate dal sistema.");
    }
}