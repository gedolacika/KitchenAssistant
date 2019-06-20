package com.example.laci.kitchenassistant.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.laci.kitchenassistant.BaseClasses.StepCount;
import com.example.laci.kitchenassistant.R;
import com.example.laci.kitchenassistant.Tools.Charts.PersonalizeChart;
import com.example.laci.kitchenassistant.firebase.Account;
import com.example.laci.kitchenassistant.firebase.RetrieveDataListener;
import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;


public class DiagramsFragment extends Fragment {
    private LineChart consumedBurnedChart, plusMinusChart, estimatedChart;
    private RadioButton recommendRadioButton, previousRadioButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_diagrams, container, false);
        initViews(view);
        ((MainActivity)getActivity()).setTitle("Diagrams");

        PersonalizeChart.setDiagramsFragmentLineCharts(view,consumedBurnedChart,plusMinusChart, estimatedChart, ((MainActivity)getActivity()).intookedFoods,((MainActivity)getActivity()).allSteps,((MainActivity)getActivity()).trainings, getCalorieNeedForOneDay());


        return view;
    }

    private int getCalorieNeedForOneDay(){
        int calorieNeed = 0;
        //if male
        if(((MainActivity)getActivity()).user.getGender()==0){
            if(((MainActivity)getActivity()).user.getAge() <= 18){
                calorieNeed = (int)17.5 * ((MainActivity)getActivity()).user.getWeight() + 651;
            }else {
                if(((MainActivity)getActivity()).user.getAge() >= 31){
                    calorieNeed = (int)11.6 * ((MainActivity)getActivity()).user.getWeight() + 879;
                }else{
                    calorieNeed = (int)15.3 * ((MainActivity)getActivity()).user.getWeight() + 679;
                }
            }
        }else { //if female
            if(((MainActivity)getActivity()).user.getAge() <= 18){
                calorieNeed = (int)12.2 * ((MainActivity)getActivity()).user.getWeight() + 746;
            }else {
                if(((MainActivity)getActivity()).user.getAge() >= 31){
                    calorieNeed = (int)08.7 * ((MainActivity)getActivity()).user.getWeight() + 829;
                }else{
                    calorieNeed = (int)14.7 * ((MainActivity)getActivity()).user.getWeight() + 496;
                }
            }
        }
        return  calorieNeed;
    }


    private void initViews(View view){
        consumedBurnedChart = view.findViewById(R.id.fragment_diagrams_consumed_burned_chart);
        plusMinusChart = view.findViewById(R.id.fragment_diagrams_plus_minus_chart);
        estimatedChart = view.findViewById(R.id.fragment_diagrams_estimated_chart);
        recommendRadioButton = view.findViewById(R.id.fragment_diagrams_reccomendations_radioButton);
        previousRadioButton = view.findViewById(R.id.fragment_diagrams_previous_radioButton);
    }
}
