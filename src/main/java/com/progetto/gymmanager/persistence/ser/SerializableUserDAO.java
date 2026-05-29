package com.progetto.gymmanager.persistence.ser;

import com.progetto.gymmanager.model.RuoloUtente;
import com.progetto.gymmanager.model.User;
import com.progetto.gymmanager.persistence.dao.UserDAO;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SerializableUserDAO implements UserDAO {
    private static final String FILE_PATH = "users.ser";

    @SuppressWarnings("unchecked")
    private List<User> loadAll() throws IOException {
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            List<User> users = new ArrayList<>();
            users.add(new User("Gulyx", "pass123", "gulyx@gymmanager.com", RuoloUtente.ISTRUTTORE, "Via Roma 1", "3331112222", "1990-01-01"));
            users.add(new User("David", "pass123", "david@gymmanager.com", RuoloUtente.ABBONATO, "Via Milano 2", "3334445555", "1995-05-05"));
            users.add(new User("admin", "admin123", "admin@gymmanager.com", RuoloUtente.AMMINISTRATORE, "Direzione", "000000", "1980-01-01"));
            saveAll(users);
            return users;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<User>) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("Errore durante la deserializzazione degli utenti.", e);
        }
    }

    private void saveAll(List<User> users) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(users);
        }
    }

    @Override
    public User findByNicknameAndPassword(String nickname, String password) throws IOException {
        return loadAll().stream()
                .filter(u -> u.getNickname().equalsIgnoreCase(nickname) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean existsNickname(String nickname) throws IOException {
        return loadAll().stream().anyMatch(u -> u.getNickname().equalsIgnoreCase(nickname));
    }

    @Override
    public void save(User user) throws IOException {
        List<User> users = loadAll();
        users.add(user);
        saveAll(users);
    }

    @Override
    public void updateUser(User user) throws IOException {
        List<User> users = loadAll();
        users.removeIf(u -> u.getNickname().equalsIgnoreCase(user.getNickname()));
        users.add(user);
        saveAll(users);
    }

    @Override
    public void deleteUser(String nickname) throws IOException {
        List<User> users = loadAll();
        users.removeIf(u -> u.getNickname().equalsIgnoreCase(nickname));
        saveAll(users);
    }

    @Override
    public List<User> findUsersByRole(String role) throws IOException {
        return loadAll().stream()
                .filter(u -> u.getRuolo().name().equalsIgnoreCase(role))
                .toList();
    }
}