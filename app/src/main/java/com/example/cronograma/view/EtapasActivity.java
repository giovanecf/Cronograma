package com.example.cronograma.view;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.example.cronograma.R;
import com.example.cronograma.adapter.EtapaAdapter;
import com.example.cronograma.database.DatabaseHelper;
import com.example.cronograma.domain.Etapa;
import com.example.cronograma.utils.RecyclerTouchListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EtapasActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<Etapa> mList = new ArrayList<>();
    private TextView listaVazia;
    private TextView data;
    private DatabaseHelper db;
    private EtapaAdapter adapter;
    private int idTarefa;
    private String dataVencimentoEtapa;



    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    private void updateLabel() {

        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt","BR"));

        dataVencimentoEtapa = sdf.format(myCalendar.getTime());
        data.setText(dataVencimentoEtapa);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etapas);

        Intent intent = getIntent();
        String message = intent.getStringExtra("tarefa");
        idTarefa = Integer.parseInt(message);
        listaVazia = findViewById(R.id.listaVazia);
        final PullRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_WATER_DROP);

        db = new DatabaseHelper(this);

        mList.addAll(db.obterEtapas(idTarefa));

        LinearLayoutManager llm = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        adapter = new EtapaAdapter(this, mList);

        llm.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {

                if( mList.get(position).getCompletada() == 0 ){

                    mList.get(position).setCompletada(1);
                    db.atualizar(mList.get(position));
                    adapter.notifyItemChanged(position);
                }
                else{

                    mList.get(position).setCompletada(0);
                    db.atualizar(mList.get(position));
                    adapter.notifyItemChanged(position);
                }
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
                        Toast.makeText(getApplicationContext(), "Atualizado !", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 500);
            }
        });



        listaVazia();

        fabCreate();

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

    private void criar(Etapa e) {
        e.setCompletada(0);
        long id = db.inserir(e);
        e = db.obter(id,0);
        if (e != null) {
            adapter.notifyDataSetChanged();

            listaVazia();
        }

        atualizarLista();
    }

    private void atualizarLista(){

        List<Etapa> eList = db.obterEtapas(idTarefa);

        mList.clear();
        mList.addAll(eList);
        adapter.notifyDataSetChanged();

        listaVazia();
    }

    private void update(String nome, String data, int position) {
        Etapa e = mList.get(position);
        e.setNome( nome );
        e.setDataVencimento( data );

        db.atualizar(e);

        mList.set(position, e);
        adapter.notifyItemChanged(position);

        listaVazia();
    }

    private void deletar(int position) {
        db.deletar(mList.get(position));

        mList.remove(position);
        adapter.notifyItemRemoved(position);

        listaVazia();
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
                    deletar(position);
                }
            }
        });
        builder.show();
    }



    private void mostrarItemTela(final boolean shouldUpdate, final Etapa item, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.add_tela, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(EtapasActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final TextView titulo_tela = view.findViewById(R.id.titulo_tela);
        final EditText nome = view.findViewById(R.id.nome);

        titulo_tela.setText("Nova Etapa");
        //Toast.makeText()
        data = view.findViewById(R.id.data);
        data.setPaintFlags(data.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EtapasActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        data.setVisibility(View.VISIBLE);
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
                    Toast.makeText(EtapasActivity.this, "Insira o Nome!", Toast.LENGTH_SHORT).show();
                } else if(dataVencimentoEtapa == null){
                    Toast.makeText(EtapasActivity.this, "Insira a Data de Vencimento!", Toast.LENGTH_SHORT).show();
                } else {
                    alertDialog.dismiss();
                }

                // check if user updating note
                if (shouldUpdate && item != null) {
                    // update note by it's id
                    update(nome.getText().toString(), dataVencimentoEtapa, position);
                } else {
                    // create new note

                    Etapa e = new Etapa(0, nome.getText().toString(), dataVencimentoEtapa, 0, idTarefa );
                    criar(e);
                }
            }
        });

        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { alertDialog.dismiss();}});
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

        if (db.obterContador(idTarefa) > 0) {
            listaVazia.setVisibility(View.GONE);
        } else {
            listaVazia.setVisibility(View.VISIBLE);
        }
    }



}

