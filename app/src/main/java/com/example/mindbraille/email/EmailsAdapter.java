package com.example.mindbraille.email;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mindbraille.R;
import com.example.mindbraille.models.MailModel;

import java.util.ArrayList;

public class EmailsAdapter extends RecyclerView.Adapter<EmailsAdapter.EmailViewHolder> {

    private ArrayList<MailModel> mailList;
    private OnEmailListener mOnContactListener;

    @NonNull
    @Override
    public EmailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mail_card,parent,false);
        EmailViewHolder ovh = new EmailViewHolder(v,mOnContactListener);
        return ovh;
    }

    @Override
    public void onBindViewHolder(@NonNull EmailViewHolder holder, int position) {
        MailModel currentItem = mailList.get(position);

        holder.name.setText(currentItem.getSenderName());
        holder.email.setText(currentItem.getSenderMail());
        holder.timestamp.setText(currentItem.getRecTime());
        holder.body.setText(currentItem.getBodyPreview());
        holder.subject.setText(currentItem.getSubject());


    }

    @Override
    public int getItemCount() {
        return mailList.size();
    }

    public void UpdateList(ArrayList<MailModel> data) {
        mailList = data;
        notifyDataSetChanged();
    }

    public EmailsAdapter(ArrayList<MailModel> mailList, OnEmailListener onContactListener){
        this.mailList = mailList;
        this.mOnContactListener = onContactListener;
    }

    public static class EmailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name;
        public TextView email;
        public TextView timestamp;
        public TextView body;
        public TextView subject;
        OnEmailListener onContactListener;

        public EmailViewHolder(@NonNull View itemView, OnEmailListener onContactListener) {
            super(itemView);
            name = itemView.findViewById(R.id.sendername);
            email = itemView.findViewById(R.id.sendermail);
            body = itemView.findViewById(R.id.body);
            timestamp = itemView.findViewById(R.id.timestamp);
            subject = itemView.findViewById(R.id.subject);
            this.onContactListener = onContactListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onContactListener.onContactClick(getAdapterPosition());
        }
    }

    public interface OnEmailListener {
        void onContactClick(int position);
    }

}
