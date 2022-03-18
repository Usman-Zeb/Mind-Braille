package com.example.mindbraille;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mindbraille.email.EmailsAdapter;
import com.example.mindbraille.email.MailDetails;
import com.example.mindbraille.email.ReadMailActivity;
import com.example.mindbraille.globals.GlobalClass;
import com.example.mindbraille.models.MailModel;
import com.example.mindbraille.sms.New_SMS_Number;
import com.example.mindbraille.sms.Recent_SMS;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewsView extends AppCompatActivity implements NewsAdapter.OnNewsListener {

    SpinKitView loading;
    private RecyclerView mRecyclerView;
    ArrayList<NewsModel> allNews;
    Button scrolldown;
    Button scrollup;
    private RecyclerView.Adapter mAdapter;
    LinearLayout ll;
    private LinearLayoutManager mLayoutManager;
    int mailCount;

    final Handler handler = new Handler();
    Thread thread;
    int selector = 0;
    boolean running = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_view);
        mailCount = 15;
        allNews = new ArrayList<NewsModel>();
        scrolldown = findViewById(R.id.scrolldown);
        scrollup = findViewById(R.id.scrollup);
        Button selectbtn = findViewById(R.id.select_news_btn);
        Button backbtn = findViewById(R.id.back_news_btn);
        loading = findViewById(R.id.loading);
        ll = findViewById(R.id.linearLayout);
        mRecyclerView = findViewById(R.id.news_recycler);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mAdapter = new NewsAdapter(allNews,this::onContactClick);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getApplicationContext().getResources().getDrawable(R.drawable.divider));
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        getNews();


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

        selectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onContactClick(mLayoutManager.findFirstVisibleItemPosition());
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
                    findViewById(R.id.scrolldown),
                    findViewById(R.id.scrollup),
                    findViewById(R.id.select_news_btn),
                    findViewById(R.id.back_news_btn)
            };

            if(selector>3)
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
                    case R.id.scrolldown:
                        finish();
                        break;
                    case R.id.scrollup:
                        mRecyclerView.smoothScrollToPosition(mLayoutManager.findLastVisibleItemPosition() + 1);
                        break;
                    case R.id.select_news_btn:
                        if(mLayoutManager.findFirstVisibleItemPosition()>0){
                            mRecyclerView.smoothScrollToPosition(mLayoutManager.findFirstVisibleItemPosition() - 1);}
                        break;

                    case R.id.back_news_btn:
                        //Select News
                        onContactClick(mLayoutManager.findFirstVisibleItemPosition());
                        break;
                }
            }

            selector++;
        }
    };

    @Override
    public void onContactClick(int position) {
        NewsModel emailHTML = allNews.get(position);
        Intent intent = new Intent(getApplicationContext(), NewsDetails.class);
        intent.putExtra("obj",allNews.get(position));
        startActivity(intent);
    }

    void getNews(){
        //https://newsapi.org/v2/top-headlines?country=us&apiKey=API_KEY
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String uri = "https://newsapi.org/v2/top-headlines?country=us&apiKey=e3a9bb071cba4effa6b13cdd1c6fca08";
        JsonObjectRequest  myReq = new JsonObjectRequest(Request.Method.GET,uri,null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray articles = response.getJSONArray("articles");
                            for(int i = 0 ; i < articles.length(); i++){
                                NewsModel newNews = new NewsModel(
                                        articles.getJSONObject(i).getJSONObject("source").getString("name"),
                                        articles.getJSONObject(i).getString("title"),
                                        articles.getJSONObject(i).getString("description"),
                                        articles.getJSONObject(i).getString("url"),
                                        articles.getJSONObject(i).getString("urlToImage"),
                                        articles.getJSONObject(i).getString("publishedAt")
                                );
                                allNews.add(newNews);
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((NewsAdapter) mAdapter).UpdateList(allNews);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){

            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("User-Agent", "Mozilla/5.0");
                return params;
            }};
        requestQueue.add(myReq);
    }
}