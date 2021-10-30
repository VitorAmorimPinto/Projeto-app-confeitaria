package com.example.aplicativoconfeitaria.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.aplicativoconfeitaria.auxiliar.Permissao;
import com.example.aplicativoconfeitaria.configfirebase.ConfiguracaoFirebase;

import com.example.aplicativoconfeitaria.R;
import com.example.aplicativoconfeitaria.model.Bolo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.example.aplicativoconfeitaria.auxiliar.Base64Custom;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class CadastroBoloActivity extends AppCompatActivity {

    private String [] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA

    };


    private EditText txtNomeBolo, txtPreco, txtIngredientes, txtDescricao;
    private Bolo bolo;
    private Button btnCadastrarBolo;
    private DatabaseReference referencia = ConfiguracaoFirebase.getFirebaseDataBase();
    private ImageButton btncamera,btngaleria;
    private static final int codigoCamera  = 100;
    private static final int codigoGaleria = 200;
    private ImageView imgbolopreview;
    private StorageReference storageReference;
    private Bitmap imagem = null;
    private String identificaBolo;
    private String pegaBolo;
    private Uri urlBolo;
    private FirebaseAuth autenticacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_bolo);
        txtNomeBolo = findViewById(R.id.edtNomeBolo);
        txtPreco = findViewById(R.id.edtPreco);
        txtIngredientes = findViewById(R.id.edtIngredientes);
        txtDescricao = findViewById(R.id.edtDescricao);
        btnCadastrarBolo = findViewById(R.id.btnCadastraBolo);
        imgbolopreview = findViewById(R.id.imgBoloPreview);

        btncamera = findViewById(R.id.btnCamera);
        btngaleria = findViewById(R.id.btnGaleria);

        storageReference = ConfiguracaoFirebase.getFirebaseStorage();

        Permissao.validarPermissoes( permissoesNecessarias, this, 1);

        //Configurando Ações do botão que abre a camera
        btncamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if ( i.resolveActivity(getPackageManager()) != null ){
                    startActivityForResult(i, codigoCamera );
                }
            }
        });
        //Configurando Ações do botão que abre a galeria de fotos
        btngaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if ( i.resolveActivity(getPackageManager()) != null ){
                    startActivityForResult(i, codigoGaleria );
                }
            }
        });

        //Configurando Ações do botão cadastrar
        btnCadastrarBolo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
                // Converte o nome do bolo para base 64
                pegaBolo = txtNomeBolo.getText().toString();
                identificaBolo = Base64Custom.codificarBase64(pegaBolo);

                String NomeBolo = txtNomeBolo.getText().toString();
                String Preco = txtPreco.getText().toString();
                //Double Preco = Double.parseDouble(txtPreco.getText().toString());
                String Ingredientes = txtIngredientes.getText().toString();
                String Descricao = txtDescricao.getText().toString();

                //Verifica se o nome do bolo já existe no banco
                DatabaseReference bolino = referencia.child("bolos");
                bolino.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            if (snapshot.child(identificaBolo).exists()) {
                                //do ur stuff
                                Toast.makeText(CadastroBoloActivity.this,
                                        "O nome de bolo escolhido já existe" +
                                                " por favor digite um nome diferente",
                                        Toast.LENGTH_SHORT).show();
                                txtNomeBolo.setText("");
                                break;
                            } else {
                                //do something if not exists
                                //Validar se os campos foram preenchidos
                                if (!NomeBolo.isEmpty() && !Ingredientes.isEmpty() && !Descricao.isEmpty() && !Preco.isEmpty()) {

                                    bolo = new Bolo();
                                    bolo.setNome(NomeBolo);
                                    bolo.setPreco(Double.parseDouble(Preco));
                                    bolo.setIngredientes(Ingredientes);
                                    bolo.setDescricao(Descricao);
                                    bolo.setIdBolo(identificaBolo);
                                    uploadImagem();
//                                  cadastrarBolo();
                                   // limpaInformacoes();


                                } else {
                                    Toast.makeText(CadastroBoloActivity.this,
                                            "Preencha Todos os campos!",
                                            Toast.LENGTH_SHORT).show();
                                }
                                break;
                            }


                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });

    }
    //Recupera as imagems selecionadas pelo o usuario ou tiradas na camera
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
             imagem = null;

            try {

                switch (requestCode) {
                    case codigoCamera:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                    case codigoGaleria:
                        Uri localImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);
                        break;
                }

                if (imagem != null) {

                    imgbolopreview.setImageBitmap(imagem);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadImagem() {
        if (imagem != null) {

            //Recuperar dados da imagem para o firebase
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            byte[] dadosImagem = baos.toByteArray();

            //Salvar imagem no firebase
            final StorageReference imagemRef = storageReference
                    .child("imagens")
                    .child("bolos")
                    .child(identificaBolo)
                    .child("bolo.jpeg");

            UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CadastroBoloActivity.this,
                            "Erro ao fazer upload da imagem",
                            Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(CadastroBoloActivity.this,
                            "Sucesso ao fazer upload da imagem",
                            Toast.LENGTH_SHORT).show();

                    imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            Uri url = task.getResult();
                            //bolo.setFoto( url.toString());
                            //bolo.atualizar();

                                cadastrarBolo(url.toString());
                        }

                    });


                }
            });
        }
        else {
            Toast.makeText(CadastroBoloActivity.this,
                    "Por favor escolha uma imagem",
                    Toast.LENGTH_SHORT).show();
        }
    }


    //Cadastra os bolos no banco de dados
    public void cadastrarBolo(String url){
        String mensagem = "";
        try {
            // Converte o nome do bolo para base 64
//             pegaBolo = txtNomeBolo.getText().toString();
//             identificaBolo = Base64Custom.codificarBase64(pegaBolo);
             bolo.setFoto(url);
            //Adicionando objetos no banco de dados
            DatabaseReference bolos = referencia.child("bolos");
            bolos.child(identificaBolo).setValue(bolo);

            mensagem = "Bolo cadastrado com sucesso!";
        }catch (Exception ex){
            mensagem = "Erro ao cadastrar bolo Tente novamente mais tarde.";
        }
        new AlertDialog.Builder(this)
                .setMessage(mensagem)
                .setTitle("Mensagem")
                .setPositiveButton("Ok", null)
                .show();
        limpaInformacoes();
    }
    //Limpa os campos de texto da tela
    private void limpaInformacoes() {
        txtNomeBolo.setText("");
        txtDescricao.setText("");
        txtIngredientes.setText("");
        txtPreco.setText("");
//        bolo = new Bolo();
    }
    //Configura a ação que deve ser tomada caso o usuario negue as permissões necessarias
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for ( int permissaoResultado : grantResults ){
            if ( permissaoResultado == PackageManager.PERMISSION_DENIED ){
                alertaValidacaoPermissao();
            }
        }

    }
    //Configura a mensagem de alerta caso o usuario negue as permissões
    private void alertaValidacaoPermissao(){

        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }


}