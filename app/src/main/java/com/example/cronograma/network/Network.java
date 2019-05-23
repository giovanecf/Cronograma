package com.example.cronograma.network;

import android.content.Context;

import com.example.cronograma.API.TarefaAPI;
import com.example.cronograma.R;
import com.example.cronograma.database.DatabaseHelper;
import com.example.cronograma.domain.Etapa;
import com.example.cronograma.domain.Tarefa;
import com.example.cronograma.utils.TarefaDes;
import com.example.cronograma.view.MainActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Network {

    private static String API = "https://noiseless.000webhostapp.com/";//"https://api.myjson.com/";

    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Tarefa.class, new TarefaDes())
            .setLenient()
            .create();

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(API)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    private static TarefaAPI tarefaAPI = retrofit.create(TarefaAPI.class);

    private Context ctx;
    private DatabaseHelper db;

    public Network(Context context) {

        this.ctx = context;
        db = new DatabaseHelper(ctx);
    }


    //MANY TAREFA - REQUEST
    public boolean pegarTudo() {
        final Call<List<Tarefa>> callTudo = tarefaAPI.obterTudo("obter-tudo", MainActivity.android_id);

        db.atualizarBD( db.obterTodos() ); // pra limpar o Banco de Dados e então fazer o download

        new Thread() {
            @Override
            public void run() {
                super.run();
                List<Tarefa> lista = null;

                //Log.i("index", "REQUEST: "+callTudo.request().toString());

                try {
                    lista = callTudo.execute().body();

                    if (lista != null) {
                        for (Tarefa t : lista) {
                            /*Log.i("index", "\nTAREFA \n"
                                    + "\n ID: " + t.getId()
                                    + "\n Nome: " + t.getNome()
                                    + "\n Data: " + formatarData(t.getDataFimTarefa(), true)
                                    + "\nEtapas: " + t.getTotalEtapasCompletas() + "/" + t.getTotalEtapas());*/
                            t.setDataFimTarefa(MainActivity.formatarData(t.getDataFimTarefa(), R.string.data_bd_formato));
                            db.inserir(t);
                            List<Etapa> eList = t.getEtapas();
                            for(Etapa e : eList){
                                /*Log.i("index", "VINDO ETAPA \n"
                                        + "\n ID: " + e.getId()
                                        + "\n Nome: " + e.getNome()
                                        + "\n Data: " + e.getDataVencimento()
                                        + "\n Data Tarefa: " + t.getDataFimTarefa()
                                        + "\n IDTAREFA: " + e.getIdTarefa()
                                        + "\n COMPLETADO:" + e.getCompletada());*/
                                e.setDataVencimento(MainActivity.formatarData(e.getDataVencimento(), R.string.data_bd_formato));
                                db.inserir(e);

                            }
                        }

                    }

                    MainActivity.RODANDO = false;
                    MainActivity.RESULTADO = lista.size() < 1 ? false : true;

                } catch (Exception e) {
                    e.printStackTrace();
                    //Log.i("index", "MANY TAREFA error: "+e.getMessage());
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Log.i("index", "MANY TAREFA request Ok");
                    }
                });
            }

            private void runOnUiThread(Runnable runnable) {
            }
        }.start();





        return false;
    }

    //SAVE CAR - REQUEST
    public boolean mandarTudo() {
        List<Tarefa> auxTList =  db.obterTodos();
        List<Tarefa> tList = new ArrayList<>();
        List<Etapa> auxEList =  db.obterEtapas();
        List<Etapa> eList = new ArrayList<>();
        for (Tarefa t: auxTList) {

            t.setUsuario(MainActivity.android_id);
            String data = t.getDataFimTarefa() == null ? "00/00/0000" : t.getDataFimTarefa();
            t.setDataFimTarefa(MainActivity.formatarData(data, R.string.data_nuvem_formato));
            //Log.i("index", "DATA ENVIADA:"+t.getDataFimTarefa());
            tList.add(t);
        }


        for (Etapa e: auxEList) {

            e.setDataVencimento(MainActivity.formatarData(e.getDataVencimento(), R.string.data_nuvem_formato));
            eList.add(e);

            /*Log.i("index", "INDO ETAPA \n"
                    + "\n ID: " + e.getId()
                    + "\n Nome: " + e.getNome()
                    + "\n Data: " + e.getDataVencimento()
                    + "\n Data Tarefa: " + t.getDataFimTarefa()
                    + "\n IDTAREFA: " + e.getIdTarefa()
                    + "\n COMPLETADO:" + e.getCompletada());*/
        }

        Call<Tarefa> call = tarefaAPI.mandarTudo("mandar-tudo", new Gson().toJson(tList), new Gson().toJson(eList));

        call.enqueue(new Callback<Tarefa>() {
            @Override
            public void onResponse(Call<Tarefa> call, retrofit2.Response<Tarefa> response) {
                //Log.i("index", "SAVE TAREFAS onResponse: " + response.message());
            }

            @Override
            public void onFailure(Call<Tarefa> call, Throwable t) {
                //Log.i("index", "SAVE TAREFAS onFailure: " + t.getMessage());
            }

        });
        return false;
    }

}
