package com.progetto.gymmanager.persistence.memory;

import com.progetto.gymmanager.engineering.factory.DAOFactory;
import com.progetto.gymmanager.persistence.dao.CorsoDAO;
import com.progetto.gymmanager.persistence.dao.SchedaDAO;
import com.progetto.gymmanager.persistence.dao.ShopDAO;
import com.progetto.gymmanager.persistence.dao.UserDAO;

public class MemoryDAO extends DAOFactory {
    @Override
    public UserDAO getUserDAO() {
        return new MemoryUserDAO();
    }

    @Override
    public CorsoDAO getCorsoDAO() {
        return new MemoryCorsoDAO();
    }

    @Override
    public SchedaDAO getSchedaDAO() {
        return new MemorySchedaDAO();
    }

    @Override
    public ShopDAO getShopDAO() {
        return new MemoryShopDAO();
    }
}
