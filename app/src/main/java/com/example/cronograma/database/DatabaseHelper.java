package com.example.cronograma.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cronograma.R;
import com.example.cronograma.domain.Etapa;
import com.example.cronograma.domain.Tarefa;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ravi on 15/03/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 6;

    // Database Name
    private static final String DATABASE_NAME = "cronogramaBD";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(Tarefa.CRIAR_TABELA);
        db.execSQL(Etapa.CRIAR_TABELA);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Tarefa.NOME_TABELA);
        db.execSQL("DROP TABLE IF EXISTS " + Etapa.NOME_TABELA);

        // Create tables again
        onCreate(db);
    }

    public long inserir(Tarefa t) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Tarefa.COLUNA_NOME, t.getNome());
        values.put(Tarefa.COLUNA_FOTO, R.drawable.ic_tarefa);
        if(t.getId() != 0)values.put(Tarefa.COLUNA_ID, t.getId());
        // insert row
        long id = db.insert(Tarefa.NOME_TABELA, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public long inserir(Etapa e) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Etapa.COLUNA_NOME, e.getNome());
        values.put(Etapa.COLUNA_IDTAREFA, e.getIdTarefa());
        values.put(Etapa.COLUNA_DATAVENCIMENTO, e.getDataVencimento());
        values.put(Etapa.COLUNA_COMPLETADO, e.getCompletada());

        // insert row
        long id = db.insert(Etapa.NOME_TABELA, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public Tarefa obter(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Tarefa.NOME_TABELA,
                new String[]{Tarefa.COLUNA_ID, Tarefa.COLUNA_NOME, Tarefa.COLUNA_FOTO, Tarefa.COLUNA_TOTALETAPAS, Tarefa.COLUNA_TOTALETAPACOMPLETA, Tarefa.COLUNA_DATAFIMTAREFA},
                Tarefa.COLUNA_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        Tarefa tarefa = new Tarefa(
                cursor.getInt(cursor.getColumnIndex(Tarefa.COLUNA_ID)),
                cursor.getString(cursor.getColumnIndex(Tarefa.COLUNA_NOME)),
                cursor.getInt(cursor.getColumnIndex(Tarefa.COLUNA_FOTO)),
                cursor.getInt(cursor.getColumnIndex(Tarefa.COLUNA_TOTALETAPAS)),
                cursor.getInt(cursor.getColumnIndex(Tarefa.COLUNA_TOTALETAPACOMPLETA)),
                cursor.getString(cursor.getColumnIndex(Tarefa.COLUNA_DATAFIMTAREFA))
        );

        // close the db connection
        cursor.close();

        return tarefa;
    }

    public Etapa obter(long id, int a) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Etapa.NOME_TABELA,
                new String[]{Etapa.COLUNA_ID, Etapa.COLUNA_NOME, Etapa.COLUNA_DATAVENCIMENTO, Etapa.COLUNA_COMPLETADO, Etapa.COLUNA_IDTAREFA},
                Etapa.COLUNA_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        Etapa etapa = new Etapa(
                cursor.getInt(cursor.getColumnIndex(Etapa.COLUNA_ID)),
                cursor.getString(cursor.getColumnIndex(Etapa.COLUNA_NOME)),
                cursor.getString(cursor.getColumnIndex(Etapa.COLUNA_DATAVENCIMENTO)),
                cursor.getInt(cursor.getColumnIndex(Etapa.COLUNA_COMPLETADO)),
                cursor.getInt(cursor.getColumnIndex(Etapa.COLUNA_IDTAREFA))
        );

        // close the db connection
        cursor.close();

        return etapa;
    }

    public List<Tarefa> obterTodos() {
        List<Tarefa> tarefas = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Tarefa.NOME_TABELA + " ORDER BY " +
                Tarefa.COLUNA_NOME + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Tarefa tarefa = new Tarefa(
                        cursor.getInt(cursor.getColumnIndex(Tarefa.COLUNA_ID)),
                        cursor.getString(cursor.getColumnIndex(Tarefa.COLUNA_NOME)),
                        cursor.getInt(cursor.getColumnIndex(Tarefa.COLUNA_FOTO)),
                        cursor.getInt(cursor.getColumnIndex(Tarefa.COLUNA_TOTALETAPAS)),
                        cursor.getInt(cursor.getColumnIndex(Tarefa.COLUNA_TOTALETAPACOMPLETA)),
                        cursor.getString(cursor.getColumnIndex(Tarefa.COLUNA_DATAFIMTAREFA))
                );

                tarefas.add(tarefa);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return tarefas;
    }

    public List<Etapa> obterEtapas() {
        List<Etapa> etapas = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + Etapa.NOME_TABELA + " ORDER BY " +
                Etapa.COLUNA_NOME + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Etapa etapa = new Etapa(
                        cursor.getInt(cursor.getColumnIndex(Etapa.COLUNA_ID)),
                        cursor.getString(cursor.getColumnIndex(Etapa.COLUNA_NOME)),
                        cursor.getString(cursor.getColumnIndex(Etapa.COLUNA_DATAVENCIMENTO)),
                        cursor.getInt(cursor.getColumnIndex(Etapa.COLUNA_COMPLETADO)),
                        cursor.getInt(cursor.getColumnIndex(Etapa.COLUNA_IDTAREFA))
                );
                etapas.add(etapa);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return etapas;
    }

    public List<Etapa> obterEtapas(long id) {
        List<Etapa> etapas = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(Etapa.NOME_TABELA,
                new String[]{Etapa.COLUNA_ID, Etapa.COLUNA_NOME, Etapa.COLUNA_DATAVENCIMENTO, Etapa.COLUNA_COMPLETADO, Etapa.COLUNA_IDTAREFA},
                Etapa.COLUNA_IDTAREFA + "=?",
                new String[]{String.valueOf(id)}, null, null, Etapa.COLUNA_DATAVENCIMENTO, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Etapa etapa = new Etapa(
                        cursor.getInt(cursor.getColumnIndex(Etapa.COLUNA_ID)),
                        cursor.getString(cursor.getColumnIndex(Etapa.COLUNA_NOME)),
                        cursor.getString(cursor.getColumnIndex(Etapa.COLUNA_DATAVENCIMENTO)),
                        cursor.getInt(cursor.getColumnIndex(Etapa.COLUNA_COMPLETADO)),
                        cursor.getInt(cursor.getColumnIndex(Etapa.COLUNA_IDTAREFA))
                );
                etapas.add(etapa);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return etapas;
    }

    public int obterContador() {
        String countQuery = "SELECT  * FROM " + Tarefa.NOME_TABELA;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int obterContador(long id) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Etapa.NOME_TABELA,
                new String[]{Etapa.COLUNA_ID},
                Etapa.COLUNA_IDTAREFA + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int atualizar(Tarefa tarefa) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Tarefa.COLUNA_NOME, tarefa.getNome());
        values.put(Tarefa.COLUNA_TOTALETAPACOMPLETA, tarefa.getTotalEtapasCompletas());
        values.put(Tarefa.COLUNA_TOTALETAPAS, tarefa.getTotalEtapas());
        values.put(Tarefa.COLUNA_DATAFIMTAREFA, tarefa.getDataFimTarefa());

        // updating row
        return db.update(Tarefa.NOME_TABELA, values, Tarefa.COLUNA_ID + " = ?",
                new String[]{String.valueOf(tarefa.getId())});
    }

    public int atualizar(Etapa etapa) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Etapa.COLUNA_NOME, etapa.getNome());
        values.put(Etapa.COLUNA_DATAVENCIMENTO, etapa.getDataVencimento());
        values.put(Etapa.COLUNA_IDTAREFA, etapa.getIdTarefa());
        values.put(Etapa.COLUNA_COMPLETADO, etapa.getCompletada());

        // updating row
        return db.update(Etapa.NOME_TABELA, values, Etapa.COLUNA_ID + " = ?",
                new String[]{String.valueOf(etapa.getId())});
    }

    public void deletar(Tarefa tarefa) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Etapa.NOME_TABELA, Etapa.COLUNA_IDTAREFA + " = ?",
                new String[]{String.valueOf(tarefa.getId())});
        db.delete(Tarefa.NOME_TABELA, Tarefa.COLUNA_ID + " = ?",
                new String[]{String.valueOf(tarefa.getId())});
        db.close();
    }

    public void deletar(Etapa etapa) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Etapa.NOME_TABELA, Etapa.COLUNA_ID + " = ?",
                new String[]{String.valueOf(etapa.getId())});
        db.close();
    }

    public void atualizarBD(List<Tarefa> tarefas){
        SQLiteDatabase db = this.getWritableDatabase();

        for ( Tarefa tarefa : tarefas ) {
            db.delete(Etapa.NOME_TABELA, Etapa.COLUNA_IDTAREFA + " = ?",
                    new String[]{String.valueOf(tarefa.getId())});
            db.delete(Tarefa.NOME_TABELA, Tarefa.COLUNA_ID + " = ?",
                    new String[]{String.valueOf(tarefa.getId())});
        }
        db.close();
    }

}
