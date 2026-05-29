package com.progetto.gymmanager.engineering.singleton;

import com.progetto.gymmanager.model.User;

public class SessioneAttuale {
    private static SessioneAttuale instance = null;
    private User currentUser;

    private SessioneAttuale() {}

    public static synchronized SessioneAttuale getInstance() {
        if (instance == null) {
            instance = new SessioneAttuale();
        }
        return instance;
    }

    public void login(User user) {
        this.currentUser = user;
    }

    public void logout() {
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public User setCurrentUser() {
        return currentUser;
    }
}