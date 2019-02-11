package com.example.laci.kitchenassistant.Activities;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.laci.kitchenassistant.BaseClasses.Recipe;
import com.example.laci.kitchenassistant.R;
import com.example.laci.kitchenassistant.firebase.Account;

import java.util.ArrayList;

import static com.example.laci.kitchenassistant.Tools.ActivityNavigation.navigateToMain;

public class WelcomeActivity extends AppCompatActivity {
    private ImageView cover,profile,food;
    private TextView foodName,tip;
    private ArrayList<Recipe> recipes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        food = findViewById(R.id.welcome_food_image);
        cover = findViewById(R.id.welcome_cover);
        profile = findViewById(R.id.welcome_profile);

        Account.downloadProfilePicture(profile,this);
        Account.downloadCoverPhoto(cover,this);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                navigateToMain(WelcomeActivity.this);

            }
        }, 5000);

    }
}
