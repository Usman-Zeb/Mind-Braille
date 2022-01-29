package com.example.mindbraille.email;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mindbraille.R;
import com.example.mindbraille.models.ContactModel;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class OutlookContactsAdapter extends RecyclerView.Adapter<OutlookContactsAdapter.OutlookContactsViewHolder> {

    private ArrayList<ContactModel> contactList;
    private OnContactListener mOnContactListener;

    @NonNull
    @Override
    public OutlookContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_card,parent,false);
        OutlookContactsViewHolder ovh = new OutlookContactsViewHolder(v,mOnContactListener);
        return ovh;
    }

    @Override
    public void onBindViewHolder(@NonNull OutlookContactsViewHolder holder, int position) {
        ContactModel currentItem = contactList.get(position);

        holder.name.setText(currentItem.getName());
        holder.email.setText(currentItem.getEmail());

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public OutlookContactsAdapter(ArrayList<ContactModel> contactList,OnContactListener onContactListener){
        this.contactList = contactList;
        this.mOnContactListener = onContactListener;
    }

    public static class OutlookContactsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name;
        public TextView email;
        OnContactListener onContactListener;

        public OutlookContactsViewHolder(@NonNull View itemView, OnContactListener onContactListener) {
            super(itemView);
            name = itemView.findViewById(R.id.contact_name);
            email = itemView.findViewById(R.id.contact_mail);
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
