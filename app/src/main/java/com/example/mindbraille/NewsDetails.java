package com.example.mindbraille;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.mindbraille.models.AuthInfo;
import com.example.mindbraille.models.MailModel;

public class NewsDetails extends AppCompatActivity {
    Button reply;
    Button back;
    WebView webView;
    TextView sendername;
    TextView sendermail;
    FrameLayout frameLayout;
    TextView datetime;
    NewsModel news;
    AuthInfo userauthInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        news = (NewsModel) getIntent().getSerializableExtra("obj");
        webView = findViewById(R.id.webview);
        webView.loadUrl(news.getURL());
        back = findViewById(R.id.goback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}