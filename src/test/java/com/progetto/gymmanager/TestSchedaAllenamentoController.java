package com.progetto.gymmanager;

import com.progetto.gymmanager.bean.SchedaAllenamentoBean;
import com.progetto.gymmanager.controller.SchedaAllenamentoController;
import com.progetto.gymmanager.engineering.exception.SchedaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestSchedaAllenamentoController {
    //Author: David Dumbrava
    private SchedaAllenamentoController schedaController;

    @BeforeEach
    void setUp() {
        schedaController = new SchedaAllenamentoController();
    }

    @Test
    void testRichiediSchedaSenzaNome() {
        assertThrows(SchedaException.class, () -> {
            schedaController.richiediScheda("MarioRossi", "", "Voglio fare massa");
        }, "Deve lanciare eccezione se non viene fornito il nome della scheda.");
    }

    @Test
    void testCompilaSchedaSenzaEsercizi() {
        SchedaAllenamentoBean bean = new SchedaAllenamentoBean();
        bean.setNicknameAtleta("MarioRossi");
        bean.setNomeScheda("Forza Base");
        // Non aggiungiamo volontariamente nessun esercizio al bean

        assertThrows(SchedaException.class, () -> {
            schedaController.compilaScheda(bean);
        }, "Salvare una scheda vuota (0 esercizi) deve lanciare SchedaException.");
    }
}