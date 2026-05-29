package com.progetto.gymmanager.persistence.fs;

import com.progetto.gymmanager.bean.CorsoBean;
import com.progetto.gymmanager.persistence.dao.CorsoDAO;
import com.progetto.gymmanager.persistence.memory.SystemData;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileSystemCorsoDAO implements CorsoDAO {
    private static final String CORSI_FILE = "corsi.csv";
    private static final String ISCR_FILE = "iscrizioni.csv";

    @Override
    public List<CorsoBean> findAllCorsi() throws IOException {
        List<CorsoBean> lista = new ArrayList<>();
        File file = new File(CORSI_FILE);
        if (!file.exists()) return lista;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length >= 3) {
                    CorsoBean c = new CorsoBean();
                    c.setNome(data[0]);
                    c.setData(data[1]);
                    c.setIstruttore(data[2]);
                    lista.add(c);
                }
            }
        }
        return lista;
    }

    @Override
    public void saveCorso(CorsoBean corso) throws IOException {
        List<CorsoBean> corsi = findAllCorsi();
        corsi.removeIf(c -> c.getNome().equalsIgnoreCase(corso.getNome()));
        corsi.add(corso);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CORSI_FILE))) {
            for (CorsoBean c : corsi) {
                bw.write(c.getNome() + "," + c.getData() + "," + c.getIstruttore());
                bw.newLine();
            }
        }
    }

    @Override
    public void deleteCorso(String nomeCorso) throws IOException {
        List<CorsoBean> corsi = findAllCorsi();
        corsi.removeIf(c -> c.getNome().equalsIgnoreCase(nomeCorso));
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CORSI_FILE))) {
            for (CorsoBean c : corsi) {
                bw.write(c.getNome() + "," + c.getData() + "," + c.getIstruttore());
                bw.newLine();
            }
        }
    }

    @Override
    public void updateCorso(CorsoBean corso) throws IOException {
        saveCorso(corso);
    }

    @Override
    public List<SystemData.Iscrizione> findAllIscrizioni() throws IOException {
        List<SystemData.Iscrizione> lista = new ArrayList<>();
        File file = new File(ISCR_FILE);
        if (!file.exists()) return lista;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length >= 3) {
                    lista.add(new SystemData.Iscrizione(data[0], data[1], data[2]));
                }
            }
        }
        return lista;
    }

    @Override
    public void saveIscrizione(SystemData.Iscrizione iscrizione) throws IOException {
        List<SystemData.Iscrizione> lista = findAllIscrizioni();
        lista.removeIf(i -> i.getCorso().equalsIgnoreCase(iscrizione.getCorso()) && i.getAtleta().equalsIgnoreCase(iscrizione.getAtleta()));
        lista.add(iscrizione);
        writeIscrizioni(lista);
    }

    @Override
    public void updateIscrizione(SystemData.Iscrizione iscrizione) throws IOException {
        saveIscrizione(iscrizione);
    }

    @Override
    public void deleteIscrizione(String nomeCorso, String atleta) throws IOException {
        List<SystemData.Iscrizione> lista = findAllIscrizioni();
        lista.removeIf(i -> i.getCorso().equalsIgnoreCase(nomeCorso) && i.getAtleta().equalsIgnoreCase(atleta));
        writeIscrizioni(lista);
    }

    private void writeIscrizioni(List<SystemData.Iscrizione> lista) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ISCR_FILE))) {
            for (SystemData.Iscrizione i : lista) {
                bw.write(i.getAtleta() + "," + i.getCorso() + "," + i.getStato());
                bw.newLine();
            }
        }
    }
}