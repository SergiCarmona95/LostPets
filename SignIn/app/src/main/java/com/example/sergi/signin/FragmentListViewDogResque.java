package com.example.sergi.signin;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergi on 05/04/2017.
 */

public class FragmentListViewDogResque extends Fragment {
    View view;
    MyTodoRecyclerViewAdapter myTodoRecyclerViewAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.list_view_dogs,container,false);
        cargarVariables();
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

    class MyTodoRecyclerViewAdapter extends RecyclerView.Adapter<MyTodoRecyclerViewAdapter.CustomViewHolder> implements Datos.EncontradosChangeListener{

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
            customViewHolder.colorPerro.setText(perroItem.getColor());
            customViewHolder.razaPerro.setText(perroItem.getRaza());
            String ni = perroItem.getImageUri();
            File f = new File(view.getContext().getFilesDir().getAbsolutePath()+"/imagenes/"+ni+".jpg");
            if (f.exists()){
                customViewHolder.fotoPerro.setImageBitmap(Datos.cambiarTamañoFoto(f));
            }
            customViewHolder.descripcionPerro.setText(String.valueOf(perroItem.getDescripcion()));
            customViewHolder.fechaPerro.setText(perroItem.getFecha());
            customViewHolder.nombrePropietarioPerro.setText(perroItem.getUser().getUsername());
            customViewHolder.emailPropietarioPerro.setText(String.valueOf(perroItem.getUser().getEmail()));
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
            protected TextView colorPerro;
            protected ImageView fotoPerro;
            protected TextView razaPerro;
            protected TextView descripcionPerro;
            protected TextView fechaPerro;
            protected TextView nombrePropietarioPerro;
            protected TextView emailPropietarioPerro;

            public CustomViewHolder(View view) {
                super(view);
                this.colorPerro = (TextView) view.findViewById(R.id.colorTextViewDogResque);
                this.fotoPerro = (ImageView) view.findViewById(R.id.fotoPerroResque);
                this.razaPerro = (TextView) view.findViewById(R.id.razaTextViewDogResque);
                this.descripcionPerro = (TextView) view.findViewById(R.id.descripcionTextViewDogResque);
                this.fechaPerro= (TextView) view.findViewById(R.id.fechaTextViewDogResque);
                this.nombrePropietarioPerro = (TextView) view.findViewById(R.id.nombrePropietarioTextViewDogResque);
                this.emailPropietarioPerro= (TextView) view.findViewById(R.id.emailPropietarioTextViewDogResque);
            }
        }
    }
}