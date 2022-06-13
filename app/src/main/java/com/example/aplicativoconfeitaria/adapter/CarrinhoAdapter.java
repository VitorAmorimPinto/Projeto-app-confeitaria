package com.example.aplicativoconfeitaria.adapter;

import android.content.Context;
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
import com.example.aplicativoconfeitaria.configfirebase.ConfiguracaoFirebase;
import com.example.aplicativoconfeitaria.model.ItemPedido;
import com.example.aplicativoconfeitaria.model.Pedido;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class CarrinhoAdapter extends RecyclerView.Adapter<CarrinhoAdapter.ViewHolderCarrinho> {
    private List<ItemPedido> itens;
    private Context context;
    private DatabaseReference firebaseref = ConfiguracaoFirebase.getFirebaseDataBase();

    public CarrinhoAdapter(List<ItemPedido> listaItens, Context c) {
        this.itens = listaItens;
        this.context = c;
    }
    @NonNull
    @Override
    public ViewHolderCarrinho onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from( parent.getContext() ).inflate(R.layout.adapter_carrinho, parent, false);
        return new CarrinhoAdapter.ViewHolderCarrinho(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull CarrinhoAdapter.ViewHolderCarrinho holder, int position) {
        ItemPedido item = itens.get(position);

        holder.nomeBolo.setText(item.getNomeBolo());
        holder.preco.setText( "R$ "+item.getPreco().toString());
        if(item.getFoto() != null){
            Uri uri = Uri.parse( item.getFoto() );
            Glide.with ( context ).load( uri ).into( holder.foto );
        }else{
            holder.foto.setImageResource(R.drawable.bolopadrao);
        }
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    public class ViewHolderCarrinho extends RecyclerView.ViewHolder{
        ImageView foto;
        TextView nomeBolo, preco;
        LinearLayout parentLayout;
        public ViewHolderCarrinho(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.imageViewBoloPedidoCarrinho);
            nomeBolo = itemView.findViewById(R.id.textTituloBoloCarrinho);
            preco = itemView.findViewById(R.id.textPrecoItemCarrinho);
        }
    }
}
