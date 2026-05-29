package com.progetto.gymmanager.persistence.memory;

import com.progetto.gymmanager.bean.CorsoBean;
import com.progetto.gymmanager.persistence.dao.CorsoDAO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MemoryCorsoDAO implements CorsoDAO {

    @Override
    public List<CorsoBean> findAllCorsi() throws IOException {
        return new ArrayList<>(SystemData.corsi);
    }

    @Override
    public void saveCorso(CorsoBean corso) throws IOException {
        SystemData.corsi.removeIf(c -> c.getNome().equalsIgnoreCase(corso.getNome()));
        SystemData.corsi.add(corso);
    }

    @Override
    public void deleteCorso(String nomeCorso) throws IOException {
        SystemData.corsi.removeIf(c -> c.getNome().equalsIgnoreCase(nomeCorso));
    }

    @Override
    public void updateCorso(CorsoBean corso) throws IOException {
        saveCorso(corso);
    }

    @Override
    public List<SystemData.Iscrizione> findAllIscrizioni() throws IOException {
        return new ArrayList<>(SystemData.iscrizioni);
    }

    @Override
    public void saveIscrizione(SystemData.Iscrizione iscrizione) throws IOException {
        SystemData.iscrizioni.removeIf(i -> i.getCorso().equalsIgnoreCase(iscrizione.getCorso()) && i.getAtleta().equalsIgnoreCase(iscrizione.getAtleta()));
        SystemData.iscrizioni.add(iscrizione);
    }

    @Override
    public void updateIscrizione(SystemData.Iscrizione iscrizione) throws IOException {
        saveIscrizione(iscrizione);
    }

    @Override
    public void deleteIscrizione(String nomeCorso, String atleta) throws IOException {
        SystemData.iscrizioni.removeIf(i -> i.getCorso().equalsIgnoreCase(nomeCorso) && i.getAtleta().equalsIgnoreCase(atleta));
    }
}