package com.example.mindbraille.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mindbraille.R;
import com.example.mindbraille.globals.GlobalClass;
import com.example.mindbraille.interfaces.UserAccountsApi;
import com.example.mindbraille.models.AuthInfo;
import com.example.mindbraille.models.UserAccounts;
import com.github.pwittchen.neurosky.library.NeuroSky;
import com.github.pwittchen.neurosky.library.exception.BluetoothNotEnabledException;
import com.github.pwittchen.neurosky.library.listener.ExtendedDeviceMessageListener;
import com.github.pwittchen.neurosky.library.message.enums.BrainWave;
import com.github.pwittchen.neurosky.library.message.enums.Signal;
import com.github.pwittchen.neurosky.library.message.enums.State;
import com.github.ybq.android.spinkit.SpinKitView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.microsoft.graph.authentication.IAuthenticationProvider;
import com.microsoft.graph.concurrency.ICallback;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.http.IHttpRequest;
import com.microsoft.graph.models.extensions.Drive;
import com.microsoft.graph.requests.extensions.GraphServiceClient;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.requests.extensions.GraphServiceClient;
import com.microsoft.identity.client.AuthenticationCallback;
import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.client.IPublicClientApplication;
import com.microsoft.identity.client.ISingleAccountPublicClientApplication;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.SilentAuthenticationCallback;
import com.microsoft.identity.client.exception.MsalException;
import com.neurosky.thinkgear.TGDevice;
import com.example.mindbraille.questionDialog;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {


    AuthInfo userauthInfo;
    String accessToken;
    Button connectMindwave;


    TextView tvState;
    TextView tvAttention;
    TextView tvMeditation;
    TextView tvBlink;
    GraphView graphView;
    LineGraphSeries<DataPoint> deltaseries = new LineGraphSeries<DataPoint>();
    LineGraphSeries<DataPoint> lowalpha = new LineGraphSeries<>();
    LineGraphSeries<DataPoint> highalpha = new LineGraphSeries<>();
    LineGraphSeries<DataPoint> lowgamma = new LineGraphSeries<>();

    LineGraphSeries<DataPoint> midgamma = new LineGraphSeries<>();
    LineGraphSeries<DataPoint> lowbeta = new LineGraphSeries<>();
    LineGraphSeries<DataPoint> highbeta = new LineGraphSeries<>();
    LineGraphSeries<DataPoint> theta = new LineGraphSeries<>();
    BluetoothAdapter bluetoothAdapter;
    TGDevice tgDevice;
    //LineChart lineChart;
    // LineDataSet deltaset = new LineDataSet(null, "Delta");

    // Description description = new Description();
    int time;
    Button button;
    Button button2;


    boolean isLogingFirstTime = true;
    boolean isAccountExists = false;
    NeuroSky neuroSky;
    SpinKitView loading;
    private UserAccounts account;
    private final static String[] SCOPES = {"Files.Read","Mail.Send","Mail.Read","Mail.ReadBasic","Contacts.Read","Contacts.ReadWrite","Calendars.Read"};
    /* Azure AD v2 Configs */
    final static String AUTHORITY = "https://login.microsoftonline.com/common";
    private ISingleAccountPublicClientApplication mSingleAccountApp;
    private static final String TAG = LoginActivity.class.getSimpleName();
    String accountID;
    Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        initializeUI();
        connectMindwave = findViewById(R.id.connectmindwave);

        connectMindwave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                neuro();
            }
        });


        tvState = findViewById(R.id.tv_state);

        // lineChart = findViewById(R.id.linechart);
        //description.setText("EEG Bands");
        //description.setTextSize(28);
        //lineChart.setDescription(description);
        deltaseries.setColor(R.color.black);
        //highalpha.setColor(R.color.purple_200);
        //highbeta.setColor(R.color.teal_200);
        // midgamma.setColor(R.color.teal_700);
        //lowbeta.setColor(120);
        //lowgamma.setColor(420);
        //lowalpha.setColor(69);
        //theta.setColor(1000);

        deltaseries.setTitle("Delta");
        highalpha.setTitle("High Alpha");
        highbeta.setTitle("High Beta");
        midgamma.setTitle("Mid Gamma");
        lowbeta.setTitle("Low Beta");
        lowgamma.setTitle("Low Gamma");
        lowalpha.setTitle("Low Alpha");
        theta.setTitle("Theta");
        graphView = findViewById(R.id.graphview);
        graphView.addSeries(deltaseries);
        graphView.addSeries(highalpha);
        graphView.addSeries(highbeta);
        graphView.addSeries(midgamma);
        graphView.addSeries(lowalpha);
        graphView.addSeries(lowbeta);
        graphView.addSeries(lowgamma);
        graphView.addSeries(theta);


        graphView.onDataChanged(false, false);
        Viewport viewport = graphView.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setScalable(true);
        viewport.setScalableY(true);
        viewport.setMinY(0);
        viewport.setMaxY(20000000);
        viewport.setScrollable(true);
        time = 0;

        ((GlobalClass) this.getApplication()).setInCallMenu(false);
        ((GlobalClass) this.getApplication()).setInCallPhoneBook(false);
        ((GlobalClass) this.getApplication()).setInMain(true);
        ((GlobalClass) this.getApplication()).setInMenu(false);
        ((GlobalClass) this.getApplication()).setInNewSMS(false);







       /* thread = new Thread(new Runnable() {
            @Override
            public void run() {
                neuro();
            }
        });*/

        loading = findViewById(R.id.spin_kit);




        PublicClientApplication.createSingleAccountPublicClientApplication(getApplicationContext(),
                R.raw.auth_config_single_account, new IPublicClientApplication.ISingleAccountApplicationCreatedListener() {
                    @Override
                    public void onCreated(ISingleAccountPublicClientApplication application) {
                        mSingleAccountApp = application;
                        loadAccount();
                    }
                    @Override
                    public void onError(MsalException exception) {

                    }
                });

    }

    private void changeActivity() throws IOException {

        getAccountInfo();
        Intent intent = new Intent(this, MainActivity.class);
        if(account.getAccountType().equals("UNREGISTERED")){
           intent = new Intent(this,RegistrationActivity.class);
        }

        startActivity(intent);
    }

    void getAccountInfo() throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://mindbraillebackend.azurewebsites.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserAccountsApi userAccountsApi = retrofit.create(UserAccountsApi.class);

        Call<List<UserAccounts>> call = userAccountsApi.getUserAccount(accountID);

        Response<List<UserAccounts>> response =  call.execute();

        account = response.body().get(0);

    }

    void checkIfExists() throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://mindbraillebackend.azurewebsites.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserAccountsApi userAccountsApi = retrofit.create(UserAccountsApi.class);

        Call<List<UserAccounts>> call = userAccountsApi.getUserAccount(accountID);

        Response<List<UserAccounts>> response = call.execute();
        if(response.body().size() > 0){
            isAccountExists = true;
        }else {
            isAccountExists = false;
            Log.d("LOL", "dd: "+isAccountExists);
        }

    }

    void createAccount() throws IOException {
        UserAccounts newUser = new UserAccounts(
                accountID,accountID,
                "UNREGISTERED",
                "Null",
                "Null","Null","Null",
                "Null",true,false,
                "Null","Null");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://mindbraillebackend.azurewebsites.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserAccountsApi userAccountsApi = retrofit.create(UserAccountsApi.class);

        Call<UserAccounts> call = userAccountsApi.createUser(newUser);
        Response<UserAccounts> response = call.execute();
    }

    private void loadAccount() {
        if (mSingleAccountApp == null) {
            return;
        }
        else{
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

        builder.setTitle("Alert");
        builder.setMessage("Do you want to continue without connecting EEG device?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                // Do nothing, but close the dialog
                mSingleAccountApp.getCurrentAccountAsync(new ISingleAccountPublicClientApplication.CurrentAccountCallback() {
                    @Override
                    public void onAccountLoaded(@Nullable IAccount activeAccount) {
                        // You can use the account data to update your UI or your app database.
                        if(activeAccount != null){
                            accountID = activeAccount.getId();
                            new loginProcess().execute("void");
                        }
                    }

                    @Override
                    public void onAccountChanged(@Nullable IAccount priorAccount, @Nullable IAccount currentAccount) {
                        if (currentAccount == null) {
                            // Perform a cleanup task as the signed-in account changed.

                        }
                    }

                    @Override
                    public void onError(@NonNull MsalException exception) {

                    }
                });
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
        }



    }

    private void initializeUI(){
        signInButton = findViewById(R.id.login_button);

        //Sign in user
        signInButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

                builder.setTitle("Alert");
                builder.setMessage("Do you want to continue without connecting EEG device?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing, but close the dialog
                        mSingleAccountApp.signIn(LoginActivity.this, null, SCOPES, getAuthInteractiveCallback());
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
                if (mSingleAccountApp == null) {
                    return;
                }


            }
        });
    }

    private AuthenticationCallback getAuthInteractiveCallback() {
        return new AuthenticationCallback() {
            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                /* Successfully got a token, use it to call a protected resource - MSGraph */
                accountID = authenticationResult.getAccount().getId();
                new loginProcess().execute("void");

            }

            @Override
            public void onError(MsalException exception) {
                /* Failed to acquireToken */
                Log.d(TAG, "Authentication failed: " + exception.toString());
            }
            @Override
            public void onCancel() {
                /* User canceled the authentication */
                Log.d(TAG, "User cancelled login.");
            }
        };
    }
    public class loginProcess extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... strings) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loading.setVisibility(View.VISIBLE);
                }
            });
            try {
                checkIfExists();
            } catch (IOException e) {
                e.printStackTrace();
            }if(!isAccountExists){
                try {
                    createAccount();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                changeActivity();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loading.setVisibility(View.GONE);
                }
            });

        }
    }


    void neuro() {
        ((GlobalClass) this.getApplication()).setBlinked(false);
        neuroSky = createNeuroSky();

        try {
            neuroSky.connect();
        } catch (BluetoothNotEnabledException e) {

            Log.d("NeuroSkyError", e.getMessage());
        }

        neuroSky.startMonitoring();


    }


    NeuroSky createNeuroSky() {
        return new NeuroSky(new ExtendedDeviceMessageListener() {
            @Override
            public void onStateChange(State state) {
                handleStateChange(state);
            }

            @Override
            public void onSignalChange(Signal signal) {
                handleSignalChange(signal);
            }

            @Override
            public void onBrainWavesChange(Set<BrainWave> brainWaves) {
                handleBrainWavesChange(brainWaves);
            }
        });
    }

    void handleStateChange(final State state) {
        if (neuroSky != null && state.equals(State.CONNECTED)) {
            neuroSky.startMonitoring();
        }

        tvState.setText(state.toString());
        Log.d("NeuroSkyStateChange", state.toString());
    }

    void handleSignalChange(final Signal signal) {
        switch (signal) {
            case ATTENTION:
                ((GlobalClass) this.getApplication()).setConcentrationValue(signal.getValue());

                break;
            case MEDITATION:

                break;
            case BLINK:
                Log.d("NeuroSkySignalChange", String.format("%s: %d", signal.toString(), signal.getValue()));
                ((GlobalClass) this.getApplication()).setBlinkValue(signal.getValue());
                ((GlobalClass) this.getApplication()).setBlinked(true);
                //button2.performClick();
                break;
        }


        //((GlobalClass) this.getApplication()).setBlinked(false);

    }

    String getFormattedMessage(String messageFormat, Signal signal) {
        return String.format(Locale.getDefault(), messageFormat, signal.getValue());
    }

    void handleBrainWavesChange(final Set<BrainWave> brainWaves) {
        for (BrainWave brainWave : brainWaves) {
            switch (brainWave) {
                case LOW_ALPHA:
                    lowalpha.appendData(new DataPoint(time, brainWave.getValue()), true, 10);
                    //graphView.refreshDrawableState();

                    //deltaset.addEntry(new Entry(brainWave.getValue(),0));
                    //deltaset.setFillAlpha(120);
                    //LineData lineData = new LineData(deltaset);
                    //lineChart.setData(lineData);
                    break;
                case LOW_BETA:
                    lowbeta.appendData(new DataPoint(time, brainWave.getValue()), true, 10);
                    break;
                case LOW_GAMMA:
                    lowgamma.appendData(new DataPoint(time, brainWave.getValue()), true, 10);
                    break;
                case HIGH_ALPHA:
                    highalpha.appendData(new DataPoint(time, brainWave.getValue()), true, 10);
                    break;
                case HIGH_BETA:
                    highbeta.appendData(new DataPoint(time, brainWave.getValue()), true, 10);
                    break;
                case MID_GAMMA:
                    midgamma.appendData(new DataPoint(time, brainWave.getValue()), true, 10);
                    break;
                case THETA:
                    theta.appendData(new DataPoint(time, brainWave.getValue()), true, 10);
                    break;
                case DELTA:
                    deltaseries.appendData(new DataPoint(time, brainWave.getValue()), true, 10);
                    break;
                default:
                    break;


            }
            graphView.onDataChanged(false, false);

            //Log.d("NeuroBrainsss", String.format("%s: %d", brainWave.toString(), brainWave.getValue()));
        }
        time++;
    }
    void openDialog(){
        questionDialog dialog = new questionDialog(getApplicationContext());
        dialog.show();
    }

}