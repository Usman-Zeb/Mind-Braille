package com.example.mindbraille.email;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mindbraille.R;
import com.example.mindbraille.models.AuthInfo;

public class MailMode extends AppCompatActivity {

    private AuthInfo userauthInfo;
    Button sendmail;
    Button readmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_mode);
        userauthInfo =(AuthInfo) getIntent().getSerializableExtra("userauthInfo");
        sendmail = findViewById(R.id.sendmail);
        readmail = findViewById(R.id.readmail);
        sendmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SendMailActivity.class);
                intent.putExtra("userauthInfo",userauthInfo);
                startActivity(intent);
            }
        });
        readmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReadMailActivity.class);
                intent.putExtra("userauthInfo",userauthInfo);
                startActivity(intent);
            }
        });
    }



}