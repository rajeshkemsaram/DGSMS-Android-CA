package com.ideabytes.dgsms.ca.fcm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


import com.ideabytes.dgsms.landstar.R;



public class ResultActivity extends AppCompatActivity {
TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        textView = (TextView)findViewById(R.id.textView1);
        textView.setText("Welcome to the Result Activity");

    }
}