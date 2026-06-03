package com.progetto.gymmanager.engineering.factory;

import com.progetto.gymmanager.persistence.dao.*;
import com.progetto.gymmanager.persistence.fs.*;
import com.progetto.gymmanager.persistence.memory.*;
import com.progetto.gymmanager.persistence.ser.*;
import com.progetto.gymmanager.persistence.mysql.*;

import java.io.InputStream;
import java.util.Properties;


public abstract class DAOFactory {
    private static final String MEMORY = "MEMORY";
    private static final String MYSQL = "MYSQL";
    private static final String FS = "FS";
    private static final String SER = "SER";
    private static String persistenceType = MEMORY;

    public static DAOFactory getDAOFactory() {
        try (InputStream input = DAOFactory.class.getResourceAsStream("/config.properties")) {
            if (input != null) {
                Properties prop = new Properties();
                prop.load(input);
                persistenceType = prop.getProperty("persistence.type", MEMORY);
            }
        } catch (Exception ignored) {
            persistenceType = MEMORY;
        }
        switch (persistenceType) {
            case FS:
                return new FileSystemDAO();
            case SER:
                return new SerializableDAO();
            case MYSQL:
                return new MySQLDAO();
            default:
                return new MemoryDAO();
        }
    }

    public abstract UserDAO getUserDAO();

    public abstract CorsoDAO getCorsoDAO();

    public abstract SchedaDAO getSchedaDAO();

    public abstract ShopDAO getShopDAO();
}
