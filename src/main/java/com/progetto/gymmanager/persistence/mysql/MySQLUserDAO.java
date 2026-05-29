package com.progetto.gymmanager.persistence.mysql;

import com.progetto.gymmanager.model.RuoloUtente;
import com.progetto.gymmanager.model.User;
import com.progetto.gymmanager.persistence.dao.UserDAO;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLUserDAO implements UserDAO {

    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User u = new User(
                rs.getString("nickname"),
                rs.getString("password"),
                rs.getString("email"),
                RuoloUtente.valueOf(rs.getString("ruolo")),
                rs.getString("indirizzo"),
                rs.getString("telefono"),
                rs.getString("data_nascita")
        );
        Date scadenza = rs.getDate("data_scadenza_abbonamento");
        if (scadenza != null) {
            u.setDataScadenzaAbbonamento(scadenza.toLocalDate());
        }
        return u;
    }

    @Override
    public User findByNicknameAndPassword(String nickname, String password) throws IOException {
        String query = "SELECT nickname, password, email, ruolo, indirizzo, telefono, data_nascita, data_scadenza_abbonamento FROM users WHERE nickname = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nickname);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return extractUserFromResultSet(rs);
        } catch (SQLException e) { throw new IOException("Errore DB", e); }
        return null;
    }

    @Override
    public boolean existsNickname(String nickname) throws IOException {
        String query = "SELECT 1 FROM users WHERE nickname = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nickname);
            return stmt.executeQuery().next();
        } catch (SQLException e) { throw new IOException("Errore DB", e); }
    }

    @Override
    public void save(User user) throws IOException {
        String query = "INSERT INTO users (nickname, password, email, ruolo, indirizzo, telefono, data_nascita, data_scadenza_abbonamento) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            impostaParametriUtente(stmt, user);
            stmt.executeUpdate();
        } catch (SQLException e) { throw new IOException("Errore salvataggio DB", e); }
    }

    @Override
    public void updateUser(User user) throws IOException {
        String query = "UPDATE users SET password=?, email=?, ruolo=?, indirizzo=?, telefono=?, data_nascita=?, data_scadenza_abbonamento=? WHERE nickname=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getPassword());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getRuolo().name());
            stmt.setString(4, user.getIndirizzo());
            stmt.setString(5, user.getTelefono());
            stmt.setString(6, user.getDataNascita());
            stmt.setDate(7, user.getDataScadenzaAbbonamento() != null ? Date.valueOf(user.getDataScadenzaAbbonamento()) : null);
            stmt.setString(8, user.getNickname());
            stmt.executeUpdate();
        } catch (SQLException e) { throw new IOException("Errore aggiornamento DB", e); }
    }

    @Override
    public void deleteUser(String nickname) throws IOException {
        String query = "DELETE FROM users WHERE nickname = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nickname);
            stmt.executeUpdate();
        } catch (SQLException e) { throw new IOException("Errore eliminazione DB", e); }
    }

    @Override
    public List<User> findUsersByRole(String role) throws IOException {
        List<User> list = new ArrayList<>();
        String query = "SELECT nickname, password, email, ruolo, indirizzo, telefono, data_nascita, data_scadenza_abbonamento FROM users WHERE ruolo = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, role);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(extractUserFromResultSet(rs));
        } catch (SQLException e) { throw new IOException("Errore DB", e); }
        return list;
    }

    private void impostaParametriUtente(PreparedStatement stmt, User user) throws SQLException {
        stmt.setString(1, user.getNickname());
        stmt.setString(2, user.getPassword());
        stmt.setString(3, user.getEmail());
        stmt.setString(4, user.getRuolo().name());
        stmt.setString(5, user.getIndirizzo());
        stmt.setString(6, user.getTelefono());
        stmt.setString(7, user.getDataNascita());
        stmt.setDate(8, user.getDataScadenzaAbbonamento() != null ? Date.valueOf(user.getDataScadenzaAbbonamento()) : null);
    }
}