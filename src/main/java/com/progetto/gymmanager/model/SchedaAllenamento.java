package com.progetto.gymmanager.model;

import java.io.Serializable;
import java.util.List;

public class SchedaAllenamento implements Serializable {
    private static final long serialVersionUID = 1L;

    private String utente;
    private String istruttore;
    private String nomeScheda;
    private String note;
    private List<Esercizio> esercizi;

    public SchedaAllenamento(String utente, String istruttore, String nomeScheda, List<Esercizio> esercizi) {
        this.utente = utente;
        this.istruttore = istruttore;
        this.nomeScheda = nomeScheda;
        this.esercizi = esercizi;
        this.note = "";
    }

    public String getUtente() { return utente; }
    public void setUtente(String utente) { this.utente = utente; }
    public String getIstruttore() { return istruttore; }
    public void setIstruttore(String istruttore) { this.istruttore = istruttore; }
    public String getNomeScheda() { return nomeScheda; }
    public void setNomeScheda(String nomeScheda) { this.nomeScheda = nomeScheda; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public List<Esercizio> getEsercizi() { return esercizi; }
    public void setEsercizi(List<Esercizio> esercizi) { this.esercizi = esercizi; }
}