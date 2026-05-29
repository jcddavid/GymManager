package com.progetto.gymmanager.controller;

import com.progetto.gymmanager.bean.CredentialsBean;
import com.progetto.gymmanager.bean.RegistrationBean;
import com.progetto.gymmanager.bean.UserBean;
import com.progetto.gymmanager.engineering.exception.LoginException;
import com.progetto.gymmanager.engineering.factory.DAOFactory;
import com.progetto.gymmanager.engineering.singleton.SessioneAttuale;
import com.progetto.gymmanager.model.RuoloUtente;
import com.progetto.gymmanager.model.User;
import com.progetto.gymmanager.persistence.dao.UserDAO;
import java.io.IOException;

public class LoginController {

    public UserBean login(CredentialsBean creds) throws LoginException {
        if (creds == null || creds.getNickname() == null || creds.getPassword() == null || creds.getNickname().trim().isEmpty() || creds.getPassword().trim().isEmpty()) {
            throw new LoginException("Nickname e password sono campi obbligatori.");
        }
        try {
            UserDAO dao = DAOFactory.getUserDAO();
            User user = dao.findByNicknameAndPassword(creds.getNickname().trim(), creds.getPassword());
            if (user == null) {
                throw new LoginException("Credenziali errate. Riprova.");
            }
            SessioneAttuale.getInstance().login(user);
            UserBean bean = new UserBean();
            bean.setEmail(user.getEmail());
            bean.setRuolo(user.getRuolo().name());
            return bean;
        } catch (IOException e) {
            throw new LoginException("Errore tecnico di persistenza durante l'autenticazione.");
        }
    }

    public void registraUtente(RegistrationBean regBean) throws LoginException {
        if (regBean == null) {
            throw new LoginException("I dati di registrazione sono mancanti.");
        }
        validaDatiRegistrazione(regBean);
        try {
            UserDAO dao = DAOFactory.getUserDAO();
            if (dao.existsNickname(regBean.getNickname().trim())) {
                throw new LoginException("Il nickname inserito è già utilizzato da un altro utente.");
            }
            User newUser = new User(
                    regBean.getNickname().trim(),
                    regBean.getPassword(),
                    regBean.getEmail().trim().toLowerCase(),
                    RuoloUtente.valueOf(regBean.getRuolo().toUpperCase()),
                    regBean.getIndirizzo().trim(),
                    regBean.getTelefono().trim(),
                    regBean.getDataNascita()
            );
            dao.save(newUser);
        } catch (IOException e) {
            throw new LoginException("Errore tecnico nel salvataggio del nuovo account.");
        }
    }

    private void validaDatiRegistrazione(RegistrationBean regBean) throws LoginException {
        if (regBean.getNickname() == null || regBean.getNickname().trim().length() < 3) {
            throw new LoginException("Il nickname deve contenere almeno 3 caratteri validi.");
        }
        if (regBean.getPassword() == null || regBean.getPassword().length() < 6) {
            throw new LoginException("La password deve contenere almeno 6 caratteri.");
        }
        if (regBean.getEmail() == null || !regBean.getEmail().contains("@") || !regBean.getEmail().contains(".")) {
            throw new LoginException("L'indirizzo email inserito non è sintatticamente valido.");
        }
        if (regBean.getIndirizzo() == null || regBean.getIndirizzo().trim().isEmpty()) {
            throw new LoginException("L'indirizzo di residenza è obbligatorio.");
        }
        if (regBean.getTelefono() == null || regBean.getTelefono().trim().isEmpty()) {
            throw new LoginException("Il numero di telefono è obbligatorio.");
        }
        if (regBean.getDataNascita() == null || regBean.getDataNascita().isEmpty()) {
            throw new LoginException("La data di nascita è obbligatoria.");
        }
        if (regBean.getRuolo() == null) {
            throw new LoginException("Il ruolo utente deve essere specificato.");
        }
    }
}