package com.progetto.gymmanager.persistence.ser;

import com.progetto.gymmanager.engineering.factory.DAOFactory;
import com.progetto.gymmanager.persistence.dao.CorsoDAO;
import com.progetto.gymmanager.persistence.dao.SchedaDAO;
import com.progetto.gymmanager.persistence.dao.ShopDAO;
import com.progetto.gymmanager.persistence.dao.UserDAO;

public class SerializableDAOFactory extends DAOFactory {
    @Override
    public UserDAO getUserDAO() {
        return new SerializableUserDAO();
    }

    @Override
    public CorsoDAO getCorsoDAO() {
        return new SerializableCorsoDAO();
    }

    @Override
    public SchedaDAO getSchedaDAO() {
        return new SerializableSchedaDAO();
    }

    @Override
    public ShopDAO getShopDAO() {
        return new SerializableShopDAO();
    }
}
