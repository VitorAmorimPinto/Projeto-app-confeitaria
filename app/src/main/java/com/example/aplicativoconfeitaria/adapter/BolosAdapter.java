package com.example.aplicativoconfeitaria.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aplicativoconfeitaria.R;
import com.example.aplicativoconfeitaria.activity.activity_detalhes_item;
import com.example.aplicativoconfeitaria.model.Bolo;

import java.util.List;

public class BolosAdapter extends RecyclerView.Adapter <BolosAdapter.MyViewHolder> {

    private List<Bolo> bolos;
    private Context context;

    public BolosAdapter(List<Bolo> listaBolos, Context c) {
        this.bolos = listaBolos;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from( parent.getContext() ).inflate(R.layout.adapter_bolos, parent, false);
        return new MyViewHolder( itemLista );
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Bolo bolo = bolos.get( position );

        holder.nome.setText( bolo.getNome());
        holder.descricao.setText( bolo.getDescricao() );
        holder.preco.setText( "R$ "+bolo.getPreco().toString() );

        if ( bolo.getFoto() != null){
            Uri uri = Uri.parse( bolo.getFoto() );
            Glide.with ( context ).load( uri ).into( holder.foto );
        }else {
            holder.foto.setImageResource( R.drawable.bolopadrao );
        }

        holder.parentLayout.setOnClickListener((view) -> {
            Intent intent = new Intent(context, activity_detalhes_item.class);
            intent.putExtra("objetoBolo", bolo);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return bolos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView foto;
        TextView nome, descricao, preco;
        LinearLayout parentLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            foto = itemView.findViewById(R.id.imageViewBoloPedidosAdmin);
            nome = itemView.findViewById(R.id.textTituloBolo);
            descricao = itemView.findViewById(R.id.textDescricaoBolo);
            preco = itemView.findViewById(R.id.textPrecoBolo);
            parentLayout = itemView.findViewById(R.id.linearLayoutListaBolos);
        }
    }

}
