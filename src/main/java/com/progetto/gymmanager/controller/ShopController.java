package com.progetto.gymmanager.controller;

import com.progetto.gymmanager.bean.ProdottoShopBean;
import com.progetto.gymmanager.engineering.exception.AcquistoException;
import com.progetto.gymmanager.engineering.factory.DAOFactory;
import com.progetto.gymmanager.model.CodiceSconto;
import com.progetto.gymmanager.model.Prodotto;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

public class ShopController {
    private static final Logger logger = Logger.getLogger(ShopController.class.getName());

    public List<ProdottoShopBean> getProdottiDisponibili() {
        List<ProdottoShopBean> beans = new ArrayList<>();
        try {
            for (Prodotto p : DAOFactory.getDAOFactory().getShopDAO().loadAllProdotti()) {
                ProdottoShopBean bean = new ProdottoShopBean();
                bean.setNome(p.getNome());
                bean.setPrezzo(p.getPrezzo());
                beans.add(bean);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Errore caricamento prodotti disponibili", e);
        }
        return beans;
    }

    public double calcolaTotale(List<ProdottoShopBean> carrello, String codiceSconto) {
        double totale = 0.0;
        for (ProdottoShopBean p : carrello) {
            totale += p.getPrezzo();
        }
        if (codiceSconto != null && !codiceSconto.trim().isEmpty()) {
            totale = applicaSconto(totale, codiceSconto);
        }
        return totale;
    }

    private double applicaSconto(double totale, String codice) {
        try {
            for (CodiceSconto cs : DAOFactory.getDAOFactory().getShopDAO().loadAllSconti()) {
                if (cs.getCodice().equalsIgnoreCase(codice.trim())) {
                    return totale - (totale * cs.getPercentuale() / 100.0);
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Errore applicazione sconto", e);
        }
        return totale;
    }

    public void finalizzaAcquisto(List<ProdottoShopBean> carrello, String nicknameUtente, String codiceSconto) throws AcquistoException {
        if (carrello == null || carrello.isEmpty()) {
            throw new AcquistoException("Il carrello e' vuoto. Aggiungi dei prodotti prima di pagare.");
        }
        if (nicknameUtente == null || nicknameUtente.trim().isEmpty()) {
            throw new AcquistoException("Utente non identificato.");
        }
        double totaleDaPagare = calcolaTotale(carrello, codiceSconto);
        if (totaleDaPagare < 5.00) {
            throw new AcquistoException("L'ordine minimo deve essere di 5.00 euro.");
        }
    }
}