package com.progetto.gymmanager.persistence.memory;

import com.progetto.gymmanager.bean.CorsoBean;
import com.progetto.gymmanager.model.CodiceSconto;
import com.progetto.gymmanager.model.Prodotto;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SystemData {

    private SystemData() {
        throw new IllegalStateException("Utility class");
    }

    public static class Notifica implements Serializable {
        private static final long serialVersionUID = 1L;

        private String utenteDestinatario;
        private String messaggio;
        private String lettaDa;

        public Notifica(String utenteDestinatario, String messaggio, String lettaDa) {
            this.utenteDestinatario = utenteDestinatario;
            this.messaggio = messaggio;
            this.lettaDa = lettaDa;
        }

        public Notifica(String utenteDestinatario, String messaggio) {
            this(utenteDestinatario, messaggio, "");
        }

        // Getter e Setter per Notifica
        public String getUtenteDestinatario() { return utenteDestinatario; }
        public void setUtenteDestinatario(String utenteDestinatario) { this.utenteDestinatario = utenteDestinatario; }

        public String getMessaggio() { return messaggio; }
        public void setMessaggio(String messaggio) { this.messaggio = messaggio; }

        public String getLettaDa() { return lettaDa; }
        public void setLettaDa(String lettaDa) { this.lettaDa = lettaDa; }
    }

    public static class Iscrizione implements Serializable {
        private static final long serialVersionUID = 1L;

        private String atleta;
        private String corso;
        private String stato;

        public Iscrizione(String atleta, String corso, String stato) {
            this.atleta = atleta;
            this.corso = corso;
            this.stato = stato;
        }

        public Iscrizione(String atleta, String corso) {
            this(atleta, corso, "PENDING");
        }

        // Getter e Setter per Iscrizione
        public String getAtleta() { return atleta; }
        public void setAtleta(String atleta) { this.atleta = atleta; }

        public String getCorso() { return corso; }
        public void setCorso(String corso) { this.corso = corso; }

        public String getStato() { return stato; }
        public void setStato(String stato) { this.stato = stato; }
    }

    public static List<Prodotto> getProdottiShop() { return prodottiShop; }
    public static List<CodiceSconto> getCodiciSconto() { return codiciSconto; }

    static final List<CorsoBean> corsi = new ArrayList<>();
    static final List<Iscrizione> iscrizioni = new ArrayList<>();
    static final List<Notifica> notifiche = new ArrayList<>();
    static final List<Prodotto> prodottiShop = new ArrayList<>();
    static final List<CodiceSconto> codiciSconto = new ArrayList<>();

    static {
        prodottiShop.add(new Prodotto("Proteine Whey 1kg", 29.99));
        prodottiShop.add(new Prodotto("Creatina Monoidrato 500g", 19.99));
        prodottiShop.add(new Prodotto("BCAA 2:1:1", 15.50));
        prodottiShop.add(new Prodotto("Multivitaminico", 9.99));
        prodottiShop.add(new Prodotto("Barretta Proteica", 2.50));

        codiciSconto.add(new CodiceSconto("GYM10", 10));
        codiciSconto.add(new CodiceSconto("BENVENUTO20", 20));
    }
}