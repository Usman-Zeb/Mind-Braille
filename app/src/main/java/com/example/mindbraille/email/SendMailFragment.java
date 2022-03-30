package com.example.mindbraille.email;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mindbraille.R;
import com.example.mindbraille.activities.MainActivity;
import com.example.mindbraille.models.AuthInfo;
import com.example.mindbraille.models.EmailModel;
import com.github.ybq.android.spinkit.SpinKitView;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechRecognitionResult;
import com.microsoft.cognitiveservices.speech.SpeechRecognizer;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SendMailFragment extends Fragment {

    Button sendmailButton;
    Button audioInputButton;
    String sendAddress;
    TextView emailBody;
    TextView subjectBody;
    AuthInfo userauthInfo;
    SpeechConfig speechConfig;
    SpinKitView loading;
    TextView target;
    boolean isReply;

    private void SendEmail(String address) throws JSONException {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JSONObject body = new JSONObject();
        if(isReply == false){
        body.put("contentType","Text");
        body.put("content",emailBody.getText().toString());}
        JSONArray allRecipients = new JSONArray();
        for(int i = 0 ; i < EmailModel.recipients.size();i++){
            JSONObject recipients = new JSONObject();
            if(isReply == false){
            recipients.put("emailAddress",new JSONObject().put("address",EmailModel.recipients.get(i)));}else{
                recipients.put("emailAddress",new JSONObject().put("address",EmailModel.recipients.get(i)));
            }

            allRecipients.put(recipients);
        }
        JSONArray allccs = new JSONArray();
        for(int i = 0 ; i < EmailModel.ccs.size();i++) {
            JSONObject ccr = new JSONObject();
            ccr.put("emailAddress", new JSONObject().put("address", EmailModel.ccs.get(i)));
            allccs.put(ccr);
        }

        JSONObject message  =new JSONObject();
        if(isReply == false){
        message.put("subject", subjectBody.getText().toString() );}
        if(isReply == false){
        message.put("body",body);}
        message.put("toRecipients",allRecipients);
        if (isReply == false){
        message.put("ccRecipients",allccs);}
        JSONObject req = new JSONObject();
        req.put("message",message);
        if(isReply == true){
            req.put("comment",emailBody.getText().toString());
        }
        if(isReply == false){
        req.put("saveToSentItems","True");}

        final String requestBody = req.toString();


        Log.d("JSONTEST",req.toString());

        if(isReply == false) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://graph.microsoft.com/v1.0/me/sendMail",
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("VOLLEY", response);
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("VOLLEY", error.toString());
                        }
                    }) {

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
                    params.put("Authorization", "Bearer " + userauthInfo.getAccessToken());
                    return params;
                }
            };
            requestQueue.add(stringRequest);
            Toast.makeText(getContext(),"Mail sent successfully",Toast.LENGTH_LONG).show();
            getActivity().finish();
        }else{
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://graph.microsoft.com/v1.0/me/messages/"+EmailModel.id+"/reply",
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("VOLLEY", "https://graph.microsoft.com/v1.0/me/messages/"+EmailModel.id+"/reply");
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("VOLLEY", error.toString());
                        }
                    }) {

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
                    params.put("Authorization", "Bearer " + userauthInfo.getAccessToken());
                    return params;
                }
            };
            requestQueue.add(stringRequest);
            Toast.makeText(getContext(),"Mail sent successfully",Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        speechConfig = SpeechConfig.fromSubscription("a72658606e1a4b449d18d1ea99be2ef9", "eastus");

        View v = inflater.inflate(R.layout.emailbody_fragment,container,false);
        isReply = false;
        loading = v.findViewById(R.id.spin_kit);
        sendAddress = getArguments().getString("r_address");
        userauthInfo = (AuthInfo) getArguments().getSerializable("userauthInfo");
        emailBody = v.findViewById(R.id.emailbodyfield);
        sendmailButton = v.findViewById(R.id.sendemailbutton);
        audioInputButton = v.findViewById(R.id.audioInput);
        subjectBody = v.findViewById(R.id.subjectField);
        subjectBody.setText("Write subject here");
        isReply = getArguments().getBoolean("isReply");
        if(isReply == true){
            subjectBody.setVisibility(View.GONE);
        }
        subjectBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                target = view.findViewById(R.id.subjectField);
            }
        });

        subjectBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                target = view.findViewById(R.id.emailbodyfield);
            }
        });

        audioInputButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            fromMic(speechConfig,emailBody);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        sendmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SendEmail(sendAddress);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return v;
    }

    private void fromMic(SpeechConfig speechConfig,TextView target) throws ExecutionException, InterruptedException {
        new getAudioInput().execute(null,null,null);
    }

    public class getAudioInput extends AsyncTask<SpeechRecognitionResult,SpeechRecognitionResult,SpeechRecognitionResult>{

        @Override
        protected SpeechRecognitionResult doInBackground(SpeechRecognitionResult... speechRecognitionResults) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loading.setVisibility(View.VISIBLE);
                }
            });

            AudioConfig audioConfig = AudioConfig.fromDefaultMicrophoneInput();
            SpeechRecognizer recognizer = new SpeechRecognizer(speechConfig, audioConfig);

            System.out.println("Speak into your microphone.");
            Future<SpeechRecognitionResult> task = recognizer.recognizeOnceAsync();
            SpeechRecognitionResult result = null;
            try {
                result = task.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(SpeechRecognitionResult speechRecognitionResult) {
            super.onPostExecute(speechRecognitionResult);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loading.setVisibility(View.GONE);
                    emailBody.setText(emailBody.getText() + " "+speechRecognitionResult.getText().toString());
                }
            });


        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LayoutInflater layoutInflater = getLayoutInflater();
        View kb = layoutInflater.inflate(R.layout.mindbraillekb,null);


        RelativeLayout smsrl = Objects.requireNonNull(getActivity()).findViewById(R.id.emailbody_fragment_relative_layout);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        //params.addRule(RelativeLayout.BELOW, R.id.new_sms_final_back_btn);
        kb.setLayoutParams(params);
        smsrl.addView(kb);
    }
}
