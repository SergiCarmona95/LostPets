package com.example.sergi.signin;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class NewNoticeActivity extends AppCompatActivity{
    Button cargarImagen;
    ImageView imageView;
    Button save;
    Button elegirFecha;
    EditText nomEditText;
    EditText recompensaEditText;
    EditText colorEditText;
    EditText otherRT;
    TextView fechaTextView;
    CheckBox otherR;
    CheckBox recompensaCheck;
    RadioButton chipSi;
    RadioButton chipNo;
    RadioButton cogerSi;
    RadioButton cogerNo;
    Spinner razas;
    int dia,mes,año;
    static String day;
    boolean todoOk=false;
    boolean ok=false;
    public Uri imageUri;
    public String uriImage;
    String nombre,colorPerro,chip,coger,raza;
    int recompensa;
    final int RC_IMAGE_PICK=1;
    static final int DATE_DIALOG_ID = 999;
    ArrayAdapter<CharSequence> adapter;
    DatabaseReference mDatabase;
    private FirebaseStorage fStorage;
    private StorageReference mStorageRef;
    public static final String Storage_Path="image/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_notice);
        cargarVariables();

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        razas.setAdapter(adapter);

       cargarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RC_IMAGE_PICK);
            }
        });

        recompensaCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recompensaCheck.isChecked()){
                    recompensaEditText.setVisibility(View.VISIBLE);
                }else{
                    recompensaEditText.setVisibility(View.INVISIBLE);
                }
            }
        });


        otherR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otherR.isChecked()){
                    otherRT.setVisibility(View.VISIBLE);
                }else{
                    otherRT.setVisibility(View.INVISIBLE);
                }
            }
        });

        elegirFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });


       razas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              raza=adapter.getItem(position).toString();
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombre=nomEditText.getText().toString();
                colorPerro=colorEditText.getText().toString();
                if (chipSi.isChecked()){
                   chip="yes";
                }else if (chipNo.isChecked()){
                    chip="no";
                }

                if (!recompensaCheck.isChecked()){
                    recompensa=0;
                }else{
                    recompensa=Integer.parseInt(recompensaEditText.getText().toString());
                }

                if (otherR.isChecked()){
                    raza=otherRT.getText().toString();
                }

                if (cogerSi.isChecked()){
                    coger="yes";
                }else if (cogerNo.isChecked()){
                    coger="no";
                }

                comprobarDatos();
                if (todoOk==true) {
                    writeNewDog();
                }
            }
        });


    }


    public void comprobarDatos(){
        if (nombre.equals("")){
            Toast.makeText(this, "No has puesto nombre", Toast.LENGTH_SHORT).show();
        }else if (colorPerro.equals("")){
            Toast.makeText(this, "No has puesto color del perro", Toast.LENGTH_SHORT).show();
        }else if (!cogerSi.isChecked() && !cogerNo.isChecked()){
            Toast.makeText(this, "No has elegido si es amistoso o no", Toast.LENGTH_SHORT).show();
        }else if(!chipSi.isChecked() && !chipNo.isChecked()){
            Toast.makeText(this, "No has elegido si tiene chip o no", Toast.LENGTH_SHORT).show();
        }else{
            todoOk=true;
        }
    }

    private void writeNewDog(){

        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        final String userEmail =FirebaseAuth.getInstance().getCurrentUser().getEmail();

        String key = mDatabase.child("perro").push().getKey();
        System.out.println("a"+key);

        uploadImage();
        User u = new User(userId,userEmail,username);
        Perro p =new Perro(nombre,imageUri.getLastPathSegment(),key,colorPerro,coger,chip,recompensa,raza,false,true,u,day);
        System.out.println(p.toString());
        if (todoOk==true) {
            Map<String, Object> childUpdates = new HashMap<>();
            p.setId(key);
            childUpdates.put("/perro/" + key, p);
            //childUpdates.put("/user-perro/" + u + "/" + key, p);
            childUpdates.put("/user-perro/" + userId+ "/" + key, p);
            mDatabase.updateChildren(childUpdates);
            Toast.makeText(getBaseContext(), "Perro Guardado", Toast.LENGTH_LONG).show();

            Intent i = new Intent(this, DrawerActivity.class);
            startActivity(i);
        }
    }

    public void uploadImage(){
        if (imageUri!=null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            StorageReference imageRef= mStorageRef.child("Photos").child(imageUri.getLastPathSegment());
            System.out.println();

            imageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(NewNoticeActivity.this, "Upload Done", Toast.LENGTH_SHORT).show();
                    
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println("ERROR="+e.toString());
                            Toast.makeText(NewNoticeActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
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
        fechaTextView=(TextView)findViewById(R.id.fechaTextView);
        elegirFecha=(Button) findViewById(R.id.ponerFecha);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        fStorage= FirebaseStorage.getInstance();
        recompensaCheck=(CheckBox) findViewById(R.id.recompensaCheck);
        mDatabase= FirebaseDatabase.getInstance().getReference();
        cargarImagen= (Button) findViewById(R.id.image_button);
        imageView= (ImageView) findViewById(R.id.imageView2);
        otherR=(CheckBox) findViewById(R.id.otraRazaCheck);
        otherRT=(EditText) findViewById(R.id.otraRaza);
        save=(Button)findViewById(R.id.Save);
        nomEditText= (EditText)findViewById(R.id.nameDog);
        recompensaEditText= (EditText)findViewById(R.id.reward);
        colorEditText= (EditText)findViewById(R.id.colorDog);
        chipSi= (RadioButton)findViewById(R.id.chipSi);
        chipNo= (RadioButton)findViewById(R.id.chipNo);
        cogerSi= (RadioButton)findViewById(R.id.takeYes);
        cogerNo= (RadioButton)findViewById(R.id.takeNo);
        razas= (Spinner)findViewById(R.id.raza);
        adapter = ArrayAdapter.createFromResource(this, R.array.razas, android.R.layout.simple_spinner_item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== RC_IMAGE_PICK){
            if (data!=null){
                imageUri=data.getData();
                System.out.println(imageUri.toString());
                imageView.setImageURI(imageUri);
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
            day= new String(dia+"/"+m+"/"+año);
            fechaTextView.setText(new StringBuilder()
                    .append(dia)
                    .append("/")
                    .append(mes+1)
                    .append("/")
                    .append(año));
        }

    };



}
