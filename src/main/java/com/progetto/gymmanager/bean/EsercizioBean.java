package com.progetto.gymmanager.bean;

public class EsercizioBean {
    private String nome;
    private int serie;
    private int ripetizioni;
    private String carico;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public int getSerie() { return serie; }
    public void setSerie(int serie) { this.serie = serie; }
    public int getRipetizioni() { return ripetizioni; }
    public void setRipetizioni(int ripetizioni) { this.ripetizioni = ripetizioni; }
    public String getCarico() { return carico; }
    public void setCarico(String carico) { this.carico = carico; }
}