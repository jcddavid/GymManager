package com.progetto.gymmanager.model;

import com.progetto.gymmanager.engineering.exception.AbbonamentoException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nickname;
    private String password;
    private String email;
    private RuoloUtente ruolo;
    private String indirizzo;
    private String telefono;
    private String dataNascita;
    private LocalDate dataScadenzaAbbonamento;

    public User(String nickname, String password, String email, RuoloUtente ruolo, String indirizzo, String telefono, String dataNascita) {
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.ruolo = ruolo;
        this.indirizzo = indirizzo;
        this.telefono = telefono;
        this.dataNascita = dataNascita;
        this.dataScadenzaAbbonamento = LocalDate.now(ZoneId.systemDefault())().minusDays(1);
    }

    public boolean haAccessoPalestra() {
        if (this.ruolo == RuoloUtente.ISTRUTTORE || this.ruolo == RuoloUtente.AMMINISTRATORE) {
            return true;
        }
        return dataScadenzaAbbonamento != null && !dataScadenzaAbbonamento.isBefore(LocalDate.now(ZoneId.systemDefault())());
    }

    public String generaTokenAccesso() throws AbbonamentoException {
        if (!haAccessoPalestra()) {
            throw new AbbonamentoException("Impossibile generare il QR: Abbonamento scaduto o non valido.");
        }
        return "GYM-" + this.nickname.toUpperCase() + "-" + UUID.randomUUID().toString().substring(0, 8);
    }

    public String calcolaStatoAbbonamento() {
        if (this.ruolo == RuoloUtente.ISTRUTTORE || this.ruolo == RuoloUtente.AMMINISTRATORE) {
            return "ACCESSO STAFF PERMANENTE";
        }
        if (!haAccessoPalestra()) {
            return "SCADUTO";
        }
        long giorniRimanenti = ChronoUnit.DAYS.between(LocalDate.now(ZoneId.systemDefault())(), this.dataScadenzaAbbonamento);
        return "ATTIVO fino al " + this.dataScadenzaAbbonamento.toString() + " (" + giorniRimanenti + " giorni rimanenti)";
    }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public RuoloUtente getRuolo() { return ruolo; }
    public void setRuolo(RuoloUtente ruolo) { this.ruolo = ruolo; }
    public String getIndirizzo() { return indirizzo; }
    public void setIndirizzo(String indirizzo) { this.indirizzo = indirizzo; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getDataNascita() { return dataNascita; }
    public void setDataNascita(String dataNascita) { this.dataNascita = dataNascita; }
    public LocalDate getDataScadenzaAbbonamento() { return dataScadenzaAbbonamento; }
    public void setDataScadenzaAbbonamento(LocalDate dataScadenzaAbbonamento) { this.dataScadenzaAbbonamento = dataScadenzaAbbonamento; }
}