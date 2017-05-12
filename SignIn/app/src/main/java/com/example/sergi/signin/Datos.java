package com.example.sergi.signin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergi on 02/05/2017.
 */

public class Datos {

    interface EncontradosChangeListener {
        void notifyEncontradosChange();
    }

    interface PerdidosChangeListener {
        void notifyPerdidosChange();
    }

    interface ImageLoadListener {
        void onImageLoad(ImageView i,File f);
    }

    static Context context;
    DatabaseReference mDatabase;
    static ArrayList<Perro> listperrosPerdidos;
    static ArrayList<Perro> listperrosEncontrados;
    static File dirImagenes;
    static private StorageReference mStorageRef;
    static EncontradosChangeListener encontradosChangeListener;
    static PerdidosChangeListener perdidosChangeListener;
    static int contador=0;

    static void setEncontradosChangeListener(EncontradosChangeListener listener){
        encontradosChangeListener = listener;
    }

    static void setPerdidosChangeListener(PerdidosChangeListener listener){
        perdidosChangeListener = listener;
    }

    public Datos(Context context){
        this.context=context;
        listperrosPerdidos=new ArrayList<>();
        listperrosEncontrados=new ArrayList<Perro>();
        dirImagenes= new File(context.getFilesDir().getAbsolutePath()+"/imagenes");
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://signin-2913c.appspot.com");
    }

    public void cargarPerrosPerdidos(){
        mDatabase= FirebaseDatabase.getInstance().getReference();
        FirebaseDatabase.getInstance().getReference("perro").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Perro perro = dataSnapshot.getValue(Perro.class);
                if (perro.isPerdido()==true){
                    listperrosPerdidos.add(perro);
                    if(perdidosChangeListener != null) {
                        perdidosChangeListener.notifyPerdidosChange();
                    }

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

    public void cargarPerrosEncontrados(){
        mDatabase= FirebaseDatabase.getInstance().getReference();
        FirebaseDatabase.getInstance().getReference("perro").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Perro perro = dataSnapshot.getValue(Perro.class);
                if (perro.isEncontrado()==true){
                    listperrosEncontrados.add(perro);
                    //descargarImagenesPerro(perro);
                    if(encontradosChangeListener != null) {
                        encontradosChangeListener.notifyEncontradosChange();
                    }
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

    public static void crearCarpeta(){
        if (!dirImagenes.exists()) {
            Toast.makeText(context, "CREADO LA CARPETA", Toast.LENGTH_SHORT).show();
            dirImagenes.mkdirs();
        }
    }

    public static void descargarImagenesPerro(Perro perro, final ImageView imageView, final ImageLoadListener l){
        String nomIma=perro.getImageUri();
        StorageReference imagRef=mStorageRef.child("Photos/"+nomIma);
        final long ONE_MEGABYTE = 1024 * 1024;

        final File localFile = new  File(dirImagenes.getAbsolutePath() + "/" + nomIma + ".jpg");
        System.out.println(localFile.toString());
        if (!localFile.exists()){
            imagRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    l.onImageLoad(imageView, localFile);
                    //  Toast.makeText(DrawerActivity.this, "Descargada imagen "+localFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            });
        }
    }

    public static Bitmap cambiarTamañoFoto(File f){
        Bitmap originalImage = BitmapFactory.decodeFile(f.getAbsolutePath());
        int width=originalImage.getWidth();
        int height = originalImage.getHeight();
        Matrix matrix = new Matrix();
        int newWidth = 200;
        int newHeight = 200;
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap myBitmap =Bitmap.createBitmap(originalImage, 0, 0, width, height, matrix, true);
        return myBitmap;
    }

}
