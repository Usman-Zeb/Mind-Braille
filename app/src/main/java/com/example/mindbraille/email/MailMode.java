package com.example.mindbraille.email;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.example.mindbraille.R;
import com.example.mindbraille.globals.GlobalClass;
import com.example.mindbraille.models.AuthInfo;
import com.example.mindbraille.sms.New_SMS_Number;
import com.example.mindbraille.sms.Recent_SMS;

public class MailMode extends AppCompatActivity {

    final Handler handler = new Handler();
    Thread thread;
    int selector =0;
    boolean running = true;

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

    @Override
    protected void onResume() {
        super.onResume();

        running=true;
        thread = new Thread(new Runnable() {
            public void run() {

                while (running) {
                    try {
                        Thread.sleep(1000);
                        handler.post(updateRunner);
                    }
                    catch (InterruptedException e)
                    {
                        running=false;
                    }

                }
            }
        });

        thread.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        thread.interrupt();
    }


    final Runnable updateRunner = new Runnable() {

        public void run() {

            final Button[] kb_buttons = {
                    findViewById(R.id.sendmail),
                    findViewById(R.id.readmail),
            };

            if(selector>1)
            {selector=0;}

            for(Button i: kb_buttons)
            {
                i.setEnabled(false);
            }

            kb_buttons[selector].setEnabled(true);

            if(((GlobalClass) getApplication()).getBlinked() && (((GlobalClass) getApplication()).getBlinkValue()>70))
            {
                Intent intent;
                switch (kb_buttons[selector].getId())
                {
                    case R.id.sendmail:
                        intent = new Intent(getApplicationContext(), ReadMailActivity.class);
                        intent.putExtra("userauthInfo",userauthInfo);
                        startActivity(intent);
                        break;
                    case R.id.readmail:
                       intent = new Intent(getApplicationContext(), SendMailActivity.class);
                        intent.putExtra("userauthInfo",userauthInfo);
                        startActivity(intent);
                        break;

                }
            }

            selector++;
        }
    };
}