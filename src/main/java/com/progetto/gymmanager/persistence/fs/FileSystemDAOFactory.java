package com.progetto.gymmanager.persistence.fs;

import com.progetto.gymmanager.engineering.factory.DAOFactory;
import com.progetto.gymmanager.persistence.dao.CorsoDAO;
import com.progetto.gymmanager.persistence.dao.SchedaDAO;
import com.progetto.gymmanager.persistence.dao.ShopDAO;
import com.progetto.gymmanager.persistence.dao.UserDAO;

public class FileSystemDAOFactory extends DAOFactory {
    @Override
    public UserDAO getUserDAO() {
        return new FileSystemUserDAO();
    }

    @Override
    public CorsoDAO getCorsoDAO() {
        return new FileSystemCorsoDAO();
    }

    @Override
    public SchedaDAO getSchedaDAO() {
        return new FileSystemSchedaDAO();
    }

    @Override
    public ShopDAO getShopDAO() {
        return new FileSystemShopDAO();
    }
}
