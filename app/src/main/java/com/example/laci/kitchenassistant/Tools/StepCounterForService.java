package com.example.laci.kitchenassistant.Tools;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.laci.kitchenassistant.BaseClasses.StepCount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessStatusCodes;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.result.DataSourcesResult;

import java.util.concurrent.TimeUnit;

import static com.google.android.gms.fitness.data.DataSource.TYPE_DERIVED;

public class StepCounterForService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
        private int step = 0;
        private int REQUEST_OAUTH = 10;
        private boolean authInProgress = false;
        private static final String TAG = "FitActivity";
        private GoogleApiClient mClient = null;
        private OnDataPointListener mListener;
        private Context context;



        public StepCounterForService(Context context) {
            this.context = context;
        }




        public void connectFitness() {
            Log.i(TAG, "Connecting...");

            // Create the Google API Client
            if (this != null) {
                mClient = new GoogleApiClient
                        .Builder(context)
                        // select the Fitness API
                        .addApi(Fitness.SENSORS_API)
                        // specify the scopes of access
                        .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ))
                        .addScope(new Scope(Scopes.FITNESS_LOCATION_READ))
                        .addScope(new Scope(Scopes.FITNESS_BODY_READ_WRITE))
                        // provide callbacks
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .build();

                // Connect the Google API client
                mClient.connect();
            }else{
                Log.e("CONTEXT ERROR","CONTEXT IS NULL!!!!");
            }
        }

        // Manage OAuth authentication
        @Override
        public void onConnectionFailed(ConnectionResult result) {

            // Error while connecting. Try to resolve using the pending intent returned.
            if (result.getErrorCode() == ConnectionResult.SIGN_IN_REQUIRED ||
                    result.getErrorCode() == FitnessStatusCodes.NEEDS_OAUTH_PERMISSIONS) {
                /*try {
                    // Request authentication
                    //result.startResolutionForResult(activity, REQUEST_OAUTH);
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Exception connecting to the fitness service", e);
                }*/
                Log.e("ERROR","onConnectionFailed in service");
            } else {
                Log.e(TAG, "Unknown connection issue. Code = " + result.getErrorCode());
            }
        }


        @Override
        public void onConnectionSuspended(int i) {
            // If your connection gets lost at some point,
            // you'll be able to determine the reason and react to it here.
            if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                Log.i(TAG, "Connection lost.  Cause: Network Lost.");
            } else if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
                Log.i(TAG, "Connection lost.  Reason: Service Disconnected");
            }
        }

        @Override
        public void onConnected(Bundle bundle) {

            Log.i(TAG, "Connected!");

            // Now you can make calls to the Fitness APIs.
            invokeFitnessAPIs();

        }

        private void invokeFitnessAPIs() {

            // Create a listener object to be called when new data is available
            OnDataPointListener listener = new OnDataPointListener() {
                @Override
                public void onDataPoint(DataPoint dataPoint) {

                    for (Field field : dataPoint.getDataType().getFields()) {
                        Value val = dataPoint.getValue(field);
                        StepSaverForService.saveSteps(context,new StepCount(System.currentTimeMillis(),val.asInt()));
                    }

                }
            };

            //Specify what data sources to return
            DataSourcesRequest req = new DataSourcesRequest.Builder()
                    .setDataSourceTypes(TYPE_DERIVED)
                    .setDataTypes(DataType.TYPE_STEP_COUNT_DELTA)
                    .build();

            //  Invoke the Sensors API with:
            // - The Google API client object
            // - The data sources request object
            PendingResult<DataSourcesResult> pendingResult =
                    Fitness.SensorsApi.findDataSources(mClient, req);

            //  Build a sensor registration request object
            SensorRequest sensorRequest = new SensorRequest.Builder()
                    .setDataType(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                    .setSamplingRate(1, TimeUnit.SECONDS)
                    .build();

            //  Invoke the Sensors API with:
            // - The Google API client object
            // - The sensor registration request object
            // - The listener object
            PendingResult<Status> regResult =
                    Fitness.SensorsApi.add(mClient,
                            new SensorRequest.Builder()
                                    .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                                    .build(),
                            listener);


            // 4. Check the result asynchronously
            regResult.setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(Status status) {
                    if (status.isSuccess()) {
                        Log.d(TAG, "listener registered");
                        // listener registered
                    } else {
                        Log.d(TAG, "listener not registered");
                        // listener not registered
                    }
                }
            });
        }
}
