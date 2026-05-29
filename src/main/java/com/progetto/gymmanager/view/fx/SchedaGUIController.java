package com.progetto.gymmanager.view.fx;

import com.progetto.gymmanager.bean.EsercizioBean;
import com.progetto.gymmanager.bean.SchedaAllenamentoBean;
import com.progetto.gymmanager.controller.SchedaAllenamentoController;
import com.progetto.gymmanager.engineering.exception.SchedaException;
import com.progetto.gymmanager.engineering.singleton.SessioneAttuale;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.ArrayList;
import java.util.List;

public class SchedaGUIController {
    @FXML private ComboBox<String> schedaComboBox;
    @FXML private Label istruttoreLabel;
    @FXML private ListView<String> eserciziListView;
    @FXML private TextField esercizioField;
    @FXML private TextField serieField;
    @FXML private TextField repsField;
    @FXML private TextField caricoField;
    @FXML private TextField nomeNuovaSchedaField;
    @FXML private TextField noteNuovaSchedaField;

    private final SchedaAllenamentoController controller = new SchedaAllenamentoController();
    private List<SchedaAllenamentoBean> schedeUtente = new ArrayList<>();
    private final List<EsercizioBean> listaTemporanea = new ArrayList<>();
    private String istruttoreCorrente = "";

    @FXML
    public void initialize() {
        caricaSchede();
    }

    private void caricaSchede() {
        String nickname = SessioneAttuale.getInstance().getCurrentUser().getNickname();
        try {
            schedeUtente = controller.consultaSchedeUtente(nickname);
            schedaComboBox.getItems().clear();
            for (SchedaAllenamentoBean s : schedeUtente) {
                schedaComboBox.getItems().add(s.getNomeScheda());
            }
        } catch (SchedaException e) {
            mostraAlert(Alert.AlertType.ERROR, e.getMessage());
        }
    }

    @FXML
    protected void caricaSchedaSelezionata() {
        String selezionata = schedaComboBox.getValue();
        if (selezionata == null) return;

        eserciziListView.getItems().clear();
        listaTemporanea.clear();

        for (SchedaAllenamentoBean s : schedeUtente) {
            if (s.getNomeScheda().equals(selezionata)) {
                istruttoreCorrente = s.getNicknameIstruttore();
                if ("DA_ASSEGNARE".equals(istruttoreCorrente)) {
                    istruttoreLabel.setText("Stato: In attesa di compilazione da parte dell'istruttore.");
                } else {
                    istruttoreLabel.setText("Istruttore: " + istruttoreCorrente);
                    for (EsercizioBean e : s.getEsercizi()) {
                        listaTemporanea.add(e);
                        eserciziListView.getItems().add(e.getNome() + " | " + e.getSerie() + "x" + e.getRipetizioni() + " | Carico: " + e.getCarico());
                    }
                }
                break;
            }
        }
    }

    @FXML
    protected void creaPersonale() {
        TextInputDialog dialog = new FormDialog("Nuova Scheda", "Inserisci il nome della tua nuova scheda autogestita:");
        dialog.showAndWait().ifPresent(nome -> {
            if (!nome.trim().isEmpty()) {
                try {
                    controller.creaSchedaPersonale(SessioneAttuale.getInstance().getCurrentUser().getNickname(), nome);
                    caricaSchede();
                    schedaComboBox.setValue(nome);
                    caricaSchedaSelezionata();
                } catch (SchedaException e) {
                    mostraAlert(Alert.AlertType.ERROR, e.getMessage());
                }
            }
        });
    }

    @FXML
    protected void rinominaScheda() {
        String selezionata = schedaComboBox.getValue();
        if (selezionata != null) {
            TextInputDialog dialog = new FormDialog("Rinomina", "Nuovo nome per la scheda:");
            dialog.showAndWait().ifPresent(nuovoNome -> {
                if (!nuovoNome.trim().isEmpty()) {
                    try {
                        String nick = SessioneAttuale.getInstance().getCurrentUser().getNickname();
                        controller.eliminaScheda(nick, selezionata);

                        SchedaAllenamentoBean sb = new SchedaAllenamentoBean();
                        sb.setNicknameAtleta(nick);
                        sb.setNomeScheda(nuovoNome);
                        listaTemporanea.forEach(sb::addEsercizio);

                        controller.modificaPropriaScheda(sb, istruttoreCorrente);
                        caricaSchede();
                        schedaComboBox.setValue(nuovoNome);
                        caricaSchedaSelezionata();
                    } catch (SchedaException e) {
                        mostraAlert(Alert.AlertType.ERROR, e.getMessage());
                    }
                }
            });
        } else {
            mostraAlert(Alert.AlertType.WARNING, "Seleziona prima una scheda da rinominare.");
        }
    }

