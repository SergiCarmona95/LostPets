package com.example.sergi.signin;

import android.content.Intent;
import android.graphics.Movie;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.felipecsl.gifimageview.library.GifImageView;
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

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class SplashScreen extends AppCompatActivity {
    private ProgressBar progressBar;
    private ArrayList<Perro> perros;
    DatabaseReference mDatabase;
    static File dirImagenes;
    static private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        cargarVariables();
        crearCarpeta();
        cargarTodosLosPerros();

        progressBar = (ProgressBar) findViewById(R.id.progresBar);
        progressBar.setVisibility(progressBar.VISIBLE);;


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               //SplashScreen.this.startActivity(new Intent(SplashScreen.this, SignInActivity.class));
                SplashScreen.this.startActivity(new Intent(SplashScreen.this, SignInActivity.class));
                SplashScreen.this.finish();
            }
        },1000);
    }

    public void descargarImagenes(Perro p){
        final String nomIma=p.getImageUri();
        StorageReference imagRef=mStorageRef.child("Photos/"+nomIma);
        final long ONE_MEGABYTE = 1024 * 1024;

        final File localFile = new  File(dirImagenes.getAbsolutePath() + "/" + nomIma + ".jpg");
        System.out.println("Imagen "+nomIma);
        if (!localFile.exists()){
            imagRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    System.out.println("Imagen "+nomIma+" Descargada");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            });
        }
    }

    public static void crearCarpeta(){
        if (!dirImagenes.exists()) {
            System.out.println("Creada la carpeta");
            dirImagenes.mkdirs();
        }
    }

    public void cargarTodosLosPerros(){
        System.out.println("A");
        mDatabase= FirebaseDatabase.getInstance().getReference();
        FirebaseDatabase.getInstance().getReference("perro").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Perro perro = dataSnapshot.getValue(Perro.class);
                System.out.println(perro.getNombre());
                perros.add(perro);
                //descargarImagenes(perro);
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


    public void cargarVariables(){
        perros= new ArrayList<Perro>();
        dirImagenes= new File(getBaseContext().getFilesDir().getAbsolutePath()+"/imagenes");
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://signin-2913c.appspot.com");
    }
}
