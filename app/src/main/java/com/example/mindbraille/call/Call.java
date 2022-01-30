package com.example.mindbraille.call;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mindbraille.R;
import com.example.mindbraille.globals.GlobalClass;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Call extends AppCompatActivity {
    boolean running = true;
    final Handler handler = new Handler();
    Thread thread;

    boolean rowwise=true;

    final Handler col_handler = new Handler();
    Thread col_thread;
    boolean col_running=true;

    int selector =0;
    int row_selector=0;
    int col_selector=0;
    TextView textView;
    Button zerobtn;
    ImageButton delbtn;
    Button callbtn;
    ImageButton phbtn;
    ImageButton homebtn;

    Button b0;
    Button b1;
    Button b2;
    Button b3;
    Button b4;
    Button b5;
    Button b6;
    Button b7;
    Button b8;
    Button b9;
    Button bstar;
    Button bhash;




    Object[][] buttons;

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

    final Runnable colRunner = new Runnable() {

        public void run() {


            if(col_selector> buttons[row_selector].length-1)
            {
                col_selector=0;
            }

            for(int i=0;i<buttons[row_selector].length;i++)
            {
                if(row_selector==0)
                {
                    ImageButton temp = (ImageButton) buttons[row_selector][i];
                    temp.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.mygrey));
                    temp.setEnabled(false);
                }
                else
                {
                    Button temp = (Button) buttons[row_selector][i];
                    if(i==buttons.length-1) temp.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.mygrey));
                    temp.setEnabled(false);
                }
            }


            if(row_selector==0 && col_selector == 0)
            {
                ImageButton temp = (ImageButton) buttons[row_selector][col_selector];
                temp.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.mygreygrey));
                temp.setEnabled(true);
            }
            else if(row_selector == 0 && col_selector == 1)
            {
                ImageButton temp = (ImageButton) buttons[row_selector][col_selector];
                temp.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.myaqua));
                temp.setEnabled(true);
            }
            else if (row_selector == 0 && col_selector == 2)
            {
                ImageButton temp = (ImageButton) buttons[row_selector][col_selector];
                temp.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.myred));
                temp.setEnabled(true);
            }
            else
            {
                Button temp = (Button) buttons[row_selector][col_selector];
                temp.setEnabled(true);
            }

            if(((GlobalClass) getApplication()).getBlinked() && (((GlobalClass) getApplication()).getBlinkValue()>50) || rowwise==true)
            {
                if(row_selector==0)
                {
                    ImageButton temp = (ImageButton) buttons[row_selector][col_selector];
                    switch(temp.getId())
                    {
                        case R.id.deletebtn:
                            phonebook pb = new phonebook();
                            pb.show(getSupportFragmentManager(),"pb");
                            //textView.setText("");
                            break;
                        case R.id.btnph:
                            finish();
                            //phonebook pb = new phonebook();
                            //pb.show(getSupportFragmentManager(),"pb");
                            break;
                        case R.id.btnback:
                            textView.setText("");

                            //finish();
                            break;
                    }
                }
                else
                {
                    Button temp = (Button) buttons[row_selector][col_selector];
                    switch(temp.getId())
                    {
                        case R.id.button0:
                            textView.append("*");
                            break;
                        case R.id.button1:
                            textView.append("3");
                            //textView.append("#");
                            break;
                        case R.id.button2:
                            textView.append("1");
                            break;
                        case R.id.button3:
                            textView.append("2");
                            break;
                        case R.id.button4:
                            textView.append("6");
                            break;
                        case R.id.button5:
                            textView.append("4");
                            break;
                        case R.id.button6:
                            textView.append("5");
                            break;
                        case R.id.button7:
                            textView.append("9");
                            break;
                        case R.id.button8:
                            textView.append("7");
                            break;
                        case R.id.button9:
                            textView.append("8");
                            break;
                        case R.id.buttonstar:

                            textView.append("#");
                            break;
                        case R.id.buttonhash:
                            textView.append("0");
                            break;
                        case R.id.buttoncall:


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
                            break;
                        default:
                            break;
                    }
                }
                ((GlobalClass) getApplication()).setBlinked(false);
                rowwise=true;

                col_selector=0;

                row_selector++;

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
                running=true;

                thread.start();
                col_thread.interrupt();

            }



            col_selector++;
        }
    };

    final Runnable updateRunner = new Runnable() {

        public void run() {

            b0 = findViewById(R.id.button0);
            b1 = findViewById(R.id.button1);
            b2 = findViewById(R.id.button2);
            b3 = findViewById(R.id.button3);
            b4 = findViewById(R.id.button4);
            b5 = findViewById(R.id.button5);
            b6 = findViewById(R.id.button6);
            b7 = findViewById(R.id.button7);
            b8 = findViewById(R.id.button8);
            b9 = findViewById(R.id.button9);
            bstar = findViewById(R.id.buttonstar);
            bhash = findViewById(R.id.buttonhash);
            buttons = new Object[][]{{homebtn, phbtn, delbtn}, {b1, b2, b3}, {b4, b5, b6}, {b7, b8, b9}, {bstar, b0, bhash}, {callbtn}};



            if(row_selector>5 || row_selector<0)
            {row_selector=0;}

            for(int i=0;i<buttons.length;i++)
            {
                for(int j=0;j<buttons[i].length;j++)
                {
                    if(i==0)
                    {
                        ImageButton temp = (ImageButton) buttons[i][j];
                        temp.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.mygrey));
                        temp.setEnabled(false);
                    }
                    else
                    {
                        Button temp = (Button) buttons[i][j];
                        if(i==buttons.length-1) temp.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.mygrey));
                        temp.setEnabled(false);
                    }
                }
            }

            if(row_selector==0)
            {
                for(int i=0;i<buttons[row_selector].length;i++)
                {
                    ImageButton temp = (ImageButton) buttons[row_selector][i];

                    if(i == 0)
                    {
                        temp.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.mygreygrey));
                    }
                    else if(i == 1)
                    {
                        temp.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.myaqua));
                    }
                    else temp.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.myred));
                    temp.setEnabled(true);
                }

            }
            else
            {
                for(int i=0;i<buttons[row_selector].length;i++)
                {
                    Button temp = (Button) buttons[row_selector][i];
                    if(row_selector == buttons.length-1)
                    {
                        temp.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.mygreen));

                    }

                    temp.setEnabled(true);
                }
            }




            if(((GlobalClass) getApplication()).getBlinked() && (((GlobalClass) getApplication()).getBlinkValue()>50))
            {
                ((GlobalClass)getApplication()).setBlinked(false);
                col_selector=0;
                row_selector--;
                col_running=true;
                col_thread = new Thread(new Runnable() {
                    public void run() {

                        while (col_running) {
                            try {
                                Thread.sleep((long) (1000 - (((GlobalClass) getApplication()).getConcentrationValue())));
                                col_handler.post(colRunner);
                            }
                            catch (InterruptedException e){
                                col_running=false;
                            }

                        }
                    }
                });

                col_thread.start();

                thread.interrupt();

            }

            Log.d("In Call blink", String.format("%d", ((GlobalClass) getApplication()).getBlinkValue()));

            if(!running) {
                return;
            }

            row_selector++;
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

        if(rowwise)
        {
            rowwise=false;
            col_selector=0;
            row_selector--;
            col_running=true;
            col_thread = new Thread(new Runnable() {
                public void run() {

                    while (col_running) {
                        try {
                            Thread.sleep((long) (1000 - (((GlobalClass) getApplication()).getConcentrationValue())));
                            col_handler.post(colRunner);
                        }
                        catch (InterruptedException e){
                            col_running=false;
                        }

                    }
                }
            });

            col_thread.start();

            thread.interrupt();
        }
        else
        {
            rowwise=true;
        }

    }
}