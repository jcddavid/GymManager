package com.progetto.gymmanager.persistence.fs;

import com.progetto.gymmanager.model.RuoloUtente;
import com.progetto.gymmanager.model.User;
import com.progetto.gymmanager.persistence.dao.UserDAO;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FileSystemUserDAO implements UserDAO {
    private static final String FILE_PATH = "users.csv";

    private List<User> loadAll() throws IOException {
        List<User> users = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            users.add(new User("Gulyx", "pass123", "gulyx@gymmanager.com", RuoloUtente.ISTRUTTORE, "Via Roma 1", "3331112222", "1990-01-01"));
            users.add(new User("David", "pass123", "david@gymmanager.com", RuoloUtente.ABBONATO, "Via Milano 2", "3334445555", "1995-05-05"));
            users.add(new User("admin", "admin123", "admin@gymmanager.com", RuoloUtente.AMMINISTRATORE, "Direzione", "000000", "1980-01-01"));
            saveAll(users);
            return users;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length >= 8) {
                    User u = new User(data[0], data[1], data[2], RuoloUtente.valueOf(data[3]), data[4], data[5], data[6]);
                    if (!data[7].isEmpty()) u.setDataScadenzaAbbonamento(LocalDate.parse(data[7]));
                    users.add(u);
                }
            }
        }
        return users;
    }

    private void saveAll(List<User> users) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (User u : users) {
                String scadenza = u.getDataScadenzaAbbonamento() != null ? u.getDataScadenzaAbbonamento().toString() : "";
                bw.write(u.getNickname() + "," + u.getPassword() + "," + u.getEmail() + "," + u.getRuolo().name() + ","
                        + u.getIndirizzo() + "," + u.getTelefono() + "," + u.getDataNascita() + "," + scadenza);
                bw.newLine();
            }
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