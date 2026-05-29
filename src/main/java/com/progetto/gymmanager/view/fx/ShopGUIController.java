package com.progetto.gymmanager.view.fx;

import com.progetto.gymmanager.bean.ProdottoShopBean;
import com.progetto.gymmanager.controller.ShopController;
import com.progetto.gymmanager.engineering.exception.AcquistoException;
import com.progetto.gymmanager.engineering.singleton.SessioneAttuale;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;

public class ShopGUIController {

    @FXML private ListView<String> prodottiListView;
    @FXML private ListView<String> carrelloListView;
    @FXML private Label totaleLabel;
    @FXML private TextField codiceScontoField;

    private final ShopController shopController = new ShopController();
    private final List<ProdottoShopBean> catalogo = new ArrayList<>();
    private final List<ProdottoShopBean> carrello = new ArrayList<>();
    private String codiceScontoAttuale = "";

    @FXML
    public void initialize() {
        catalogo.addAll(shopController.getProdottiDisponibili());
        for (ProdottoShopBean p : catalogo) {
            prodottiListView.getItems().add(p.getNome() + " - " + p.getPrezzo() + "€");
        }
        aggiornaSchermata();
    }

    @FXML
    protected void aggiungiAlCarrello() {
        int index = prodottiListView.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            carrello.add(catalogo.get(index));
            aggiornaSchermata();
        } else {
            mostraMessaggio(Alert.AlertType.WARNING, "Attenzione", "Seleziona un prodotto dalla lista.");
        }
    }

    @FXML
    protected void applicaSconto() {
        codiceScontoAttuale = codiceScontoField.getText();
        aggiornaSchermata();
        mostraMessaggio(Alert.AlertType.INFORMATION, "Sconto", "Codice ricalcolato.");
    }

    @FXML
    protected void finalizzaAcquisto() {
        String nickname = SessioneAttuale.getInstance().getCurrentUser().getNickname();
        try {
            shopController.finalizzaAcquisto(carrello, nickname, codiceScontoAttuale);
            carrello.clear();
            codiceScontoAttuale = "";
            codiceScontoField.clear();
            aggiornaSchermata();
            mostraMessaggio(Alert.AlertType.INFORMATION, "Acquisto Completato", "Grazie per il tuo ordine!");
        } catch (AcquistoException e) {
            mostraMessaggio(Alert.AlertType.ERROR, "Errore Acquisto", e.getMessage());
        }
    }

    private void aggiornaSchermata() {
        carrelloListView.getItems().clear();
        for (ProdottoShopBean p : carrello) {
            carrelloListView.getItems().add(p.getNome() + " (" + p.getPrezzo() + "€)");
        }
        double totale = shopController.calcolaTotale(carrello, codiceScontoAttuale);
        totaleLabel.setText("Totale: " + String.format("%.2f", totale) + "€");
    }

    private void mostraMessaggio(Alert.AlertType tipo, String titolo, String testo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(testo);
        alert.showAndWait();
    }

    @FXML
    protected void tornaHome() {
        SceneManager.changeScene("HomeGUI.fxml");
    }
}