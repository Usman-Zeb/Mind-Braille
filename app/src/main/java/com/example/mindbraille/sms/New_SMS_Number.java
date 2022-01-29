package com.example.mindbraille.sms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mindbraille.R;
import com.example.mindbraille.call.phonebook;
import com.example.mindbraille.globals.GlobalClass;

import static java.lang.Thread.sleep;

public class New_SMS_Number extends AppCompatActivity {
    public TextView textView;
    final Handler handler = new Handler();
    Thread thread;
    int selector =0;
    volatile boolean running = true;
    ImageButton delbtn;
    ImageButton backbtn;
    ImageButton phbtn;


    final Handler col_handler = new Handler();
    Thread col_thread;
    boolean col_running=true;

    int row_selector=0;
    int col_selector=0;


    Object[][] kb_buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__s_m_s__number);
        textView = findViewById(R.id.new_sms_num_enter_box);
        delbtn = findViewById(R.id.new_sms_num_delete);
        phbtn = findViewById(R.id.new_sms_num_phonebook);
        backbtn = findViewById(R.id.new_sms_num_back);
    }

    public void dialdigit(View view)
    {
        switch(view.getId())
        {
            case R.id.sms_button0:
                textView.append("0");
                break;
            case R.id.sms_button1:
                textView.append("1");
                break;
            case R.id.sms_button2:
                textView.append("2");
                break;
            case R.id.sms_button3:
                textView.append("3");
                break;
            case R.id.sms_button4:
                textView.append("4");
                break;
            case R.id.sms_button5:
                textView.append("5");
                break;
            case R.id.sms_button6:
                textView.append("6");
                break;
            case R.id.sms_button7:
                textView.append("7");
                break;
            case R.id.sms_button8:
                textView.append("8");
                break;
            case R.id.sms_button9:
                textView.append("9");
                break;
            case R.id.sms_buttonhash:
                textView.append("#");
                break;
            case R.id.sms_buttonstar:
                textView.append("*");
                break;
            case R.id.new_sms_num_phonebook:
                phonebook pb = new phonebook();
                pb.show(getSupportFragmentManager(),"pb");
                break;
            case R.id.sms_buttonOk:
                Intent intent = new Intent(getApplicationContext(), New_SMS_Text.class);
                intent.putExtra("Number", textView.getText().toString());
                startActivity(intent);
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((GlobalClass) this.getApplication()).setInNewSMS(true);

        running=true;
        thread = new Thread(new Runnable() {
            public void run() {

                while (running) {
                    try {
                        sleep(1000);
                        handler.post(updateRunner);

                    }
                    catch (InterruptedException e)
                    {
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
        ((GlobalClass) this.getApplication()).setInNewSMS(false);
        thread.interrupt();
    }

    final Runnable colRunner = new Runnable() {

        public void run() {

            if(col_selector> kb_buttons[row_selector].length-1)
            {
                col_selector=0;
            }
            if(row_selector> 5) row_selector = 0;

            for(int i=0;i<kb_buttons[row_selector].length;i++)
            {
                if(row_selector==0)
                {
                    ImageButton temp = (ImageButton) kb_buttons[row_selector][i];
                    temp.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.mygrey));
                    temp.setEnabled(false);
                }
                else
                {
                    Button temp = (Button) kb_buttons[row_selector][i];
                    if(i==kb_buttons.length-1) temp.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.mygrey));
                    temp.setEnabled(false);
                }
            }


            if(row_selector==0 && col_selector == 0)
            {
                ImageButton temp = (ImageButton) kb_buttons[row_selector][col_selector];
                temp.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.mygreygrey));
                temp.setEnabled(true);
            }
            else if(row_selector == 0 && col_selector == 1)
            {
                ImageButton temp = (ImageButton) kb_buttons[row_selector][col_selector];
                temp.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.myaqua));
                temp.setEnabled(true);
            }
            else if (row_selector == 0 && col_selector == 2)
            {
                ImageButton temp = (ImageButton) kb_buttons[row_selector][col_selector];
                temp.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.myred));
                temp.setEnabled(true);
            }
            else
            {
                Button temp = (Button) kb_buttons[row_selector][col_selector];
                temp.setEnabled(true);
            }

            if(((GlobalClass) getApplication()).getBlinked() && (((GlobalClass) getApplication()).getBlinkValue()>50))
            {
                if(row_selector==0)
                {
                    ImageButton temp = (ImageButton) kb_buttons[row_selector][col_selector];
                    switch(temp.getId())
                    {
                        case R.id.new_sms_num_delete:
                            phonebook pb = new phonebook();
                            pb.show(getSupportFragmentManager(), "pb");
                            //textView.setText("");
                            break;
                        case R.id.new_sms_num_phonebook:
                            finish();
                            //phonebook pb = new phonebook();
                            //pb.show(getSupportFragmentManager(),"pb");
                            break;
                        case R.id.new_sms_num_back:
                            textView.setText("");
                            //finish();
                            break;
                    }
                }
                else
                {
                    Button temp = (Button) kb_buttons[row_selector][col_selector];
                    switch(temp.getId())
                    {
                        case R.id.sms_button0:
                            textView.append("*");
                            break;
                        case R.id.sms_button1:
                            textView.setText("3");
                            //textView.append("#");
                            break;
                        case R.id.sms_button2:
                            textView.append("1");
                            break;
                        case R.id.sms_button3:
                            textView.append("2");
                            break;
                        case R.id.sms_button4:
                            textView.append("6");
                            break;
                        case R.id.sms_button5:
                            textView.append("4");
                            break;
                        case R.id.sms_button6:
                            textView.append("5");
                            break;
                        case R.id.sms_button7:
                            textView.append("9");
                            break;
                        case R.id.sms_button8:
                            textView.append("7");
                            break;
                        case R.id.sms_button9:
                            textView.append("8");
                            break;
                        case R.id.sms_buttonstar:
                            textView.append("#");
                            break;
                        case R.id.sms_buttonhash:
                            textView.append("0");
                            break;
                        case R.id.sms_buttonOk:
                            String number = textView.getText().toString();
                            if (number.isEmpty()) {
                                Toast.makeText(getApplicationContext(), "Please Enter a Number!", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(getApplicationContext(), New_SMS_Text.class);
                                intent.putExtra("Number", textView.getText().toString());
                                startActivity(intent);
                            }
                            break;
                        default:
                            break;
                    }
                }
                ((GlobalClass) getApplication()).setBlinked(false);

                col_selector=0;
                row_selector++;

                thread = new Thread(new Runnable() {
                    public void run() {

                        while (running) {
                            try {
                                sleep((long) (1000 - (((GlobalClass) getApplication()).getConcentrationValue())));
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

            kb_buttons = new Object[][]{
                    {backbtn, phbtn, delbtn},
                    {findViewById(R.id.sms_button1),
                            findViewById(R.id.sms_button2),
                            findViewById(R.id.sms_button3)},
                    {findViewById(R.id.sms_button4),
                            findViewById(R.id.sms_button5),
                            findViewById(R.id.sms_button6)},
                    {findViewById(R.id.sms_button7),
                            findViewById(R.id.sms_button8),
                            findViewById(R.id.sms_button9)},
                    {findViewById(R.id.sms_buttonstar),
                            findViewById(R.id.sms_button0),
                            findViewById(R.id.sms_buttonhash)},
                    {findViewById(R.id.sms_buttonOk)}};

            //Object[] objects = {imageButtons, kb_buttons};

            if (row_selector > 5 || row_selector < 0) {
                row_selector = 0;
            }

            for (int i = 0; i < kb_buttons.length; i++) {
                for (int j = 0; j < kb_buttons[i].length; j++) {
                    if (i == 0) {
                        ImageButton temp = (ImageButton) kb_buttons[i][j];
                        temp.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.mygrey));
                        temp.setEnabled(false);
                    } else {
                        Button temp = (Button) kb_buttons[i][j];
                        if (i == kb_buttons.length - 1)
                            temp.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.mygrey));
                        temp.setEnabled(false);
                    }
                }
            }

            if (row_selector == 0) {
                for (int i = 0; i < kb_buttons[row_selector].length; i++) {
                    ImageButton temp = (ImageButton) kb_buttons[row_selector][i];

                    if (i == 0) {
                        temp.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.mygreygrey));
                    } else if (i == 1) {
                        temp.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.myaqua));
                    } else
                        temp.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.myred));
                    temp.setEnabled(true);
                }

            } else {
                for (int i = 0; i < kb_buttons[row_selector].length; i++) {
                    Button temp = (Button) kb_buttons[row_selector][i];
                    if (row_selector == kb_buttons.length - 1) {
                        temp.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.mygreen));

                    }

                    temp.setEnabled(true);
                }
            }



            if (((GlobalClass) getApplication()).getBlinked() && (((GlobalClass) getApplication()).getBlinkValue()>50)) {

                ((GlobalClass) getApplication()).setBlinked(false);
                col_selector=0;
                row_selector--;
                col_running=true;
                col_thread = new Thread(new Runnable() {
                    public void run() {

                        while (col_running) {
                            try {
                                sleep((long) (1000 - (((GlobalClass) getApplication()).getConcentrationValue())));
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

            if(!running) {
                return;
            }
            row_selector++;



        }

    };

}