package com.progetto.gymmanager.persistence.fs;

import com.progetto.gymmanager.model.Esercizio;
import com.progetto.gymmanager.model.SchedaAllenamento;
import com.progetto.gymmanager.persistence.dao.SchedaDAO;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileSystemSchedaDAO implements SchedaDAO {
    private static final String FILE_PATH = "schede.csv";

    @Override
    public List<SchedaAllenamento> findSchedeByUtente(String nicknameUtente) throws IOException {
        return findAllSchede().stream()
                .filter(s -> s.getUtente().equalsIgnoreCase(nicknameUtente))
                .toList();
    }

    @Override
    public List<SchedaAllenamento> findAllSchede() throws IOException {
        List<SchedaAllenamento> schede = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return schede;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length >= 4) {
                    List<Esercizio> esercizi = new ArrayList<>();
                    if (data.length == 5) {
                        esercizi = parseEsercizi(data[4]);
                    }
                    SchedaAllenamento s = new SchedaAllenamento(data[0], data[1], data[2], esercizi);
                    s.setNote(data[3]);
                    schede.add(s);
                }
            }
        }
        return schede;
    }

    private List<Esercizio> parseEsercizi(String eserciziData) {
        List<Esercizio> esercizi = new ArrayList<>();
        if (eserciziData == null || eserciziData.trim().isEmpty()) {
            return esercizi;
        }
        String[] arrayEsercizi = eserciziData.split("\\|");
        for (String esStr : arrayEsercizi) {
            String[] esData = esStr.split(":");
            if (esData.length == 4) {
                esercizi.add(new Esercizio(esData[0], Integer.parseInt(esData[1]), Integer.parseInt(esData[2]), esData[3]));
            }
        }
        return esercizi;
    }

    @Override
    public void saveScheda(SchedaAllenamento scheda) throws IOException {
        List<SchedaAllenamento> schede = findAllSchede();
        schede.removeIf(s -> s.getUtente().equalsIgnoreCase(scheda.getUtente()) && s.getNomeScheda().equalsIgnoreCase(scheda.getNomeScheda()));
        schede.add(scheda);
        scriviSuFile(schede);
    }

    @Override
    public void deleteScheda(String nicknameUtente, String nomeScheda) throws IOException {
        List<SchedaAllenamento> schede = findAllSchede();
        schede.removeIf(s -> s.getUtente().equalsIgnoreCase(nicknameUtente) && s.getNomeScheda().equalsIgnoreCase(nomeScheda));
        scriviSuFile(schede);
    }

    private void scriviSuFile(List<SchedaAllenamento> schede) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (SchedaAllenamento s : schede) {
                StringBuilder esBuilder = new StringBuilder();
                for (int i = 0; i < s.getEsercizi().size(); i++) {
                    Esercizio e = s.getEsercizi().get(i);
                    esBuilder.append(e.getNome()).append(":")
                            .append(e.getSerie()).append(":")
                            .append(e.getRipetizioni()).append(":")
                            .append(e.getCarico());
                    if (i < s.getEsercizi().size() - 1) esBuilder.append("|");
                }
                String noteStr = s.getNote() != null ? s.getNote() : "";
                bw.write(s.getUtente() + "," + s.getIstruttore() + "," + s.getNomeScheda() + "," + noteStr + "," + esBuilder.toString());
                bw.newLine();
            }
        }
    }
}