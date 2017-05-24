package com.example.sergi.signin.Activitys;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sergi.signin.Class.Datos;
import com.example.sergi.signin.Class.Perro;
import com.example.sergi.signin.R;

import java.io.File;

public class InformationDogLost extends AppCompatActivity
        implements Datos.ImageLoadListener{

    Perro p;
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
        textViewDescripcion.setText(p.getNombre()+"/"+p.getRaza());
        //textViewDescripcion.setText(p.getDescripcion());

    }

    private void cargarVariables() {
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
