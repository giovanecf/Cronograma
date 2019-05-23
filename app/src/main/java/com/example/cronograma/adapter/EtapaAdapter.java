package com.example.cronograma.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cronograma.R;
import com.example.cronograma.domain.Etapa;
import com.example.cronograma.interfaces.RecyclerViewOnClickListenerHack;
import com.example.cronograma.view.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class EtapaAdapter extends RecyclerView.Adapter<EtapaAdapter.MyViewHolder> {
    private List<Etapa> mList;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack;
    private Context c;

    public EtapaAdapter(Context c, List<Etapa> l){
        mList = l;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.c = c;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.item_layout_adapter, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int position) {
        boolean COMPLETA = true;
        boolean ATRASADA = false;
        String currentTime = new SimpleDateFormat("MM/dd/yyyy").format(Calendar.getInstance().getTime());

        myViewHolder.iv.setImageResource( R.drawable.ic_etapa );
        if(mList.get(position).getNome() != null ) myViewHolder.nome.setText( mList.get(position).getNome() );
        else myViewHolder.nome.setText( "-- --" );

        if(mList.get(position).getDataVencimento() != null ) myViewHolder.itens.setText(MainActivity.formatarData(mList.get(position).getDataVencimento(), R.string.data_usuario_formato) );
        else myViewHolder.itens.setText( "-- --" );

        if (mList.get(position).getCompletada() != 1) {
            COMPLETA = false;
            if (Integer.parseInt(MainActivity.formatarData(currentTime, R.string.data_ano_formato)) >
                    Integer.parseInt(MainActivity.formatarData(mList.get(position).getDataVencimento(), R.string.data_ano_formato))) {
                ATRASADA = true;
            } else {
                if (Integer.parseInt(MainActivity.formatarData(currentTime, R.string.data_mes_formato)) >
                        Integer.parseInt(MainActivity.formatarData(mList.get(position).getDataVencimento(), R.string.data_mes_formato))) {
                    ATRASADA = true;
                }
            }
        }

        if(ATRASADA) myViewHolder.itemCard.setBackgroundColor(c.getResources().getColor(R.color.holo_red_light));
        else if(!ATRASADA && !COMPLETA) myViewHolder.itemCard.setBackgroundColor(c.getResources().getColor(R.color.colorPrimary));
        else if(COMPLETA) myViewHolder.itemCard.setBackgroundColor(c.getResources().getColor(R.color.colorAccent));
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r){
        recyclerViewOnClickListenerHack = r;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder{


        public ImageView iv;
        public TextView nome;
        public TextView itens;
        public ConstraintLayout itemCard;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemCard = itemView.findViewById(R.id.itemCard);
            iv = itemView.findViewById(R.id.iv);
            nome = itemView.findViewById(R.id.nome);
            itens = itemView.findViewById(R.id.itens);
        }

    }
}