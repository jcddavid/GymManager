package com.progetto.gymmanager;

import com.progetto.gymmanager.bean.CredentialsBean;
import com.progetto.gymmanager.bean.RegistrationBean;
import com.progetto.gymmanager.controller.LoginController;
import com.progetto.gymmanager.engineering.exception.LoginException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestLoginController {
    // Author: David Dumbrava
    private LoginController loginController;

    @BeforeEach
    void setUp() {
        loginController = new LoginController();
    }

    @Test
    void testRegistrazioneSuccesso() {
        RegistrationBean bean = new RegistrationBean();
        bean.setNickname("Atleta99");
        bean.setPassword("passwordSicura123");
        bean.setEmail("atleta@gym.com");
        bean.setIndirizzo("Via Roma 1");
        bean.setTelefono("3331234567");
        bean.setDataNascita("1999-01-01");
        bean.setRuolo("ABBONATO");

        assertDoesNotThrow(() -> {
            loginController.registraUtente(bean);
        }, "La registrazione con dati validi non deve lanciare eccezioni.");
    }

    @Test
    void testRegistrazioneFallitaPasswordCorta() {
        RegistrationBean bean = new RegistrationBean();
        bean.setNickname("User");
        bean.setPassword("123");
        bean.setEmail("user@gym.com");
        bean.setRuolo("ABBONATO");

        LoginException exception = assertThrows(LoginException.class, () -> {
            loginController.registraUtente(bean);
        });
        assertTrue(exception.getMessage().contains("almeno 6 caratteri"));
    }

    @Test
    void testLoginCampiVuoti() {
        CredentialsBean creds = new CredentialsBean("", "");

        assertThrows(LoginException.class, () -> {
            loginController.login(creds);
        }, "Il login con campi vuoti deve lanciare una LoginException.");
    }
}