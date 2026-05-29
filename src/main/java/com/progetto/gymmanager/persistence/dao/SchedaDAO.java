package com.progetto.gymmanager.persistence.dao;

import com.progetto.gymmanager.model.SchedaAllenamento;
import java.io.IOException;
import java.util.List;

public interface SchedaDAO {
    List<SchedaAllenamento> findSchedeByUtente(String nicknameUtente) throws IOException;
    List<SchedaAllenamento> findAllSchede() throws IOException;
    void saveScheda(SchedaAllenamento scheda) throws IOException;
    void deleteScheda(String nicknameUtente, String nomeScheda) throws IOException;
}