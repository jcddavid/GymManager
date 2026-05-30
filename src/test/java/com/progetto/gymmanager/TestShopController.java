package com.progetto.gymmanager;

import com.progetto.gymmanager.bean.ProdottoShopBean;
import com.progetto.gymmanager.controller.ShopController;
import com.progetto.gymmanager.engineering.exception.AcquistoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class TestShopController {
    // Author: David Dumbrava
    private ShopController shopController;

    @BeforeEach
    void setUp() {
        shopController = new ShopController();
    }

    @Test
    void testAcquistoCarrelloVuoto() {
        List<ProdottoShopBean> carrelloVuoto = new ArrayList<>();

        assertThrows(AcquistoException.class, () -> {
            shopController.finalizzaAcquisto(carrelloVuoto, "MarioRossi", "");
        }, "Il checkout con carrello vuoto deve lanciare AcquistoException.");
    }

    @Test
    void testAcquistoSottoSogliaMinima() {
        ProdottoShopBean barretta = new ProdottoShopBean();
        barretta.setNome("Barretta");
        barretta.setPrezzo(2.50);

        List<ProdottoShopBean> carrello = List.of(barretta);

        assertThrows(AcquistoException.class, () -> {
            shopController.finalizzaAcquisto(carrello, "MarioRossi", "");
        }, "Deve bloccare gli acquisti sotto i 5.00 euro.");
    }

    @Test
    void testCalcoloTotaleCorretto() {
        List<ProdottoShopBean> carrello = new ArrayList<>();
        ProdottoShopBean p1 = new ProdottoShopBean();
        p1.setPrezzo(10.0);
        ProdottoShopBean p2 = new ProdottoShopBean();
        p2.setPrezzo(20.0);
        carrello.add(p1);
        carrello.add(p2);

        double totale = shopController.calcolaTotale(carrello, null);
        assertEquals(30.0, totale, "Il calcolo della somma dei prodotti nel carrello deve essere esatto.");
    }
}