package com.example.sergi.signin.Activitys;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sergi.signin.Class.Datos;
import com.example.sergi.signin.Class.Perro;
import com.example.sergi.signin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyNoticeActivity extends AppCompatActivity {
    DatabaseReference mDatabase;
    private List<Perro> listPerro;
    FloatingActionButton floatingActionButton;
    MyTodoRecyclerViewAdapter myTodoRecyclerViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notice);
        cargarVariables();
        String userEmail =FirebaseAuth.getInstance().getCurrentUser().getEmail();
        //cargarPerrosFirebaseInList();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),MyNoticeActivityDelete.class);
                startActivity(i);
            }
        });
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.listViewMyNotice);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        //  adapter=new ArrayAdapter<Perro>(view.getContext(),R.layout.layout_dog_lost,listPerro);
        myTodoRecyclerViewAdapter = new MyTodoRecyclerViewAdapter();
        //  listView.setAdapter(myTodoRecyclerViewAdapter);
        mRecyclerView.setAdapter(myTodoRecyclerViewAdapter);
        for (Perro perro:Datos.listperrosEncontrados) {
            if (perro.getUser().getEmail().equals(userEmail)){
                if (perro.isEncontrado()){perro.setNombre("Perro Perdido");}
                listPerro.add(perro);
                myTodoRecyclerViewAdapter.getList().add(perro);
                myTodoRecyclerViewAdapter.notifyItemInserted(myTodoRecyclerViewAdapter.getItemCount());
            }
        }
        for (Perro perro:Datos.listperrosPerdidos) {
            if (perro.getUser().getEmail().equals(userEmail)){
                if (perro.isEncontrado()){perro.setNombre("Perro Perdido");}
                listPerro.add(perro);
                myTodoRecyclerViewAdapter.getList().add(perro);
                myTodoRecyclerViewAdapter.notifyItemInserted(myTodoRecyclerViewAdapter.getItemCount());
            }
        }
    }

    private void cargarVariables() {
        myTodoRecyclerViewAdapter = new MyTodoRecyclerViewAdapter();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        listPerro=new ArrayList<>();
        floatingActionButton = (FloatingActionButton)findViewById(R.id.botonBorrar);
        //listView=(ListView)findViewById(R.id.listViewMyNotice);
    }

  /*  public void cargarPerrosFirebaseInList(){
        mDatabase=FirebaseDatabase.getInstance().getReference();
        final String userEmail =FirebaseAuth.getInstance().getCurrentUser().getEmail();
        FirebaseDatabase.getInstance().getReference("perro").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                Perro perro = dataSnapshot.getValue(Perro.class);
                if (perro.getUser().getEmail().equals(userEmail)){
                    if (perro.isEncontrado()){perro.setNombre("Perro Perdido");}
                    listPerro.add(perro);
                    myTodoRecyclerViewAdapter.getList().add(perro);
                    //myTodoRecyclerViewAdapter.notifyDataSetChanged();
                    myTodoRecyclerViewAdapter.notifyItemInserted(myTodoRecyclerViewAdapter.getItemCount());
                }else{

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }*/

    class MyTodoRecyclerViewAdapter extends RecyclerView.Adapter<MyTodoRecyclerViewAdapter.CustomViewHolder>
        implements Datos.PerdidosChangeListener,
            Datos.EncontradosChangeListener,
            Datos.ImageThumbLoadListener{

        private List<Perro> listPerro;

        public MyTodoRecyclerViewAdapter() {
            this.listPerro = new ArrayList<>();
        }

        public List<Perro> getList(){
            return listPerro;
        }

        @Override
        public MyTodoRecyclerViewAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_dog_lost, null);
            MyTodoRecyclerViewAdapter.CustomViewHolder viewHolder = new MyTodoRecyclerViewAdapter.CustomViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {

            Perro perroItem = listPerro.get(i);
            customViewHolder.nombrePerro.setText(perroItem.getNombre());
            customViewHolder.razaPerro.setText(perroItem.getRaza());
            String ni = perroItem.getImageUri();
            File f = new File(getBaseContext().getFilesDir().getAbsolutePath()+"/imagenes/"+ni+"Thumb.jpg");
            if (!f.exists()){
                customViewHolder.fotoPerro.setImageBitmap(Datos.cambiarTamañoFoto(f));
            }else {
                customViewHolder.fotoPerro.setImageBitmap(Datos.cambiarTamañoFoto(f));
            }
            customViewHolder.fechaPerro.setText(perroItem.getFecha());
        }

        @Override
        public int getItemCount() {
            return (null != listPerro ? listPerro.size() : 0);
        }

        @Override
        public void notifyPerdidosChange() {
            notifyDataSetChanged();
        }

        @Override
        public void notifyEncontradosChange() {
            notifyDataSetChanged();
        }

        @Override
        public void onImageThumbLoad(ImageView i, File f) {
            i.setImageBitmap(BitmapFactory.decodeFile(f.getAbsolutePath()));
        }


        class CustomViewHolder extends RecyclerView.ViewHolder {
            protected TextView nombrePerro;
            protected ImageView fotoPerro;
            protected TextView razaPerro;
            protected TextView fechaPerro;

            public CustomViewHolder(View view) {
                super(view);
                this.nombrePerro = (TextView) view.findViewById(R.id.textViewNombre);
                this.fotoPerro = (ImageView) view.findViewById(R.id.imageViewDog);
                this.razaPerro = (TextView) view.findViewById(R.id.textViewRaza);
                this.fechaPerro = (TextView) view.findViewById(R.id.textViewFecha);
            }
        }
    }
}
