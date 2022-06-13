package com.example.aplicativoconfeitaria.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicativoconfeitaria.R;
import com.example.aplicativoconfeitaria.activity.ActivityFinalizarPedido;
import com.example.aplicativoconfeitaria.adapter.BolosAdapter;
import com.example.aplicativoconfeitaria.adapter.CarrinhoAdapter;
import com.example.aplicativoconfeitaria.auxiliar.Base64Custom;
import com.example.aplicativoconfeitaria.configfirebase.ConfiguracaoFirebase;
import com.example.aplicativoconfeitaria.model.Bolo;
import com.example.aplicativoconfeitaria.model.Confeitaria;
import com.example.aplicativoconfeitaria.model.Endereco;
import com.example.aplicativoconfeitaria.model.ItemPedido;
import com.example.aplicativoconfeitaria.model.ItensMenu;
import com.example.aplicativoconfeitaria.model.Pedido;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CarrinhoFragment extends Fragment {
    private TextView tvNomeBolo, tvDescricaoBolo, tvPrecoBolo, tvDataEntrega, tvLocalDeEntrega;
    private EditText edtObservacoes;
    private String nomeBolo, descricaoBolo, ingredientesBolo, precoBolo, pedidoId, horarioEntrega, emailUsuario, idUsuario, metodoPagamento, enderecoEntregaUsuario, enderecoEntregaConfeitaria;
    private ImageView ivImagemBolo;
    private Button btnFinalizarPedido;
    private Button buttonContinuarComprandoItemCarrinho,buttonEscolherDataFragment, buttonFinalizarPedidoUsuarioCarrinho;
    private Spinner spnHoraEntrega, spnMetodoPagamento;
    private DatabaseReference dbReference = ConfiguracaoFirebase.getFirebaseDataBase();
    private FirebaseAuth autenticacao;
    private Bolo bolo;
    private Pedido pedido;
    private SimpleDateFormat dateFormat;
    private RadioButton rbReceberEmCasa, rbRetirarConfeitaria;
    private RadioGroup rgOpcaoEntrega;
    private List<ItemPedido> itensCarrinho = new ArrayList<>();
    private Pedido pedidoRecuperado;
    private String statusPedido;
    private RecyclerView recyclerViewListaItensCarrinho;
    private CarrinhoAdapter adapter;
    private DatabaseReference pedidosRef;
    private ValueEventListener valueEventListenerPedidos;
    private Fragment selectedFragment;
    private Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_pedido_carrinho, container, false);
        tvDataEntrega = view.findViewById(R.id.textViewDataEntregaFinalizarPedidoCarrinho);
        rbRetirarConfeitaria = view.findViewById(R.id.radioButtonRetirarConfeitariaCarrinho);
        rbReceberEmCasa = view.findViewById(R.id.radioButtonReceberEmCasaCarrinho);
        rgOpcaoEntrega = view.findViewById(R.id.radioGroupOpcaoEntregaCarrinho);
        tvLocalDeEntrega = view.findViewById(R.id.textViewLocalDeEntregaFinalizarPedidoCarrinho);
        buttonFinalizarPedidoUsuarioCarrinho = view.findViewById(R.id.buttonFinalizarPedidoUsuarioCarrinho);

        recyclerViewListaItensCarrinho = view.findViewById(R.id.recyclerViewListaItensCarrinho);
        pedidosRef = ConfiguracaoFirebase.getFirebaseDataBase().child("pedidos").child("-N4L2Gc8yHiH-zSf5QZ2").child("itens");
        adapter = new CarrinhoAdapter(itensCarrinho, getActivity());
        buttonContinuarComprandoItemCarrinho = view.findViewById(R.id.buttonContinuarComprandoItemCarrinho);
        buttonEscolherDataFragment = view.findViewById(R.id.buttonEscolherDataFragment);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getActivity() );
        recyclerViewListaItensCarrinho.setLayoutManager( layoutManager );
        recyclerViewListaItensCarrinho.setHasFixedSize( true );
        recyclerViewListaItensCarrinho.setAdapter( adapter );
        recuperarEnderecos();
        radioButton();
        buttonContinuarComprandoItemCarrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        buttonEscolherDataFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int dia = cal.get(Calendar.DAY_OF_MONTH);
                int mes = cal.get(Calendar.MONTH);
                int ano = cal.get(Calendar.YEAR);

                DatePickerDialog dialog = new DatePickerDialog(
                        context,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        ano, mes, dia
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
            DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    i1 += 1;

                    String data = i2 + "/" + i1 + "/" + i;
                    tvDataEntrega.setText(data);
                }
            };
        });
        buttonFinalizarPedidoUsuarioCarrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tvDataEntrega.getText().toString().isEmpty()) {

                    Date dataPedido = Calendar.getInstance().getTime();
                    String dataHoraEntregaPedido = tvDataEntrega.getText().toString() + " " + "10:30";
                    SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    Date dataEntregaCompara = null;
                    try {
                        dataEntregaCompara = formatter1.parse(dataHoraEntregaPedido);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (dataEntregaCompara.compareTo(dataPedido) < 0) {
                        Toast.makeText(context,
                                "Perdão, mas não possuimos um DeLorean para fazer entregas no passado. " + dataEntregaCompara.compareTo(dataPedido),
                                Toast.LENGTH_SHORT).show();
                    } else{
                        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        String dataPedidoString = dateFormat.format(dataPedido);
//                        String idBoloPedido = Base64Custom.codificarBase64(bolo.getNome());
                        String valorTotalPedido = precoBolo;
                        String localEntregaPedido = tvLocalDeEntrega.getText().toString();
//                        String observacaoPedido = edtObservacoes.getText().toString();


                        DatabaseReference pedidosReference = dbReference.child("pedidos").child("-N4L2Gc8yHiH-zSf5QZ2");
                        pedido = new Pedido();

                        pedido.setIdBolo("Qm9sbyB2dWxjw6NvIGRlIGJyaWdhZGVpcm8=");
                        pedido.setIdUsuario("Y2xpZW50ZUBnbWFpbC5jb20=");
                        pedido.setValorTotal(82.95);
                        pedido.setMetodoPagamento("Dinheiro");
                        pedido.setDataRealizacao(dataPedidoString);
                        pedido.setDataEntrega(dataHoraEntregaPedido);
                        pedido.setLocalEntrega(localEntregaPedido);
                        pedido.setObservacao("");
                        pedido.setObservacaoConfeiteiro("");
                        pedido.setItens(itensCarrinho);
                        pedido.setStatus(0);

                        pedidosReference.setValue(pedido);

                        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                        dialog.setTitle("Sucesso");
                        dialog.setMessage("Seu pedido foi enviado para a confeitaria!");

                        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                        goToPrincipal();
                            }
                        });

                        dialog.create();
                        dialog.show();
                    }
                } else {
                    Toast.makeText(context,
                            "Escolha a data de entrega para prosseguir",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;

    }
//    public void cadastraPedido() throws ParseException {
//        if (!tvDataEntrega.getText().toString().isEmpty()) {
//
//            Date dataPedido = Calendar.getInstance().getTime();
//            String dataHoraEntregaPedido = tvDataEntrega.getText().toString() + " " + horarioEntrega;
//            SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
//            Date dataEntregaCompara = formatter1.parse(dataHoraEntregaPedido);
//            if (dataEntregaCompara.compareTo(dataPedido) < 0) {
//                Toast.makeText(context,
//                        "Perdão, mas não possuimos um DeLorean para fazer entregas no passado. " + dataEntregaCompara.compareTo(dataPedido),
//                        Toast.LENGTH_SHORT).show();
//            } else{
//                dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
//                String dataPedidoString = dateFormat.format(dataPedido);
//                String idBoloPedido = Base64Custom.codificarBase64(bolo.getNome());
//                String valorTotalPedido = precoBolo;
//                String localEntregaPedido = tvLocalDeEntrega.getText().toString();
//                String observacaoPedido = edtObservacoes.getText().toString();
//
//
//                DatabaseReference pedidosReference = dbReference.child("pedidos").child("-N4L2Gc8yHiH-zSf5QZ2").child("itens");
//                pedido = new Pedido();
//
//                pedido.setIdBolo(idBoloPedido);
//                pedido.setIdUsuario(idUsuario);
//                pedido.setValorTotal(Double.parseDouble(valorTotalPedido.replace(",", ".")));
//                pedido.setMetodoPagamento(metodoPagamento);
//                pedido.setDataRealizacao(dataPedidoString);
//                pedido.setDataEntrega(dataHoraEntregaPedido);
//                pedido.setLocalEntrega(localEntregaPedido);
//                pedido.setObservacao(observacaoPedido);
//                pedido.setObservacaoConfeiteiro("");
//                pedido.setItens(itensCarrinho);
//                pedido.setStatus(0);
//
//                pedidosReference.setValue(pedido);
//
//                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
//                dialog.setTitle("Sucesso");
//                dialog.setMessage("Seu pedido foi enviado para a confeitaria!");
//
//                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
////                        goToPrincipal();
//                    }
//                });
//
//                dialog.create();
//                dialog.show();
//            }
//        } else {
//            Toast.makeText(context,
//                    "Escolha a data de entrega para prosseguir",
//                    Toast.LENGTH_SHORT).show();
//        }
//
//    }

    public void recuperarPedido(){
        valueEventListenerPedidos = pedidosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itensCarrinho.clear();
                for ( DataSnapshot dados: dataSnapshot.getChildren() ){
                    ItemPedido i = dados.getValue( ItemPedido.class );
                    itensCarrinho.add(i);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void recuperarEnderecos() {
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDataBase().child("enderecos");

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    String t = data.getKey();
                    if (t.equals("Y2xpZW50ZUBnbWFpbC5jb20=")) {
                        Endereco dados = data.getValue(Endereco.class);
                        enderecoEntregaUsuario = dados.getLogradouro() + ", nº " + dados.getNumero() + " / " + dados.getBairro() + ", " + dados.getLocalidade();
                        rbReceberEmCasa.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        firebase = ConfiguracaoFirebase.getFirebaseDataBase().child("confeitaria");

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Confeitaria dados = data.getValue(Confeitaria.class);
                    enderecoEntregaConfeitaria = dados.getEndereco();
                    tvLocalDeEntrega.setText(enderecoEntregaConfeitaria);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void radioButton() {
        rgOpcaoEntrega.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radioButtonRetirarConfeitariaCarrinho) {
                    tvLocalDeEntrega.setText(enderecoEntregaConfeitaria);
                } else if (i == R.id.radioButtonReceberEmCasaCarrinho) {
                    tvLocalDeEntrega.setText(enderecoEntregaUsuario);
                }
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        itensCarrinho.clear();
        recuperarPedido();
    }
    @Override
    public void onStop() {
        super.onStop();
        pedidosRef.removeEventListener( valueEventListenerPedidos );
    }
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

}
