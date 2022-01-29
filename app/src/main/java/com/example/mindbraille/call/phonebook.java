package com.example.mindbraille.call;


import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mindbraille.sms.New_SMS_Number;
import com.example.mindbraille.R;
import com.example.mindbraille.globals.GlobalClass;

import java.util.HashMap;
import java.util.Map;


public class phonebook extends DialogFragment implements PhoneBookAdapter.OnContactListener
{
    TextView dialedtv;
    Map<String,String> phone_numbers = new HashMap<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_phonebook, container, false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = getView().findViewById(R.id.phonebookrecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Map<String,String> phone_numbers = getcontacts();
        String[] data = {"dsa","dasdas","asdasda","dsa","dasdas","asdasda","dsa","dasdas","asdasda"};
        recyclerView.setAdapter(new PhoneBookAdapter(phone_numbers, this));
        getView().findViewById(R.id.numberdialed);
    }

    Map<String,String> getcontacts()
    {

        String[] projection = new String[] {
                ContactsContract.Contacts.PHONETIC_NAME,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.STARRED};
        String selection =ContactsContract.Contacts.STARRED + "='1'";


        Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,"starred=?",new String[] {"1"},null);

        while(cursor.moveToNext())
        {

            String title = cursor.getString(
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String number = cursor.getString(
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            //String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            //String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            phone_numbers.put(title,number);
        }
        return phone_numbers;
    }

    @Override
    public void OnContactClick(int position) {
        String name = (String) phone_numbers.keySet().toArray()[position];
        String number = phone_numbers.get(name);
        if (((GlobalClass) getActivity().getApplication()).getInCallMenu())
            ((Call) getActivity()).textView.setText(number);
        else if (((GlobalClass) getActivity().getApplication()).getInNewSMS())
            ((New_SMS_Number) getActivity()).textView.setText(number);

        //Log.d("BRUHH",number);
        //dialedtv.setText(number);
        dismiss();
    }

   /* @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Phonebook");
        Map<String,String> phone_numbers = getcontacts();
        String[] numbers = new String[phone_numbers.size()];
        int i=0;
        for(Map.Entry<String, String> entry : phone_numbers.entrySet())
        {
            numbers[i] = entry.getKey() + " " + entry.getValue();
            i++;
        }

        builder.setItems(numbers, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }*/


}