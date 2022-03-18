package com.example.mindbraille;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mindbraille.models.MailModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private ArrayList<NewsModel> newsList;
    private OnNewsListener onNewsListener;

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_card,parent,false);
        NewsViewHolder ovh = new NewsViewHolder(v,onNewsListener);
        return ovh;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsModel currentItem = newsList.get(position);

        holder.title.setText(currentItem.getTitle());
        holder.description.setText(currentItem.getDescription());
        holder.soruce.setText(currentItem.getSourcename());
        holder.timestamp.setText(currentItem.getPublishDate());
        Picasso.get().load(currentItem.getUrltoImage()).into(holder.newsImage);

    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public void UpdateList(ArrayList<NewsModel> data) {
        newsList = data;
        notifyDataSetChanged();
    }

    public NewsAdapter(ArrayList<NewsModel> mailList, OnNewsListener onNewsListener){
        this.newsList = mailList;
        this.onNewsListener = onNewsListener;
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView title;
        public TextView description;
        public TextView soruce;
        public TextView timestamp;
        public ImageView newsImage;
        OnNewsListener onContactListener;

        public NewsViewHolder(@NonNull View itemView, OnNewsListener onContactListener) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            soruce = itemView.findViewById(R.id.source);
            timestamp = itemView.findViewById(R.id.timestamp);
            newsImage = itemView.findViewById(R.id.newsphoto);
            this.onContactListener = onContactListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onContactListener.onContactClick(getAdapterPosition());

        }
    }

    public interface OnNewsListener {
        void onContactClick(int position);
    }

}
