package com.example.mindbraille;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Map;

public class RecentSMSAdapter extends RecyclerView.Adapter<RecentSMSAdapter.RecentSMSViewHolder> {

    Map<String, String> data;
    public RecentSMSAdapter(Map<String, String> data)
    {
        this.data = data;
    }
    @NonNull
    @Override
    public RecentSMSViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recent_sms_item_list, parent, false);

        return new RecentSMSViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentSMSViewHolder holder, int position) {
        String sender = (String) data.keySet().toArray()[position];
        String message = data.get(sender);
        holder.sender.append(sender);
        holder.message.append(message);
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class RecentSMSViewHolder extends RecyclerView.ViewHolder{
        TextView sender;
        TextView message;
        public RecentSMSViewHolder(@NonNull View itemView) {
            super(itemView);

            sender = itemView.findViewById(R.id.recent_sms_item_list_sender);
            message = itemView.findViewById(R.id.recent_sms_item_list_message);

        }
    }

}