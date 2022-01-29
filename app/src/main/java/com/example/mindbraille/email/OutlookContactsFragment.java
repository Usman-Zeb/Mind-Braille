package com.example.mindbraille.email;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mindbraille.R;
import com.example.mindbraille.models.ContactModel;

import java.util.ArrayList;

public class OutlookContactsFragment extends Fragment implements OutlookContactsAdapter.OnContactListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<ContactModel> contactList = new ArrayList<ContactModel>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_outlook_contacts,container,false);

        contactList = (ArrayList<ContactModel>) getArguments().getSerializable("contactList");


        mRecyclerView = v.findViewById(R.id.contacts_recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new OutlookContactsAdapter(contactList,this::onContactClick);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        return v;
    }

    @Override
    public void onContactClick(int position) {
        ContactModel selectedContact = contactList.get(position);
        Intent intent = new Intent(getContext(), OutlookContactsFragment.class);
        intent.putExtra("selectedContact",selectedContact);
        getTargetFragment().onActivityResult(getTargetRequestCode(),999,intent);
        getFragmentManager().popBackStack();
    }
}
