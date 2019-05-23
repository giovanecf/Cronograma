package com.example.cronograma.domain;

import java.sql.Date;

public class Etapa {

    public static final String NOME_TABELA = "Etapa";
    public static final String COLUNA_ID = "ID";
    public static final String COLUNA_NOME = "NOME";
    public static final String COLUNA_DATAVENCIMENTO = "DATAVENCIMENTO";
    public static final String COLUNA_COMPLETADO = "COMPLETADO";
    public static final String COLUNA_IDTAREFA = "IDTAREFA";

    public static final String CRIAR_TABELA =
            "CREATE TABLE "+ NOME_TABELA +"("
                    + COLUNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUNA_NOME +" TEXT,"
                    + COLUNA_DATAVENCIMENTO + " DATE,"
                    + COLUNA_COMPLETADO + " INTEGER,"
                    + COLUNA_IDTAREFA + " INTEGER"
                    + ")";

    int id, idTarefa, completada;
    String nome, dataVencimento;

    public Etapa(int id, String nome, String dataVencimento, int completada, int id_tarefa) {
        this.id = id;
        this.nome = nome;
        this.dataVencimento = dataVencimento;
        this.completada = completada;
        this.idTarefa = id_tarefa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdTarefa() {
        return idTarefa;
    }

    public void setIdTarefa(int idTarefa) {
        this.idTarefa = idTarefa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(String dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public int getCompletada() {
        return completada;
    }

    public void setCompletada(int completada) {
        this.completada = completada;
    }
}
