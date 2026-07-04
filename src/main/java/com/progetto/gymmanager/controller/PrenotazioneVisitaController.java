package com.progetto.gymmanager.controller;

import com.progetto.gymmanager.engineering.exception.PrenotazioneException;
import com.progetto.gymmanager.model.VisitaMedica;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class PrenotazioneVisitaController {

    private static final List<VisitaMedica> visiteTemporanee = new ArrayList<>();

    public void prenotaVisita(String data, String orario, String nicknameUtente) throws PrenotazioneException {
        if (data == null || orario == null || data.trim().isEmpty() || orario.trim().isEmpty()) {
            throw new PrenotazioneException("La data e l'orario della visita sono obbligatori.");
        }
        if (nicknameUtente == null || nicknameUtente.trim().isEmpty()) {
            throw new PrenotazioneException("Sessione utente non valida per la prenotazione.");
        }

        LocalDate dataPrenotazione;
        try {
            dataPrenotazione = LocalDate.parse(data);
        } catch (DateTimeParseException e) {
            throw new PrenotazioneException("Il formato della data non è valido. Usa il calendario.");
        }

        if (dataPrenotazione.isBefore(LocalDate.now(ZoneId.systemDefault())())) {
            throw new PrenotazioneException("Impossibile prenotare una visita medica nel passato.");
        }
        if (dataPrenotazione.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new PrenotazioneException("Il centro medico interno è chiuso la domenica.");
        }

        if (verificaIdoneitaInCorso(nicknameUtente)) {
            throw new PrenotazioneException("Hai già una prenotazione attiva. Disdici la precedente prima di prenotarne un'altra.");
        }

        visiteTemporanee.add(new VisitaMedica(nicknameUtente, data + " " + orario, false));
    }

    public List<String> getOrariDisponibili(String data) throws PrenotazioneException {
        if (data == null || data.trim().isEmpty()) {
            throw new PrenotazioneException("Selezionare una data valida per verificare gli orari.");
        }
        List<String> orari = new ArrayList<>();
        orari.add("09:00");
        orari.add("10:00");
        orari.add("11:00");
        orari.add("15:00");
        orari.add("16:00");
        orari.add("17:00");
        return orari;
    }

    public boolean verificaIdoneitaInCorso(String nicknameUtente) {
        if (nicknameUtente == null || nicknameUtente.trim().isEmpty()) {
            return false;
        }
        return visiteTemporanee.stream()
                .anyMatch(v -> v.getUtente().equalsIgnoreCase(nicknameUtente));
    }
}