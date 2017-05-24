package com.example.sergi.signin.Activitys;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sergi.signin.Class.Coordenadas;
import com.example.sergi.signin.Class.Perro;
import com.example.sergi.signin.Class.User;
import com.example.sergi.signin.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NewDogRescueActivity extends AppCompatActivity {
    double lat;
    double lon;
    boolean ubicacionOk=false;
    boolean todoOk=false;
    EditText colorP;
    Spinner spinner;
    Button elegirImagen;
    ImageView imagen;
    TextView textView;
    Button fechaButton;
    Button save;
    EditText description;
    CheckBox descriptionCheckBox;
    final int RC_IMAGE_PICK=1;
    static final int DATE_DIALOG_ID = 999;
    int dia,mes,año;
    public Uri imageUri;
    ArrayAdapter<CharSequence> adapter;
    DatabaseReference mDatabase;
    private StorageReference mStorageRef;
    String color, fecha,raza,descriptionText;
    Coordenadas objeto;
    Button cojerUbicacion;
    TextView ubicacionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dog_rescue);
        cargarVariables();
        cargarUbicacion();
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        elegirImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RC_IMAGE_PICK);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                raza=adapter.getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        descriptionCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (descriptionCheckBox.isChecked()){
                    description.setVisibility(View.VISIBLE);
                }else{
                    description.setVisibility(View.INVISIBLE);
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ubicacionOk){
                    Toast.makeText(NewDogRescueActivity.this, "No has añadido ubicación", Toast.LENGTH_SHORT).show();
                }else{
                    color=colorP.getText().toString();
                    if (descriptionCheckBox.isChecked()){
                        descriptionText=description.getText().toString();
                    }else{
                        descriptionText="";
                    }
                    uploadImage();
                    uploadImageThumb();
                    String key = mDatabase.child("perro").push().getKey();
                    final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    final String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                    final String userEmail =FirebaseAuth.getInstance().getCurrentUser().getEmail();
                    String id = mDatabase.child("perro").push().getKey();
                    User u = new User(userID,userEmail,username);
                    Perro p= new Perro(false,true,raza,fecha,color,u,descriptionText,imageUri.getLastPathSegment(),id,lat,lon);
                    Map<String, Object> childUpdates = new HashMap<>();
                    p.setId(key);
                    childUpdates.put("/perro/" + key, p);
                    childUpdates.put("/user-perro/" + userID+ "/" + key, p);
                    mDatabase.updateChildren(childUpdates);
                    Toast.makeText(getBaseContext(), "Perro Guardado", Toast.LENGTH_LONG).show();

                    Intent i = new Intent(getBaseContext(), DrawerActivity.class);
                    startActivity(i);
                }
            }
        });

        fechaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        cojerUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),MapsActivity.class);
                Coordenadas objeto= new Coordenadas("newDogResque",0.0,0.0);
                intent.putExtra("activity",objeto);
                startActivity(intent);
            }
        });

    }

    private void cargarUbicacion() {
        if (objeto.getActivity().equals("maps")){
            lat=objeto.getLatitud();
            lon=objeto.getLongitud();
            System.out.println("activity new Latitud:"+lat);
            System.out.println("ativity new Longitud:"+lon);
        }

        if (lat==0&& lon==0){
            ubicacionTextView.setText("No has puesto ubicacion");
        }else{
            ubicacionTextView.setText("Ubicación correcta");
            ubicacionOk=true;
        }

    }

    public void uploadImage(){
        if (imageUri!=null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            StorageReference imageRef= mStorageRef.child("Photos").child(imageUri.getLastPathSegment());
            imageRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(NewDogRescueActivity.this, "Upload Done", Toast.LENGTH_SHORT).show();

                        }
                     })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(NewDogRescueActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage(((int)  progress) +"% Uploaded...");
                        }
                    });
            ;
        }else{
            Toast.makeText(this, "No has puesto ninguna Foto", Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadImageThumb(){
        if (imageUri!=null){
            System.out.println("entra");
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            StorageReference imageRef= mStorageRef.child("PhotosTumb").child(imageUri.getLastPathSegment());
            System.out.println("perro "+imageUri.toString());
            Bitmap b = null;
            try {
                b = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("perro "+b.toString());
            Bitmap thumbImage = ThumbnailUtils.extractThumbnail(b, 150, 150);
            System.out.println("perro "+thumbImage.toString());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            thumbImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            imageRef.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(NewDogRescueActivity.this, "Upload Done", Toast.LENGTH_SHORT).show();

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(NewDogRescueActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage((int)  progress +"% Uploaded...");
                        }
                    });
            ;
        }else{
            Toast.makeText(this, "No has puesto ninguna Foto", Toast.LENGTH_SHORT).show();
        }
    }

    private void cargarVariables() {
        ubicacionTextView=(TextView)findViewById(R.id.ubicacionTextViewResque);
        objeto = (Coordenadas)getIntent().getExtras().getSerializable("activity");
        cojerUbicacion=(Button) findViewById(R.id.maps_button_new_dog_resque);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        colorP=(EditText)findViewById(R.id.colorDogRescue);
        spinner=(Spinner)findViewById(R.id.spinner);
        elegirImagen=(Button)findViewById(R.id.takeImage);
        imagen=(ImageView)findViewById(R.id.imagenPerroEncontrado);
        textView=(TextView)findViewById(R.id.fechaTextView2);
        fechaButton=(Button)findViewById(R.id.fechaButton);
        save=(Button)findViewById(R.id.saveDog);
        description=(EditText) findViewById(R.id.description);
        descriptionCheckBox=(CheckBox) findViewById(R.id.descriptionCheckBox);
        adapter = ArrayAdapter.createFromResource(this, R.array.razas, android.R.layout.simple_spinner_item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== RC_IMAGE_PICK){
            if (data!=null){
                imageUri=data.getData();
                imagen.setImageURI(imageUri);
            }
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, datePickerListener,
                        dia,mes,año);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            año = selectedYear;
            mes = selectedMonth;
            dia = selectedDay;
            int m=mes+1;
            fecha= new String(dia+"/"+m+"/"+año);
            textView.setText(new StringBuilder()
                    .append(dia)
                    .append("/")
                    .append(mes+1)
                    .append("/")
                    .append(año));
        }

    };

}
