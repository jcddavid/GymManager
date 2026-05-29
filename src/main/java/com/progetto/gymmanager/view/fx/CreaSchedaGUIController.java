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

public class CreaSchedaGUIController {
    @FXML private ListView<String> richiesteListView;
    @FXML private TextField atletaField;
    @FXML private TextField nomeSchedaField;
    @FXML private TextField esercizioField;
    @FXML private TextField serieField;
    @FXML private TextField repsField;
    @FXML private TextField caricoField;
    @FXML private ListView<String> eserciziListView;

    private final SchedaAllenamentoController controller = new SchedaAllenamentoController();
    private final List<EsercizioBean> listaTemporanea = new ArrayList<>();
    private final List<SchedaAllenamentoBean> listaRichiesteBean = new ArrayList<>();

    @FXML
    public void initialize() {
        try {
            listaRichiesteBean.addAll(controller.ottieniRichiestePendenti());
            for (SchedaAllenamentoBean req : listaRichiesteBean) {
                richiesteListView.getItems().add(req.getNicknameAtleta() + " - " + req.getNomeScheda() + " (" + req.getNoteRichiesta() + ")");
            }
        } catch (SchedaException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    @FXML
    protected void selezionaRichiesta() {
        int index = richiesteListView.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            SchedaAllenamentoBean req = listaRichiesteBean.get(index);
            atletaField.setText(req.getNicknameAtleta());
            nomeSchedaField.setText(req.getNomeScheda());
            listaTemporanea.clear();
            eserciziListView.getItems().clear();
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
            new Alert(Alert.AlertType.WARNING, "Compila correttamente tutti i campi (Serie e Reps devono essere numeri).").showAndWait();
        }
    }

    @FXML
    protected void salvaScheda() {
        try {
            SchedaAllenamentoBean bean = new SchedaAllenamentoBean();
            bean.setNicknameAtleta(atletaField.getText());
            bean.setNomeScheda(nomeSchedaField.getText());
            bean.setNicknameIstruttore(SessioneAttuale.getInstance().getCurrentUser().getNickname());
            listaTemporanea.forEach(bean::addEsercizio);

            controller.compilaScheda(bean);
            new Alert(Alert.AlertType.INFORMATION, "Scheda salvata correttamente!").showAndWait();
            tornaHome();
        } catch (SchedaException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    @FXML protected void tornaHome() { SceneManager.changeScene("HomeGUI.fxml"); }
}