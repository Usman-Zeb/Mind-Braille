package com.example.mindbraille.sms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.mindbraille.R;

public class Recent_SMS extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_RECEIVER_SMS = 0;
    String Data="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent__s_m_s);
        receiveSMS();
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
        ContentResolver contentResolver = getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"),
                null,null,null,null);
        int indexBody = smsInboxCursor.getColumnIndex("body");
        int indexAddress = smsInboxCursor.getColumnIndex("address");

        if(indexBody<0 || !smsInboxCursor.moveToFirst()) return;

        for(int i=0;i<5;i++) {
            Data = smsInboxCursor.getString(indexAddress) + "\n";
            Data += smsInboxCursor.getString(indexBody);
            Log.d("SMS From", Data);
            smsInboxCursor.moveToNext();
            Data = "";
        }
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
}