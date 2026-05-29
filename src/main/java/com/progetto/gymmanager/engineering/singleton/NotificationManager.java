package com.progetto.gymmanager.engineering.singleton;

import com.progetto.gymmanager.engineering.observer.Subject;

public class NotificationManager extends Subject {
    private static NotificationManager instance = null;

    private NotificationManager() {}

    public static NotificationManager getInstance() {
        if (instance == null) {
            instance = new NotificationManager();
        }
        return instance;
    }

    public void triggerNotifica() {
        notifyObservers();
    }
}