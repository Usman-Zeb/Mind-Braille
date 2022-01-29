package com.example.mindbraille.email;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


import com.example.mindbraille.R;
import com.example.mindbraille.email.EmailsAdapter;
import com.example.mindbraille.email.MailDetails;
import com.example.mindbraille.models.AuthInfo;
import com.example.mindbraille.models.MailModel;
import com.github.ybq.android.spinkit.SpinKitView;
import com.microsoft.graph.options.Option;
import com.microsoft.graph.options.QueryOption;
import com.microsoft.graph.requests.extensions.IMessageCollectionPage;

import java.util.ArrayList;
import java.util.LinkedList;

public class ReadMailActivity extends AppCompatActivity implements EmailsAdapter.OnEmailListener{

    SpinKitView loading;
    AuthInfo userauthInfo;
    ArrayList<MailModel> allMails;
    IMessageCollectionPage mailpages;
    private RecyclerView mRecyclerView;
    Button scrolldown;
    Button scrollup;
    Button loadmore;
    private RecyclerView.Adapter mAdapter;
    LinearLayout ll;
    private LinearLayoutManager mLayoutManager;
    int mailCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_mail);
        mailCount = 15;
        allMails = new ArrayList<MailModel>();
        scrolldown = findViewById(R.id.scrolldown);
        scrollup = findViewById(R.id.scrollup);
        loadmore = findViewById(R.id.loadmore);
        loading = findViewById(R.id.loading);
        ll = findViewById(R.id.linearLayout);
        mRecyclerView = findViewById(R.id.email_recycler);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());

        mAdapter = new EmailsAdapter(allMails,this::onContactClick);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getApplicationContext().getResources().getDrawable(R.drawable.divider));
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        userauthInfo =(AuthInfo) getIntent().getSerializableExtra("userauthInfo");
        new getMails().execute("void");
        scrolldown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerView.smoothScrollToPosition(mLayoutManager.findLastVisibleItemPosition() + 1);
            }
        });

        scrollup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mLayoutManager.findFirstVisibleItemPosition()>0){
                mRecyclerView.smoothScrollToPosition(mLayoutManager.findFirstVisibleItemPosition() - 1);}
            }
        });

        loadmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int prevCount = mailCount;
                mailCount = mailCount + 10;
                new getMails().execute("void");
                mRecyclerView.scrollToPosition(prevCount-1);
            }
        });

    }

    IMessageCollectionPage getmails(){
        LinkedList<Option> requestOptions = new LinkedList<Option>();
        //requestOptions.add(new QueryOption("filter", "(receivedDateTime ge 2020-03-13T04:14:08Z) and (receivedDateTime le 2021-04-12T05:15:08Z)"));
        IMessageCollectionPage x = AuthInfo.graphClient.me().messages().buildRequest(requestOptions).top(mailCount).get();
        return x;
    }

    @Override
    public void onContactClick(int position) {
        MailModel emailHTML = allMails.get(position);
        Intent intent = new Intent(getApplicationContext(), MailDetails.class);
        intent.putExtra("obj",emailHTML);
        intent.putExtra("userauthInfo",userauthInfo);
        startActivity(intent);
    }

    public class getMails extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loading.setVisibility(View.VISIBLE);
                    ll.setVisibility(View.GONE);
                }
            });
            mailpages = getmails();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loading.setVisibility(View.GONE);
                    ll.setVisibility(View.VISIBLE);
                }
            });
            allMails.clear();
            Log.d("emails", mailpages.getCurrentPage().get(0).receivedDateTime.getTime().toString());
            //Log.d("emails", mailpages.getCurrentPage().get(1).sender.emailAddress.address);
            //Log.d("emails", String.valueOf(mailpages.getCurrentPage().size()));
            for( int i = 0; i < mailpages.getCurrentPage().size();i++){
                String bodyPrev = mailpages.getCurrentPage().get(i).bodyPreview;
                if(bodyPrev.length()>64){
                bodyPrev = bodyPrev.substring(0,64);
                }
                ArrayList<String> toRecipeints = new ArrayList<String>();
                ArrayList<String> namesto = new ArrayList<String>();
                for( int j = 0 ; j < mailpages.getCurrentPage().get(i).toRecipients.size();j++){
                    toRecipeints.add(mailpages.getCurrentPage().get(i).toRecipients.get(j).emailAddress.address);
                    namesto.add(mailpages.getCurrentPage().get(i).toRecipients.get(j).emailAddress.name);
                }
                ArrayList<String> ccRecipeints = new ArrayList<String>();
                for( int k = 0 ; k < mailpages.getCurrentPage().get(i).ccRecipients.size();k++){
                    ccRecipeints.add(mailpages.getCurrentPage().get(i).ccRecipients.get(k).emailAddress.address);
                }
                MailModel newMail = new MailModel(
                        mailpages.getCurrentPage().get(i).sender.emailAddress.name,
                        mailpages.getCurrentPage().get(i).sender.emailAddress.address,
                        mailpages.getCurrentPage().get(i).subject,
                        mailpages.getCurrentPage().get(i).receivedDateTime.getTime().toString(),
                        bodyPrev,
                        mailpages.getCurrentPage().get(i).body.content
                );
                newMail.setCcRecipients(ccRecipeints);
                newMail.setToRecipients(toRecipeints);
                newMail.setId(mailpages.getCurrentPage().get(i).id);
                allMails.add(newMail);
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((EmailsAdapter) mAdapter).UpdateList(allMails);
                }
            });

        }
    }


}