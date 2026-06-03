package com.progetto.gymmanager.persistence.mysql;

import com.progetto.gymmanager.engineering.factory.DAOFactory;
import com.progetto.gymmanager.persistence.dao.CorsoDAO;
import com.progetto.gymmanager.persistence.dao.SchedaDAO;
import com.progetto.gymmanager.persistence.dao.ShopDAO;
import com.progetto.gymmanager.persistence.dao.UserDAO;

public class MySQLDAO extends DAOFactory {
    @Override
    public UserDAO getUserDAO() {
        return new MySQLUserDAO();
    }

    @Override
    public CorsoDAO getCorsoDAO() {
        return new MySQLCorsoDAO();
    }

    @Override
    public SchedaDAO getSchedaDAO() {
        return new MySQLSchedaDAO();
    }

    @Override
    public ShopDAO getShopDAO() {
        return new MySQLShopDAO();
    }
}
