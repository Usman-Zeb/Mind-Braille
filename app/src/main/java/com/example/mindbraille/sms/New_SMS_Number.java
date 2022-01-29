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

public class New_SMS_Number extends AppCompatActivity {
    public TextView textView;
    final Handler handler = new Handler();
    Thread thread;
    int selector =0;
    boolean running = true;
    ImageButton delbtn;
    ImageButton backbtn;
    ImageButton phbtn;

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
                        Thread.sleep(1000);
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

    final Runnable updateRunner = new Runnable() {

        public void run() {

            final Object[] kb_buttons = {
                    backbtn, phbtn, delbtn,
                    findViewById(R.id.sms_button1),
                    findViewById(R.id.sms_button2),
                    findViewById(R.id.sms_button3),
                    findViewById(R.id.sms_button4),
                    findViewById(R.id.sms_button5),
                    findViewById(R.id.sms_button6),
                    findViewById(R.id.sms_button7),
                    findViewById(R.id.sms_button8),
                    findViewById(R.id.sms_button9),
                    findViewById(R.id.sms_buttonstar),
                    findViewById(R.id.sms_button0),
                    findViewById(R.id.sms_buttonhash),
                    findViewById(R.id.sms_buttonOk)
            };

            //Object[] objects = {imageButtons, kb_buttons};

            if(selector>15)
            {selector=0;}

            for(int i=0; i<=15; i++)
            {
                if(i<3)
                {
                    ImageButton temp = (ImageButton) kb_buttons[i];
                    temp.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.mygrey));
                    //temp.setColorFilter(Color.parseColor("#D3D3D3"));
                    //temp.setBackgroundColor(Color.parseColor("#D3D3D3"));
                    temp.setEnabled(false);
                }
                else
                {
                    Button temp = (Button) kb_buttons[i];
                    if(i==15) temp.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.mygrey));
                    temp.setEnabled(false);
                }
            }

            if(selector < 3)
            {
                ImageButton temp = (ImageButton) kb_buttons[selector];
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
                Button temp = (Button) kb_buttons[selector];
                if(selector == 15)
                {
                    temp.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.mygreen));

                }

                temp.setEnabled(true);
            }


            if(((GlobalClass) getApplication()).getBlinked() && (((GlobalClass) getApplication()).getBlinkValue()>70))
            {
                if(selector<3)
                {
                    ImageButton temp = (ImageButton) kb_buttons[selector];
                    switch(temp.getId())
                    {
                        case R.id.new_sms_num_delete:
                            phonebook pb = new phonebook();
                            pb.show(getSupportFragmentManager(),"pb");
                            //textView.setText("");
                            break;
                        case R.id.new_sms_num_phonebook:
                            finish();
                            /*phonebook pb = new phonebook();
                            pb.show(getSupportFragmentManager(),"pb");*/
                            break;
                        case R.id.new_sms_num_back:
                            String number = textView.getText().toString();
                            if(number.isEmpty())
                            {
                                Toast.makeText(getApplicationContext(), "Please Enter a Number!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Intent intent = new Intent(getApplicationContext(), New_SMS_Text.class);
                                startActivity(intent);
                            }
                            //finish();
                            break;
                    }
                }
                else
                {
                    Button temp = (Button) kb_buttons[selector];
                    switch(temp.getId())
                    {
                        case R.id.sms_button0:
                            textView.append("*");
                            break;
                        case R.id.sms_button1:
                            textView.setText("");
                            //textView.append("#");
                            break;
                        case R.id.sms_button2:
                            textView.append("1");
                            break;
                        case R.id.sms_button3:
                            textView.append("2");
                            break;
                        case R.id.sms_button4:
                            textView.append("3");
                            break;
                        case R.id.sms_button5:
                            textView.append("4");
                            break;
                        case R.id.sms_button6:
                            textView.append("5");
                            break;
                        case R.id.sms_button7:
                            textView.append("6");
                            break;
                        case R.id.sms_button8:
                            textView.append("7");
                            break;
                        case R.id.sms_button9:
                            textView.append("8");
                            break;
                        case R.id.sms_buttonstar:
                            textView.append("9");
                            break;
                        case R.id.sms_buttonhash:
                            textView.append("0");
                            break;
                        case R.id.sms_buttonOk:
                            textView.append("#");
                            break;
                        default:
                            break;
                    }
                }


                ((GlobalClass) getApplication()).setBlinked(false);
            }

            selector++;
        }
    };
}