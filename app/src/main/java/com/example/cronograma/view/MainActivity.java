package com.example.cronograma.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.example.cronograma.R;
import com.example.cronograma.adapter.TarefaAdapter;
import com.example.cronograma.database.DatabaseHelper;
import com.example.cronograma.domain.Etapa;
import com.example.cronograma.domain.Tarefa;
import com.example.cronograma.network.Network;
import com.example.cronograma.utils.RecyclerTouchListener;

import android.provider.Settings.Secure;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private RecyclerView mRecyclerView;
    private List<Tarefa> mList = new ArrayList<>();
    private TextView listaVazia;
    private Network nw;
    private TarefaAdapter adapter;
    private PullRefreshLayout swipeRefreshLayout;
    private DatabaseHelper db;


    private String AVISORESTAURAR = "Os dados atuais do dispositivo serão substituidos pelos do último backup feito." +
            "\nDeseja Continuar ? (O PROCESSO PODE LEVAR ALGUNS SEGUNDOS)",
            AVISOBACKUP = "Iremos substituir os dados já salvos na nuvem, por estes do seu dispositivo." +
                    "\nDeseja Continuar ?";
    public static boolean RODANDO = false;
    public static boolean RESULTADO = false;
    public static String android_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = findViewById(R.id.rv_list);
        listaVazia = findViewById(R.id.listaVazia);

        db = new DatabaseHelper(this);
        nw = new Network(this);


        mList.addAll(db.obterTodos());

        swipeRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_WATER_DROP);


        LinearLayoutManager llm = new LinearLayoutManager(this);
        adapter = new TarefaAdapter(this, mList);

        llm.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                Intent intent = new Intent(MainActivity.this, EtapasActivity.class);
                intent.putExtra("tarefa", ""+mList.get(position).getId());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));

        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        atualizarLista();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 500);
            }
        });

        listaVazia();
        fabCreate();

        android_id = Secure.getString(this.getContentResolver(),
                Secure.ANDROID_ID);
    }

    @Override
    protected void onResume() {
        super.onResume();

        atualizarLista();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_settings){
            Toast.makeText(this, "Em construção...", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.action_sync){
            telaSync();
        }

        return super.onOptionsItemSelected(item);
    }

    private void criarTarefa(Tarefa t) {

        t.setTotalEtapasCompletas(0);
        t.setTotalEtapas(0);
        t.setId(0);
        t.setDataFimTarefa("00/00/0000");
        db.inserir(t);

        atualizarLista();
    }

    private void atualizarLista(){

        int etapasCompletada = 0;


        List<Tarefa> auxT = db.obterTodos();
        List<Etapa> auxE;

        for(Tarefa t : auxT){
            auxE = db.obterEtapas(t.getId());
            for (Etapa e : auxE) {

                if (e.getCompletada() == 1) etapasCompletada++;

                /*Log.i("index", "\nMAIN: ETAPA \n"
                        + "\n ID: " + e.getId()
                        + "\n Nome: " + e.getNome()
                        + "\n Data: " + e.getDataVencimento()
                        + "\n Data Tarefa: " + t.getDataFimTarefa()
                        + "\n IDTAREFA: " + e.getIdTarefa()
                        + "\n COMPLETADO:" + e.getCompletada());*/
            }
                t.setEtapas(auxE);
                if(!auxE.isEmpty())t.setDataFimTarefa(auxE.get(auxE.size()-1).getDataVencimento());
                else t.setDataFimTarefa("00/00/0000");
                t.setTotalEtapas(auxE.size());
                t.setTotalEtapasCompletas(etapasCompletada); // São as atepas completas, pra ser 2/5 - duas completas de 5 tarefas
                etapasCompletada = 0;

                db.atualizar(t);
        }

        mList.clear();
        mList.addAll(auxT);
        adapter.notifyDataSetChanged();

        listaVazia();

        //Log.i("index", "notifyDataSetChanged()");

    }

    private void updateTarefa(String nome, int position) {
        Tarefa t = mList.get(position);
        t.setNome(nome);

        db.atualizar(t);
        mList.set(position, t);

        atualizarLista();

    }


    private void deletarTarefa(int position) {
        db.deletar(mList.get(position));

        mList.remove(position);
        adapter.notifyItemRemoved(position);

        atualizarLista();
    }

    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Editar", "Deletar"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Opções");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    mostrarItemTela(true, mList.get(position), position);
                } else {
                    deletarTarefa(position);
                }
            }
        });
        builder.show();
    }

    private void mostrarItemTela(final boolean shouldUpdate, final Tarefa item, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.add_tela, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText nome = view.findViewById(R.id.nome);
        TextView dialogTitle = view.findViewById(R.id.titulo_tela);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_note_title) : getString(R.string.lbl_edit_note_title));

        if (shouldUpdate && item != null) {
            nome.setText(item.getNome());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "Atualizar" : "Salvar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(nome.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Insira o Nome!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // check if user updating note
                if (shouldUpdate && item != null) {
                    // update note by it's id
                    updateTarefa(nome.getText().toString(), position);
                } else {
                    // create new note
                    Tarefa t = new Tarefa(0, nome.getText().toString(), 0, 0, 0, null);

                    criarTarefa(t);
                }
            }
        });

        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { alertDialog.dismiss(); }});
    }

    private void telaSync() {
        CharSequence colors[] = new CharSequence[]{"Restaurar", "BackUp"};
        //LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        //View view = layoutInflaterAndroid.inflate(R.layout.tela_load, null);
        //final ProgressBar pB = view.findViewById(R.id.progressBar);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setTitle("Opções");
        //alertDialogBuilderUserInput.setView(view);
        alertDialogBuilderUserInput.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0) {
                    telaAviso(AVISORESTAURAR, which);
                } else {
                    telaAviso(AVISOBACKUP, which);
                }
            }
        });
        alertDialogBuilderUserInput.show();

    }


    private void telaAviso(String aviso) {

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.tela, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final TextView texto = view.findViewById(R.id.texto);
        final ProgressBar progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        texto.setText(aviso);


        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                teste();
            }
        });

        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { alertDialog.dismiss(); }});

    }


    private void telaAviso(String aviso, final int operacao) {

        //CharSequence colors[] = new CharSequence[]{"Continuar", "Cancelar"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ATENÇÃO !");
        builder.setMessage(aviso);
        builder.setPositiveButton(R.string.continuar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (operacao == 0) {

                    nw.pegarTudo();
                    RODANDO = true;

                    while (RODANDO) ;

                    String Resultado;
                    if (RESULTADO) Resultado = "PEGOU UM OU MAIS !";
                    else Resultado = "PEGOU UM OU NADA !";

                    Toast.makeText(getApplicationContext(), Resultado, Toast.LENGTH_LONG)
                            .show();

                    atualizarLista();
                } else {
                    nw.mandarTudo();
                }
            }
        });

        builder.show();

    }

    public void teste(){

        Toast.makeText(this, "Por favor, aguarde...", Toast.LENGTH_SHORT).show();

        nw.pegarTudo();
        RODANDO = true;

        while (RODANDO) ;

        String Resultado;
        if (RESULTADO) Resultado = "PEGOU UM OU MAIS !";
        else Resultado = "PEGOU UM OU NADA !";

        Toast.makeText(getApplicationContext(), Resultado, Toast.LENGTH_SHORT)
                .show();

        atualizarLista();
    }

    private void fabCreate(){
        findViewById(R.id.icon_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarItemTela(false, null, -1);

            }
        });

    }

    private void listaVazia() {
        // you can check notesList.size() > 0

        if (db.obterContador() > 0) {
            listaVazia.setVisibility(View.GONE);
        } else {
            listaVazia.setVisibility(View.VISIBLE);
        }

    }

    public static String formatarData(String dateStr, int tipoFormato) {

        if( tipoFormato == R.string.data_bd_formato ){

            try {
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                Date date = fmt.parse(dateStr);
                SimpleDateFormat fmtOut = new SimpleDateFormat("MM/dd/yyyy");
                return fmtOut.format(date);
            } catch (ParseException e) {

            }
        } else if( tipoFormato == R.string.data_nuvem_formato ) {

            try {
                SimpleDateFormat fmt = new SimpleDateFormat("MM/dd/yyyy");
                Date date = fmt.parse(dateStr);
                SimpleDateFormat fmtOut = new SimpleDateFormat("yyyy-MM-dd");
                return fmtOut.format(date);
            } catch (ParseException e) {

            }
        } else if( tipoFormato == R.string.data_usuario_formato ){

            try {
                SimpleDateFormat fmt = new SimpleDateFormat("MM/dd/yyyy");
                Date date = fmt.parse(dateStr);
                SimpleDateFormat fmtOut = new SimpleDateFormat("MMM dd");
                return fmtOut.format(date);
            } catch (ParseException e) {

            }
        } else if( tipoFormato == R.string.data_mes_formato ) {

            try {
                SimpleDateFormat fmt = new SimpleDateFormat("MM/dd/yyyy");
                Date date = fmt.parse(dateStr);
                SimpleDateFormat fmtOut = new SimpleDateFormat("MM");
                return fmtOut.format(date);
            } catch (ParseException e) {

            }

        } else if( tipoFormato == R.string.data_ano_formato ) {

            try {
                SimpleDateFormat fmt = new SimpleDateFormat("MM/dd/yyyy");
                Date date = fmt.parse(dateStr);
                SimpleDateFormat fmtOut = new SimpleDateFormat("yyyy");
                return fmtOut.format(date);
            } catch (ParseException e) {

            }

        }

        return "";
    }

}