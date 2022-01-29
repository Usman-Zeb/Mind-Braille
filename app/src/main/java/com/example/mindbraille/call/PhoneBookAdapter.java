package com.example.mindbraille.call;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mindbraille.R;

import java.util.HashMap;
import java.util.Map;

public class PhoneBookAdapter extends RecyclerView.Adapter<PhoneBookAdapter.PhoneBookViewHolder> {
    Map<String,String> phone_numbers = new HashMap<>();
    private OnContactListener monContactListener;
    public  PhoneBookAdapter(Map<String,String> data, OnContactListener onContactListener) {
        this.phone_numbers = data;
        this.monContactListener = onContactListener;

    }
    @NonNull
    @Override
    public PhoneBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item, parent,false);
        return new PhoneBookViewHolder(view, monContactListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneBookViewHolder holder, int position) {
        String name = (String) phone_numbers.keySet().toArray()[position];
        String number = phone_numbers.get(name);
        holder.name.append(name);
        holder.number.append(number);
    }

    @Override
    public int getItemCount() {
        return phone_numbers.size();
    }

    public class PhoneBookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        TextView number;
        OnContactListener onContactListener;
        public PhoneBookViewHolder(@NonNull View itemView, OnContactListener onContactListener) {
            super(itemView);
            name = itemView.findViewById(R.id.fav_name);
            number = itemView.findViewById(R.id.fav_number);
            this.onContactListener = onContactListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onContactListener.OnContactClick(getAdapterPosition());
        }
    }

    public interface OnContactListener{
        void OnContactClick(int position);
    }

}
