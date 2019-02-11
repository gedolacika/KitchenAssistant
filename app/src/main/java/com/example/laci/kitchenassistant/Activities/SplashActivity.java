package com.example.laci.kitchenassistant.Activities;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.laci.kitchenassistant.R;
import com.google.firebase.auth.FirebaseAuth;

import static com.example.laci.kitchenassistant.Tools.ActivityNavigation.navigateToLogin;
import static com.example.laci.kitchenassistant.Tools.ActivityNavigation.navigateToWelcome;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //waiting 2 seconds and after it the app will throw us to the login screen
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    navigateToLogin(SplashActivity.this);
                } else {
                    //THIS SHOULD BE HANDLE
                    navigateToWelcome(SplashActivity.this);
                }

            }
        }, 2000);
    }
}
