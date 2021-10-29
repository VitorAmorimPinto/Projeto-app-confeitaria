package com.example.aplicativoconfeitaria.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicativoconfeitaria.R;
import com.example.aplicativoconfeitaria.model.Bolo;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Myadapter extends RecyclerView.Adapter<Myadapter.MyViewHolder> {

    Context context;
    ArrayList<Bolo> boloArrayList;

    public Myadapter(Context context, ArrayList<Bolo> boloArrayList) {
        this.context = context;
        this.boloArrayList = boloArrayList;
    }

    @NonNull
    @Override
    public Myadapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.listagem,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Bolo bolo = boloArrayList.get(position);

        holder.nome.setTextContent(bolo.getNome());
        holder.descricao.setTextContent(bolo.getDescricao());
        holder.preco.setTextContent(String.valueOf(bolo.getPreco()));

    }

    @Override
    public int getItemCount() {
        return boloArrayList.size();
    }

    public void notifyItemRangeRemoved() {
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        Text nome, descricao, preco;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.tvnome);
            descricao = itemView.findViewById(R.id.tvdescricao);
            preco = itemView.findViewById(R.id.tvpreco);
        }
    }

}
