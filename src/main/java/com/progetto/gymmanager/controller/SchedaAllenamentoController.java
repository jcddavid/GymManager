package com.progetto.gymmanager.controller;

import com.progetto.gymmanager.bean.EsercizioBean;
import com.progetto.gymmanager.bean.SchedaAllenamentoBean;
import com.progetto.gymmanager.engineering.exception.SchedaException;
import com.progetto.gymmanager.engineering.factory.DAOFactory;
import com.progetto.gymmanager.engineering.singleton.NotificationManager;
import com.progetto.gymmanager.model.Esercizio;
import com.progetto.gymmanager.model.SchedaAllenamento;
import com.progetto.gymmanager.persistence.dao.SchedaDAO;
import com.progetto.gymmanager.persistence.memory.SystemData;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SchedaAllenamentoController {

    private static final String DA_ASSEGNARE = "DA_ASSEGNARE";

    public List<SchedaAllenamentoBean> consultaSchedeUtente(String nickname) throws SchedaException {
        if (nickname == null || nickname.trim().isEmpty()) {
            throw new SchedaException("Riferimento nickname utente non valido.");
        }
        try {
            SchedaDAO dao = DAOFactory.getDAOFactory().getSchedaDAO();
            List<SchedaAllenamento> schede = dao.findSchedeByUtente(nickname.trim());
            List<SchedaAllenamentoBean> beans = new ArrayList<>();

            for (SchedaAllenamento scheda : schede) {
                SchedaAllenamentoBean bean = new SchedaAllenamentoBean();
                bean.setNicknameAtleta(scheda.getUtente());
                bean.setNicknameIstruttore(scheda.getIstruttore());
                bean.setNomeScheda(scheda.getNomeScheda());
                bean.setNoteRichiesta(scheda.getNote());

                for (Esercizio e : scheda.getEsercizi()) {
                    EsercizioBean eb = new EsercizioBean();
                    eb.setNome(e.getNome());
                    eb.setSerie(e.getSerie());
                    eb.setRipetizioni(e.getRipetizioni());
                    eb.setCarico(e.getCarico());
                    bean.addEsercizio(eb);
                }
                beans.add(bean);
            }
            return beans;
        } catch (IOException e) {
            throw new SchedaException("Errore di lettura dei dati durante il recupero delle schede.");
        }
    }

    public void creaSchedaPersonale(String nicknameUtente, String nomeScheda) throws SchedaException {
        if (nomeScheda == null || nomeScheda.trim().isEmpty()) {
            throw new SchedaException("Devi obbligatoriamente assegnare un nome alla scheda.");
        }
        try {
            SchedaAllenamento schedaVuota = new SchedaAllenamento(nicknameUtente.trim(), "Auto-Gestita", nomeScheda.trim(), new ArrayList<>());
            DAOFactory.getDAOFactory().getSchedaDAO().saveScheda(schedaVuota);
        } catch (IOException e) {
            throw new SchedaException("Impossibile creare la scheda personale causa errore IO.");
        }
    }

    public void richiediScheda(String nicknameUtente, String nomeScheda, String note) throws SchedaException {
        if (nomeScheda == null || nomeScheda.trim().isEmpty()) {
            throw new SchedaException("Devi obbligatoriamente assegnare un nome alla scheda richiesta.");
        }
        try {
            SchedaAllenamento schedaVuota = new SchedaAllenamento(nicknameUtente.trim(), DA_ASSEGNARE, nomeScheda.trim(), new ArrayList<>());
            schedaVuota.setNote(note != null ? note.trim() : "");
            DAOFactory.getDAOFactory().getSchedaDAO().saveScheda(schedaVuota);

            SystemData.Notifica n = new SystemData.Notifica("ISTRUTTORI", "Nuova richiesta scheda da: " + nicknameUtente);
            DAOFactory.getDAOFactory().getShopDAO().saveNotifica(n);
            NotificationManager.getInstance().triggerNotifica();
        } catch (IOException e) {
            throw new SchedaException("Impossibile inoltrare la richiesta della scheda causa errore IO.");
        }
    }

    public List<SchedaAllenamentoBean> ottieniRichiestePendenti() throws SchedaException {
        try {
            List<SchedaAllenamento> tutte = DAOFactory.getDAOFactory().getSchedaDAO().findAllSchede();
            List<SchedaAllenamentoBean> beans = new ArrayList<>();

            for (SchedaAllenamento s : tutte) {
                if (DA_ASSEGNARE.equals(s.getIstruttore())) {
                    SchedaAllenamentoBean bean = new SchedaAllenamentoBean();
                    bean.setNicknameAtleta(s.getUtente());
                    bean.setNomeScheda(s.getNomeScheda());
                    bean.setNoteRichiesta(s.getNote());
                    beans.add(bean);
                }
            }
            return beans;
        } catch (IOException e) {
            throw new SchedaException("Impossibile caricare l'elenco delle richieste pendenti degli atleti.");
        }
    }

    public void compilaScheda(SchedaAllenamentoBean bean) throws SchedaException {
        if (bean == null || bean.getEsercizi().isEmpty()) {
            throw new SchedaException("Impossibile compilare e salvare una scheda priva di esercizi.");
        }
        try {
            SchedaDAO dao = DAOFactory.getDAOFactory().getSchedaDAO();
            List<Esercizio> listaModello = new ArrayList<>();
            for (EsercizioBean eb : bean.getEsercizi()) {
                listaModello.add(new Esercizio(eb.getNome(), eb.getSerie(), eb.getRipetizioni(), eb.getCarico()));
            }

            SchedaAllenamento scheda = new SchedaAllenamento(bean.getNicknameAtleta(), bean.getNicknameIstruttore(), bean.getNomeScheda(), listaModello);
            dao.saveScheda(scheda);

            SystemData.Notifica n = new SystemData.Notifica(bean.getNicknameAtleta(), "L'istruttore " + bean.getNicknameIstruttore() + " ha completato la tua scheda: " + bean.getNomeScheda());
            DAOFactory.getDAOFactory().getShopDAO().saveNotifica(n);
            NotificationManager.getInstance().triggerNotifica();
        } catch (IOException e) {
            throw new SchedaException("Errore di sistema nel salvataggio della scheda compilata.");
        }
    }

    public void modificaPropriaScheda(SchedaAllenamentoBean bean, String istruttoreOriginario) throws SchedaException {
        if (bean == null || bean.getNomeScheda() == null || bean.getNomeScheda().trim().isEmpty()) {
            throw new SchedaException("La scheda deve possedere un nome valido.");
        }
        try {
            SchedaDAO schedaDAO = DAOFactory.getDAOFactory().getSchedaDAO();
            List<Esercizio> listaModello = new ArrayList<>();
            for (EsercizioBean eb : bean.getEsercizi()) {
                listaModello.add(new Esercizio(eb.getNome(), eb.getSerie(), eb.getRipetizioni(), eb.getCarico()));
            }

            String istruttoreDaSalvare = (istruttoreOriginario != null && !istruttoreOriginario.isEmpty() && !istruttoreOriginario.equals(DA_ASSEGNARE))
                    ? istruttoreOriginario : "Auto-Gestita";

            SchedaAllenamento schedaAggiornata = new SchedaAllenamento(bean.getNicknameAtleta(), istruttoreDaSalvare, bean.getNomeScheda(), listaModello);
            schedaDAO.saveScheda(schedaAggiornata);
        } catch (IOException e) {
            throw new SchedaException("Impossibile sovrascrivere le modifiche apportate alla scheda.");
        }
    }

    public void eliminaScheda(String nickname, String nomeScheda) throws SchedaException {
        if (nomeScheda == null || nomeScheda.trim().isEmpty()) {
            throw new SchedaException("Selezionare una scheda valida ai fini dell'eliminazione.");
        }
        try {
            DAOFactory.getDAOFactory().getSchedaDAO().deleteScheda(nickname, nomeScheda.trim());
        } catch (IOException e) {
            throw new SchedaException("Errore tecnico durante l'eliminazione fisica della scheda.");
        }
    }

    public String ottieniUrlVideoEsecuzione(String nomeEsercizio) {
        return Esercizio.ottieniVideoDiRiferimento(nomeEsercizio);
    }
}