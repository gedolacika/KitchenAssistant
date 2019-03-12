package com.example.laci.kitchenassistant.main.Service;

import android.app.Activity;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import com.example.laci.kitchenassistant.Tools.StepCounterForService;
import com.example.laci.kitchenassistant.Tools.StepCounterForUI;

public class StepCounter extends Service {
    private Activity activity = null;
    private Context context = null;
    private StepCounterForService stepCounterForUI;
    public static final int JOB_ID = 1;




    @Override
    public void onCreate() {
        super.onCreate();

    }

    public StepCounter(){

    }
    public StepCounter(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }
    public StepCounter(Context context) {
        this.context = context;
    }

   /* @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }*/



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder builder = new Notification.Builder(this, "Cica")
                    .setContentTitle("wifi listener bla bla bla")
                    .setContentText("Cica")
                    .setAutoCancel(true);

            Notification notification = builder.build();
            startForeground(1, notification);
        }
        stepCounterForUI = new StepCounterForService(this);
        stepCounterForUI.connectFitness();
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");
        Intent broadcastIntent = new Intent(this, SensorRestarterBroadcastReceiver.class);

        sendBroadcast(broadcastIntent);

        //stoptimertask();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
