package com.example.laci.kitchenassistant.Tools.Charts;

import android.view.View;

import com.example.laci.kitchenassistant.BaseClasses.IntakeFood;
import com.example.laci.kitchenassistant.BaseClasses.StepCount;
import com.example.laci.kitchenassistant.Tools.Tools;
import com.example.laci.kitchenassistant.main.MainActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Date;


public class PersonalizeChart {

    public static void setStepsPieChart(View view, PieChart pieChart, ArrayList<StepCount> dailyStepCount, int targetStepCount){
        ArrayList<PieEntry> values = new ArrayList<>();
        int stepCount = 0;
        for (int i = 0; i < dailyStepCount.size(); ++i) {
            stepCount += dailyStepCount.get(i).getSteps();
        }

        stepCount += Tools.getStepsFromService(view.getContext());

        values.add(new PieEntry(Float.parseFloat(String.valueOf(stepCount)), "Steps: " + stepCount));
        values.add(new PieEntry(Float.parseFloat(String.valueOf(10000 - stepCount)), "Goal: " + String.valueOf(targetStepCount)));


        SetGenerallyCharts.setUpPieChart(view,pieChart, values, "Steps");
    }

    public static void setIntookCaloriePieChart(View view, PieChart pieChart, ArrayList<IntakeFood> IntookCalorie, int targetCalorie){
        ArrayList<PieEntry> values = new ArrayList<>();
        int intookCalorie = 0;
        Date currentDate = new Date();
        Date tempDate;
        for(int i = 0 ; i < IntookCalorie.size();++i)
        {
            tempDate = new Date( IntookCalorie.get(i).getTime());
            if( currentDate.getYear() == tempDate.getYear() &&
                    currentDate.getMonth() == tempDate.getMonth() &&
                    currentDate.getDay() == tempDate.getDay()) {
                intookCalorie += IntookCalorie.get(i).getCalorie();
            }
        }

        values.add(new PieEntry(Float.parseFloat(String.valueOf(intookCalorie)),"Calories: " + intookCalorie));
        values.add(new PieEntry(Float.parseFloat(String.valueOf(2000-intookCalorie)),"Goal: " + String.valueOf(targetCalorie)));


        SetGenerallyCharts.setUpPieChart(view,pieChart, values, "Calories");
    }

}
