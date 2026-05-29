package com.progetto.gymmanager.persistence.fs;

import com.progetto.gymmanager.model.CodiceSconto;
import com.progetto.gymmanager.model.Prodotto;
import com.progetto.gymmanager.persistence.dao.ShopDAO;
import com.progetto.gymmanager.persistence.memory.SystemData;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileSystemShopDAO implements ShopDAO {
    private static final String PROD_FILE = "prodotti.csv";
    private static final String SCON_FILE = "sconti.csv";
    private static final String NOTI_FILE = "notifiche.csv";

    @Override
    public List<Prodotto> loadAllProdotti() throws IOException {
        List<Prodotto> lista = new ArrayList<>();
        File file = new File(PROD_FILE);
        if (!file.exists()) {
            lista.addAll(SystemData.getProdottiShop());
            saveAllProdotti(lista);
            return lista;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length >= 2) {
                    lista.add(new Prodotto(data[0], Double.parseDouble(data[1])));
                }
            }
        }
        return lista;
    }

    private void saveAllProdotti(List<Prodotto> lista) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PROD_FILE))) {
            for (Prodotto p : lista) {
                bw.write(p.getNome() + "," + p.getPrezzo());
                bw.newLine();
            }
        }
    }

    @Override
    public void saveProdotto(Prodotto prodotto) throws IOException {
        List<Prodotto> lista = loadAllProdotti();
        lista.removeIf(p -> p.getNome().equalsIgnoreCase(prodotto.getNome()));
        lista.add(prodotto);
        saveAllProdotti(lista);
    }

    @Override
    public List<CodiceSconto> loadAllSconti() throws IOException {
        List<CodiceSconto> lista = new ArrayList<>();
        File file = new File(SCON_FILE);
        if (!file.exists()) {
            lista.addAll(SystemData.getCodiciSconto());
            saveAllSconti(lista);
            return lista;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length >= 2) {
                    lista.add(new CodiceSconto(data[0], Integer.parseInt(data[1])));
                }
            }
        }
        return lista;
    }

    private void saveAllSconti(List<CodiceSconto> lista) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SCON_FILE))) {
            for (CodiceSconto c : lista) {
                bw.write(c.getCodice() + "," + c.getPercentuale());
                bw.newLine();
            }
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

    @Override
    public List<SystemData.Notifica> loadAllNotifiche() throws IOException {
        List<SystemData.Notifica> lista = new ArrayList<>();
        File file = new File(NOTI_FILE);
        if (!file.exists()) return lista;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length >= 3) {
                    lista.add(new SystemData.Notifica(data[0], data[1], data[2]));
                } else if (data.length == 2) {
                    lista.add(new SystemData.Notifica(data[0], data[1], ""));
                }
            }
        }
        return lista;
    }

    @Override
    public void saveNotifica(SystemData.Notifica notifica) throws IOException {
        List<SystemData.Notifica> lista = loadAllNotifiche();
        lista.add(notifica);
        writeNotifiche(lista);
    }

    @Override
    public void updateNotifica(SystemData.Notifica notifica) throws IOException {
        List<SystemData.Notifica> lista = loadAllNotifiche();
        for (SystemData.Notifica n : lista) {
            if (n.getMessaggio().equals(notifica.getMessaggio()) && n.getUtenteDestinatario().equals(notifica.getUtenteDestinatario())) {
                n.setLettaDa(notifica.getLettaDa());
            }
        }
        writeNotifiche(lista);
    }

    private void writeNotifiche(List<SystemData.Notifica> lista) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(NOTI_FILE))) {
            for (SystemData.Notifica n : lista) {
                bw.write(n.getUtenteDestinatario() + "," + n.getMessaggio() + "," + n.getLettaDa());
                bw.newLine();
            }
        }
    }
}