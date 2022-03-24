package com.example.mindbraille.email;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.mindbraille.R;
import com.example.mindbraille.models.AppConstants;
import com.example.mindbraille.models.AuthInfo;
import com.example.mindbraille.models.ContactModel;
import com.example.mindbraille.models.EmailModel;
import com.microsoft.graph.concurrency.ICallback;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.requests.extensions.IContactCollectionPage;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AddressFragment extends Fragment {


    private static final int RESULT_OK = 999;
    AuthInfo userauthInfo;
    EditText r_address;
    Button buttontoemailbody;
    Button buttontocontacts;
    ArrayList<ContactModel> contactModelArrayList;

    RelativeLayout smsrl;
    View kb;



    ArrayList<ContactModel> fakeContacts;
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == AppConstants.FRAGMENT_CODE){
                ContactModel selectedContect = (ContactModel) data.getSerializableExtra("selectedContact");
                String prevText = r_address.getText().toString()+';';
                r_address.setText(prevText + selectedContect.getEmail());
            }
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.address_fragment,container,false);
        userauthInfo = (AuthInfo) getArguments().getSerializable("userauthInfo");
        contactModelArrayList = new ArrayList<ContactModel>();
        fakeContacts = new ArrayList<ContactModel>();
        r_address = v.findViewById(R.id.recipientmailfield);
        buttontoemailbody = v.findViewById(R.id.buttontoemailbody);
        buttontocontacts = v.findViewById(R.id.buttontocontactlistemail);



        buttontocontacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OutlookContactsFragment fragment = new OutlookContactsFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("contactList",contactModelArrayList);
                fragment.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                fragment.setTargetFragment(AddressFragment.this, AppConstants.FRAGMENT_CODE);
                ft.addToBackStack(AddressFragment.class.getName());
                ft.add(R.id.frag_cont,fragment,"TAG");
                ft.commit();
            }
        });

        buttontoemailbody.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {



                Bundle bundle = new Bundle();
                bundle.putString("r_address",r_address.getText().toString());
                bundle.putSerializable("userauthInfo",userauthInfo);
                if(r_address.getText().toString().length()>0){
                    char first = r_address.getText().toString().charAt(0);
                    List<String> addresses = Arrays.asList(r_address.getText().toString().substring(1).split(";"));
                    if(addresses.size() == 1){
                        List<String> ads = new ArrayList<String>();
                        if(first!=';'){
                        ads.add(r_address.getText().toString());
                            EmailModel.recipients = ads;
                        }
                        if(first == ';'){
                            EmailModel.recipients = addresses;
                        }
                    }else{
                        EmailModel.recipients = addresses;
                    }
                Log.d("dee",EmailModel.recipients.toString());
                    CCFragment fragment = new CCFragment();
                fragment.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.addToBackStack(AddressFragment.class.getName());
                ft.add(R.id.frag_cont,fragment,"TAG");
                smsrl.removeView(kb);
                ft.commit();}
                else{
                    Toast.makeText(getContext(), "Email address required", Toast.LENGTH_LONG).show();
                }
            }
        });

        AuthInfo.graphClient.me().contacts().buildRequest().select("emailAddresses,displayName").top(5).get(new ICallback<IContactCollectionPage>() {
            @Override
            public void success(IContactCollectionPage iContactCollectionPage) {
                for( int i = 0; i < iContactCollectionPage.getCurrentPage().size();i++){
                    contactModelArrayList.add(
                            new ContactModel(
                                    iContactCollectionPage.getCurrentPage().get(i).displayName,
                                    iContactCollectionPage.getCurrentPage().get(i).emailAddresses.get(0).address
                            )

                    );
                }
                Log.d("CONTACTS",contactModelArrayList.toString());
            }

            @Override
            public void failure(ClientException ex) {

            }
        });


        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        smsrl = requireActivity().findViewById(R.id.address_frament_relative_layout);;
        LayoutInflater layoutInflater = getLayoutInflater();
        kb = layoutInflater.inflate(R.layout.mindbraillekb,null);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        //params.addRule(RelativeLayout.BELOW, R.id.new_sms_final_back_btn);
        kb.setLayoutParams(params);
        smsrl.addView(kb);
    }
}
