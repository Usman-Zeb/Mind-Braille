package com.example.mindbraille.activities;


import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mindbraille.Game.FlappyBird;
import com.example.mindbraille.NewsView;
import com.example.mindbraille.email.MailMode;
import com.example.mindbraille.R;
import com.example.mindbraille.sms.SMS;
import com.example.mindbraille.calendar.CalendarActivity;
import com.example.mindbraille.email.SendMailActivity;
import com.example.mindbraille.globals.GlobalClass;
import com.example.mindbraille.interfaces.UserAccountsApi;
import com.example.mindbraille.models.AuthInfo;
import com.example.mindbraille.models.EventModel;
import com.example.mindbraille.models.UserAccounts;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.View;

import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.github.ybq.android.spinkit.SpinKitView;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;
import com.microsoft.cognitiveservices.speech.intent.IntentRecognitionResult;
import com.microsoft.cognitiveservices.speech.intent.IntentRecognizer;
import com.microsoft.cognitiveservices.speech.intent.LanguageUnderstandingModel;
import com.microsoft.graph.authentication.IAuthenticationProvider; //Imports the Graph sdk Auth interface
import com.microsoft.graph.concurrency.ICallback;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.http.IHttpRequest;
import com.microsoft.graph.models.extensions.*;
import com.microsoft.graph.requests.extensions.GraphServiceClient;
import com.microsoft.identity.client.*;
import com.microsoft.identity.client.exception.*;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    /* Display Variables */

    String username;
    String userid;
    ArrayList<EventModel> eventList = new ArrayList<EventModel>();
    private final static String[] SCOPES = {"Files.Read","Mail.Send","Mail.Read","Mail.ReadBasic","Contacts.Read","Contacts.ReadWrite","Calendars.Read"};
    /* Azure AD v2 Configs */
    final static String AUTHORITY = "https://login.microsoftonline.com/common";
    private ISingleAccountPublicClientApplication mSingleAccountApp;
    private static final String TAG = MainActivity.class.getSimpleName();
    private AuthInfo userauthInfo;

    /* UI & Debugging Variables */
    CircleImageView i;
    Button audioInput;
    Button signOutButton;
    Button gameButton;
    Button callGraphApiSilentButton;
    Button emailButton;
    Button calendarButton;
    Button SMS_Button;
    Button callButton;
    Button newsButton;
    TextView logTextView;
    TextView currentUserTextView;
    TextView textResult;
    TextView userName;
    TextView userID;
    SpinKitView loading;
    ProgressBar progressBar;
    ConstraintLayout constraintLayout;

    boolean running = true;
    final Handler handler = new Handler();
    Thread thread;
    int selector =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestperms();
        i = findViewById(R.id.imageView);
        ((GlobalClass) this.getApplication()).setInCallMenu(false);
        ((GlobalClass) this.getApplication()).setInCallPhoneBook(false);
        ((GlobalClass) this.getApplication()).setInMain(true);
        ((GlobalClass) this.getApplication()).setInMenu(false);
        ((GlobalClass) this.getApplication()).setInNewSMS(false);

        constraintLayout = findViewById(R.id.constraintLayout);
        loading = findViewById(R.id.loading);

        signOutButton = findViewById(R.id.clearCache);
        gameButton = findViewById(R.id.togameactivity);
        currentUserTextView = findViewById(R.id.current_user);
        emailButton = findViewById(R.id.tomailactivity);
        audioInput = findViewById(R.id.audioInput);
        calendarButton = findViewById(R.id.tocalendaractivity);
        newsButton = findViewById(R.id.tonewsactivity);
        callButton = findViewById(R.id.callactivity);
        SMS_Button = findViewById(R.id.smsbutton);
        username = getIntent().getStringExtra("username");
        userid = getIntent().getStringExtra("userid");
        userName = findViewById(R.id.displayName);
        progressBar = findViewById(R.id.spin_kit);

        userauthInfo = new AuthInfo();

        initializeUI();

        PublicClientApplication.createSingleAccountPublicClientApplication(getApplicationContext(),
                R.raw.auth_config_single_account, new IPublicClientApplication.ISingleAccountApplicationCreatedListener() {
                    @Override
                    public void onCreated(ISingleAccountPublicClientApplication application) {
                        mSingleAccountApp = application;
                        loadAccount();
                        mSingleAccountApp.acquireTokenSilentAsync(SCOPES, AUTHORITY, getAuthSilentCallback());
                    }
                    @Override
                    public void onError(MsalException exception) {
                        displayError(exception);
                    }
                });


    }

    @Override
    protected void onResume() {
        super.onResume();
        running = true;

        //((GlobalClass) this.getApplication()).setInCallMenu(true);

        thread = new Thread(new Runnable() {
            public void run() {

                while (running) {
                    try {
                        Thread.sleep(1000);
                        handler.post(updateRunner);
                    }
                    catch (InterruptedException e){
                        running=false;
                    }

                }
            }
        });

        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((GlobalClass) this.getApplication()).setInCallMenu(false);
        thread.interrupt();
    }
    final Runnable updateRunner = new Runnable() {

        public void run() {
            Button[] buttons = {audioInput,emailButton, calendarButton, SMS_Button, callButton, gameButton,newsButton};

            if(selector>6) selector=0;

            for(int i=0;i<buttons.length;i++)
            {
                buttons[i].setEnabled(false);
            }

            buttons[selector].setEnabled(true);
            selector++;
            if(((GlobalClass) getApplication()).getBlinked() && (((GlobalClass) getApplication()).getBlinkValue()>50))
            {
                Intent intent;
                switch (buttons[selector].getId())
                {
                    case R.id.audioInput:

                        intent = new Intent(MainActivity.this, NewsView.class);
                        startActivity(intent);

                        break;
                    case R.id.tomailactivity:

                        try {
                            voiceInput();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        break;
                    case R.id.tocalendaractivity:

                        intent = new Intent(getApplicationContext(), SendMailActivity.class);
                        intent.putExtra("userauthInfo",userauthInfo);
                        startActivity(intent);

                        break;
                    case R.id.smsbutton:

                        intent = new Intent(getApplicationContext(), CalendarActivity.class);
                        intent.putExtra("userauthInfo",userauthInfo);
                        startActivity(intent);

                         break;
                     case R.id.callactivity:
                         intent = new Intent(getApplicationContext(), SMS.class);
                         startActivity(intent);

                         break;
                     case R.id.togameactivity:
                         intent = new Intent(MainActivity.this, com.example.mindbraille.call.Call.class);
                         startActivity(intent);

                         break;
                     case R.id.tonewsactivity:
                         intent = new Intent(MainActivity.this, FlappyBird.class);
                         startActivity(intent);
                         break;

                }
                ((GlobalClass) getApplication()).setBlinked(false);
            }
        }
    };

    //When app comes to the foreground, load existing account to determine if user is signed in
    private void loadAccount() {
        if (mSingleAccountApp == null) {
            return;
        }

        mSingleAccountApp.getCurrentAccountAsync(new ISingleAccountPublicClientApplication.CurrentAccountCallback() {
            @Override
            public void onAccountLoaded(@Nullable IAccount activeAccount) {
                // You can use the account data to update your UI or your app database.
                updateUI(activeAccount);
            }

            @Override
            public void onAccountChanged(@Nullable IAccount priorAccount, @Nullable IAccount currentAccount) {
                if (currentAccount == null) {
                    // Perform a cleanup task as the signed-in account changed.
                    performOperationOnSignOut();
                }
            }

            @Override
            public void onError(@NonNull MsalException exception) {
                displayError(exception);
            }
        });
    }


    private void initializeUI(){


        audioInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    voiceInput();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                intent.putExtra("userauthInfo",userauthInfo);
                startActivity(intent);
        }
        });

        gameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FlappyBird.class);
                startActivity(intent);
            }
        });

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MailMode.class);
                intent.putExtra("userauthInfo",userauthInfo);
                startActivity(intent);
            }
        });

        //Sign out user
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSingleAccountApp == null){
                    return;
                }
                mSingleAccountApp.signOut(new ISingleAccountPublicClientApplication.SignOutCallback() {
                    @Override
                    public void onSignOut() {
                        updateUI(null);
                        performOperationOnSignOut();
                    }
                    @Override
                    public void onError(@NonNull MsalException exception){
                        displayError(exception);
                    }
                });
            }
        });
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), com.example.mindbraille.call.Call.class);
                startActivity(intent);
            }
        });

        SMS_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SMS.class);
                startActivity(intent);
            }
        });

        newsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),NewsView.class);
                startActivity(intent);
            }
        });

    }

    private SilentAuthenticationCallback getAuthSilentCallback() {
        return new SilentAuthenticationCallback() {
            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                Log.d(TAG, "Successfully authenticated");
                try {
                    callGraphAPI_USERINFO(authenticationResult);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(MsalException exception) {
                Log.d(TAG, "Authentication failed: " + exception.toString());
                displayError(exception);
            }
        };
    }

    private void callGraphAPI_USERINFO(IAuthenticationResult authenticationResult) throws JSONException {

        final String accessToken = authenticationResult.getAccessToken();
        userauthInfo.setAccessToken(accessToken);
        IGraphServiceClient graphClient =
                GraphServiceClient
                        .builder()
                        .authenticationProvider(new IAuthenticationProvider() {
                            @Override
                            public void authenticateRequest(IHttpRequest request) {
                                Log.d(TAG, "Authenticating request," + request.getRequestUrl());
                                request.addHeader("Authorization", "Bearer " + accessToken);

                            }
                        })
                        .buildClient();

        AuthInfo.graphClient = graphClient;

        GetUserImage(accessToken);

        GetCosmoDbData(graphClient);

    }

    private void GetCosmoDbData(IGraphServiceClient graphClient) {
        graphClient
                .me()
                .drive()
                .buildRequest()
                .get(new ICallback<Drive>() {
                    @Override
                    public void success(Drive drive) {
                        username = drive.getRawObject().get("owner").getAsJsonObject().get("user").getAsJsonObject().get("displayName").getAsString();
                        userid = drive.getRawObject().get("owner").getAsJsonObject().get("user").getAsJsonObject().get("id").getAsString();
                        Log.d(TAG, "username: " + username);
                        Log.d(TAG, "id: " + userid);

                        userauthInfo.setUID(userid);
                        userauthInfo.setUserName(username);
                        userauthInfo.setUserEmail(username);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                userName.setText(username);

                            }
                        });


                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("https://mindbraillebackend.azurewebsites.net/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        UserAccountsApi userAccountsApi = retrofit.create(UserAccountsApi.class);

                        Call<List<UserAccounts>> call = userAccountsApi.getUserAccount(userid);

                        call.enqueue(new Callback<List<UserAccounts>>() {
                            @Override
                            public void onResponse(Call<List<UserAccounts>> call, Response<List<UserAccounts>> response) {
                                    if(!response.isSuccessful()){
                                        Log.d("Lost Call", String.valueOf(response.code()));
                                        return;
                                    }
                                    //Log.d("Address",response.body().get(0).getPermanent_home_address());
                            }

                            @Override
                            public void onFailure(Call<List<UserAccounts>> call, Throwable t) {
                                    Log.d("Lost",t.toString());
                            }
                        });

                    }

                    @Override
                    public void failure(ClientException ex) {
                        displayError(ex);
                    }
                });
    }

    private void SendEmail(String accessToken) throws JSONException {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject body = new JSONObject();
        body.put("contentType","Text");
        body.put("content","The new cafeteria");
        JSONObject recipients = new JSONObject();
        recipients.put("emailAddress",new JSONObject().put("address","hanzla_hawk@outlook.com"));
        JSONArray allRecipients = new JSONArray();
        allRecipients.put(recipients);
        JSONObject ccr = new JSONObject();
        ccr.put("emailAddress",new JSONObject().put("address","hanzla_hawk@outlook.com"));
        JSONArray allccs = new JSONArray();
        allccs.put(ccr);
        JSONObject message  =new JSONObject();
        message.put("subject", "Meet for lunch?" );
        message.put("body",body);
        message.put("toRecipients",allRecipients);
        message.put("ccRecipients",allccs);
        JSONObject req = new JSONObject();
        req.put("message",message);
        req.put("saveToSentItems","True");

        final String requestBody = req.toString();


        Log.d("JSONTEST",req.toString());

        StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://graph.microsoft.com/v1.0/me/sendMail",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("VOLLEY", response);
                    }
                },
                new com.android.volley.Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("VOLLEY", error.toString());
                    }
                }){

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
        }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + accessToken);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void GetUserImage(String accessToken) {
        i.setVisibility(View.GONE);
        constraintLayout.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        // Retrieves an image specified by the URL, displays it in the UI.
        ImageRequest request = new ImageRequest("https://graph.microsoft.com/v1.0/me/photo/$value", new com.android.volley.Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        i.setImageBitmap(bitmap);
                        i.setVisibility(View.VISIBLE);
                        constraintLayout.setVisibility(View.VISIBLE);
                        i.setTranslationX(400);
                        i.animate().translationX(0).setDuration(1000).setStartDelay(400);
                        loading.setVisibility(View.GONE);
                        constraintLayout.setTranslationY(700);
                        constraintLayout.animate().translationY(0).setDuration(1000).setStartDelay(400);
                    }
                }, 0, 0, null,
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Picasso.get().load("https://i.imgur.com/NMtRC7Y_d.webp?maxwidth=760&fidelity=grand").into(i);
                        i.setVisibility(View.VISIBLE);
                        constraintLayout.setVisibility(View.VISIBLE);
                        i.setTranslationX(400);
                        i.animate().translationX(0).setDuration(1000).setStartDelay(400);
                        loading.setVisibility(View.GONE);
                        constraintLayout.setTranslationY(700);
                        constraintLayout.animate().translationY(0).setDuration(1000).setStartDelay(400);
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




    private void updateUI(@Nullable final IAccount account) {
        if (account != null) {
            signOutButton.setEnabled(true);
            currentUserTextView.setText(account.getUsername());
        } else {
            signOutButton.setEnabled(false);
            currentUserTextView.setText("");
        }
    }

    private void displayError(@NonNull final Exception exception) {
        logTextView.setText(exception.toString());
    }

    private void performOperationOnSignOut() {
        final String signOutText = "Signed Out.";
        currentUserTextView.setText("");
        Toast.makeText(getApplicationContext(), signOutText, Toast.LENGTH_SHORT)
                .show();
        this.finishAffinity();
    }

    void voiceInput() throws ExecutionException, InterruptedException {
        new voiceandexecute().execute(null,null,null);

    }


    public class voiceandexecute extends AsyncTask<IntentRecognitionResult,IntentRecognitionResult,IntentRecognitionResult>{

        @Override
        protected IntentRecognitionResult doInBackground(IntentRecognitionResult... intents) {
            final SpeechConfig intentConfig = SpeechConfig.fromSubscription("e8cd34b635de481baeb612af968ac23d", "westus");
            final AudioConfig audioInput = AudioConfig.fromDefaultMicrophoneInput();
            final IntentRecognizer reco = new IntentRecognizer(intentConfig, audioInput);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.VISIBLE);
                }
            });

            LanguageUnderstandingModel intentModel = LanguageUnderstandingModel.fromAppId("7f8343ef-fe2c-4c83-9718-32c7181ca012");
            reco.addAllIntents(intentModel);

            final Future<IntentRecognitionResult> task = reco.recognizeOnceAsync();
            try {
                IntentRecognitionResult result = task.get();
                return result;

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(IntentRecognitionResult intentRecognitionResult) {
            super.onPostExecute(intentRecognitionResult);
            Intent intent;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                }
            });
            switch (intentRecognitionResult.getIntentId()){
                case "Email":
                    intent = new Intent(getApplicationContext(),SendMailActivity.class);
                    intent.putExtra("userauthInfo",userauthInfo);
                    startActivity(intent);
                    break;
                case "Events":
                    intent = new Intent(getApplicationContext(), CalendarActivity.class);
                    intent.putExtra("userauthInfo",userauthInfo);
                    startActivity(intent);
                    break;
                case "Game":
                    intent = new Intent(getApplicationContext(),FlappyBird.class);
                    startActivity(intent);
                    break;
                case "SMS":
                    intent = new Intent(getApplicationContext(), SMS.class);
                    startActivity(intent);
                    break;
                case "Call":
                    intent = new Intent(getApplicationContext(), com.example.mindbraille.call.Call.class);
                    startActivity(intent);
                    break;
            }

        }
    }
    private  void requestperms()
    {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_CONTACTS,Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_PHONE_NUMBERS,Manifest.permission.READ_SMS,Manifest.permission.SEND_SMS,Manifest.permission.CALL_PHONE,Manifest.permission.READ_CONTACTS},1);
    }


}

