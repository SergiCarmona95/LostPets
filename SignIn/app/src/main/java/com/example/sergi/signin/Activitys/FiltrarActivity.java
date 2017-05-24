package com.example.sergi.signin.Activitys;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sergi.signin.Class.Coordenadas;
import com.example.sergi.signin.Class.Datos;
import com.example.sergi.signin.Class.Perro;
import com.example.sergi.signin.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FiltrarActivity extends AppCompatActivity {
    String nombre,color,raza,chip;
    boolean nom=false, col=false,ra=false;
    LayoutInflater inflater;
    FiltrarActivity.MyTodoRecyclerViewAdapter myTodoRecyclerViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtrar);
        cargarVariables();
       Toast.makeText(FiltrarActivity.this, "Nombre:"+nombre+", Color:"+color+", Chip:"+chip+", Raza:"+raza, Toast.LENGTH_SHORT).show();
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.listViewFiltrar);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        myTodoRecyclerViewAdapter = new FiltrarActivity.MyTodoRecyclerViewAdapter();
        myTodoRecyclerViewAdapter.setListPerroPerdidos(Datos.listperrosPerdidos);
        myTodoRecyclerViewAdapter.setListPerroEncontrados(Datos.listperrosEncontrados);
        for (Perro perro:myTodoRecyclerViewAdapter.getListPerroEncontrados()) {
            if (!color.equals("")){
                col=true;
            }
            if (!raza.equals("Selecciona Raza Aquí")){
                ra=true;
            }
            if (!nombre.equals("")){
                nom=true;
            }
                if (nom!=true){
                    if (col== true){
                        if (perro.getColor().equals(color)){
                            if (ra==true) {
                                if (perro.getRaza().equals(raza)) {
                                    if (perro.getChip().equals(chip)) {
                                        myTodoRecyclerViewAdapter.addPerro(perro);
                                    }
                                }
                            }else{
                                if (perro.getChip().equals(chip)){
                                    myTodoRecyclerViewAdapter.addPerro(perro);
                                }
                            }
                        }
                    }else {
                        if (ra==true){
                            if (perro.getRaza().equals(raza)){
                                myTodoRecyclerViewAdapter.addPerro(perro);
                            }
                        }else{
                            myTodoRecyclerViewAdapter.addPerro(perro);

                        }
                    }
                }
        }
        for (Perro perro:myTodoRecyclerViewAdapter.getListPerroPerdidos()) {
            //comprobarDatosFiltrar(perro);
            if (!nombre.equals("")){
                nom=true;
            }
            if (!color.equals("")){
                col=true;
            }
            if (!raza.equals("Selecciona Raza Aquí")){
                ra=true;
            }

            if (nom==true){
                if (perro.getNombre().equals(nombre)){
                    if (col== true){
                        if (perro.getColor().equals(color)){
                            if (ra==true) {
                                if (perro.getRaza().equals(raza)) {
                                    if (perro.getChip().equals(chip)) {
                                        myTodoRecyclerViewAdapter.addPerro(perro);
                                    }
                                }
                            }else{
                                if (perro.getChip().equals(chip)){
                                    myTodoRecyclerViewAdapter.addPerro(perro);
                                }
                            }
                        }
                    }else {
                        if (ra==true){
                            if (perro.getRaza().equals(raza)){
                                if (perro.getChip().equals(chip)){
                                    myTodoRecyclerViewAdapter.addPerro(perro);
                                }
                            }
                        }else{
                            if (perro.getChip().equals(chip)){
                                myTodoRecyclerViewAdapter.addPerro(perro);
                            }
                        }
                    }
                }
            }else{
                if (col== true){
                    if (perro.getColor().equals(color)){
                        if (ra==true){
                            if (perro.getRaza().equals(raza)){
                                if (perro.getChip().equals(chip)){
                                    myTodoRecyclerViewAdapter.addPerro(perro);
                                }
                            }
                        }else{
                            if (perro.getChip().equals(chip)){
                                myTodoRecyclerViewAdapter.addPerro(perro);
                            }
                        }
                    }
                }else {
                    if (ra==true){
                        if (perro.getRaza().equals(raza)){
                            if (perro.getChip().equals(chip)){
                                myTodoRecyclerViewAdapter.addPerro(perro);
                            }
                        }
                    }else{
                        if (perro.getChip().equals(chip)){
                            myTodoRecyclerViewAdapter.addPerro(perro);
                        }
                    }
                }
            }
            nom=false;
            col=false;
            ra=false;
        }


        mRecyclerView.setAdapter(myTodoRecyclerViewAdapter);

    }

    private void comprobarDatosFiltrar(Perro perro){
        if (!nombre.equals("")){
            nom=true;
        }
        if (!color.equals("")){
            col=true;
        }
        if (!raza.equals("Selecciona Raza Aquí")){
            ra=true;
        }

        if (nom==true){
            if (perro.getNombre().equals(nombre)){
                if (col== true){
                    if (perro.getColor().equals(color)){
                        if (ra==true) {
                            if (perro.getRaza().equals(raza)) {
                                if (perro.getChip().equals(chip)) {
                                    myTodoRecyclerViewAdapter.addPerro(perro);
                                }
                            }
                        }else{
                            if (perro.getChip().equals(chip)){
                                myTodoRecyclerViewAdapter.addPerro(perro);
                            }
                        }
                    }
                }else {
                    if (ra==true){
                        if (perro.getRaza().equals(raza)){
                            if (perro.getChip().equals(chip)){
                                myTodoRecyclerViewAdapter.addPerro(perro);
                            }
                        }
                    }else{
                        if (perro.getChip().equals(chip)){
                            myTodoRecyclerViewAdapter.addPerro(perro);
                        }
                    }
                }
            }
        }else{
            if (col== true){
                if (perro.getColor().equals(color)){
                    if (ra==true){
                        if (perro.getRaza().equals(raza)){
                            if (perro.getChip().equals(chip)){
                                myTodoRecyclerViewAdapter.addPerro(perro);
                            }
                        }
                    }else{
                        if (perro.getChip().equals(chip)){
                            myTodoRecyclerViewAdapter.addPerro(perro);
                        }
                    }
                }
            }else {
                if (ra==true){
                    if (perro.getRaza().equals(raza)){
                        if (perro.getChip().equals(chip)){
                            myTodoRecyclerViewAdapter.addPerro(perro);
                        }
                    }
                }else{
                    if (perro.getChip().equals(chip)){
                        myTodoRecyclerViewAdapter.addPerro(perro);
                    }
                }
            }
        }
        nom=false;
        col=false;
        ra=false;
    }

    private void cargarVariables() {
        inflater=getLayoutInflater();
        myTodoRecyclerViewAdapter = new FiltrarActivity.MyTodoRecyclerViewAdapter();
        nombre= getIntent().getExtras().getString("nombre");
        color= getIntent().getExtras().getString("color");
        raza= getIntent().getExtras().getString("raza");
        chip= getIntent().getExtras().getString("chip");
    }

    class MyTodoRecyclerViewAdapter extends RecyclerView.Adapter<FiltrarActivity.MyTodoRecyclerViewAdapter.CustomViewHolder> implements
            Datos.PerdidosChangeListener,
            Datos.ImageThumbLoadListener,
            Datos.EncontradosChangeListener{

        private List<Perro> listPerro;
        private List<Perro> listPerroEncontrados;
        private List<Perro> listPerroPerdidos;


        public MyTodoRecyclerViewAdapter() {
            this.listPerro = new ArrayList<>();
        }

        public List<Perro> getListPerro() {
            return listPerro;
        }

        public void setListPerro(List<Perro> listPerro) {
            this.listPerro = listPerro;
        }

        public void addPerro(Perro perro) {
            this.listPerro.add(perro);
        }

        public List<Perro> getListPerroEncontrados() {
            return listPerroEncontrados;
        }

        public void setListPerroEncontrados(List<Perro> listPerroEncontrados) {
            this.listPerroEncontrados = listPerroEncontrados;
        }

        public List<Perro> getListPerroPerdidos() {
            return listPerroPerdidos;
        }

        public void setListPerroPerdidos(List<Perro> listPerroPerdidos) {
            this.listPerroPerdidos = listPerroPerdidos;
        }

        @Override
        public FiltrarActivity.MyTodoRecyclerViewAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_dog_lost, null);
            FiltrarActivity.MyTodoRecyclerViewAdapter.CustomViewHolder viewHolder = new FiltrarActivity.MyTodoRecyclerViewAdapter.CustomViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(CustomViewHolder holder, int position) {
            Perro perroItem = listPerro.get(position);
            holder.perro=perroItem;
            holder.lat=perroItem.getLat();
            holder.lon=perroItem.getLon();
            holder.nombrePerro.setText(perroItem.getNombre());
            holder.razaPerro.setText(perroItem.getRaza());
            String ni = perroItem.getImageUri();
            File f = new File(getBaseContext().getFilesDir().getAbsolutePath()+"/imagenes/"+ni+"Thumb.jpg");
            if (!f.exists()){
                holder.fotoPerro.setImageBitmap(Datos.cambiarTamañoFoto(f));
            }else {
                holder.fotoPerro.setImageBitmap(Datos.cambiarTamañoFoto(f));
            }
            holder.fechaPerro.setText(perroItem.getFecha());
        }

        @Override
        public int getItemCount() {
            return (null != listPerro ? listPerro.size() : 0);
        }

        @Override
        public void notifyEncontradosChange() {
            notifyDataSetChanged();
        }

        @Override
        public void onImageThumbLoad(ImageView i, File f) {
            i.setImageBitmap(BitmapFactory.decodeFile(f.getAbsolutePath()));
        }

        @Override
        public void notifyPerdidosChange() {
            notifyDataSetChanged();
        }

        public void setList(ArrayList<Perro> listPerro) {
            this.listPerro = listPerro;
        }


        class CustomViewHolder extends RecyclerView.ViewHolder {
            protected TextView nombrePerro;
            protected ImageView fotoPerro;
            protected TextView razaPerro;
            protected TextView fechaPerro;
            protected double lat;
            protected double lon;
            protected Perro perro;

            public CustomViewHolder(View view) {
                super(view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(FiltrarActivity.this);
                        View mView=inflater.inflate(R.layout.dialog_dog_buttons,null);
                        Button buttonMaps=(Button)mView.findViewById(R.id.boton_mapa);
                        Button buttonDog=(Button)mView.findViewById(R.id.boton_layout_dog);

                        buttonMaps.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(getBaseContext(),MapsActivity.class);
                                Coordenadas objeto= new Coordenadas("fragment",lat,lon);
                                i.putExtra("activity",objeto);
                                startActivity(i);
                                System.out.println("lat:"+lat);
                                System.out.println("lat-lon:"+lon);
                            }
                        });

                        buttonDog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(getBaseContext(),InformationDogLost.class);
                                Perro dog = perro;
                                i.putExtra("dog",dog);
                                startActivity(i);
                            }
                        });

                        mBuilder.setView(mView);
                        AlertDialog dialog=mBuilder.create();
                        dialog.show();
                    }
                });
                this.nombrePerro = (TextView) view.findViewById(R.id.textViewNombre);
                this.fotoPerro = (ImageView) view.findViewById(R.id.imageViewDog);
                this.razaPerro = (TextView) view.findViewById(R.id.textViewRaza);
                this.fechaPerro = (TextView) view.findViewById(R.id.textViewFecha);
            }
        }
    }
}
