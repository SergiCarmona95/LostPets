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

public class InformationDogResque extends AppCompatActivity
        implements Datos.ImageLoadListener{

    TextView textViewRaza;
    TextView textViewColor;
    TextView textViewFecha;
    TextView textViewDescripcion;
    ImageButton gmail;
    ImageView imageView;
    Perro p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_dog_resque);
        cargarVariables();
        cargarDatosEnLayout();

        gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),SendMailActivity.class);
                i.putExtra("emailUsuario",p.getUser().getEmail());
                i.putExtra("raza",p.getRaza());
                i.putExtra("activity","informationDogResque");
                startActivity(i);
            }
        });
}

    private void cargarDatosEnLayout() {
        String ni = p.getImageUri();
        File f = new File(getBaseContext().getFilesDir().getAbsolutePath()+"/imagenes/"+ni+".jpg");
        if (f.exists()){
            imageView.setImageBitmap(BitmapFactory.decodeFile(f.getAbsolutePath()));
        }else{
            Datos.descargarImagenesPerro(p, imageView, this);
        }
        textViewRaza.setText(p.getRaza());
        textViewColor.setText(p.getColor());
        textViewFecha.setText(p.getFecha());
        textViewDescripcion.setText(p.getDescripcion());
    }

    private void cargarVariables() {
        p = (Perro)getIntent().getExtras().getSerializable("dog");
        textViewRaza=(TextView)findViewById(R.id.razaPerroInformacionResque);
        textViewColor=(TextView)findViewById(R.id.colorPerroInformacionResque);
        textViewFecha=(TextView)findViewById(R.id.fechaPerroInformacionResque);
        textViewDescripcion=(TextView)findViewById(R.id.descriptionInformationDogResque);
        gmail=(ImageButton)findViewById(R.id.botonGmailResque);
        imageView=(ImageView)findViewById(R.id.fotoPerroInformacionResque);

    }

    @Override
    public void onImageLoad(ImageView i, File f) {
        imageView.setImageBitmap(BitmapFactory.decodeFile(f.getAbsolutePath()));
    }
}
