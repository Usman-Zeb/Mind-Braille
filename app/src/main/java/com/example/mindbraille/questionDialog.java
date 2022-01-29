package com.example.mindbraille;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;

public class questionDialog extends AppCompatDialog {
    public questionDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getOwnerActivity());
        builder.setTitle("Alert").setMessage("Do you want to continue without connecting EEG headset?").setPositiveButton("Yes", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setNegativeButton("No", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        super.onCreate(savedInstanceState);
    }
}
