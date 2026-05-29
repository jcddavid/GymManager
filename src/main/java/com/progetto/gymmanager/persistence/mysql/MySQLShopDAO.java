package com.progetto.gymmanager.persistence.mysql;

import com.progetto.gymmanager.model.CodiceSconto;
import com.progetto.gymmanager.model.Prodotto;
import com.progetto.gymmanager.persistence.dao.ShopDAO;
import com.progetto.gymmanager.persistence.memory.SystemData;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLShopDAO implements ShopDAO {

    @Override
    public List<Prodotto> loadAllProdotti() throws IOException {
        List<Prodotto> lista = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT nome, prezzo FROM prodotti")) {
            while (rs.next()) {
                lista.add(new Prodotto(rs.getString("nome"), rs.getDouble("prezzo")));
            }
        } catch (SQLException e) { throw new IOException("Errore DB", e); }


        return lista;
    }

    @Override
    public void saveProdotto(Prodotto prodotto) throws IOException {
        String q = "INSERT INTO prodotti (nome, prezzo) VALUES (?, ?) ON DUPLICATE KEY UPDATE prezzo=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(q)) {
            stmt.setString(1, prodotto.getNome());
            stmt.setDouble(2, prodotto.getPrezzo());
            stmt.setDouble(3, prodotto.getPrezzo());
            stmt.executeUpdate();
        } catch (SQLException e) { throw new IOException("Errore DB", e); }
    }

    @Override
    public List<CodiceSconto> loadAllSconti() throws IOException {
        List<CodiceSconto> lista = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT codice, percentuale FROM sconti")) {
            while (rs.next()) {
                lista.add(new CodiceSconto(rs.getString("codice"), rs.getInt("percentuale")));
            }
        } catch (SQLException e) { throw new IOException("Errore DB", e); }
        return lista;
    }

    @Override
    public void saveSconto(CodiceSconto sconto) throws IOException {
        String q = "INSERT INTO sconti (codice, percentuale) VALUES (?, ?) ON DUPLICATE KEY UPDATE percentuale=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(q)) {
            stmt.setString(1, sconto.getCodice());
            stmt.setInt(2, sconto.getPercentuale());
            stmt.setInt(3, sconto.getPercentuale());
            stmt.executeUpdate();
        } catch (SQLException e) { throw new IOException("Errore DB", e); }
    }

    @Override
    public void deleteSconto(String codice) throws IOException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM sconti WHERE codice=?")) {
            stmt.setString(1, codice);
            stmt.executeUpdate();
        } catch (SQLException e) { throw new IOException("Errore DB", e); }
    }

    @Override
    public List<SystemData.Notifica> loadAllNotifiche() throws IOException {
        List<SystemData.Notifica> lista = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT utente_destinatario, messaggio, letta_da FROM notifiche")) {
            while (rs.next()) {
                String lette = rs.getString("letta_da");
                lista.add(new SystemData.Notifica(
                        rs.getString("utente_destinatario"),
                        rs.getString("messaggio"),
                        lette == null ? "" : lette
                ));
            }
        } catch (SQLException e) { throw new IOException("Errore DB", e); }
        return lista;
    }

    @Override
    public void saveNotifica(SystemData.Notifica notifica) throws IOException {
        String q = "INSERT INTO notifiche (utente_destinatario, messaggio, letta_da) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(q)) {
            stmt.setString(1, notifica.getUtenteDestinatario());
            stmt.setString(2, notifica.getMessaggio());
            stmt.setString(3, notifica.getLettaDa());
            stmt.executeUpdate();
        } catch (SQLException e) { throw new IOException("Errore DB", e); }
    }

    @Override
    public void updateNotifica(SystemData.Notifica notifica) throws IOException {
        String q = "UPDATE notifiche SET letta_da=? WHERE utente_destinatario=? AND messaggio=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(q)) {
            stmt.setString(1, notifica.getLettaDa());
            stmt.setString(2, notifica.getUtenteDestinatario());
            stmt.setString(3, notifica.getMessaggio());
            stmt.executeUpdate();
        } catch (SQLException e) { throw new IOException("Errore DB", e); }
    }
}