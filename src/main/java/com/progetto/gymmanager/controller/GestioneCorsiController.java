package com.progetto.gymmanager.controller;

import com.progetto.gymmanager.bean.CorsoBean;
import com.progetto.gymmanager.engineering.exception.CorsoException;
import com.progetto.gymmanager.engineering.factory.DAOFactory;
import com.progetto.gymmanager.engineering.singleton.NotificationManager;
import com.progetto.gymmanager.persistence.memory.SystemData;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GestioneCorsiController {

    private static final Logger logger = Logger.getLogger(GestioneCorsiController.class.getName());
    static final String ACCEPTEDSTR = "ACCEPTEDSTR";
    static final String PENDINGSTR = "PENDINGSTR";
    

    public void creaCorso(String nomeCorso, String dataCorso, String istruttore) throws CorsoException {
        if (nomeCorso == null || nomeCorso.trim().isEmpty() || dataCorso == null || dataCorso.trim().isEmpty()) {
            throw new CorsoException("Il nome e la data del corso sono parametri obbligatori.");
        }
        try {
            boolean esiste = DAOFactory.getDAOFactory().getCorsoDAO().findAllCorsi().stream().anyMatch(c -> c.getNome().equalsIgnoreCase(nomeCorso.trim()));
            if (esiste) {
                throw new CorsoException("Esiste già un corso registrato con questo nome.");
            }
            CorsoBean nuovoCorso = new CorsoBean();
            nuovoCorso.setNome(nomeCorso.trim());
            nuovoCorso.setData(dataCorso);
            nuovoCorso.setIstruttore(istruttore);
            DAOFactory.getDAOFactory().getCorsoDAO().saveCorso(nuovoCorso);

            SystemData.Notifica n = new SystemData.Notifica("TUTTI", "Nuovo corso: " + nomeCorso.trim() + " programmato per il " + dataCorso);
            DAOFactory.getDAOFactory().getShopDAO().saveNotifica(n);
            NotificationManager.getInstance().triggerNotifica();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Errore persistente durante la creazione del corso.", e);
            throw new CorsoException("Errore persistente durante la creazione del corso.");
        }
    }

    public List<CorsoBean> getCorsiDisponibili() throws CorsoException {
        try {
            return DAOFactory.getDAOFactory().getCorsoDAO().findAllCorsi();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Impossibile recuperare l'elenco dei corsi dal database.", e);
            throw new CorsoException("Impossibile recuperare l'elenco dei corsi dal database.");
        }
    }

    public List<CorsoBean> getCorsiPerIstruttore(String nicknameIstruttore) throws CorsoException {
        if (nicknameIstruttore == null || nicknameIstruttore.trim().isEmpty()) {
            throw new CorsoException("Filtro istruttore non valido.");
        }
        try {
            return DAOFactory.getDAOFactory().getCorsoDAO().findAllCorsi().stream()
                    .filter(c -> c.getIstruttore().equalsIgnoreCase(nicknameIstruttore.trim()))
                    .toList();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Errore nel filtraggio dei corsi per l'istruttore corrente.", e);
            throw new CorsoException("Errore nel filtraggio dei corsi per l'istruttore corrente.");
        }
    }

    public void modificaDataCorso(String nomeCorso, String nuovaData, String nicknameIstruttore) throws CorsoException {
        if (nomeCorso == null || nuovaData == null || nomeCorso.trim().isEmpty() || nuovaData.trim().isEmpty()) {
            throw new CorsoException("Dati incompleti per la modifica della data del corso.");
        }
        try {
            CorsoBean trovato = DAOFactory.getDAOFactory().getCorsoDAO().findAllCorsi().stream()
                    .filter(c -> c.getNome().equalsIgnoreCase(nomeCorso.trim()) && c.getIstruttore().equalsIgnoreCase(nicknameIstruttore.trim()))
                    .findFirst()
                    .orElseThrow(() -> new CorsoException("Corso non trovato o non associato al tuo account."));

            trovato.setData(nuovaData);
            DAOFactory.getDAOFactory().getCorsoDAO().updateCorso(trovato);

            SystemData.Notifica n = new SystemData.Notifica("TUTTI", "La data del corso " + nomeCorso + " è cambiata al " + nuovaData);
            DAOFactory.getDAOFactory().getShopDAO().saveNotifica(n);
            NotificationManager.getInstance().triggerNotifica();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Impossibile salvare le modifiche della data del corso.", e);
            throw new CorsoException("Impossibile salvare le modifiche della data del corso.");
        }
    }

    public void richiediIscrizione(String nomeCorso, String nicknameUtente) throws CorsoException {
        if (nomeCorso == null || nicknameUtente == null || nomeCorso.trim().isEmpty() || nicknameUtente.trim().isEmpty()) {
            throw new CorsoException("Riferimenti corso o utente mancanti per l'iscrizione.");
        }
        try {
            boolean giaRichiesto = DAOFactory.getDAOFactory().getCorsoDAO().findAllIscrizioni().stream()
                    .anyMatch(i -> i.getCorso().equalsIgnoreCase(nomeCorso.trim()) && i.getAtleta().equalsIgnoreCase(nicknameUtente.trim()));
            if (giaRichiesto) {
                throw new CorsoException("Hai già inviato una richiesta di iscrizione per questo corso.");
            }
            SystemData.Iscrizione nuovaIscrizione = new SystemData.Iscrizione(nicknameUtente.trim(), nomeCorso.trim());
            DAOFactory.getDAOFactory().getCorsoDAO().saveIscrizione(nuovaIscrizione);

            SystemData.Notifica n = new SystemData.Notifica("ISTRUTTORI", "Nuova richiesta iscrizione da " + nicknameUtente + " per " + nomeCorso);
            DAOFactory.getDAOFactory().getShopDAO().saveNotifica(n);
            NotificationManager.getInstance().triggerNotifica();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Errore di rete nel salvataggio della richiesta di iscrizione.", e);
            throw new CorsoException("Errore di rete nel salvataggio della richiesta di iscrizione.");
        }
    }

    public void disiscriviUtente(String nomeCorso, String nicknameUtente) throws CorsoException {
        if (nomeCorso == null || nicknameUtente == null || nomeCorso.trim().isEmpty() || nicknameUtente.trim().isEmpty()) {
            throw new CorsoException("Impossibile procedere alla disiscrizione: parametri errati.");
        }
        try {
            DAOFactory.getDAOFactory().getCorsoDAO().deleteIscrizione(nomeCorso.trim(), nicknameUtente.trim());
            SystemData.Notifica n = new SystemData.Notifica("ISTRUTTORI", nicknameUtente + " si è cancellato dal corso " + nomeCorso);
            DAOFactory.getDAOFactory().getShopDAO().saveNotifica(n);
            NotificationManager.getInstance().triggerNotifica();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Nessuna iscrizione o prenotazione trovata per i dati specificati.", e);
            throw new CorsoException("Nessuna iscrizione o prenotazione trovata per i dati specificati.");
        }
    }

    public List<String> getRichiestePendenti() throws CorsoException {
        try {
            return DAOFactory.getDAOFactory().getCorsoDAO().findAllIscrizioni().stream()
                    .filter(i -> PENDINGSTR.equals(i.getStato()))
                    .map(i -> i.getAtleta() + " -> " + i.getCorso())
                    .toList();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Impossibile caricare l'elenco delle richieste pendenti.", e);
            throw new CorsoException("Impossibile caricare l'elenco delle richieste pendenti.");
        }
    }

    public void gestisciRichiesta(String rigaRichiesta, boolean approva) throws CorsoException {
        if (rigaRichiesta == null || !rigaRichiesta.contains(" -> ")) {
            throw new CorsoException("Formato della richiesta selezionata non valido.");
        }
        String[] parts = rigaRichiesta.split(" -> ");
        String atleta = parts[0].trim();
        String corso = parts[1].trim();
        try {
            List<SystemData.Iscrizione> tutte = DAOFactory.getDAOFactory().getCorsoDAO().findAllIscrizioni();
            for (SystemData.Iscrizione i : tutte) {
                if (i.getAtleta().equalsIgnoreCase(atleta) && i.getCorso().equalsIgnoreCase(corso) && PENDINGSTR.equals(i.getStato())) {
                    i.setStato(approva ? ACCEPTEDSTR : "REJECTED");
                    DAOFactory.getDAOFactory().getCorsoDAO().updateIscrizione(i);
                    String esito = approva ? "approvata" : "rifiutata";
                    SystemData.Notifica n = new SystemData.Notifica(atleta, "La tua richiesta per il corso " + corso + " è stata " + esito);
                    DAOFactory.getDAOFactory().getShopDAO().saveNotifica(n);
                    break;
                }
            }
            NotificationManager.getInstance().triggerNotifica();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Errore nell'aggiornamento dello stato della richiesta.", e);
            throw new CorsoException("Errore nell'aggiornamento dello stato della richiesta.");
        }
    }

    public void eliminaCorso(String nomeCorso) throws CorsoException {
        if (nomeCorso == null || nomeCorso.trim().isEmpty()) {
            throw new CorsoException("Selezionare un corso valido da eliminare.");
        }
        try {
            DAOFactory.getDAOFactory().getCorsoDAO().deleteCorso(nomeCorso.trim());
            List<SystemData.Iscrizione> iscritti = DAOFactory.getDAOFactory().getCorsoDAO().findAllIscrizioni().stream()
                    .filter(i -> i.getCorso().equalsIgnoreCase(nomeCorso.trim())).toList();

            for (SystemData.Iscrizione i : iscritti) {
                if (ACCEPTEDSTR.equals(i.getStato()) || PENDINGSTR.equals(i.getStato())) {
                    SystemData.Notifica n = new SystemData.Notifica(i.getAtleta(), "Il corso " + nomeCorso + " è stato annullato dall'istruttore.");
                    DAOFactory.getDAOFactory().getShopDAO().saveNotifica(n);
                }
                DAOFactory.getDAOFactory().getCorsoDAO().deleteIscrizione(nomeCorso.trim(), i.getAtleta());
            }
            NotificationManager.getInstance().triggerNotifica();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Errore di persistenza durante l'eliminazione definitiva del corso.", e);
            throw new CorsoException("Errore di persistenza durante l'eliminazione definitiva del corso.");
        }
    }

    public List<String> getIscrittiCorso(String nomeCorso) throws CorsoException {
        if (nomeCorso == null || nomeCorso.trim().isEmpty()) {
            throw new CorsoException("Specificare un corso per estrarre gli iscritti.");
        }
        try {
            return DAOFactory.getDAOFactory().getCorsoDAO().findAllIscrizioni().stream()
                    .filter(i -> i.getCorso().equalsIgnoreCase(nomeCorso.trim()) && ACCEPTEDSTR.equals(i.getStato()))
                    .map(i -> i.getAtleta())
                    .toList();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Errore nel recupero degli atleti iscritti.", e);
            throw new CorsoException("Errore nel recupero degli atleti iscritti.");
        }
    }
}