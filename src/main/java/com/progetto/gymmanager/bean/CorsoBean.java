package com.progetto.gymmanager.bean;

import java.io.Serializable;

public class CorsoBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nome;
    private String data;
    private String istruttore;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
    public String getIstruttore() { return istruttore; }
    public void setIstruttore(String istruttore) { this.istruttore = istruttore; }
}