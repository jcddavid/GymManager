package com.progetto.gymmanager.model;

import java.io.Serializable;

public class Esercizio implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nome;
    private int serie;
    private int ripetizioni;
    private String carico;

    public Esercizio(String nome, int serie, int ripetizioni, String carico) {
        this.nome = nome;
        this.serie = serie;
        this.ripetizioni = ripetizioni;
        this.carico = carico;
    }

    public static String ottieniVideoDiRiferimento(String nomeEsercizio) {
        if (nomeEsercizio.toLowerCase().contains("panca")) return "https://www.youtube.com/watch?v=panca_piana";
        if (nomeEsercizio.toLowerCase().contains("squat")) return "https://www.youtube.com/watch?v=squat_tutorial";
        if (nomeEsercizio.toLowerCase().contains("stacco")) return "https://www.youtube.com/watch?v=stacco_da_terra";
        return "Nessun video specifico trovato per " + nomeEsercizio + ". Cerca su YouTube!";
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public int getSerie() { return serie; }
    public void setSerie(int serie) { this.serie = serie; }
    public int getRipetizioni() { return ripetizioni; }
    public void setRipetizioni(int ripetizioni) { this.ripetizioni = ripetizioni; }
    public String getCarico() { return carico; }
    public void setCarico(String carico) { this.carico = carico; }
}