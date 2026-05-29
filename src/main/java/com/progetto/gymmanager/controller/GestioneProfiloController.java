package com.progetto.gymmanager.controller;

import com.progetto.gymmanager.bean.RegistrationBean;
import com.progetto.gymmanager.engineering.exception.AbbonamentoException;
import com.progetto.gymmanager.engineering.factory.DAOFactory;
import com.progetto.gymmanager.engineering.singleton.SessioneAttuale;
import com.progetto.gymmanager.model.User;
import java.io.IOException;

public class GestioneProfiloController {

    public void aggiornaDatiUtente(RegistrationBean bean) throws IOException, IllegalArgumentException {
        if (bean.getIndirizzo().isEmpty() || bean.getTelefono().isEmpty()) {
            throw new IllegalArgumentException("Indirizzo e telefono sono obbligatori.");
        }

        User user = SessioneAttuale.getInstance().getCurrentUser();
        user.setIndirizzo(bean.getIndirizzo());
        user.setTelefono(bean.getTelefono());
        user.setDataNascita(bean.getDataNascita());
        user.setEmail(bean.getEmail());

        DAOFactory.getUserDAO().updateUser(user);
    }

    public String generaQrCodeAccesso() throws AbbonamentoException {
        User user = SessioneAttuale.getInstance().getCurrentUser();
        return user.generaTokenAccesso();
    }
}