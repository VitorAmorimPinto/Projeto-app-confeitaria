package com.example.aplicativoconfeitaria.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aplicativoconfeitaria.R;
import com.example.aplicativoconfeitaria.model.ItensMenu;

import java.util.ArrayList;

public class MinhaContaAdapter extends ArrayAdapter<ItensMenu> {
    private final Context context;
    private final ArrayList<ItensMenu> elementos;

    public MinhaContaAdapter(Context context, ArrayList<ItensMenu> elementos) {
        super(context, R.layout.lista_minha_conta, elementos);
        this.context = context;
        this.elementos = elementos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.lista_minha_conta, parent, false);

        TextView nomeOpcao = (TextView) rowView.findViewById(R.id.opcao);
        TextView descricao = (TextView) rowView.findViewById(R.id.descricao);
        ImageView imagem = (ImageView) rowView.findViewById(R.id.imagem);

        nomeOpcao.setText(elementos.get(position).getItem1());
        descricao.setText(elementos.get(position).getItem2());
        imagem.setImageResource(elementos.get(position).getImagem());

        return rowView;
    }

}
