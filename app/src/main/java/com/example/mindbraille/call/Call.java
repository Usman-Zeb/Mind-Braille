package com.example.mindbraille.call;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mindbraille.R;
import com.example.mindbraille.globals.GlobalClass;

import java.util.ArrayList;

public class Call extends AppCompatActivity {
    boolean running = true;
    final Handler handler = new Handler();
    Thread thread;
    int selector =0;

    TextView textView;
    Button zerobtn;
    ImageButton delbtn;
    Button callbtn;
    ImageButton phbtn;
    ImageButton homebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_call);

        textView = findViewById(R.id.numberdialed);
        zerobtn = findViewById(R.id.button0);
        callbtn = findViewById(R.id.buttoncall);
        phbtn = findViewById(R.id.btnph);
        homebtn = findViewById(R.id.btnback);

        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });


        zerobtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                textView.append("+");
                return true;
            }
        });

        zerobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.append("0");
            }
        });

        delbtn = findViewById(R.id.deletebtn);
        delbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = textView.getText().toString();
                if(textView.getText().length()>0)
                    textView.setText(temp.toCharArray(), 0, textView.getText().length()-1);
            }
        });

        delbtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                textView.setText("");
                return true;
            }
        });

        callbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = textView.getText().toString();
                if(number.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Enter a Number!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String s = "tel:" + Uri.encode(number);
                    Log.d("This string s", s);
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse(s));
                    startActivity(intent);
                }
            }
        });

        phbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phonebook pb = new phonebook();
                pb.show(getSupportFragmentManager(),"pb");

            }
        });

        requestperms();



    }

    @Override
    protected void onResume() {
        super.onResume();
        running = true;

        ((GlobalClass) this.getApplication()).setInCallMenu(true);

        thread = new Thread(new Runnable() {
            public void run() {

                while (running) {
                    try {
                        Thread.sleep((long) (1000 - (((GlobalClass) getApplication()).getConcentrationValue())));
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

            ImageButton[] imageButtons = {homebtn,phbtn,delbtn};


            final Button b0 = findViewById(R.id.button0);
            final Button b1 = findViewById(R.id.button1);
            final Button b2 = findViewById(R.id.button2);
            final Button b3 = findViewById(R.id.button3);
            final Button b4 = findViewById(R.id.button4);
            final Button b5 = findViewById(R.id.button5);
            final Button b6 = findViewById(R.id.button6);
            final Button b7 = findViewById(R.id.button7);
            final Button b8 = findViewById(R.id.button8);
            final Button b9 = findViewById(R.id.button9);
            final Button bstar = findViewById(R.id.buttonstar);
            final Button bhash = findViewById(R.id.buttonhash);




            Object[] buttons = {homebtn,phbtn,delbtn,b1,b2,b3,b4,b5,b6,b7,b8,b9,bstar,b0,bhash, callbtn};
            ArrayList<Object> all_bts = new ArrayList<>();
            all_bts.add(imageButtons);
            all_bts.add(buttons);



            if(selector>15)
            {selector=0;}
            for(int i=0;i<=15;i++)
            {
                if(i<3)
                {
                    ImageButton temp = (ImageButton) buttons[i];
                    temp.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.mygrey));
                    //temp.setColorFilter(Color.parseColor("#D3D3D3"));
                    //temp.setBackgroundColor(Color.parseColor("#D3D3D3"));
                    temp.setEnabled(false);
                }
                else
                {
                    Button temp = (Button) buttons[i];
                    if(i==15) temp.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.mygrey));
                    temp.setEnabled(false);
                }

            }

            Log.d("In Call blink", String.format("%d", ((GlobalClass) getApplication()).getBlinkValue()));
            if(selector < 3)
            {
                ImageButton temp = (ImageButton) buttons[selector];
                if(selector == 0)
                {
                    temp.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.mygreygrey));
                }
                else if(selector == 1)
                {
                    temp.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.myaqua));
                }
                else temp.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.myred));
                temp.setEnabled(true);
            }
            else
            {
                Button temp = (Button) buttons[selector];
                if(selector == 15)
                {
                    temp.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.mygreen));

                }

                temp.setEnabled(true);
            }


            if(((GlobalClass) getApplication()).getBlinked() && (((GlobalClass) getApplication()).getBlinkValue()>50))
            {
                if(selector<3)
                {
                    ImageButton temp = (ImageButton) buttons[selector];
                    switch(temp.getId())
                    {
                        case R.id.deletebtn:
                            phonebook pb = new phonebook();
                            pb.show(getSupportFragmentManager(),"pb");
                            //textView.setText("");
                            break;
                        case R.id.btnph:
                            finish();
                            /*phonebook pb = new phonebook();
                            pb.show(getSupportFragmentManager(),"pb");*/
                            break;
                        case R.id.btnback:
                            String number = textView.getText().toString();
                            if(number.isEmpty())
                            {
                                Toast.makeText(getApplicationContext(), "Please Enter a Number!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                String s = "tel:" + Uri.encode(number);
                                Log.d("This string s", s);
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse(s));
                                startActivity(intent);
                            }
                            //finish();
                            break;
                    }
                }
                else
                {
                    Button temp = (Button) buttons[selector];
                    switch(temp.getId())
                    {
                        case R.id.button0:
                            textView.append("*");
                            break;
                        case R.id.button1:
                            textView.setText("");
                            //textView.append("#");
                            break;
                        case R.id.button2:
                            textView.append("1");
                            break;
                        case R.id.button3:
                            textView.append("2");
                            break;
                        case R.id.button4:
                            textView.append("3");
                            break;
                        case R.id.button5:
                            textView.append("4");
                            break;
                        case R.id.button6:
                            textView.append("5");
                            break;
                        case R.id.button7:
                            textView.append("6");
                            break;
                        case R.id.button8:
                            textView.append("7");
                            break;
                        case R.id.button9:
                            textView.append("8");
                            break;
                        case R.id.buttonstar:
                            textView.append("9");
                            break;
                        case R.id.buttonhash:
                            textView.append("0");
                            break;
                        case R.id.buttoncall:
                            textView.append("#");
                            /*
                            String number = textView.getText().toString();
                            if(number.isEmpty())
                            {
                                Toast.makeText(getApplicationContext(), "Please Enter a Number!", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                String s = "tel:" + Uri.encode(number);
                                Log.d("This string s", s);
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse(s));
                                startActivity(intent);

                            }*/
                            break;
                        default:
                            break;
                    }
                }


                ((GlobalClass) getApplication()).setBlinked(false);

            }
            //Log.d("call","69");
            selector++;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }



    private  void requestperms()
    {
        ActivityCompat.requestPermissions(Call.this, new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_CONTACTS},1);
    }




    public void dialdigit(View view)
    {
        switch(view.getId())
        {
            case R.id.button0:
                textView.append("0");
                break;
            case R.id.button1:
                textView.append("1");
                break;
            case R.id.button2:
                textView.append("2");
                break;
            case R.id.button3:
                textView.append("3");
                break;
            case R.id.button4:
                textView.append("4");
                break;
            case R.id.button5:
                textView.append("5");
                break;
            case R.id.button6:
                textView.append("6");
                break;
            case R.id.button7:
                textView.append("7");
                break;
            case R.id.button8:
                textView.append("8");
                break;
            case R.id.button9:
                textView.append("9");
                break;
            case R.id.buttonhash:
                textView.append("#");
                break;
            case R.id.buttonstar:
                textView.append("*");
                break;

        }

    }
}