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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aplicativoconfeitaria.R;
import com.example.aplicativoconfeitaria.activity.ActivityDetalhesPedido;
import com.example.aplicativoconfeitaria.activity.ActivityDetalhesPedidoUsuario;
import com.example.aplicativoconfeitaria.configfirebase.ConfiguracaoFirebase;
import com.example.aplicativoconfeitaria.model.Bolo;
import com.example.aplicativoconfeitaria.model.Pedido;
import com.example.aplicativoconfeitaria.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PedidosClienteAdapter extends RecyclerView.Adapter<PedidosClienteAdapter.ViewHolderPedidosCliente>{

    private List<Pedido> pedidos;
    private Context context;
    private DatabaseReference firebaseref = ConfiguracaoFirebase.getFirebaseDataBase();
    private String statusText;


    public PedidosClienteAdapter(List<Pedido> listaPedidos, Context c) {
        this.pedidos = listaPedidos;
        this.context = c;
    }

    @NonNull
    @Override
    public PedidosClienteAdapter.ViewHolderPedidosCliente onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from( parent.getContext() ).inflate(R.layout.adapter_pedidos_cliente, parent, false);
        return new PedidosClienteAdapter.ViewHolderPedidosCliente( itemLista );
    }

    @Override
    public void onBindViewHolder(@NonNull PedidosClienteAdapter.ViewHolderPedidosCliente holder, int position) {
        Pedido pedido = pedidos.get( position );
        DatabaseReference boloref = firebaseref.child("bolos").child(pedido.getIdBolo());
        DatabaseReference pedidoref = firebaseref.child("pedidos").child(pedido.getId());

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

        Date dataEntegaPedido= null;
        try {
            dataEntegaPedido = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(pedido.getDataEntrega());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(new Date().after(dataEntegaPedido) && (pedido.getStatus().equals(0) || pedido.getStatus().equals(1))){
            pedido.setStatus(5);
            pedidoref.setValue(pedido);
        }

        switch (pedido.getStatus()){
            case 0:
                statusText = "Novo";
                holder.statusPedido.setBackground(ContextCompat.getDrawable(context, R.drawable.status_novo));
                break;
            case 1:
                statusText = "Em andamento";
                holder.statusPedido.setBackground(ContextCompat.getDrawable(context, R.drawable.status_em_andamento));

                break;
            case 2:
                statusText = "Finalizado";
                holder.statusPedido.setBackground(ContextCompat.getDrawable(context, R.drawable.status_finalizado));
                break;
            case 3:
                statusText = "Cancelado";
                holder.statusPedido.setBackground(ContextCompat.getDrawable(context, R.drawable.status_cancelado));
                break;
            case 4:
                statusText = "Recusado";
                holder.statusPedido.setBackground(ContextCompat.getDrawable(context, R.drawable.status_recusado));
                break;
            case 5:
                statusText = "Atrasado";
                holder.statusPedido.setBackground(ContextCompat.getDrawable(context, R.drawable.status_atrasado));
                break;
        }
        holder.statusPedido.setText(statusText);

        //Define os demais holders
        holder.parentLayout.setOnClickListener((view) -> {
            Intent intent = new Intent(context, ActivityDetalhesPedidoUsuario.class);
            intent.putExtra("idPedido", pedido.getId());
            context.startActivity(intent);
        });
        holder.horaEntrega.setText(pedido.getDataEntrega());
        holder.horaRealizacao.setText(pedido.getDataRealizacao());

    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    public class ViewHolderPedidosCliente extends RecyclerView.ViewHolder{
        ImageView foto;
        TextView nomeBolo, horaEntrega, horaRealizacao, statusPedido;
        LinearLayout parentLayout;
        public ViewHolderPedidosCliente(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.imageViewBoloPedidosCliente);
            nomeBolo = itemView.findViewById(R.id.textViewNomeBoloPedidosCliente);
            horaRealizacao = itemView.findViewById(R.id.textViewDataRealizacaoPedidosCliente);
            horaEntrega = itemView.findViewById(R.id.textViewEntregaPedidosCliente);
            parentLayout = itemView.findViewById(R.id.linearLayoutPedidosCliente);
            statusPedido = itemView.findViewById(R.id.textViewStatusPedidoCliente);
        }
    }
}
