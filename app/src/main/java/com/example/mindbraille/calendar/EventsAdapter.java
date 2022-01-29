package com.example.mindbraille.calendar;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mindbraille.R;
import com.example.mindbraille.models.ContactModel;
import com.example.mindbraille.models.EventModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventsViewHolder> {

    private ArrayList<EventModel> eventList;
    private OnContactListener mOnContactListener;

    @NonNull
    @Override
    public EventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card,parent,false);
        EventsViewHolder ovh = new EventsViewHolder(v,mOnContactListener);
        return ovh;
    }


    @Override
    public void onBindViewHolder(@NonNull EventsViewHolder holder, int position) {
        EventModel currentItem = eventList.get(position);


        holder.subject.setText(currentItem.getSubject());
        holder.start.setText("Start: " + currentItem.getStart().dateTime.substring(0,currentItem.getEnd().dateTime.indexOf("T")));
        holder.end.setText("End: " + currentItem.getEnd().dateTime.substring(0,currentItem.getEnd().dateTime.indexOf("T")));
        holder.location.setText(currentItem.getLocation().displayName);

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public void UpdateList(ArrayList<EventModel> data) {
        eventList = data;
        notifyDataSetChanged();
    }
    public EventsAdapter(ArrayList<EventModel> eventList, OnContactListener onContactListener){
        this.eventList = eventList;
        this.mOnContactListener = onContactListener;
    }

    public static class EventsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView subject;
        public TextView start;
        public TextView end;
        public TextView location;
        OnContactListener onContactListener;

        public EventsViewHolder(@NonNull View itemView, OnContactListener onContactListener) {
            super(itemView);
            subject = itemView.findViewById(R.id.event_subject);
            start = itemView.findViewById(R.id.event_date_start);
            end = itemView.findViewById(R.id.event_date_end);
            location = itemView.findViewById(R.id.event_location);
            this.onContactListener = onContactListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onContactListener.onContactClick(getAdapterPosition());
        }
    }

    public interface OnContactListener {
        void onContactClick(int position);
    }

}
