package com.example.mindbraille.email;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.mindbraille.R;
import com.example.mindbraille.models.AuthInfo;
import com.microsoft.graph.authentication.IAuthenticationProvider;
import com.microsoft.graph.http.IHttpRequest;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.requests.extensions.GraphServiceClient;

public class SendMailActivity extends AppCompatActivity {


    AuthInfo userauthInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mail);
        userauthInfo =(AuthInfo) getIntent().getSerializableExtra("userauthInfo");


        AddressFragment fragment = new AddressFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("userauthInfo",userauthInfo);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_cont,fragment).commit();



    }
}