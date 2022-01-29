package com.example.mindbraille.calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.LinearLayout;

import com.example.mindbraille.R;
import com.example.mindbraille.email.OutlookContactsAdapter;
import com.example.mindbraille.models.AuthInfo;
import com.example.mindbraille.models.ContactModel;
import com.example.mindbraille.models.EventModel;
import com.microsoft.graph.concurrency.ICallback;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.requests.extensions.IEventCollectionPage;
import com.microsoft.graph.requests.extensions.IEventCollectionRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CalendarActivity extends AppCompatActivity implements EventsAdapter.OnContactListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<EventModel> eventList = new ArrayList<EventModel>();
    AuthInfo userauthInfo;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        userauthInfo = (AuthInfo) getIntent().getSerializableExtra("userauthInfo");


        mRecyclerView = findViewById(R.id.events_recycler);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());

        mAdapter = new EventsAdapter(eventList,CalendarActivity.this::onContactClick);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        getCalendarInfo();

    }

    private void getCalendarInfo(){
        AuthInfo.graphClient.me().events().buildRequest().select("start,end,subject,location").get(new ICallback<IEventCollectionPage>() {
            @Override
            public void success(IEventCollectionPage iEventCollectionPage) {
                Date cur = new Date();
                for( int i = 0; i < iEventCollectionPage.getCurrentPage().size();i++){

                    Date date1 = null;
                    try {
                        date1 = new SimpleDateFormat("yyyy-mm-dd").parse( iEventCollectionPage.getCurrentPage().get(i).start.dateTime.substring(0,iEventCollectionPage.getCurrentPage().get(i).start.dateTime.indexOf("T")));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if(date1.after(cur) || DateUtils.isToday(date1.getTime())){
                    eventList.add(
                            new EventModel(
                                    iEventCollectionPage.getCurrentPage().get(i).start,
                                    iEventCollectionPage.getCurrentPage().get(i).end,
                                    iEventCollectionPage.getCurrentPage().get(i).subject,
                                    iEventCollectionPage.getCurrentPage().get(i).location
                            )

                    );
                }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((EventsAdapter) mAdapter).UpdateList(eventList);
                    }
                });
            }

            @Override
            public void failure(ClientException ex) {

            }
        });
    }

    @Override
    public void onContactClick(int position) {

    }
}