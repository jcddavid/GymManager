package com.progetto.gymmanager.bean;

import java.util.ArrayList;
import java.util.List;

public class SchedaAllenamentoBean {
    private String nicknameAtleta;
    private String nicknameIstruttore;
    private String nomeScheda;
    private String noteRichiesta;
    private List<EsercizioBean> esercizi = new ArrayList<>();

    public String getNicknameAtleta() { return nicknameAtleta; }
    public void setNicknameAtleta(String nicknameAtleta) { this.nicknameAtleta = nicknameAtleta; }
    public String getNicknameIstruttore() { return nicknameIstruttore; }
    public void setNicknameIstruttore(String nicknameIstruttore) { this.nicknameIstruttore = nicknameIstruttore; }
    public String getNomeScheda() { return nomeScheda; }
    public void setNomeScheda(String nomeScheda) { this.nomeScheda = nomeScheda; }
    public String getNoteRichiesta() { return noteRichiesta; }
    public void setNoteRichiesta(String noteRichiesta) { this.noteRichiesta = noteRichiesta; }
    public List<EsercizioBean> getEsercizi() { return esercizi; }
    public void addEsercizio(EsercizioBean es) { this.esercizi.add(es); }
}