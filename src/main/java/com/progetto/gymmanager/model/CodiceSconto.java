package com.progetto.gymmanager.model;

import java.io.Serializable;

public class CodiceSconto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String codice;
    private int percentuale;

    public CodiceSconto(String codice, int percentuale) {
        this.codice = codice;
        this.percentuale = percentuale;
    }

    public String getCodice() { return codice; }
    public void setCodice(String codice) { this.codice = codice; }
    public int getPercentuale() { return percentuale; }
    public void setPercentuale(int percentuale) { this.percentuale = percentuale; }
}