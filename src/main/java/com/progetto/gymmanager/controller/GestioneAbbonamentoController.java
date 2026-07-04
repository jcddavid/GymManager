package com.progetto.gymmanager.controller;

import com.progetto.gymmanager.engineering.exception.AbbonamentoException;
import com.progetto.gymmanager.engineering.factory.DAOFactory;
import com.progetto.gymmanager.engineering.singleton.NotificationManager;
import com.progetto.gymmanager.engineering.singleton.SessioneAttuale;
import com.progetto.gymmanager.model.User;
import com.progetto.gymmanager.persistence.dao.UserDAO;
import com.progetto.gymmanager.persistence.memory.SystemData;

import java.io.IOException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GestioneAbbonamentoController {

    private static final Logger logger = Logger.getLogger(GestioneAbbonamentoController.class.getName());

    public String getStatoAbbonamento(String nickname) throws AbbonamentoException {
        try {
            User user = getUser(nickname);
            if (user == null) {
                throw new AbbonamentoException("Utente non trovato.");
            }
            return user.calcolaStatoAbbonamento();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Errore di persistenza nel recupero dello stato dell'abbonamento.", e);
            throw new AbbonamentoException("Errore nel recupero dello stato dell'abbonamento.");
        }
    }

    public void rinnovaAbbonamento(String nickname, int mesi) throws AbbonamentoException {
        if (mesi <= 0) {
            throw new AbbonamentoException("Il numero di mesi per il rinnovo deve essere maggiore di zero.");
        }
        try {
            UserDAO dao = DAOFactory.getDAOFactory().getUserDAO();
            User user = getUser(nickname);

            if (user == null) {
                throw new AbbonamentoException("Utente non trovato.");
            }

            LocalDate scadenzaAttuale = user.getDataScadenzaAbbonamento();
            LocalDate nuovaScadenza;

            if (scadenzaAttuale == null || scadenzaAttuale.isBefore(LocalDate.now(ZoneId.systemDefault())())) {
                nuovaScadenza = LocalDate.now(ZoneId.systemDefault())().plusMonths(mesi);
            } else {
                nuovaScadenza = scadenzaAttuale.plusMonths(mesi);
            }

            user.setDataScadenzaAbbonamento(nuovaScadenza);
            dao.updateUser(user);

            SystemData.Notifica n = new SystemData.Notifica(nickname, "Abbonamento rinnovato con successo! Nuova scadenza: " + nuovaScadenza.toString());
            DAOFactory.getDAOFactory().getShopDAO().saveNotifica(n);
            NotificationManager.getInstance().triggerNotifica();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Errore di persistenza durante il salvataggio del rinnovo.", e);
            throw new AbbonamentoException("Errore di sistema durante il salvataggio del rinnovo.");
        }
    }

    private User getUser(String nickname) throws IOException {
        UserDAO dao = DAOFactory.getDAOFactory().getUserDAO();
        User dummy = dao.findByNicknameAndPassword(nickname, "dummy");

        if (dummy == null && SessioneAttuale.getInstance().getCurrentUser() != null) {
            return SessioneAttuale.getInstance().getCurrentUser();
        }

        return dummy;
    }
}