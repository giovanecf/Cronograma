package com.example.cronograma.API;

import com.example.cronograma.domain.Etapa;
import com.example.cronograma.domain.Tarefa;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface TarefaAPI {

    @GET("sync.php")//OU COLOCA UM ATRIBUTO PRA PASSAR SÒ O sync.php
    Call<Tarefa> obterTarefa();

    @FormUrlEncoded
    @POST("sync.php")
    Call<List<Tarefa>> obterTudo(@Field("method") String method, @Field("usuario") String usuario);

    @FormUrlEncoded
    @POST("sync.php")
    Call<Tarefa> mandarTudo( @Field("method") String method, @Field("tarefas") String tarefasJson, @Field("etapas") String etapasJson);//linha 25 no sync.php: $data = json_decode($_SG['tarefas'], true);
}
