package com.example.laci.kitchenassistant.main.Home;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
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

import com.example.laci.kitchenassistant.BaseClasses.IntakeFood;
import com.example.laci.kitchenassistant.BaseClasses.StepCount;
import com.example.laci.kitchenassistant.R;
import com.example.laci.kitchenassistant.Tools.Charts.PersonalizeChart;
import com.example.laci.kitchenassistant.firebase.Account;
import com.example.laci.kitchenassistant.firebase.RetrieveDataListener;
import com.example.laci.kitchenassistant.main.MainActivity;
import com.github.mikephil.charting.charts.PieChart;

import java.util.ArrayList;
import java.util.Objects;


public class HomeFragment extends Fragment {
    private Intent stepCounterServiceIntent;
    private com.example.laci.kitchenassistant.main.Service.StepCounter stepCounterService;
    private PieChart pieChart,intakePieChart;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        Account.downloadDailySteps(new RetrieveDataListener<ArrayList<StepCount>>() {
            @Override
            public void onSuccess(ArrayList<StepCount> data) {
                ((MainActivity) Objects.requireNonNull(getActivity())).dailyStepCounts = data;
                setUpStepsPieCharts(view);
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        });

        Account.getIntakeFood(new RetrieveDataListener<ArrayList<IntakeFood>>() {
            @Override
            public void onSuccess(ArrayList<IntakeFood> data) {
                ((MainActivity)getActivity()).intookedFoods = data;
                setUpIntakeFoodsPieCharts(view);
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        });


        stepCounterService = new com.example.laci.kitchenassistant.main.Service.StepCounter();
        stepCounterServiceIntent = new Intent(getContext(),stepCounterService.getClass());
        if (!isMyServiceRunning(stepCounterService.getClass())) {
            Objects.requireNonNull(getActivity()).startForegroundService(new Intent(getActivity(), stepCounterService.getClass()));
        }


        return view;
    }

    private void setUpStepsPieCharts(View view) {
        pieChart = view.findViewById(R.id.fragment_home_step_chart);
        PersonalizeChart.setStepsPieChart(view,pieChart,((MainActivity)Objects.requireNonNull(getActivity())).dailyStepCounts,10000);
        intakePieChart = view.findViewById(R.id.fragment_home_intook_chart);
        PersonalizeChart.setIntookCaloriePieChart(view,intakePieChart,((MainActivity)getActivity()).intookedFoods,2000);
    }

    private void setUpIntakeFoodsPieCharts(View view) {
        intakePieChart = view.findViewById(R.id.fragment_home_intook_chart);
        PersonalizeChart.setIntookCaloriePieChart(view,intakePieChart,((MainActivity)getActivity()).intookedFoods,2000);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }

    @Override
    public void onDestroy() {
        getActivity().stopService(stepCounterServiceIntent);
        Log.i("MAINACT", "onDestroy!");
        super.onDestroy();

    }
}
