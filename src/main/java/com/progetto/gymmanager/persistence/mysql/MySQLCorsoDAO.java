package com.progetto.gymmanager.persistence.mysql;

import com.progetto.gymmanager.bean.CorsoBean;
import com.progetto.gymmanager.persistence.dao.CorsoDAO;
import com.progetto.gymmanager.persistence.memory.SystemData;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLCorsoDAO implements CorsoDAO {

    @Override
    public List<CorsoBean> findAllCorsi() throws IOException {
        List<CorsoBean> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT nome, data_corso, istruttore FROM corsi")) {
            while (rs.next()) {
                CorsoBean c = new CorsoBean();
                c.setNome(rs.getString("nome"));
                c.setData(rs.getString("data_corso"));
                c.setIstruttore(rs.getString("istruttore"));
                list.add(c);
            }
        } catch (SQLException e) { throw new IOException("Errore DB", e); }
        return list;
    }

    @Override
    public void saveCorso(CorsoBean corso) throws IOException {
        String query = "INSERT INTO corsi (nome, data_corso, istruttore) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE data_corso=?, istruttore=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, corso.getNome());
            stmt.setString(2, corso.getData());
            stmt.setString(3, corso.getIstruttore());
            stmt.setString(4, corso.getData());
            stmt.setString(5, corso.getIstruttore());
            stmt.executeUpdate();
        } catch (SQLException e) { throw new IOException("Errore salvataggio corso", e); }
    }

    @Override
    public void updateCorso(CorsoBean corso) throws IOException { saveCorso(corso); }

    @Override
    public void deleteCorso(String nomeCorso) throws IOException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM corsi WHERE nome=?")) {
            stmt.setString(1, nomeCorso);
            stmt.executeUpdate();
        } catch (SQLException e) { throw new IOException("Errore DB", e); }
    }

    @Override
    public List<SystemData.Iscrizione> findAllIscrizioni() throws IOException {
        List<SystemData.Iscrizione> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT atleta, corso, stato FROM iscrizioni")) {
            while (rs.next()) {
                list.add(new SystemData.Iscrizione(rs.getString("atleta"), rs.getString("corso"), rs.getString("stato")));
            }
        } catch (SQLException e) { throw new IOException("Errore DB", e); }
        return list;
    }

    @Override
    public void saveIscrizione(SystemData.Iscrizione iscrizione) throws IOException {
        String q = "INSERT INTO iscrizioni (atleta, corso, stato) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE stato=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(q)) {
            stmt.setString(1, iscrizione.getAtleta());
            stmt.setString(2, iscrizione.getCorso());
            stmt.setString(3, iscrizione.getStato());
            stmt.setString(4, iscrizione.getStato());
            stmt.executeUpdate();
        } catch (SQLException e) { throw new IOException("Errore DB", e); }
    }

    @Override
    public void updateIscrizione(SystemData.Iscrizione iscrizione) throws IOException { saveIscrizione(iscrizione); }

    @Override
    public void deleteIscrizione(String nomeCorso, String atleta) throws IOException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM iscrizioni WHERE corso=? AND atleta=?")) {
            stmt.setString(1, nomeCorso);
            stmt.setString(2, atleta);
            stmt.executeUpdate();
        } catch (SQLException e) { throw new IOException("Errore DB", e); }
    }
}