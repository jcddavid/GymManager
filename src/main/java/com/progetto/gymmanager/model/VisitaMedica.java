package com.progetto.gymmanager.model;

public class VisitaMedica {
    private String utente;
    private String data;
    private boolean idoneo;

    public VisitaMedica(String utente, String data, boolean idoneo) {
        this.utente = utente;
        this.data = data;
        this.idoneo = idoneo;
    }

    public String getUtente() { return utente; }
    public String getData() { return data; }
    public boolean isIdoneo() { return idoneo; }
}