package com.progetto.gymmanager.persistence.dao;

import com.progetto.gymmanager.model.CodiceSconto;
import com.progetto.gymmanager.model.Prodotto;
import com.progetto.gymmanager.persistence.memory.SystemData;
import java.io.IOException;
import java.util.List;

public interface ShopDAO {
    List<Prodotto> loadAllProdotti() throws IOException;
    void saveProdotto(Prodotto prodotto) throws IOException;

    List<CodiceSconto> loadAllSconti() throws IOException;
    void saveSconto(CodiceSconto sconto) throws IOException;
    void deleteSconto(String codice) throws IOException;

    List<SystemData.Notifica> loadAllNotifiche() throws IOException;
    void saveNotifica(SystemData.Notifica notifica) throws IOException;
    void updateNotifica(SystemData.Notifica notifica) throws IOException;
}