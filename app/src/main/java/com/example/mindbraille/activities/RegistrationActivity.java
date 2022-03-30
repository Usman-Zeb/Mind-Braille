package com.example.mindbraille.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.mindbraille.R;
import com.example.mindbraille.interfaces.UserAccountsApi;
import com.example.mindbraille.models.UserAccounts;
import com.google.android.material.textfield.TextInputEditText;
import com.microsoft.graph.authentication.IAuthenticationProvider;
import com.microsoft.graph.concurrency.ICallback;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.http.IHttpRequest;
import com.microsoft.graph.models.extensions.Drive;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.models.extensions.User;
import com.microsoft.graph.requests.extensions.GraphServiceClient;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.client.IPublicClientApplication;
import com.microsoft.identity.client.ISingleAccountPublicClientApplication;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.SilentAuthenticationCallback;
import com.microsoft.identity.client.exception.MsalException;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistrationActivity extends AppCompatActivity {
    TextInputEditText ageInput,sosemailInput,soscontactInput,addressInput;
    ImageView i;
    String username;
    TextView welcomeText;
    Button saveInfoButton;
    RadioGroup speakGroup;
    RadioGroup colorBlind;
    String accountId;
    private RadioButton radioButton;
    private final static String[] SCOPES = {"Files.Read","Mail.Send","Mail.Read","Mail.ReadBasic","Contacts.Read","Contacts.ReadWrite","Calendars.Read"};
    /* Azure AD v2 Configs */
    final static String AUTHORITY = "https://login.microsoftonline.com/common";
    private ISingleAccountPublicClientApplication mSingleAccountApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        ageInput = findViewById(R.id.age_field);
        sosemailInput = findViewById(R.id.sos_email_field);
        soscontactInput = findViewById(R.id.sos_contact_field);
        addressInput = findViewById(R.id.address_field);
        saveInfoButton = findViewById(R.id.saveInfo);
        i = findViewById(R.id.imageView);
        welcomeText = findViewById(R.id.welcomeText);
        speakGroup = (RadioGroup) findViewById(R.id.speak_radioButton);
        colorBlind =(RadioGroup) findViewById(R.id.blind_radioButton);

        saveInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new RegisterandStart().execute("void");
            }
        });


        PublicClientApplication.createSingleAccountPublicClientApplication(getApplicationContext(),
                R.raw.auth_config_single_account, new IPublicClientApplication.ISingleAccountApplicationCreatedListener() {
                    @Override
                    public void onCreated(ISingleAccountPublicClientApplication application) {
                        mSingleAccountApp = application;
                        mSingleAccountApp.acquireTokenSilentAsync(SCOPES, AUTHORITY, getAuthSilentCallback());
                    }
                    @Override
                    public void onError(MsalException exception) {

                    }
                });


    }

    private SilentAuthenticationCallback getAuthSilentCallback() {
        return new SilentAuthenticationCallback() {
            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                Log.d("Done", "Successfully authenticated");
                GetUserImage(authenticationResult.getAccessToken());
                GetName(authenticationResult.getAccessToken());
                accountId = authenticationResult.getAccount().getId();
            }
            @Override
            public void onError(MsalException exception) {
                Log.d("Not", "Authentication failed: " + exception.toString());
            }
        };
    }

    private void GetName(String accessToken){
        IGraphServiceClient graphClient =
                GraphServiceClient
                        .builder()
                        .authenticationProvider(new IAuthenticationProvider() {
                            @Override
                            public void authenticateRequest(IHttpRequest request) {
                                Log.d("Done", "Authenticating request," + request.getRequestUrl());
                                request.addHeader("Authorization", "Bearer " + accessToken);

                            }
                        })
                        .buildClient();

        graphClient.me().drive().buildRequest().get(new ICallback<Drive>() {
            @Override
            public void success(Drive drive) {
                username =  drive.getRawObject().get("owner").getAsJsonObject().get("user").getAsJsonObject().get("displayName").getAsString();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        welcomeText.setText("Welcome " + username);
                    }
                });


            }

            @Override
            public void failure(ClientException ex) {

            }
        });
    }

    private void GetUserImage(String accessToken) {
        // Retrieves an image specified by the URL, displays it in the UI.
        ImageRequest request = new ImageRequest("https://graph.microsoft.com/v1.0/me/photo/$value", new com.android.volley.Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                i.setImageBitmap(bitmap);
            }
        }, 0, 0, null,
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Picasso.get().load("https://i.imgur.com/NMtRC7Y_d.webp?maxwidth=760&fidelity=grand").into(i);
                    }

                }
        ){

            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "image/jpg");
                params.put("Authorization", "Bearer " + accessToken);
                return params;
            }};

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    void saveUser() throws IOException {
        boolean canTalk = true;
        boolean isColorBlind = false;
        int speakID = speakGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(speakID);
        if(radioButton.getText().equals("No")){
            canTalk = false;
        }
        int blindID = colorBlind.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(blindID);
        if(radioButton.getText().equals("Yes")){
            isColorBlind = true;
        }
        UserAccounts userAccount = new UserAccounts(
                accountId,accountId,"REGISTERED",username,ageInput.getText().toString(),sosemailInput.getText().toString(),soscontactInput.getText().toString(),
                addressInput.getText().toString(),canTalk,isColorBlind,"6","0");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://mindbrailleapp.azurewebsites.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserAccountsApi userAccountsApi = retrofit.create(UserAccountsApi.class);

        Call<UserAccounts> call = userAccountsApi.createUser(userAccount);

        call.execute();
    }

    private class RegisterandStart extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            try {
                saveUser();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }
    }
}