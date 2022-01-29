package com.example.mindbraille.testing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;

import com.example.mindbraille.R;

import java.util.ArrayList;

public class TestInterface extends AppCompatActivity {
    Button buttons[] = new Button[3];
    Button b1;
    Button b2;
    Button b3;
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_interface);
        b1 = findViewById(R.id.button1);
        b2 = findViewById(R.id.button2);
        b3 = findViewById(R.id.button3);
        buttons[0] = b1;
        buttons[1] = b2;
        buttons[2] = b3;
        Handler handler = new Handler();
        Runnable run = new Runnable() {
            @Override
            public void run() {
                for(int i = 0 ; i < 3 ; i++){
                    buttons[i].setEnabled(true);
                    if(i == count%3){
                        buttons[i].setEnabled(false);
                    }
                }
                count += 1;
                handler.postDelayed(this,2000);
            }
        };
        handler.post(run);

    }
}