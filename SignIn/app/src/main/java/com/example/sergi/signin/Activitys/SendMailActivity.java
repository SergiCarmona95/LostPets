package com.example.sergi.signin.Activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.sergi.signin.Class.SendMail;
import com.example.sergi.signin.R;
import com.google.firebase.auth.FirebaseAuth;

public class SendMailActivity extends AppCompatActivity implements View.OnClickListener {

    private String raza;
    private String emailUsuario;
    private String nombrePerro;
    private String activity;
    private EditText editTextEmail;
    private EditText editTextSubject;
    private EditText editTextMessage;
    private Button buttonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mail);
        cargarVariables();

        if (activity.equals("informationDogLost")){
            nombrePerro = (String)getIntent().getExtras().getSerializable("nombrePerro");
            editTextEmail.setText(emailUsuario);
            editTextEmail.setEnabled(false);
            String emailPropio=FirebaseAuth.getInstance().getCurrentUser().getEmail();
            editTextSubject.setText("He encontrado/visto a tu perro "+nombrePerro+ " contacta conmigo en:"+emailPropio);
            editTextSubject.setEnabled(false);

        }else{
            raza= (String)getIntent().getExtras().getSerializable("raza");
            editTextEmail.setText(emailUsuario);
            editTextEmail.setEnabled(false);
            String emailPropio=FirebaseAuth.getInstance().getCurrentUser().getEmail();
            editTextSubject.setText("Tienes a mi perro de raza "+raza+". Contacta conmigo en "+emailPropio);
            editTextSubject.setEnabled(false);
        }

        //Adding click listener
        buttonSend.setOnClickListener(SendMailActivity.this);
    }

    private void cargarVariables() {
        emailUsuario = (String)getIntent().getExtras().getSerializable("emailUsuario");
        activity = (String)getIntent().getExtras().getSerializable("activity");
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextSubject = (EditText) findViewById(R.id.editTextSubject);
        editTextMessage = (EditText) findViewById(R.id.editTextMessage);
        buttonSend = (Button) findViewById(R.id.buttonSend);




    }


    private void sendEmail() {
        //Getting content for email
        String email = editTextEmail.getText().toString().trim();
        String subject = editTextSubject.getText().toString().trim();
        String message = editTextMessage.getText().toString().trim();

        //Creating SendMail object
        SendMail sm = new SendMail(this, email, subject, message);

        //Executing sendmail to send email
        sm.execute();
    }

    @Override
    public void onClick(View v) {
        sendEmail();
    }
}