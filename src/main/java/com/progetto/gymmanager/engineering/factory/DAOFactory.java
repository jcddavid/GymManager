package com.progetto.gymmanager.engineering.factory;

import com.progetto.gymmanager.persistence.dao.*;
import com.progetto.gymmanager.persistence.fs.*;
import com.progetto.gymmanager.persistence.memory.*;
import com.progetto.gymmanager.persistence.ser.*;
import com.progetto.gymmanager.persistence.mysql.*;

import java.io.InputStream;
import java.util.Properties;

public class DAOFactory {
    private static final String MEMORY = "MEMORY";
    private static final String MYSQL = "MYSQL";
    private static final String FS = "FS";
    private static final String SER = "SER";
    private static String persistenceType = MEMORY;

    static {
        try (InputStream input = DAOFactory.class.getResourceAsStream("/config.properties")) {
            if (input != null) {
                Properties prop = new Properties();
                prop.load(input);
                persistenceType = prop.getProperty("persistence.type", MEMORY);
            }
        } catch (Exception ignored) {
            persistenceType = MEMORY;
        }
    }

    private DAOFactory() {}

    public static UserDAO getUserDAO() {
        if (MYSQL.equalsIgnoreCase(persistenceType)) return new MySQLUserDAO();
        if (FS.equalsIgnoreCase(persistenceType)) return new FileSystemUserDAO();
        if (SER.equalsIgnoreCase(persistenceType)) return new SerializableUserDAO();
        return new MemoryUserDAO();
    }

    public static CorsoDAO getCorsoDAO() {
        if (MYSQL.equalsIgnoreCase(persistenceType)) return new MySQLCorsoDAO();
        if (FS.equalsIgnoreCase(persistenceType)) return new FileSystemCorsoDAO();
        if (SER.equalsIgnoreCase(persistenceType)) return new SerializableCorsoDAO();
        return new MemoryCorsoDAO();
    }

    public static SchedaDAO getSchedaDAO() {
        if (MYSQL.equalsIgnoreCase(persistenceType)) return new MySQLSchedaDAO();
        if (FS.equalsIgnoreCase(persistenceType)) return new FileSystemSchedaDAO();
        if (SER.equalsIgnoreCase(persistenceType)) return new SerializableSchedaDAO();
        return new MemorySchedaDAO();
    }

    public static ShopDAO getShopDAO() {
        if (MYSQL.equalsIgnoreCase(persistenceType)) return new MySQLShopDAO();
        if (FS.equalsIgnoreCase(persistenceType)) return new FileSystemShopDAO();
        if (SER.equalsIgnoreCase(persistenceType)) return new SerializableShopDAO();
        return new MemoryShopDAO();
    }
}