    @FXML
    protected void eliminaScheda() {
        String selezionata = schedaComboBox.getValue();
        if (selezionata != null) {
            try {
                controller.eliminaScheda(SessioneAttuale.getInstance().getCurrentUser().getNickname(), selezionata);
                mostraAlert(Alert.AlertType.INFORMATION, "Scheda eliminata.");
                eserciziListView.getItems().clear();
                istruttoreLabel.setText("Istruttore: ");
                caricaSchede();
            } catch (SchedaException e) {
                mostraAlert(Alert.AlertType.ERROR, e.getMessage());
            }
        } else {
            mostraAlert(Alert.AlertType.WARNING, "Seleziona prima una scheda da eliminare.");
        }
    }

    @FXML
    protected void aggiungiEsercizio() {
        try {
            String nome = esercizioField.getText().trim();
            int serie = Integer.parseInt(serieField.getText().trim());
            int reps = Integer.parseInt(repsField.getText().trim());
            String carico = caricoField.getText().trim();

            if (nome.isEmpty() || carico.isEmpty()) throw new NumberFormatException();

            EsercizioBean bean = new EsercizioBean();
            bean.setNome(nome); bean.setSerie(serie); bean.setRipetizioni(reps); bean.setCarico(carico);

            listaTemporanea.add(bean);
            eserciziListView.getItems().add(nome + " | " + serie + "x" + reps + " | Carico: " + carico);

            esercizioField.clear(); serieField.clear(); repsField.clear(); caricoField.clear();
        } catch (NumberFormatException e) {
            mostraAlert(Alert.AlertType.WARNING, "Compila correttamente tutti i campi (Serie e Reps numerici).");
        }
    }

    @FXML
    protected void rimuoviEsercizio() {
        int index = eserciziListView.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            listaTemporanea.remove(index);
            eserciziListView.getItems().remove(index);
        } else {
            mostraAlert(Alert.AlertType.WARNING, "Seleziona un esercizio dalla lista per rimuoverlo.");
        }
    }

    @FXML
    protected void salvaModifiche() {
        String selezionata = schedaComboBox.getValue();
        if (selezionata != null) {
            try {
                SchedaAllenamentoBean bean = new SchedaAllenamentoBean();
                bean.setNicknameAtleta(SessioneAttuale.getInstance().getCurrentUser().getNickname());
                bean.setNomeScheda(selezionata);
                listaTemporanea.forEach(bean::addEsercizio);

                controller.modificaPropriaScheda(bean, istruttoreCorrente);
                mostraAlert(Alert.AlertType.INFORMATION, "Scheda aggiornata con successo!");
                caricaSchede();
                schedaComboBox.setValue(selezionata);
            } catch (SchedaException e) {
                mostraAlert(Alert.AlertType.ERROR, e.getMessage());
            }
        } else {
            mostraAlert(Alert.AlertType.WARNING, "Seleziona o crea prima una scheda.");
        }
    }

    @FXML
    protected void richiediNuovaScheda() {
        String nickname = SessioneAttuale.getInstance().getCurrentUser().getNickname();
        String nomeScheda = nomeNuovaSchedaField.getText();
        String note = noteNuovaSchedaField.getText();

        try {
            controller.richiediScheda(nickname, nomeScheda, note);
            mostraAlert(Alert.AlertType.INFORMATION, "Richiesta per la nuova scheda inviata all'istruttore!");
            nomeNuovaSchedaField.clear();
            noteNuovaSchedaField.clear();
        } catch (SchedaException e) {
            mostraAlert(Alert.AlertType.WARNING, e.getMessage());
        }
    }

    @FXML
    protected void guardaEsecuzione() {
        String selezionato = eserciziListView.getSelectionModel().getSelectedItem();
        if (selezionato != null) {
            String nomeEsercizio = selezionato.split("\\|")[0].trim();
            mostraAlert(Alert.AlertType.INFORMATION, "Apertura video per " + nomeEsercizio + " all'indirizzo: \n" + controller.ottieniUrlVideoEsecuzione(nomeEsercizio));
        } else {
            mostraAlert(Alert.AlertType.WARNING, "Seleziona un esercizio dalla lista prima di cliccare.");
        }
    }

    private void mostraAlert(Alert.AlertType tipo, String testo) {
        Alert alert = new Alert(tipo, testo);
        alert.setHeaderText(null);

        if (tipo == Alert.AlertType.ERROR) {
            alert.setTitle("Errore");
        } else {
            alert.setTitle((tipo == Alert.AlertType.WARNING ? "Attenzione" : "Successo"));
        }

        alert.showAndWait();
    }

    @FXML protected void tornaHome() { SceneManager.changeScene("HomeGUI.fxml"); }

    private static class FormDialog extends TextInputDialog {
        public FormDialog(String title, String content) {
            super();
            this.setTitle(title);
            this.setHeaderText(null);
            this.setContentText(content);
        }
    }
}