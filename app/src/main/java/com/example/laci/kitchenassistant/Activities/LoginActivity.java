package com.example.laci.kitchenassistant.Activities;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.laci.kitchenassistant.R;
import com.example.laci.kitchenassistant.Tools.Validations;

import java.util.Objects;

import static com.example.laci.kitchenassistant.Tools.ActivityNavigation.navigateToConfirm;

public class LoginActivity extends AppCompatActivity {
    private Button button;
    private TextInputEditText phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        button = findViewById(R.id.login_button);
        phoneNumber = findViewById(R.id.login_text_input_edit_text);
        setTitle("Login");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int validation_result = Validations.validatePhoneNumber(phoneNumber.getText().toString());

                if(validation_result==0)
                    navigateToConfirm(LoginActivity.this,Objects.requireNonNull(phoneNumber.getText()).toString());
                if(validation_result==1)
                    phoneNumber.setError("The length of phone number should be 12 characters!");
                if(validation_result==2)
                    phoneNumber.setError("The phone number have to be like this: \"+[country code][your number]\".");
            }
        });
    }
}
