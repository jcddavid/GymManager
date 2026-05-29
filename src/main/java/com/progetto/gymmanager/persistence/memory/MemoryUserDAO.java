package com.progetto.gymmanager.persistence.memory;

import com.progetto.gymmanager.model.RuoloUtente;
import com.progetto.gymmanager.model.User;
import com.progetto.gymmanager.persistence.dao.UserDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MemoryUserDAO implements UserDAO {
    private static final List<User> users = new ArrayList<>();

    static {
        users.add(new User("Gulyx", "pass123", "gulyx@gymmanager.com", RuoloUtente.ISTRUTTORE, "Via Roma 1", "3331112222", "1990-01-01"));
        users.add(new User("David", "pass123", "david@gymmanager.com", RuoloUtente.ABBONATO, "Via Milano 2", "3334445555", "1995-05-05"));
        users.add(new User("admin", "admin123", "admin@gymmanager.com", RuoloUtente.AMMINISTRATORE, "Direzione", "000000", "1980-01-01"));
    }

    @Override
    public User findByNicknameAndPassword(String nickname, String password) throws IOException {
        return users.stream()
                .filter(u -> u.getNickname().equalsIgnoreCase(nickname) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean existsNickname(String nickname) throws IOException {
        return users.stream().anyMatch(u -> u.getNickname().equalsIgnoreCase(nickname));
    }

    @Override
    public void save(User user) throws IOException {
        users.add(user);
    }

    @Override
    public void updateUser(User user) throws IOException {
        users.removeIf(u -> u.getNickname().equalsIgnoreCase(user.getNickname()));
        users.add(user);
    }

    @Override
    public void deleteUser(String nickname) throws IOException {
        users.removeIf(u -> u.getNickname().equalsIgnoreCase(nickname));
    }

    @Override
    public List<User> findUsersByRole(String role) throws IOException {
        return users.stream()
                .filter(u -> u.getRuolo().name().equalsIgnoreCase(role))
                .toList();
    }
}