package com.progetto.gymmanager.controller;

import com.progetto.gymmanager.bean.RegistrationBean;
import com.progetto.gymmanager.engineering.exception.LoginException;
import com.progetto.gymmanager.engineering.factory.DAOFactory;
import com.progetto.gymmanager.engineering.singleton.NotificationManager;
import com.progetto.gymmanager.model.CodiceSconto;
import com.progetto.gymmanager.model.Prodotto;
import com.progetto.gymmanager.persistence.memory.SystemData;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AmministratoreController {

    private static final Logger logger = Logger.getLogger(AmministratoreController.class.getName());

    public List<String> getListaIstruttori() throws IOException {
        return DAOFactory.getDAOFactory().getUserDAO().findUsersByRole("ISTRUTTORE")
                .stream()
                .map(u -> u.getNickname() + " - " + u.getEmail())
                .toList();
    }

    public void eliminaIstruttore(String nickname) throws IOException {
        if (nickname == null || nickname.trim().isEmpty()) {
            throw new IllegalArgumentException("Seleziona un istruttore.");
        }
        DAOFactory.getDAOFactory().getUserDAO().deleteUser(nickname);
    }

    public void creaIstruttore(RegistrationBean regBean) throws LoginException {
        regBean.setRuolo("ISTRUTTORE");
        new LoginController().registraUtente(regBean);
    }

    public void aggiungiProdotto(String nome, double prezzo) {
        if (nome == null || nome.trim().isEmpty() || prezzo <= 0) {
            throw new IllegalArgumentException("Dati prodotto non validi.");
        }
        try {
            DAOFactory.getDAOFactory().getShopDAO().saveProdotto(new Prodotto(nome.trim(), prezzo));
            inviaComunicazione("TUTTI", "Novità nello shop! E' ora disponibile: " + nome);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Errore di persistenza durante l'aggiunta del prodotto", e);
        }
    }

    public List<String> getListaSconti() {
        try {
            return DAOFactory.getDAOFactory().getShopDAO().loadAllSconti().stream()
                    .map(c -> c.getCodice() + " (" + c.getPercentuale() + "%)")
                    .toList();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Errore durante il caricamento degli sconti", e);
            return java.util.Collections.emptyList();
        }
    }

    public void creaCodiceSconto(String codice, int percentuale) {
        if (codice == null || codice.trim().isEmpty() || percentuale <= 0 || percentuale >= 100) {
            throw new IllegalArgumentException("Codice non valido o percentuale fuori range.");
        }
        try {
            DAOFactory.getDAOFactory().getShopDAO().saveSconto(new CodiceSconto(codice.trim().toUpperCase(), percentuale));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Errore durante il salvataggio del codice sconto", e);
        }
    }

    public void eliminaCodiceSconto(String codice) {
        if (codice == null || codice.trim().isEmpty()) {
            throw new IllegalArgumentException("Seleziona un codice sconto.");
        }
        try {
            DAOFactory.getDAOFactory().getShopDAO().deleteSconto(codice.trim());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Errore durante l'eliminazione del codice sconto", e);
        }
    }

    public void ordinaRifornimento(String fornitore, String prodotto, int quantita) {
        if (fornitore == null || fornitore.trim().isEmpty() || prodotto == null || prodotto.trim().isEmpty() || quantita <= 0) {
            throw new IllegalArgumentException("Dati rifornimento incompleti.");
        }
        try {
            SystemData.Notifica n = new SystemData.Notifica("AMMINISTRATORE", "Ordine di " + quantita + "x " + prodotto + " inviato con successo a: " + fornitore);
            DAOFactory.getDAOFactory().getShopDAO().saveNotifica(n);
            NotificationManager.getInstance().triggerNotifica();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Errore durante il salvataggio della notifica di rifornimento", e);
        }
    }

    public void inviaComunicazione(String targetRuolo, String messaggio) {
        if (messaggio == null || messaggio.trim().isEmpty()) {
            throw new IllegalArgumentException("Il messaggio non puo' essere vuoto.");
        }
        try {
            SystemData.Notifica n = new SystemData.Notifica(targetRuolo.toUpperCase(), "COMUNICAZIONE ADMIN: " + messaggio.trim());
            DAOFactory.getDAOFactory().getShopDAO().saveNotifica(n);
            NotificationManager.getInstance().triggerNotifica();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Errore durante l'invio della comunicazione broadcast", e);
        }
    }
}