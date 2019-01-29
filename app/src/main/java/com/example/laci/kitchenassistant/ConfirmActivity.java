package com.example.laci.kitchenassistant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.laci.kitchenassistant.Tools.ActivityNavigation;

public class ConfirmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        TextView textview = findViewById(R.id.textView);
        textview.setText(ActivityNavigation.getPhoneNumberFromIntent(getIntent()));
    }
}
