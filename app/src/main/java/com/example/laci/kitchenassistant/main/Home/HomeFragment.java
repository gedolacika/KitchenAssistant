package com.example.laci.kitchenassistant.main.Home;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.laci.kitchenassistant.main.Foods.BasicFoods.BasicFoodsAdapter;
import com.example.laci.kitchenassistant.main.MainActivity;
import com.github.mikephil.charting.charts.PieChart;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;


public class HomeFragment extends Fragment {
    private Intent stepCounterServiceIntent;
    private com.example.laci.kitchenassistant.main.Service.StepCounter stepCounterService;
    private PieChart pieChart,intakePieChart;
    private RecyclerView foodsConsumedRecyclerView;
    private ConsumedFoodsAdapter adapter;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        initViews(view);
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
                setConsumedFoodsRecyclerView(view);

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

    private void setConsumedFoodsRecyclerView(View view){
        Date currentTime = new Date();
        ArrayList<IntakeFood> foods = new ArrayList<>();
        for(int i = 0; i < ((MainActivity)getActivity()).intookedFoods.size(); ++i){
            if(currentTime.getDay() == (new Date(((MainActivity)getActivity()).intookedFoods.get(i).getTime())).getDay()){
                foods.add(((MainActivity)getActivity()).intookedFoods.get(i));
            }
        }
        adapter = new ConsumedFoodsAdapter(foods,view.getContext());
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        foodsConsumedRecyclerView.setLayoutManager(manager);
        foodsConsumedRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void setUpStepsPieCharts(View view) {
        PersonalizeChart.setStepsPieChart(view,pieChart,((MainActivity)Objects.requireNonNull(getActivity())).dailyStepCounts,10000);
    }

    private void setUpIntakeFoodsPieCharts(View view) {
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

    private void initViews(View view){
        foodsConsumedRecyclerView = view.findViewById(R.id.fragment_home_foods_consumed_recyclerView);
        pieChart = view.findViewById(R.id.fragment_home_step_chart);
        intakePieChart = view.findViewById(R.id.fragment_home_intook_chart);
    }
}
