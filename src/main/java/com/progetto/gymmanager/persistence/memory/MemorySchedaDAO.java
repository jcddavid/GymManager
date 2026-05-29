package com.progetto.gymmanager.persistence.memory;

import com.progetto.gymmanager.model.SchedaAllenamento;
import com.progetto.gymmanager.persistence.dao.SchedaDAO;

import java.util.ArrayList;
import java.util.List;

public class MemorySchedaDAO implements SchedaDAO {
    private static final List<SchedaAllenamento> schede = new ArrayList<>();

    @Override
    public List<SchedaAllenamento> findSchedeByUtente(String nicknameUtente) {
        return schede.stream()
                .filter(s -> s.getUtente().equalsIgnoreCase(nicknameUtente))
                .toList();
    }

    @Override
    public List<SchedaAllenamento> findAllSchede() {
        return new ArrayList<>(schede);
    }

    @Override
    public void saveScheda(SchedaAllenamento scheda) {
        schede.removeIf(s -> s.getUtente().equalsIgnoreCase(scheda.getUtente()) && s.getNomeScheda().equalsIgnoreCase(scheda.getNomeScheda()));
        schede.add(scheda);
    }

    @Override
    public void deleteScheda(String nicknameUtente, String nomeScheda) {
        schede.removeIf(s -> s.getUtente().equalsIgnoreCase(nicknameUtente) && s.getNomeScheda().equalsIgnoreCase(nomeScheda));
    }
}