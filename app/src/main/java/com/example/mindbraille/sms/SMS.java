package com.example.mindbraille.sms;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.example.mindbraille.R;
import com.example.mindbraille.globals.GlobalClass;

public class SMS extends AppCompatActivity {

    final Handler handler = new Handler();
    Thread thread;
    int selector =0;
    boolean running = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_m_s);
        /*
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.mindbraillekb,null);
        RelativeLayout smsrl = findViewById(R.id.sms_rl);
       RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
       params.addRule(RelativeLayout.BELOW, R.id.belowthis);
       view.setLayoutParams(params);
       smsrl.addView(view);*/

        ActivityCompat.requestPermissions(SMS.this, new String[]{Manifest.permission.READ_SMS}, 1);
        ActivityCompat.requestPermissions(SMS.this, new String[]{Manifest.permission.RECEIVE_SMS}, 1);
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
                    findViewById(R.id.recent_sms_btn),
                    findViewById(R.id.new_sms_btn),
                    findViewById(R.id.sms_main_back_btn)
            };

            if(selector>2)
            {selector=0;}

            for(Button i: kb_buttons)
            {
                i.setEnabled(false);
            }

            kb_buttons[selector].setEnabled(true);

            if(((GlobalClass) getApplication()).getBlinked() && (((GlobalClass) getApplication()).getBlinkValue()>70))
            {
                switch (kb_buttons[selector].getId())
                {
                    case R.id.recent_sms_btn:
                        finish();
                        break;
                    case R.id.new_sms_btn:
                        Intent recent_sms_intent = new Intent(getApplicationContext(), Recent_SMS.class);
                        startActivity(recent_sms_intent);
                        break;
                    case R.id.sms_main_back_btn:
                        Intent new_sms_intent = new Intent(getApplicationContext(), New_SMS_Number.class);
                        startActivity(new_sms_intent);
                        break;
                }
            }

            selector++;
        }
    };


    public void selectoption(View view)
    {
        switch (view.getId())
        {
            case R.id.new_sms_btn:
                Intent new_sms_intent = new Intent(getApplicationContext(), New_SMS_Number.class);
                startActivity(new_sms_intent);
                break;
            case R.id.recent_sms_btn:
                Intent recent_sms_intent = new Intent(getApplicationContext(), Recent_SMS.class);
                startActivity(recent_sms_intent);
                break;
            case R.id.sms_main_back_btn:
                finish();
                break;
        }
    }
}