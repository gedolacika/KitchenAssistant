package com.example.laci.kitchenassistant.main.Home;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laci.kitchenassistant.BaseClasses.StepCount;
import com.example.laci.kitchenassistant.R;
import com.example.laci.kitchenassistant.Tools.StepCounterForUI;
import com.example.laci.kitchenassistant.firebase.Account;
import com.example.laci.kitchenassistant.firebase.RetrieveDataListener;
import com.example.laci.kitchenassistant.main.MainActivity;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.fitness.request.OnDataPointListener;

import java.util.ArrayList;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment  {
    private int step = 0;
    private int REQUEST_OAUTH = 10;
    private boolean authInProgress = false;
    private static final String TAG = "FitActivity";
    private GoogleApiClient mClient = null;
    private OnDataPointListener mListener;

    int mInitialNumberOfSteps = 0;
    private TextView textView;
    private boolean mFirstCount = true;
    private StepCounterForUI stepCounterForUI;
    private Intent stepCounterServiceIntent;
    private com.example.laci.kitchenassistant.main.Service.StepCounter stepCounterService;
    private PieChart pieChart;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        textView = view.findViewById(R.id.fragment_home_textview);
        stepCounterForUI = new StepCounterForUI(getActivity(),getContext(),textView,((MainActivity)Objects.requireNonNull(getActivity())).stepCounts);
        stepCounterForUI.connectFitness();
        Account.downloadSteps(new RetrieveDataListener<ArrayList<StepCount>>() {
            @Override
            public void onSuccess(ArrayList<StepCount> data) {
                ((MainActivity)Objects.requireNonNull(getActivity())).stepCounts = data;
                int step = 0;
                for(int i = 0; i < data.size();++i){
                    step += data.get(i).getSteps();
                }
                textView.setText(String.valueOf(step));
                stepCounterForUI.setStepCounts(data);
                setUpPieChart(view);
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
            }
        });



        stepCounterService = new com.example.laci.kitchenassistant.main.Service.StepCounter();
        stepCounterServiceIntent = new Intent(getContext(),stepCounterService.getClass());
        if (!isMyServiceRunning(stepCounterService.getClass())) {
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Objects.requireNonNull(getActivity()).startForegroundService(new Intent(getActivity(), stepCounterService.getClass()));
            } else {
                Objects.requireNonNull(getActivity()).startService(new Intent(getActivity(), stepCounterService.getClass()));
            }*/
            //Objects.requireNonNull(getActivity()).startService(stepCounterServiceIntent);
            Objects.requireNonNull(getActivity()).startForegroundService(new Intent(getActivity(), stepCounterService.getClass()));
            //StepCounter.enqueueWork(getContext(), stepCounterServiceIntent);
        }


        return view;
    }

    private void setUpPieChart(View view)
    {
        pieChart = view.findViewById(R.id.fragment_home_step_chart);

        pieChart.setUsePercentValues(false);
        pieChart.getDescription().setEnabled(false);
        //pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationFrictionCoef(0.96f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setHoleRadius(65f);
        pieChart.setTransparentCircleRadius(30f);
        pieChart.setCenterText("Steps");
        pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(0f);
        pieChart.setCenterTextSize(14f);
        pieChart.animateY(1000,Easing.EasingOption.EaseInOutCubic);

        ArrayList<PieEntry> values = new ArrayList<>();
        int stepCount = 0;
        for(int i = 0 ; i < ((MainActivity)getActivity()).stepCounts.size();++i)
        {
            stepCount += ((MainActivity)getActivity()).stepCounts.get(i).getSteps();
        }

        values.add(new PieEntry(Float.parseFloat(String.valueOf(stepCount)),"Steps: " + stepCount));
        values.add(new PieEntry(Float.parseFloat(String.valueOf(10000-stepCount)),"Steps remaining: " + String.valueOf(10000-stepCount)));

        PieDataSet dataSet = new PieDataSet(values,"");
        dataSet.setSliceSpace(1.5f);
        dataSet.setSelectionShift(0f);
        dataSet.setColors(getResources().getColor(R.color.colorPrimary),getResources().getColor(R.color.colorPieAlt));

        PieData data = new PieData((dataSet));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.YELLOW);
        data.setDrawValues(false);

        pieChart.setData(data);


    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            // If the user is not authenticated, try to connect again/ resultcode = RESULT_CANCEL
            mClient.connect();
        } else {
            stepCounterForUI.onConnected(null);
        }
    }

    //Start
    @Override
    public void onStart() {
        super.onStart();
        mFirstCount = true;

        if (mClient == null || !mClient.isConnected()) {
            stepCounterForUI.connectFitness();
        }
    }

    @Override
    public void onDestroy() {
        getActivity().stopService(stepCounterServiceIntent);
        Log.i("MAINACT", "onDestroy!");
        super.onDestroy();

    }


}
