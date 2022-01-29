package com.example.mindbraille;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.mindbraille.models.MailModel;
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
    Button loadmore;
    private RecyclerView.Adapter mAdapter;
    LinearLayout ll;
    private LinearLayoutManager mLayoutManager;
    int mailCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_view);
        mailCount = 15;
        allNews = new ArrayList<NewsModel>();
        scrolldown = findViewById(R.id.scrolldown);
        scrollup = findViewById(R.id.scrollup);
        loadmore = findViewById(R.id.loadmore);
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

    }


    @Override
    public void onContactClick(int position) {
        NewsModel emailHTML = allNews.get(position);
        Intent intent = new Intent(getApplicationContext(), MailDetails.class);
        intent.putExtra("obj",emailHTML);
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