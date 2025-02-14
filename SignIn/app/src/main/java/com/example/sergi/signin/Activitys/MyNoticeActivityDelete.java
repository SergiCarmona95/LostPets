package com.example.sergi.signin.Activitys;

import android.content.Intent;
import android.graphics.BitmapFactory;
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
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Collections;
import java.util.List;

public class MyNoticeActivityDelete extends AppCompatActivity {
    DatabaseReference mDatabase;
    private List<Perro> listPerro;
    FloatingActionButton floatingActionButton;
    MyTodoRecyclerViewAdapter myTodoRecyclerViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notice_delete);
        cargarVariables();
       // cargarPerrosFirebaseInList();
        String userEmail =FirebaseAuth.getInstance().getCurrentUser().getEmail();

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.listViewMyNotice);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        //  adapter=new ArrayAdapter<Perro>(view.getContext(),R.layout.layout_dog_lost,listPerro);
        myTodoRecyclerViewAdapter = new MyTodoRecyclerViewAdapter();
        //  listView.setAdapter(myTodoRecyclerViewAdapter);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),DrawerActivity.class);
                startActivity(i);
            }
        });
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

        mRecyclerView.setAdapter(myTodoRecyclerViewAdapter);

        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(myTodoRecyclerViewAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void cargarVariables() {
        myTodoRecyclerViewAdapter = new MyTodoRecyclerViewAdapter();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        listPerro=new ArrayList<>();
        floatingActionButton = (FloatingActionButton)findViewById(R.id.botonVolver);
    }

    /*public void cargarPerrosFirebaseInList(){
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
            ItemTouchHelperAdapter,
            Datos.ImageThumbLoadListener{

        private List<Perro> listPerro;

        public List<Perro> getListPerro() {
            return listPerro;
        }

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
            if (f.exists()){
                customViewHolder.fotoPerro.setImageBitmap(BitmapFactory.decodeFile(f.getAbsolutePath()));
            }else{
                Datos.descargarImagenesThumbPerro(perroItem, customViewHolder.fotoPerro, this);

            }
            customViewHolder.fechaPerro.setText(perroItem.getFecha());
        }

        @Override
        public int getItemCount() {
            return (null != listPerro ? listPerro.size() : 0);
        }

        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            Collections.swap(listPerro, fromPosition, toPosition);
            notifyItemMoved(fromPosition, toPosition);
            Toast.makeText(MyNoticeActivityDelete.this, "Dog DELETED", Toast.LENGTH_SHORT).show();
            return true;
        }



        @Override
        public void onItemDismiss(int position) {
            mDatabase=FirebaseDatabase.getInstance().getReference();
            Perro p=listPerro.get(position);
            String key= p.getId();
            System.out.println(key);
            mDatabase.child("perro").child(key).removeValue();
            listPerro.remove(position);
            Toast.makeText(MyNoticeActivityDelete.this, "Perro Eliminado", Toast.LENGTH_SHORT).show();
            notifyItemRemoved(position);
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
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MyNoticeActivityDelete.this, "Has seleccionado un perro", Toast.LENGTH_SHORT).show();
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

interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}

class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter mAdapter;

    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }
}