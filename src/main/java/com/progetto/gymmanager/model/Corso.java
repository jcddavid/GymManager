package com.progetto.gymmanager.model;

public class Corso {
    private String nome;
    private String istruttore;

    public Corso(String nome, String istruttore) {
        this.nome = nome;
        this.istruttore = istruttore;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getIstruttore() { return istruttore; }
    public void setIstruttore(String istruttore) { this.istruttore = istruttore; }
}