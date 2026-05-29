package com.progetto.gymmanager.persistence.mysql;

import com.progetto.gymmanager.model.Esercizio;
import com.progetto.gymmanager.model.SchedaAllenamento;
import com.progetto.gymmanager.persistence.dao.SchedaDAO;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLSchedaDAO implements SchedaDAO {

    @Override
    public List<SchedaAllenamento> findSchedeByUtente(String nicknameUtente) throws IOException {
        return ottieniSchede("SELECT utente, nome_scheda, istruttore, note FROM schede WHERE utente = ?", nicknameUtente);
    }

    @Override
    public List<SchedaAllenamento> findAllSchede() throws IOException {
        return ottieniSchede("SELECT utente, nome_scheda, istruttore, note FROM schede", null);
    }

    private List<SchedaAllenamento> ottieniSchede(String query, String parametro) throws IOException {
        List<SchedaAllenamento> lista = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (parametro != null) {
                stmt.setString(1, parametro);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String utente = rs.getString("utente");
                String nomeScheda = rs.getString("nome_scheda");
                String istruttore = rs.getString("istruttore");
                String note = rs.getString("note");

                List<Esercizio> esercizi = ottieniEsercizi(conn, utente, nomeScheda);
                SchedaAllenamento scheda = new SchedaAllenamento(utente, istruttore, nomeScheda, esercizi);
                scheda.setNote(note == null ? "" : note);
                lista.add(scheda);
            }
        } catch (SQLException e) {
            throw new IOException("Errore lettura DB", e);
        }
        return lista;
    }

    private List<Esercizio> ottieniEsercizi(Connection conn, String utente, String nomeScheda) throws SQLException {
        List<Esercizio> esercizi = new ArrayList<>();
        String q = "SELECT nome_esercizio, serie, ripetizioni, carico FROM esercizi WHERE scheda_utente = ? AND scheda_nome = ?";
        try (PreparedStatement stmt = conn.prepareStatement(q)) {
            stmt.setString(1, utente);
            stmt.setString(2, nomeScheda);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                esercizi.add(new Esercizio(
                        rs.getString("nome_esercizio"),
                        rs.getInt("serie"),
                        rs.getInt("ripetizioni"),
                        rs.getString("carico")
                ));
            }
        }
        return esercizi;
    }

    @Override
    public void saveScheda(SchedaAllenamento scheda) throws IOException {
        deleteScheda(scheda.getUtente(), scheda.getNomeScheda());

        String qScheda = "INSERT INTO schede (utente, nome_scheda, istruttore, note) VALUES (?, ?, ?, ?)";
        String qEsercizio = "INSERT INTO esercizi (scheda_utente, scheda_nome, nome_esercizio, serie, ripetizioni, carico) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmtS = conn.prepareStatement(qScheda)) {
                stmtS.setString(1, scheda.getUtente());
                stmtS.setString(2, scheda.getNomeScheda());
                stmtS.setString(3, scheda.getIstruttore());
                stmtS.setString(4, scheda.getNote());
                stmtS.executeUpdate();
            }
            try (PreparedStatement stmtE = conn.prepareStatement(qEsercizio)) {
                for (Esercizio e : scheda.getEsercizi()) {
                    stmtE.setString(1, scheda.getUtente());
                    stmtE.setString(2, scheda.getNomeScheda());
                    stmtE.setString(3, e.getNome());
                    stmtE.setInt(4, e.getSerie());
                    stmtE.setInt(5, e.getRipetizioni());
                    stmtE.setString(6, e.getCarico());
                    stmtE.addBatch();
                }
                stmtE.executeBatch();
            }
            conn.commit();
        } catch (SQLException e) {
            throw new IOException("Errore salvataggio DB", e);
        }
    }

    @Override
    public void deleteScheda(String nicknameUtente, String nomeScheda) throws IOException {
        String q = "DELETE FROM schede WHERE utente = ? AND nome_scheda = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(q)) {
            stmt.setString(1, nicknameUtente);
            stmt.setString(2, nomeScheda);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IOException("Errore eliminazione DB", e);
        }
    }
}