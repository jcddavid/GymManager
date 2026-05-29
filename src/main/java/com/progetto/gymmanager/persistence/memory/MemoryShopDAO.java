package com.progetto.gymmanager.persistence.memory;

import com.progetto.gymmanager.model.CodiceSconto;
import com.progetto.gymmanager.model.Prodotto;
import com.progetto.gymmanager.persistence.dao.ShopDAO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MemoryShopDAO implements ShopDAO {

    @Override
    public List<Prodotto> loadAllProdotti() throws IOException {
        return new ArrayList<>(SystemData.prodottiShop);
    }

    @Override
    public void saveProdotto(Prodotto prodotto) throws IOException {
        SystemData.prodottiShop.removeIf(p -> p.getNome().equalsIgnoreCase(prodotto.getNome()));
        SystemData.prodottiShop.add(prodotto);
    }

    @Override
    public List<CodiceSconto> loadAllSconti() throws IOException {
        return new ArrayList<>(SystemData.codiciSconto);
    }

    @Override
    public void saveSconto(CodiceSconto sconto) throws IOException {
        SystemData.codiciSconto.removeIf(c -> c.getCodice().equalsIgnoreCase(sconto.getCodice()));
        SystemData.codiciSconto.add(sconto);
    }

    @Override
    public void deleteSconto(String codice) throws IOException {
        SystemData.codiciSconto.removeIf(c -> c.getCodice().equalsIgnoreCase(codice));
    }

    @Override
    public List<SystemData.Notifica> loadAllNotifiche() throws IOException {
        return new ArrayList<>(SystemData.notifiche);
    }

    @Override
    public void saveNotifica(SystemData.Notifica notifica) throws IOException {
        SystemData.notifiche.add(notifica);
    }

    @Override
    public void updateNotifica(SystemData.Notifica notifica) throws IOException {
        for (SystemData.Notifica n : SystemData.notifiche) {
            if (n.getMessaggio().equals(notifica.getMessaggio()) && n.getUtenteDestinatario().equals(notifica.getUtenteDestinatario())) {
                n.setLettaDa(notifica.getLettaDa());
            }
        }
    }
}