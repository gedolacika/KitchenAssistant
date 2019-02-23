package com.example.laci.kitchenassistant.Activities.Confirm;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.laci.kitchenassistant.Tools.ActivityNavigation;
import com.example.laci.kitchenassistant.Tools.Validations;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

public class ConfirmPresenter implements ConfirmContract.Presenter {

    private ConfirmContract.View view;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String TAD = "CICAAA";
    private String phoneNumber;
    private Activity activity;
    private String mVerificationId;
    private MaterialDialog materialDialog;
    private TextView onCodeSentTextView;
    private TextInputEditText verificationCodeFromUser;

    public ConfirmPresenter(ConfirmContract.View view, String phoneNumber, Activity activity, TextView onCodeSentTextView, MaterialDialog materialDialog, TextInputEditText textInputEditText) {
        this.phoneNumber = phoneNumber;
        this.activity = activity;
        this.view = view;
        this.materialDialog = materialDialog;
        this.onCodeSentTextView = onCodeSentTextView;
        this.verificationCodeFromUser = textInputEditText;
    }


    public void verifyPhoneNumber() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                activity,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential credential) {
                        //Invoked at instant verifications.
                        Log.d(TAG, "onVerificationCompleted:" + credential);
                        materialDialog.dismiss();
                        signInWithPhoneAuthCredential(credential);

                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        // This callback is invoked in an invalid request for verification is made,
                        // for instance if the the phone number format is not valid.
                        Log.w(TAG, "onVerificationFailed", e);
                        Toast.makeText(activity,e.getMessage(),Toast.LENGTH_SHORT).show();
                        materialDialog.dismiss();
                    }

                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken token) {
                        // The SMS verification code has been sent to the provided phone number, we
                        // now need to ask the user to enter the code and then construct a credential
                        // by combining the code with a verification ID.
                        Log.d(TAG, "onCodeSent:" + verificationId);

                        // Save verification ID and resending token so we can use them later
                        onCodeSentTextView.setText("The code has been sent.");
                        onCodeSentTextView.setTextColor(Color.GREEN);
                        mVerificationId = verificationId;
                    }
                })
        ;
    }


    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this.activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            //FirebaseUser user = task.getResult().getUser();
                            ActivityNavigation.navigateToMain(activity);
                            materialDialog.dismiss();

                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                verificationCodeFromUser.setError("The verification code is invalid.");
                            }
                            materialDialog.dismiss();
                        }

                    }
                });
    }

    public void tryTheCode() {
        materialDialog.show();
        if (Validations.validateVerificationCode(Objects.requireNonNull(verificationCodeFromUser.getText()).toString())) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCodeFromUser.getText().toString());
            signInWithPhoneAuthCredential(credential);
        } else {
            verificationCodeFromUser.setError("The code is not valid.");
            materialDialog.dismiss();
        }
    }

    @Override
    public void onDetach() {
        view = null;
    }

}
