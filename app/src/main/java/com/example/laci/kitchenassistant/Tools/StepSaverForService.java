package com.example.laci.kitchenassistant.Tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.laci.kitchenassistant.BaseClasses.StepCount;
import com.example.laci.kitchenassistant.firebase.Account;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class StepSaverForService {
    private static final String TIME = "Time";
    private static final String STEP = "Step";
    private static String ACCOUNT_STEP_BASE = "Steps";
    private static String ACCOUNT_STEP_TIME = "Time";
    private static String ACCOUNT_STEP_STEP = "Step";

    public static void saveSteps(Context context, StepCount stepCount){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);


        StepCount lastStepCount = new StepCount(prefs.getLong(TIME,-1),prefs.getInt(STEP,-1));

        if(lastStepCount.getSteps() == -1 || lastStepCount.getTime() == -1){
            editor.putLong(TIME,(stepCount.getTime() - (stepCount.getTime() % 300000)));
            editor.putInt(STEP,stepCount.getSteps());
            editor.apply();
            Log.i("StepSaver","File empty, saving the first step: " + stepCount.getSteps() + " - " + stepCount.getTime());
        }else{
            if((stepCount.getTime() - lastStepCount.getTime())>= 300000){
                //lastStepCount.setSteps(lastStepCount.getSteps()/2);

                lastStepCount.setSteps(lastStepCount.getSteps());
                saveStepsToFirebase(lastStepCount);
                editor.putLong(TIME,(stepCount.getTime() - (stepCount.getTime() % 300000)));
                editor.putInt(STEP,stepCount.getSteps());
                editor.apply();
                Log.i("StepSaver","Saving to firebase: " + lastStepCount.getSteps() + " - " + lastStepCount.getTime());
            }else{
                Log.i("StepSaver","Save to file - all steps: " + stepCount.getSteps()+lastStepCount.getSteps() + " - " + lastStepCount.getTime());
                editor.putInt(STEP,stepCount.getSteps()+lastStepCount.getSteps());
                editor.apply();
            }
        }
    }

    public static void saveStepsToFirebase(final StepCount stepCount){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users/" + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        final long current_day = stepCount.getTime() - (stepCount.getTime()%(24*60*60*1000));
        if(stepCount.getTime() != -1) databaseReference.child(ACCOUNT_STEP_BASE).child(String.valueOf(current_day)).child(String.valueOf(stepCount.getTime())).child(ACCOUNT_STEP_TIME).setValue(stepCount.getTime(), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.e("STEPS-TIME ->FIREBASE","Data(time) could not be saved. Error message: " + databaseError.getMessage());
                } else {
                    Log.e("STEPS-TIME ->FIREBASE","We save the time to the firebase -  " + stepCount.getTime());
                }
            }
        });
        if(stepCount.getSteps() != -1) databaseReference.child(ACCOUNT_STEP_BASE).child(String.valueOf(current_day)).child(String.valueOf(stepCount.getTime())).child(ACCOUNT_STEP_STEP).setValue(stepCount.getSteps(), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.e("STEPS-STEPS ->FIREBASE","Data(steps) could not be saved. Error message:  " + databaseError.getMessage());
                } else {
                    Log.e("STEPS-STEPS ->FIREBASE","We save data to the cloud with: " + stepCount.getSteps() + " steps.");
                }

            }
        });
    }
}




