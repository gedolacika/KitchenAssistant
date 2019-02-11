package com.example.laci.kitchenassistant.Activities.Confirm;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.laci.kitchenassistant.R;
import com.example.laci.kitchenassistant.Tools.ActivityNavigation;

public class ConfirmActivity extends AppCompatActivity implements ConfirmContract.View {
    private ConfirmPresenter presenter;
    private Button button;
    private TextInputEditText inputVerificationCode;
    private String phoneNumber;
    private TextView phoneNumberTextView;
    private TextView onCodeSentTextView;
    private MaterialDialog materialDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        initViews();
        materialDialog = new MaterialDialog.Builder(this)
                .content("Loading...")
                .autoDismiss(false)
                .cancelable(false)
                .progress(true,0)
                .build();
        presenter = new ConfirmPresenter(this,phoneNumber,this,onCodeSentTextView,materialDialog,inputVerificationCode);

        presenter.verifyPhoneNumber();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.tryTheCode();
            }
        });



    }

    private void initViews() {
        button = findViewById(R.id.confirm_button);
        inputVerificationCode = findViewById(R.id.confirm_text_input);

        phoneNumber = ActivityNavigation.getPhoneNumberFromIntent(getIntent());
        phoneNumberTextView = findViewById(R.id.confirm_phone_number);
        phoneNumberTextView.setText(phoneNumber);

        onCodeSentTextView = findViewById(R.id.confirm_on_code_sent_text_view);

    }

}
