package com.progetto.gymmanager.persistence.ser;

import com.progetto.gymmanager.bean.CorsoBean;
import com.progetto.gymmanager.persistence.dao.CorsoDAO;
import com.progetto.gymmanager.persistence.memory.SystemData;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SerializableCorsoDAO implements CorsoDAO {
    private static final String CORSI_FILE = "corsi.ser";
    private static final String ISCR_FILE = "iscrizioni.ser";

    @SuppressWarnings("unchecked")
    @Override
    public List<CorsoBean> findAllCorsi() throws IOException {
        File file = new File(CORSI_FILE);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<CorsoBean>) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("Errore durante la deserializzazione dei corsi.", e);
        }
    }

    private void saveAllCorsi(List<CorsoBean> lista) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CORSI_FILE))) {
            oos.writeObject(lista);
        }
    }

    @Override
    public void saveCorso(CorsoBean corso) throws IOException {
        List<CorsoBean> corsi = findAllCorsi();
        corsi.removeIf(c -> c.getNome().equalsIgnoreCase(corso.getNome()));
        corsi.add(corso);
        saveAllCorsi(corsi);
    }

    @Override
    public void deleteCorso(String nomeCorso) throws IOException {
        List<CorsoBean> corsi = findAllCorsi();
        corsi.removeIf(c -> c.getNome().equalsIgnoreCase(nomeCorso));
        saveAllCorsi(corsi);
    }

    @Override
    public void updateCorso(CorsoBean corso) throws IOException {
        saveCorso(corso);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<SystemData.Iscrizione> findAllIscrizioni() throws IOException {
        File file = new File(ISCR_FILE);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<SystemData.Iscrizione>) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("Errore durante la deserializzazione delle iscrizioni.", e);
        }
    }

    private void saveAllIscrizioni(List<SystemData.Iscrizione> lista) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ISCR_FILE))) {
            oos.writeObject(lista);
        }
    }

    @Override
    public void saveIscrizione(SystemData.Iscrizione iscrizione) throws IOException {
        List<SystemData.Iscrizione> lista = findAllIscrizioni();
        lista.removeIf(i -> i.getCorso().equalsIgnoreCase(iscrizione.getCorso()) && i.getAtleta().equalsIgnoreCase(iscrizione.getAtleta()));
        lista.add(iscrizione);
        saveAllIscrizioni(lista);
    }

    @Override
    public void updateIscrizione(SystemData.Iscrizione iscrizione) throws IOException {
        saveIscrizione(iscrizione);
    }

    @Override
    public void deleteIscrizione(String nomeCorso, String atleta) throws IOException {
        List<SystemData.Iscrizione> lista = findAllIscrizioni();
        lista.removeIf(i -> i.getCorso().equalsIgnoreCase(nomeCorso) && i.getAtleta().equalsIgnoreCase(atleta));
        saveAllIscrizioni(lista);
    }
}