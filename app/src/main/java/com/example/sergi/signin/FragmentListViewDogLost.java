package com.example.sergi.signin;

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
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Sergi on 04/04/2017.
 */

public class FragmentListViewDogLost extends Fragment {
    View view;
    DatabaseReference mDatabase;
    private List<Perro> listPerro;
    MyTodoRecyclerViewAdapter myTodoRecyclerViewAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.list_view_dogs,container,false);
        cargarDatos();
        cargarPerrosFirebaseInList();
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.ListViewDowgs);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
      //  adapter=new ArrayAdapter<Perro>(view.getContext(),R.layout.layout_dog_lost,listPerro);
        myTodoRecyclerViewAdapter = new MyTodoRecyclerViewAdapter();
      //  listView.setAdapter(myTodoRecyclerViewAdapter);
        mRecyclerView.setAdapter(myTodoRecyclerViewAdapter);
        return view;
    }

    public void cargarPerrosFirebaseInList(){
        mDatabase=FirebaseDatabase.getInstance().getReference();
        FirebaseDatabase.getInstance().getReference("perro").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                Perro perro = dataSnapshot.getValue(Perro.class);
                if (perro.isPerdido()==true){
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

    public void cargarDatos(){
        myTodoRecyclerViewAdapter = new MyTodoRecyclerViewAdapter();
        mDatabase= FirebaseDatabase.getInstance().getReference();
       // listView= (ListView)view.findViewById(R.id.ListViewDowgs);
        listPerro=new ArrayList<>();
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
        public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_dog_lost, null);
            CustomViewHolder viewHolder = new CustomViewHolder(view);
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
