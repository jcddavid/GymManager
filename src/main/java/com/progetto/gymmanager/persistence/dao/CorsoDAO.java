package com.progetto.gymmanager.persistence.dao;

import com.progetto.gymmanager.bean.CorsoBean;
import com.progetto.gymmanager.persistence.memory.SystemData;
import java.io.IOException;
import java.util.List;

public interface CorsoDAO {
    List<CorsoBean> findAllCorsi() throws IOException;
    void saveCorso(CorsoBean corso) throws IOException;
    void deleteCorso(String nomeCorso) throws IOException;
    void updateCorso(CorsoBean corso) throws IOException;

    List<SystemData.Iscrizione> findAllIscrizioni() throws IOException;
    void saveIscrizione(SystemData.Iscrizione iscrizione) throws IOException;
    void updateIscrizione(SystemData.Iscrizione iscrizione) throws IOException;
    void deleteIscrizione(String nomeCorso, String atleta) throws IOException;
}
