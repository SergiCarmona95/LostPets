package com.example.sergi.signin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.R.attr.bitmap;

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
            Datos.ImageLoadListener{

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
            customViewHolder.lat=perroItem.getLat();
            customViewHolder.lon=perroItem.getLon();
            customViewHolder.nombrePerro.setText(perroItem.getNombre());
            customViewHolder.razaPerro.setText(perroItem.getRaza());

            String ni = perroItem.getImageUri();
            File f = new File(view.getContext().getFilesDir().getAbsolutePath()+"/imagenes/"+ni+".jpg");
            if (f.exists()){
                customViewHolder.fotoPerro.setImageBitmap(Datos.cambiarTamañoFoto(f));
            }else{
                //customViewHolder.fotoPerro.setImageBitmap(Datos.cambiarTamañoFoto(f));
                Datos.descargarImagenesPerro(perroItem, customViewHolder.fotoPerro, this);

            }
            customViewHolder.recompensaPerro.setText(String.valueOf(perroItem.getRecompensa()));
            customViewHolder.fechaPerro.setText(perroItem.getFecha());
            customViewHolder.nombrePropietarioPerro.setText(perroItem.getUser().getUsername());
            customViewHolder.emailPropietarioPerro.setText(String.valueOf(perroItem.getUser().getEmail()));


        }

        @Override
        public void onImageLoad(ImageView imageView, File imageFile){
            imageView.setImageBitmap(Datos.cambiarTamañoFoto(imageFile));
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
            protected TextView nombrePerro;
            protected ImageView fotoPerro;
            protected TextView razaPerro;
            protected TextView recompensaPerro;
            protected TextView fechaPerro;
            protected TextView nombrePropietarioPerro;
            protected TextView emailPropietarioPerro;
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
                                Toast.makeText(getContext(), "Boton perros", Toast.LENGTH_SHORT).show();
                                System.out.println("lat:"+lat);
                                System.out.println("lat-lon:"+lon);
                            }
                        });

                        mBuilder.setView(mView);
                        AlertDialog dialog=mBuilder.create();
                        dialog.show();

                    }
                });
                this.nombrePerro = (TextView) view.findViewById(R.id.nombreTextViewDogLost);
                this.fotoPerro = (ImageView) view.findViewById(R.id.fotoPerro);
                this.razaPerro = (TextView) view.findViewById(R.id.razaTextViewDogLost);
                this.recompensaPerro = (TextView) view.findViewById(R.id.recompensaTextViewDogLost);
                this.fechaPerro= (TextView) view.findViewById(R.id.fechaTextViewDogLost);
                this.nombrePropietarioPerro = (TextView) view.findViewById(R.id.nombrePropietarioTextViewDogLost);
                this.emailPropietarioPerro= (TextView) view.findViewById(R.id.emailPropietarioTextViewDogLost);
            }
        }
    }
}
