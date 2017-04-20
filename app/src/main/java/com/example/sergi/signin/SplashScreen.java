package com.example.sergi.signin;

import android.content.Intent;
import android.graphics.Movie;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.felipecsl.gifimageview.library.GifImageView;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class SplashScreen extends AppCompatActivity {
    private GifImageView gifGalgo;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

       // gifGalgo = (GifImageView) findViewById(R.id.gifGalgo);
        progressBar = (ProgressBar) findViewById(R.id.progresBar);
        progressBar.setVisibility(progressBar.VISIBLE);;

        /*try {
            InputStream inputStream = getAssets().open("galgogif.gif");
            byte[] bytes = IOUtils.toByteArray(inputStream);
            gifGalgo.setBytes(bytes);
            gifGalgo.startAnimation();

        }catch (IOException e) {
            e.printStackTrace();
        }*/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SplashScreen.this.startActivity(new Intent(SplashScreen.this, SignInActivity.class));
                SplashScreen.this.finish();
            }
        },1000);
    }


}
