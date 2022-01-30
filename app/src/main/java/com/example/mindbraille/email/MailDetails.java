package com.example.mindbraille.email;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.mindbraille.R;
import com.example.mindbraille.models.AuthInfo;
import com.example.mindbraille.models.EmailModel;
import com.example.mindbraille.models.MailModel;

import org.apache.commons.math3.ml.distance.EarthMoversDistance;

import java.util.ArrayList;

public class MailDetails extends AppCompatActivity {
    Button reply;
    Button back;
    WebView webView;
    TextView sendername;
    TextView sendermail;
    FrameLayout frameLayout;
    TextView datetime;
    MailModel mail;
    AuthInfo userauthInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_details);
        frameLayout = findViewById(R.id.frameLayout);
        userauthInfo =(AuthInfo) getIntent().getSerializableExtra("userauthInfo");
        mail = (MailModel) getIntent().getSerializableExtra("obj");
        sendername = findViewById(R.id.sendername);
        sendermail = findViewById(R.id.sendermail);
        datetime = findViewById(R.id.timestamp);
        reply = findViewById(R.id.reply);
        Log.d("emails",mail.getBody());
        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadData(mail.getBody(), "text/html; charset=utf-8", "UTF-8");
        sendername.setText(mail.getSenderName());
        sendermail.setText(mail.getSenderMail());
        datetime.setText(mail.getRecTime());
        for (int i = 0 ; i < mail.getToRecipients().size();i++){
            Log.d("replyto",mail.getToRecipients().get(i));
        }
        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendMailFragment fragment = new SendMailFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("userauthInfo",userauthInfo);
                bundle.putBoolean("isReply",true);
                EmailModel.recipients = new ArrayList<String>();
                EmailModel.recipients = mail.getToRecipients();
                EmailModel.name = mail.getSenderName();
                EmailModel.ccs = mail.getCcRecipients();
                EmailModel.id = mail.getId();
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).commit();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });
    }
}