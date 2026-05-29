package com.progetto.gymmanager.controller;

import com.progetto.gymmanager.engineering.factory.DAOFactory;
import com.progetto.gymmanager.persistence.memory.SystemData;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotificheController {
    private static final Logger logger = Logger.getLogger(NotificheController.class.getName());
    static final String AMMINISTRATORE = "AMMINISTRATORE";

    public List<String> getNotificheUtente(String nickname, String ruolo) {
        List<String> messaggi = new ArrayList<>();
        try {
            List<SystemData.Notifica> filtrate = DAOFactory.getShopDAO().loadAllNotifiche().stream()
                    .filter(n -> n.getUtenteDestinatario().equalsIgnoreCase(nickname)
                            || n.getUtenteDestinatario().equals("TUTTI")
                            || (ruolo.equals("ABBONATO") && n.getUtenteDestinatario().equals("ABBONATI"))
                            || (ruolo.equals("ISTRUTTORE") && n.getUtenteDestinatario().equals("ISTRUTTORI"))
                            || (ruolo.equals(AMMINISTRATORE) && n.getUtenteDestinatario().equals(AMMINISTRATORE)))
                    .toList();

            for (SystemData.Notifica n : filtrate) {
                messaggi.add(n.getMessaggio());
                if (!n.getLettaDa().contains(nickname + ";")) {
                    n.setLettaDa(n.getLettaDa() + nickname + ";");
                    DAOFactory.getShopDAO().updateNotifica(n);
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Errore caricamento notifiche", e);
        }
        return messaggi;
    }

    public int contaNotificheNonLette(String nickname, String ruolo) {
        try {
            return (int) DAOFactory.getShopDAO().loadAllNotifiche().stream()
                    .filter(n -> !n.getLettaDa().contains(nickname + ";"))
                    .filter(n -> n.getUtenteDestinatario().equalsIgnoreCase(nickname)
                            || n.getUtenteDestinatario().equals("TUTTI")
                            || (ruolo.equals("ABBONATO") && n.getUtenteDestinatario().equals("ABBONATI"))
                            || (ruolo.equals("ISTRUTTORE") && n.getUtenteDestinatario().equals("ISTRUTTORI"))
                            || (ruolo.equals(AMMINISTRATORE) && n.getUtenteDestinatario().equals(AMMINISTRATORE)))
                    .count();
        } catch (Exception e) {
            return 0;
        }
    }
}