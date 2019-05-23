package com.example.cronograma.domain;

import java.util.List;

public class Tarefa {

    public static final String NOME_TABELA = "Tarefa";
    public static final String COLUNA_ID = "ID";
    public static final String COLUNA_NOME = "NOME";
    public static final String COLUNA_FOTO = "FOTO";
    public static final String COLUNA_TOTALETAPACOMPLETA = "TOTALETAPACOMPLETA";
    public static final String COLUNA_TOTALETAPAS = "TOTALETAPAS";
    public static final String COLUNA_DATAFIMTAREFA = "DATAFIMTAREFA";

    public static final String CRIAR_TABELA =
            "CREATE TABLE "+ NOME_TABELA +"("
                    + COLUNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUNA_NOME +" TEXT,"
                    + COLUNA_FOTO + " INTEGER,"
                    + COLUNA_TOTALETAPACOMPLETA + " INTEGER,"
                    + COLUNA_TOTALETAPAS + " INTEGER,"
                    + COLUNA_DATAFIMTAREFA + " DATE"
                    + ")";

    private int id, foto, totalEtapas, totalEtapasCompletas;
    private String nome, dataFimTarefa, usuario;
    private List<Etapa> etapas;

    public Tarefa(int id, String nome, int foto, int totalEtapas, int totalEtapasCompletas, String dataFimTarefa) {
        this.id = id;
        this.foto = foto;
        this.totalEtapas = totalEtapas;
        this.totalEtapasCompletas = totalEtapasCompletas;
        this.nome = nome;
        this.dataFimTarefa = dataFimTarefa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }

    public int getTotalEtapas() {
        return totalEtapas;
    }

    public void setTotalEtapas(int totalEtapas) {
        this.totalEtapas = totalEtapas;
    }

    public int getTotalEtapasCompletas() {
        return totalEtapasCompletas;
    }

    public void setTotalEtapasCompletas(int totalEtapasCompletas) {
        this.totalEtapasCompletas = totalEtapasCompletas;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDataFimTarefa() {
        return dataFimTarefa;
    }

    public void setDataFimTarefa(String dataFimTarefa) {
        this.dataFimTarefa = dataFimTarefa;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public List<Etapa> getEtapas() {
        return etapas;
    }

    public void setEtapas(List<Etapa> etapas) {
        this.etapas = etapas;
    }
}
