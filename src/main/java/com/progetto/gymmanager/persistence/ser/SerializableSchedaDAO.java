package com.progetto.gymmanager.persistence.ser;

import com.progetto.gymmanager.model.SchedaAllenamento;
import com.progetto.gymmanager.persistence.dao.SchedaDAO;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SerializableSchedaDAO implements SchedaDAO {
    private static final String FILE_PATH = "schede.ser";

    @SuppressWarnings("unchecked")
    @Override
    public List<SchedaAllenamento> findAllSchede() throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<SchedaAllenamento>) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("Errore durante la deserializzazione delle schede.", e);
        }
    }

    private void saveAll(List<SchedaAllenamento> schede) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(schede);
        }
    }

    @Override
    public void saveScheda(SchedaAllenamento scheda) throws IOException {
        List<SchedaAllenamento> schede = findAllSchede();
        schede.removeIf(s -> s.getUtente().equalsIgnoreCase(scheda.getUtente()) && s.getNomeScheda().equalsIgnoreCase(scheda.getNomeScheda()));
        schede.add(scheda);
        saveAll(schede);
    }

    @Override
    public List<SchedaAllenamento> findSchedeByUtente(String nicknameUtente) throws IOException {
        return findAllSchede().stream()
                .filter(s -> s.getUtente().equalsIgnoreCase(nicknameUtente))
                .toList();
    }

    @Override
    public void deleteScheda(String nicknameUtente, String nomeScheda) throws IOException {
        List<SchedaAllenamento> schede = findAllSchede();
        schede.removeIf(s -> s.getUtente().equalsIgnoreCase(nicknameUtente) && s.getNomeScheda().equalsIgnoreCase(nomeScheda));
        saveAll(schede);
    }
}