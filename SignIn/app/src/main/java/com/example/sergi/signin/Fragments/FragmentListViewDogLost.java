package com.example.sergi.signin.Fragments;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.sergi.signin.Class.Coordenadas;
import com.example.sergi.signin.Class.Datos;
import com.example.sergi.signin.Class.Perro;
import com.example.sergi.signin.Activitys.InformationDogLost;
import com.example.sergi.signin.Activitys.MapsActivity;
import com.example.sergi.signin.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergi on 04/04/2017.
 */

public class FragmentListViewDogLost extends Fragment {
    View view;
    LayoutInflater inflater;
    MyTodoRecyclerViewAdapter myTodoRecyclerViewAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.list_view_dogs,container,false);
        this.inflater=inflater;
        cargarVariables();
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.ListViewDowgs);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        myTodoRecyclerViewAdapter = new FragmentListViewDogLost.MyTodoRecyclerViewAdapter();
        myTodoRecyclerViewAdapter.setList(Datos.listperrosPerdidos);
        Datos.setPerdidosChangeListener(myTodoRecyclerViewAdapter);
        mRecyclerView.setAdapter(myTodoRecyclerViewAdapter);
        return view;
    }



    public void cargarVariables(){
        myTodoRecyclerViewAdapter = new MyTodoRecyclerViewAdapter();
    }

    class MyTodoRecyclerViewAdapter extends RecyclerView.Adapter<MyTodoRecyclerViewAdapter.CustomViewHolder> implements
            Datos.PerdidosChangeListener,
            Datos.ImageThumbLoadListener{

        private List<Perro> listPerro;

        public MyTodoRecyclerViewAdapter() {
            this.listPerro = new ArrayList<>();
        }

        public List<Perro> getList(){
            return listPerro;
        }

        public void setList(List<Perro> listPerro) {
            this.listPerro = listPerro;
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_dog_lost, null);
            CustomViewHolder viewHolder = new CustomViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
            Perro perroItem = listPerro.get(i);
            customViewHolder.perro=perroItem;
            customViewHolder.lat=perroItem.getLat();
            customViewHolder.lon=perroItem.getLon();
            customViewHolder.nombrePerro.setText(perroItem.getNombre());
            customViewHolder.razaPerro.setText(perroItem.getRaza());

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
        public void notifyPerdidosChange() {
            notifyDataSetChanged();
        }

        class CustomViewHolder extends RecyclerView.ViewHolder {
            protected Perro perro;
            protected TextView nombrePerro;
            protected ImageView fotoPerro;
            protected TextView razaPerro;
            protected TextView fechaPerro;
            protected double lat;
            protected double lon;

            public CustomViewHolder(final View view) {
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
                                System.out.println("lat:"+lat);
                                System.out.println("lat-lon:"+lon);
                            }
                        });

                        buttonDog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(getContext(),InformationDogLost.class);
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
