package com.example.laci.kitchenassistant.Tools;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.laci.kitchenassistant.BaseClasses.StepCount;
import com.example.laci.kitchenassistant.main.MainActivity;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class StepSaverForUI {
    private static final String FILE_NAME = "StepCounting";
    private static final String TIME = "Time";
    private static final String STEP = "Step";
    private static long ONE_DAY_IN_MILLIS = 24*60*60*1000;

    public static void saveStep(Context context, StepCount stepCount){
        SharedPreferences.Editor editor = context.getSharedPreferences(FILE_NAME,MODE_PRIVATE).edit();
        SharedPreferences prefs = context.getSharedPreferences(FILE_NAME,MODE_PRIVATE);


            StepCount lastStepCount = new StepCount(prefs.getLong(TIME,-1),prefs.getInt(STEP,-1));

            if(lastStepCount.getSteps() == -1 || lastStepCount.getTime() == -1){
                editor.putLong(TIME,(stepCount.getTime() - (stepCount.getTime() % 300000)));
                editor.putInt(STEP,stepCount.getSteps());
                editor.apply();
            }else{
                if((stepCount.getTime() - lastStepCount.getTime())>= 300000){
                    //lastStepCount.setSteps(lastStepCount.getSteps()/2);
                    //TODO SHOULD TO UPDATE THE MAIN ACTIVITIES STEPS
                    editor.putLong(TIME,(stepCount.getTime() - (stepCount.getTime() % 300000)));
                    editor.putInt(STEP,stepCount.getSteps());
                    editor.apply();
                }else{

                    editor.putInt(STEP,stepCount.getSteps()+lastStepCount.getSteps());
                    editor.apply();
                }
            }

        }


    public static int getSteps(Activity activity, String FILE_NAME, ArrayList<StepCount> stepCounts){
        SharedPreferences prefs = activity.getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        StepCount lastStepCount = new StepCount(prefs.getLong(TIME,-1),prefs.getInt(STEP,-1));
        int steps = lastStepCount.getSteps()/2;
        if(stepCounts!= null){
            for(int i = 0; i < stepCounts.size(); ++i){
                steps += stepCounts.get(i).getSteps();
            }
        }else{
            Log.e("NULL EXCEPTION","THE STEP COUNTS FROM CLOUD IS NULL");
        }
        return steps;
    }
}
