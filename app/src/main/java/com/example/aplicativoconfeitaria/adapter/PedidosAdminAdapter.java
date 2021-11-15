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
import com.example.aplicativoconfeitaria.activity.activity_detalhes_item;
import com.example.aplicativoconfeitaria.configfirebase.ConfiguracaoFirebase;
import com.example.aplicativoconfeitaria.model.Bolo;
import com.example.aplicativoconfeitaria.model.Pedido;
import com.example.aplicativoconfeitaria.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.List;

public class PedidosAdminAdapter extends RecyclerView.Adapter<PedidosAdminAdapter.ViewHolderPedidosAdmin>{

    private List<Pedido> pedidos;
    private Context context;
    private DatabaseReference firebaseref = ConfiguracaoFirebase.getFirebaseDataBase();


    public PedidosAdminAdapter(List<Pedido> listaPedidos, Context c) {
        this.pedidos = listaPedidos;
        this.context = c;
    }

    @NonNull
    @Override
    public ViewHolderPedidosAdmin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from( parent.getContext() ).inflate(R.layout.adapter_pedidos_admin, parent, false);
        return new ViewHolderPedidosAdmin( itemLista );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPedidosAdmin holder, int position) {
        Pedido pedido = pedidos.get( position );
        DatabaseReference boloref = firebaseref.child("bolos").child(pedido.getIdBolo());
        DatabaseReference usuarioref = firebaseref.child("usuarios").child(pedido.getIdUsuario());

        //Define os holders dos dados do bolo
        boloref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Bolo bolo = snapshot.getValue(Bolo.class);
                holder.nomeBolo.setText(bolo.getNome());
                if ( bolo.getFoto() != null){
                    Uri uri = Uri.parse( bolo.getFoto() );
                    Glide.with ( context ).load( uri ).into( holder.foto );
                }else {
                    holder.foto.setImageResource( R.drawable.imagem_default );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Define os holders dos dados do cliente
        usuarioref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                holder.nomeCliente.setText(usuario.getNome());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Define os demais holders
        holder.horaEntrega.setText(pedido.getDataEntrega());
        holder.localEntrega.setText(pedido.getLocalEntrega());

    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    public class ViewHolderPedidosAdmin extends RecyclerView.ViewHolder{
        ImageView foto;
        TextView nomeBolo, nomeCliente, localEntrega, horaEntrega;
        LinearLayout parentLayout;
        public ViewHolderPedidosAdmin(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.imageViewBoloPedidosAdmin);
            nomeBolo = itemView.findViewById(R.id.textViewNomeBoloPedidosAdmin);
            nomeCliente = itemView.findViewById(R.id.textViewNomeClientePedidosAdmin);
            localEntrega = itemView.findViewById(R.id.textViewLocalEntregaPedidosAdmin);
            horaEntrega = itemView.findViewById(R.id.textViewHoraEntregaEntregaPedidosAdmin);
            parentLayout = itemView.findViewById(R.id.linearLayoutPedidosAdmin);
        }
    }
}
