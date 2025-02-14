package com.example.sergi.signin.Activitys;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sergi.signin.Class.Datos;
import com.example.sergi.signin.Class.Perro;
import com.example.sergi.signin.R;

import java.io.File;

public class InformationDogLost extends AppCompatActivity
        implements Datos.ImageLoadListener{

    Perro p;
    ImageButton gmail;
    ImageView imageViewFoto;
    TextView textViewNombre;
    TextView textViewRaza;
    TextView textViewColor;
    TextView textViewChip;
    TextView textViewFecha;
    TextView textViewRecompensa;
    TextView textViewMiedo;
    TextView textViewDescripcion;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_dog_lost);
        cargarVariables();
        cargarDatosEnLayout();
        gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),SendMailActivity.class);
                i.putExtra("emailUsuario",p.getUser().getEmail());
                i.putExtra("nombrePerro",p.getNombre());
                i.putExtra("activity","informationDogLost");
                startActivity(i);
            }
        });
    }

    private void cargarDatosEnLayout() {
        String ni = p.getImageUri();
        File f = new File(getBaseContext().getFilesDir().getAbsolutePath()+"/imagenes/"+ni+".jpg");
        if (f.exists()){
            imageViewFoto.setImageBitmap(BitmapFactory.decodeFile(f.getAbsolutePath()));
        }else{
            Datos.descargarImagenesPerro(p, imageViewFoto, this);
        }
        textViewNombre.setText(p.getNombre());
        textViewRaza.setText(p.getRaza());
        textViewColor.setText(p.getColor());
        textViewChip.setText(p.getChip());
        textViewFecha.setText(p.getFecha());
        textViewRecompensa.setText(String.valueOf(p.getRecompensa())+"€");
        textViewMiedo.setText(p.getCoger());
        textViewDescripcion.setText(p.getDescripcion());

    }

    private void cargarVariables() {
        gmail=(ImageButton)findViewById(R.id.botonGmail);
        p = (Perro)getIntent().getExtras().getSerializable("dog");
        imageViewFoto=(ImageView)findViewById(R.id.fotoPerroInformacion);
        textViewNombre=(TextView)findViewById(R.id.nombrePerroInformacion);
        textViewRaza=(TextView)findViewById(R.id.razaPerroInformacion);
        textViewColor=(TextView)findViewById(R.id.colorPerroInformacion);
        textViewChip=(TextView)findViewById(R.id.chipPerroInformacion);
        textViewFecha=(TextView)findViewById(R.id.fechaPerroInformacion);
        textViewRecompensa=(TextView)findViewById(R.id.recompensaPerroInformacion);
        textViewMiedo=(TextView)findViewById(R.id.miedoPerroInformacion);
        textViewDescripcion=(TextView)findViewById(R.id.descriptionInformationDog);
    }

    @Override
    public void onImageLoad(ImageView i, File f) {
        imageViewFoto.setImageBitmap(BitmapFactory.decodeFile(f.getAbsolutePath()));
    }
}
