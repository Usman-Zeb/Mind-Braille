package com.example.mindbraille.sms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mindbraille.R;
import com.example.mindbraille.RecentSMSAdapter;
import com.example.mindbraille.globals.GlobalClass;

import java.util.HashMap;
import java.util.Map;

public class Recent_SMS extends AppCompatActivity {
    final Handler handler = new Handler();
    Thread thread;
    int selector =0;
    boolean running = true;

    private static final int MY_PERMISSIONS_REQUEST_RECEIVER_SMS = 0;
    String Data="";
    RecyclerView programminglist;
    LinearLayoutManager layoutmanager;
    Button downscrollbtn;
    Button upscrollbtn;
    Button backbtn;
    RecentSMSAdapter recentSMSAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent__s_m_s);
        layoutmanager = new LinearLayoutManager(this);
        downscrollbtn = findViewById(R.id.recent_sms_navigation_down_btn);
        upscrollbtn = findViewById(R.id.recent_sms_navigation_up_btn);
        backbtn = findViewById(R.id.recent_sms_navigation_back_btn);
        receiveSMS();

        downscrollbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (layoutmanager.findLastCompletelyVisibleItemPosition() < (recentSMSAdapter.getItemCount() - 1)) {
                    layoutmanager.scrollToPosition(layoutmanager.findLastCompletelyVisibleItemPosition() + 1);
                }
            }
        });

        upscrollbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (layoutmanager.findFirstCompletelyVisibleItemPosition() > 0) {
                    layoutmanager.smoothScrollToPosition(programminglist, null, layoutmanager.findFirstVisibleItemPosition() -1);
                }
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    void receiveSMS()
    {
        /*if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, 1);

            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS))
            {
                //User denied permission
            }
            else
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, 1);
            }
        }
        else
        {
            ContentResolver contentResolver = getContentResolver();
            Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"),
                    null,null,null,null);
            int indexBody = smsInboxCursor.getColumnIndex("body");
            int indexAddress = smsInboxCursor.getColumnIndex("address");

            if(indexBody<0 || !smsInboxCursor.moveToFirst()) return;

            for(int i=0;i<5;i++)
            {
                Data = smsInboxCursor.getString(indexAddress) + "\n";
                Data += smsInboxCursor.getString(indexBody);
                Log.d("SMS From",Data);
                smsInboxCursor.moveToNext();
                Data="";
            }
        }*/
        Map<String, String> data = new HashMap<>();
        programminglist = (RecyclerView) findViewById(R.id.recent_sms_recyclerview);
        programminglist.setLayoutManager(layoutmanager);


        ContentResolver contentResolver = getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"),
                null,null,null,null);
        int indexBody = smsInboxCursor.getColumnIndex("body");
        int indexAddress = smsInboxCursor.getColumnIndex("address");

        if(indexBody<0 || !smsInboxCursor.moveToFirst()) return;

        for(int i=0;i<smsInboxCursor.getCount();i++) {

            Data = smsInboxCursor.getString(indexAddress) + "\n";
            Data += smsInboxCursor.getString(indexBody);
            Log.d("SMS From", Data);
            data.put(smsInboxCursor.getString(indexAddress), smsInboxCursor.getString(indexBody));
            smsInboxCursor.moveToNext();
            Data = "";
        }
        recentSMSAdapter = new RecentSMSAdapter(data);
        programminglist.setAdapter(recentSMSAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode)
        {
            case MY_PERMISSIONS_REQUEST_RECEIVER_SMS:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this, "Thank you for letting permission", Toast.LENGTH_SHORT).show();
                }
                break;
        }
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
                    findViewById(R.id.recent_sms_navigation_back_btn),
                    findViewById(R.id.recent_sms_navigation_down_btn),
                    findViewById(R.id.recent_sms_navigation_up_btn)
            };

            if(selector>2)
            {selector=0;}

            for(Button i: kb_buttons)
            {
                i.setEnabled(false);
            }

            kb_buttons[selector].setEnabled(true);

            if(((GlobalClass) getApplication()).getBlinked() && (((GlobalClass) getApplication()).getBlinkValue()>50))
            {
                switch (kb_buttons[selector].getId())
                {
                    case R.id.recent_sms_navigation_back_btn:
                        if (layoutmanager.findFirstCompletelyVisibleItemPosition() > 0) {
                            layoutmanager.smoothScrollToPosition(programminglist, null, layoutmanager.findFirstVisibleItemPosition() -1);
                        }
                        break;

                    case R.id.recent_sms_navigation_down_btn:
                        finish();

                        break;

                    case R.id.recent_sms_navigation_up_btn:
                        if (layoutmanager.findLastCompletelyVisibleItemPosition() < (recentSMSAdapter.getItemCount() - 1)) {
                            layoutmanager.scrollToPosition(layoutmanager.findLastCompletelyVisibleItemPosition() + 1);
                        }

                        break;


                }

                ((GlobalClass)getApplication()).setBlinked(false);
            }

            selector++;
        }
    };
}