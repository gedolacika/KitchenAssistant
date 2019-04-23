package com.example.laci.kitchenassistant.main.Service;

import android.app.Activity;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.service.autofill.BatchUpdates;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import com.example.laci.kitchenassistant.BaseClasses.StepCount;
import com.example.laci.kitchenassistant.Tools.StepSaverForService;

import java.util.Objects;
import java.util.Timer;

public class StepCounter extends Service implements SensorEventListener  {
    private int steps = 0, lastStep = -1;
    private SensorManager mSensorManager;
    private Sensor mStepDetectorSensor;


    @Override
    public void onCreate() {
        super.onCreate();
        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
    }

    public StepCounter(){
    }


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
        assert mSensorManager != null;
        if(mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null)
        {
            mStepDetectorSensor =
                    mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            mSensorManager.registerListener(this, mStepDetectorSensor, SensorManager.SENSOR_DELAY_UI);
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");
        Intent broadcastIntent = new Intent(this, SensorRestarterBroadcastReceiver.class);
        mSensorManager.unregisterListener(this);
        sendBroadcast(broadcastIntent);

    }




    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(lastStep == -1){
            lastStep = (int)sensorEvent.values[0];
            Log.i("KitchenAssistant","Step detected: " + lastStep);
        }else {
            Log.i("KitchenAssistant","Step detected: " + ((int)sensorEvent.values[0]-lastStep));
            StepSaverForService.saveSteps(this,new StepCount(System.currentTimeMillis(),((int)sensorEvent.values[0] - lastStep)));
            lastStep = (int)sensorEvent.values[0];
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
