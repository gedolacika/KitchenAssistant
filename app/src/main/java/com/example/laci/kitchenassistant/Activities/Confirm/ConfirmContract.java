package com.example.laci.kitchenassistant.Activities.Confirm;

import com.example.laci.kitchenassistant.BasePresenter;
import com.google.firebase.auth.PhoneAuthCredential;

public class ConfirmContract {

    public interface View {

    }

    public interface Presenter extends BasePresenter {
        void verifyPhoneNumber();
        void signInWithPhoneAuthCredential(PhoneAuthCredential credential);
        void onDetach();
    }
}
