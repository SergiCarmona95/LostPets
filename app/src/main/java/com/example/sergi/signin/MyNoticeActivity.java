package com.example.sergi.signin;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
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
        cargarDatos();
        cargarPerrosFirebaseInList();
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
    }

    private void cargarDatos() {
        myTodoRecyclerViewAdapter = new MyTodoRecyclerViewAdapter();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        listPerro=new ArrayList<>();
        floatingActionButton = (FloatingActionButton)findViewById(R.id.botonBorrar);
        //listView=(ListView)findViewById(R.id.listViewMyNotice);
    }

    public void cargarPerrosFirebaseInList(){
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
    }

    class MyTodoRecyclerViewAdapter extends RecyclerView.Adapter<MyTodoRecyclerViewAdapter.CustomViewHolder>{

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
           /* Uri uri= Uri.parse(perroItem.getImageUri());
            customViewHolder.fotoPerro.setImageURI(uri);*/
            customViewHolder.recompensaPerro.setText(String.valueOf(perroItem.getRecompensa()));
            customViewHolder.fechaPerro.setText(perroItem.getFecha());
            customViewHolder.nombrePropietarioPerro.setText(perroItem.getUser().getUsername());
            customViewHolder.emailPropietarioPerro.setText(String.valueOf(perroItem.getUser().getEmail()));
        }

        @Override
        public int getItemCount() {
            return (null != listPerro ? listPerro.size() : 0);
        }


        class CustomViewHolder extends RecyclerView.ViewHolder {
            protected TextView nombrePerro;
            protected ImageView fotoPerro;
            protected TextView razaPerro;
            protected TextView recompensaPerro;
            protected TextView fechaPerro;
            protected TextView nombrePropietarioPerro;
            protected TextView emailPropietarioPerro;

            public CustomViewHolder(View view) {
                super(view);
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
