package com.example.mindbraille.email;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mindbraille.R;
import com.example.mindbraille.models.AuthInfo;

public class EmailRecentOrDate extends AppCompatActivity {

    private AuthInfo userauthInfo;
    Button recents;
    Button bydate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_recent_or_date);

        userauthInfo =(AuthInfo) getIntent().getSerializableExtra("userauthInfo");
        recents = findViewById(R.id.recent);
        bydate = findViewById(R.id.bydate);
        recents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReadMailActivity.class);
                intent.putExtra("userauthInfo",userauthInfo);
                startActivity(intent);
            }
        });
        bydate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReadMailActivity.class);
                intent.putExtra("userauthInfo",userauthInfo);
                startActivity(intent);
            }
        });
    }
}