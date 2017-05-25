package com.example.sergi.signin.Fragments;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sergi.signin.Activitys.InformationDogLost;
import com.example.sergi.signin.Activitys.InformationDogResque;
import com.example.sergi.signin.Class.Coordenadas;
import com.example.sergi.signin.Class.Datos;
import com.example.sergi.signin.Class.Perro;
import com.example.sergi.signin.Activitys.MapsActivity;
import com.example.sergi.signin.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergi on 05/04/2017.
 */

public class FragmentListViewDogResque extends Fragment {
    View view;
    MyTodoRecyclerViewAdapter myTodoRecyclerViewAdapter;
    LayoutInflater inflater;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.list_view_dogs,container,false);
        cargarVariables();
        this.inflater=inflater;
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.ListViewDowgs);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        myTodoRecyclerViewAdapter = new MyTodoRecyclerViewAdapter();
        myTodoRecyclerViewAdapter.setList(Datos.listperrosEncontrados);
        Datos.setEncontradosChangeListener(myTodoRecyclerViewAdapter);
        mRecyclerView.setAdapter(myTodoRecyclerViewAdapter);
        return view;
    }

    public void cargarVariables(){
        myTodoRecyclerViewAdapter = new MyTodoRecyclerViewAdapter();
    }

    class MyTodoRecyclerViewAdapter extends RecyclerView.Adapter<MyTodoRecyclerViewAdapter.CustomViewHolder> implements
            Datos.EncontradosChangeListener,
            Datos.ImageThumbLoadListener{

        private List<Perro> listPerro;

        public MyTodoRecyclerViewAdapter() {

            this.listPerro = new ArrayList<>();
        }

        public List<Perro> getList(){
            return listPerro;
        }

        public void setList(List<Perro> listPerro){
            this.listPerro=listPerro;
        }

        @Override
        public MyTodoRecyclerViewAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_dog_resque, null);
            MyTodoRecyclerViewAdapter.CustomViewHolder viewHolder = new MyTodoRecyclerViewAdapter.CustomViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyTodoRecyclerViewAdapter.CustomViewHolder customViewHolder, int i) {
            Perro perroItem = listPerro.get(i);
            customViewHolder.perro=perroItem;
            customViewHolder.razaPerro.setText(perroItem.getRaza());
            customViewHolder.lat=perroItem.getLat();
            customViewHolder.lon=perroItem.getLon();
            String ni = perroItem.getImageUri();
            File f = new File(view.getContext().getFilesDir().getAbsolutePath()+"/imagenes/"+ni+"Thumb.jpg");
            if (f.exists()){
                customViewHolder.fotoPerro.setImageBitmap(BitmapFactory.decodeFile(f.getAbsolutePath()));
            }else{
                Datos.descargarImagenesThumbPerro(perroItem, customViewHolder.fotoPerro, this);

            }
            customViewHolder.fechaPerro.setText(perroItem.getFecha());
        }

        @Override
        public void onImageThumbLoad(ImageView imageView, File imageFile){
            imageView.setImageBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));
        }



        @Override
        public int getItemCount() {
            return (null != listPerro ? listPerro.size() : 0);
        }

        @Override
        public void notifyEncontradosChange() {
            notifyDataSetChanged();
        }

        class CustomViewHolder extends RecyclerView.ViewHolder {
            protected ImageView fotoPerro;
            protected TextView razaPerro;
            protected TextView fechaPerro;
            protected Perro perro;
            protected double lat;
            protected double lon;

            public CustomViewHolder(View view) {
                super(view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                        View mView=inflater.inflate(R.layout.dialog_dog_buttons,null);
                        Button buttonMaps=(Button)mView.findViewById(R.id.boton_mapa);
                        Button buttonDog=(Button)mView.findViewById(R.id.boton_layout_dog);

                        buttonMaps.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(getContext(),MapsActivity.class);
                                Coordenadas objeto= new Coordenadas("fragment",lat,lon);
                                i.putExtra("activity",objeto);
                                startActivity(i);
                                Toast.makeText(getContext(), "Boton maps", Toast.LENGTH_SHORT).show();
                                System.out.println("lat:"+lat);
                                System.out.println("lat-lon:"+lon);
                            }
                        });

                        buttonDog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(getContext(),InformationDogResque.class);
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
                this.fotoPerro = (ImageView) view.findViewById(R.id.fotoPerroResque);
                this.razaPerro = (TextView) view.findViewById(R.id.razaTextViewDogResque);
                this.fechaPerro= (TextView) view.findViewById(R.id.fechaTextViewDogResque);
            }
        }
    }
}