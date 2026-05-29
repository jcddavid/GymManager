package com.progetto.gymmanager.persistence.ser;

import com.progetto.gymmanager.model.CodiceSconto;
import com.progetto.gymmanager.model.Prodotto;
import com.progetto.gymmanager.persistence.dao.ShopDAO;
import com.progetto.gymmanager.persistence.memory.SystemData;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SerializableShopDAO implements ShopDAO {
    private static final String PROD_FILE = "prodotti.ser";
    private static final String SCON_FILE = "sconti.ser";
    private static final String NOTI_FILE = "notifiche.ser";

    @SuppressWarnings("unchecked")
    @Override
    public List<Prodotto> loadAllProdotti() throws IOException {
        File file = new File(PROD_FILE);
        if (!file.exists()) {
            List<Prodotto> defaultProds = new ArrayList<>(SystemData.getProdottiShop());
            saveAllProdotti(defaultProds);
            return defaultProds;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Prodotto>) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("Errore durante la deserializzazione dei prodotti.", e);
        }
    }

    private void saveAllProdotti(List<Prodotto> lista) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PROD_FILE))) {
            oos.writeObject(lista);
        }
    }

    @Override
    public void saveProdotto(Prodotto prodotto) throws IOException {
        List<Prodotto> lista = loadAllProdotti();
        lista.removeIf(p -> p.getNome().equalsIgnoreCase(prodotto.getNome()));
        lista.add(prodotto);
        saveAllProdotti(lista);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<CodiceSconto> loadAllSconti() throws IOException {
        File file = new File(SCON_FILE);
        if (!file.exists()) {
            List<CodiceSconto> defaultSconti = new ArrayList<>(SystemData.getCodiciSconto());
            saveAllSconti(defaultSconti);
            return defaultSconti;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<CodiceSconto>) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("Errore durante la deserializzazione degli sconti.", e);
        }
    }

    private void saveAllSconti(List<CodiceSconto> lista) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SCON_FILE))) {
            oos.writeObject(lista);
        }
    }

    @Override
    public void saveSconto(CodiceSconto sconto) throws IOException {
        List<CodiceSconto> lista = loadAllSconti();
        lista.removeIf(c -> c.getCodice().equalsIgnoreCase(sconto.getCodice()));
        lista.add(sconto);
        saveAllSconti(lista);
    }

    @Override
    public void deleteSconto(String codice) throws IOException {
        List<CodiceSconto> lista = loadAllSconti();
        lista.removeIf(c -> c.getCodice().equalsIgnoreCase(codice));
        saveAllSconti(lista);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<SystemData.Notifica> loadAllNotifiche() throws IOException {
        File file = new File(NOTI_FILE);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<SystemData.Notifica>) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("Errore durante la deserializzazione delle notifiche.", e);
        }
    }

    private void saveAllNotifiche(List<SystemData.Notifica> lista) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(NOTI_FILE))) {
            oos.writeObject(lista);
        }
    }

    @Override
    public void saveNotifica(SystemData.Notifica notifica) throws IOException {
        List<SystemData.Notifica> lista = loadAllNotifiche();
        lista.add(notifica);
        saveAllNotifiche(lista);
    }

    @Override
    public void updateNotifica(SystemData.Notifica notifica) throws IOException {
        List<SystemData.Notifica> lista = loadAllNotifiche();
        for (SystemData.Notifica n : lista) {
            if (n.getMessaggio().equals(notifica.getMessaggio()) && n.getUtenteDestinatario().equals(notifica.getUtenteDestinatario())) {
                n.setLettaDa(notifica.getLettaDa());
            }
        }
        saveAllNotifiche(lista);
    }
}