package com.example.mindbraille.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.mindbraille.R;
import com.example.mindbraille.activities.LoginActivity;

public class SplashScreen extends AppCompatActivity {
    boolean running = true;
    final Handler handler = new Handler();
    Thread thread = new Thread()
    {
        public void run()
        {
            try{
                sleep(4000);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally {
                Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();

            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        thread.start();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}