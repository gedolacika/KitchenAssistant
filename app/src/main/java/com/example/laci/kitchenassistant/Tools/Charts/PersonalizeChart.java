package com.example.laci.kitchenassistant.Tools.Charts;

import android.util.Log;
import android.view.View;

import com.example.laci.kitchenassistant.BaseClasses.IntakeFood;
import com.example.laci.kitchenassistant.BaseClasses.StepCount;
import com.example.laci.kitchenassistant.BaseClasses.Training;
import com.example.laci.kitchenassistant.BaseClasses.UserNeedPersonalInformations;
import com.example.laci.kitchenassistant.Tools.DateFunctions;
import com.example.laci.kitchenassistant.Tools.Tools;
import com.example.laci.kitchenassistant.main.MainActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;


public class PersonalizeChart {
    public static ArrayList<StepCount> stepsPerDaysLastWeek;
    public static ArrayList<StepCount> burnedCaloriesLastWeek;
    public static ArrayList<IntakeFood> consumedCaloriesLastWeek;

    public static void setDiagramsFragmentLineCharts(View view, LineChart consumedBurnedLineChart, LineChart plusMinusLineChart, LineChart estimatedLineChart, ArrayList<IntakeFood> consumedFoods, ArrayList<StepCount> steps, ArrayList<Training> trainings,  int calorieNeedForOneDay){
        setConsumedBurnedLineChart(view,consumedBurnedLineChart, consumedFoods, steps, calorieNeedForOneDay,trainings);
        setPlusMinusLineChart(view, plusMinusLineChart, burnedCaloriesLastWeek, consumedFoods);
        setEstimatedByRecommendations(view, estimatedLineChart);
    }

    public static void setEstimatedByRecommendations(View view, LineChart lineChart){
        ArrayList<StepCount> burned = new ArrayList<>();
        ArrayList<IntakeFood> consumed = new ArrayList<>();

        DateFunctions.setNextWeek(consumed, burned);

        Random random1 = new Random();
        Random random2 = new Random();
        int consumedRandom, burnedRandom;
        for(int i = 0; i < burned.size(); ++i){
            consumedRandom = UserNeedPersonalInformations.getMinimumCalorieIntake() +
                    random1.nextInt(
                            UserNeedPersonalInformations.getMaximumCalorieIntake() -
                                    UserNeedPersonalInformations.getMinimumCalorieIntake());

            burnedRandom = UserNeedPersonalInformations.getMinimumCalorieBurn() +
                    random2.nextInt(
                            UserNeedPersonalInformations.getMaximumCalorieBurn() -
                                    UserNeedPersonalInformations.getMinimumCalorieBurn());

            consumed.get(i).setCalorie(consumedRandom);
            burned.get(i).setSteps(burnedRandom);
            Log.e("MINUSPLUS", "Consumed random: " + consumedRandom + " - Burned random: " + burnedRandom);
        }

        setPlusMinusLineChart(view,lineChart,burned, consumed);
    }

    public static void setPlusMinusLineChart(View view, LineChart lineChart,ArrayList<StepCount> burned,ArrayList<IntakeFood> consumed){
        ArrayList<StepCount> lastWeekPLusMinusValues = new ArrayList<>();
        Date iDate, jDate;
        for(int i = 0; i < burned.size(); ++i){
            iDate = new Date(burned.get(i).getTime());
            lastWeekPLusMinusValues.add(new StepCount(
                    iDate.getTime(),
                    consumed.get(i).getCalorie() - burned.get(i).getSteps()
            ));
        }

        ArrayList<Entry> dataVals = new ArrayList<>();

        for(int i = 0; i < lastWeekPLusMinusValues.size(); ++i){
            dataVals.add(new Entry(lastWeekPLusMinusValues.get(i).getTime(), lastWeekPLusMinusValues.get(i).getSteps()));
        }


        SetGenerallyCharts.setUpOneLineChartWithoutFill(view.getContext(), lineChart, dataVals);
    }

    public static void setConsumedBurnedLineChart(View view, LineChart lineChart, ArrayList<IntakeFood> consumedFoods, ArrayList<StepCount> weeklySteps, int calorieNeedForOneDay, ArrayList<Training> trainings) {
        ArrayList<StepCount> stepsForEachDay = new ArrayList<>();
        ArrayList<IntakeFood> consumedCaloriesForEachDay = new ArrayList<>();
        ArrayList<StepCount> burnedCaloriesForEachDay = new ArrayList<>();

        setConsumedCaloriesForEachDay(consumedFoods, consumedCaloriesForEachDay);
        setStepsForEachDay(weeklySteps, stepsForEachDay);
        setBurnedCaloriesForEachDay(burnedCaloriesForEachDay, stepsForEachDay, calorieNeedForOneDay, trainings);

        ArrayList<Entry> foods = new ArrayList<>();
        ArrayList<Entry> burnedCalories = new ArrayList<>();

        for (int i = 0; i < burnedCaloriesForEachDay.size(); ++i) {
            Log.e("STEPS", i + " - " + burnedCaloriesForEachDay.get(i).getSteps() + " - " + burnedCaloriesForEachDay.get(i).getTime());
            burnedCalories.add(new Entry(burnedCaloriesForEachDay.get(i).getTime(), burnedCaloriesForEachDay.get(i).getSteps()));
        }
        for (int i = 0; i < consumedCaloriesForEachDay.size(); ++i) {
            Log.e("CONSUMED CALORIES", i + " - " + consumedCaloriesForEachDay.get(i).getCalorie() + " - " + consumedCaloriesForEachDay.get(i).getTime());
            foods.add(new Entry(consumedCaloriesForEachDay.get(i).getTime(), consumedCaloriesForEachDay.get(i).getCalorie()));
        }

        stepsPerDaysLastWeek = stepsForEachDay;
        burnedCaloriesLastWeek = burnedCaloriesForEachDay;
        consumedCaloriesLastWeek = consumedCaloriesForEachDay;

        SetGenerallyCharts.setUpTwoLineChart(view.getContext(), lineChart, burnedCalories, foods);
    }

    public static void setStepsPieChart(View view, PieChart pieChart, ArrayList<StepCount> dailyStepCount, int targetStepCount) {
        ArrayList<PieEntry> values = new ArrayList<>();
        int stepCount = 0;
        for (int i = 0; i < dailyStepCount.size(); ++i) {
            stepCount += dailyStepCount.get(i).getSteps();
        }

        stepCount += Tools.getStepsFromService(view.getContext());

        values.add(new PieEntry(Float.parseFloat(String.valueOf(stepCount)), "Steps: " + stepCount));
        values.add(new PieEntry(Float.parseFloat(String.valueOf(10000 - stepCount)), "Goal: " + String.valueOf(targetStepCount)));


        SetGenerallyCharts.setUpPieChart(view, pieChart, values, "Steps");
    }

    public static void setIntookCaloriePieChart(View view, PieChart pieChart, ArrayList<IntakeFood> IntookCalorie, int targetCalorie) {
        ArrayList<PieEntry> values = new ArrayList<>();
        int intookCalorie = 0;
        Date currentDate = new Date();
        Date tempDate;
        for (int i = 0; i < IntookCalorie.size(); ++i) {
            tempDate = new Date(IntookCalorie.get(i).getTime());
            if (currentDate.getYear() == tempDate.getYear() &&
                    currentDate.getMonth() == tempDate.getMonth() &&
                    currentDate.getDay() == tempDate.getDay()) {
                intookCalorie += IntookCalorie.get(i).getCalorie();
            }
        }

        values.add(new PieEntry(Float.parseFloat(String.valueOf(intookCalorie)), "Calories: " + intookCalorie));
        values.add(new PieEntry(Float.parseFloat(String.valueOf(2000 - intookCalorie)), "Goal: " + String.valueOf(targetCalorie)));


        SetGenerallyCharts.setUpPieChart(view, pieChart, values, "Calories");
    }

    /*private static void setStepsForEachDay(ArrayList<StepCount> weeklySteps, ArrayList<StepCount> targetArrayList){
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int i = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
        c.add(Calendar.DATE, -i - 7);
        Date start = c.getTime();
        c.add(Calendar.DATE, 6);
        Date end = c.getTime();

        long oneDayInMillis = 86400000;
        Date tempDate = new Date();

        for(i = 0; i < 7; ++i){
            targetArrayList.add(new StepCount(start.getTime()+i*oneDayInMillis,0));
        }

        for(i = 0; i < weeklySteps.size(); ++i){
            tempDate.setTime(weeklySteps.get(i).getTime());
            Log.e("STEPSCHECK","FOR - " + tempDate.toString() + " - " + weeklySteps.get(i).getSteps());
            if(tempDate.getTime() >= start.getTime() && tempDate.getTime() <= end.getTime()){

                targetArrayList.get((int)((tempDate.getTime()-start.getTime())/oneDayInMillis)).setSteps(
                        targetArrayList.get((int)((tempDate.getTime()-start.getTime())/oneDayInMillis)).getSteps()+weeklySteps.get(i).getSteps()
                );
            }

        }
        Log.e("STEPSCHECK","--------------------------------------------");
        for(i = 0; i < targetArrayList.size();++i)Log.e("STEPSCHECK",i + " - " + targetArrayList.get(i).getSteps() + " - " + targetArrayList.get(i).getTime());

    }*/

    private static void setStepsForEachDay(ArrayList<StepCount> weeklySteps, ArrayList<StepCount> targetArrayList) {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int i = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
        c.add(Calendar.DATE, -i - 7);
        Date start = c.getTime();
        c.add(Calendar.DATE, 6);
        Date end = c.getTime();

        long oneDayInMillis = 86400000;
        long currentTimeInMillis = System.currentTimeMillis();
        long lastWeekMillis = currentTimeInMillis - (7 * 24 * 60 * 60 * 1000);
        Date tempDate = new Date();

        for (i = 0; i < 7; ++i) {
            targetArrayList.add(new StepCount(lastWeekMillis + i * oneDayInMillis, 0));
        }

        for (i = 0; i < weeklySteps.size(); ++i) {
            tempDate.setTime(weeklySteps.get(i).getTime());
            Log.e("STEPSCHECK", "FOR - " + tempDate.toString() + " - " + weeklySteps.get(i).getSteps());
            if (tempDate.getTime() >= lastWeekMillis) {

                targetArrayList.get((int) ((tempDate.getTime() - lastWeekMillis) / oneDayInMillis)).setSteps(
                        targetArrayList.get((int) ((tempDate.getTime() - lastWeekMillis) / oneDayInMillis)).getSteps() + weeklySteps.get(i).getSteps()
                );
            }

        }

        Log.e("STEPSCHECK", "--------------------------------------------");
        for (i = 0; i < targetArrayList.size(); ++i)
            Log.e("STEPSCHECK", i + " - " + targetArrayList.get(i).getSteps() + " - " + targetArrayList.get(i).getTime());

    }

    /*private static void setConsumedCaloriesForEachDay(ArrayList<IntakeFood> weeklyCalories, ArrayList<IntakeFood> targetArrayList){
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int i = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
        c.add(Calendar.DATE, -i - 7);
        Date start = c.getTime();
        c.add(Calendar.DATE, 6);
        Date end = c.getTime();
        //System.out.println(start + " - " + end);

        long oneDayInMillis = 86400000;
        Date tempDate = new Date();

        for(i = 0; i < 7; ++i){
            IntakeFood food = new IntakeFood();
            food.setTime(start.getTime()+i*oneDayInMillis);
            food.setCalorie(0);
            targetArrayList.add(food);
        }
        //Log.e("START - END - CAL",start.toString() + "   -   " + end.toString());
        for(i = 0; i < weeklyCalories.size(); ++i){
            tempDate.setTime(weeklyCalories.get(i).getTime());
            if(tempDate.getTime() >= start.getTime() && tempDate.getTime() <= end.getTime()){
                //Log.e("Add cal",weeklyCalories.get(i).getCalorie()+"");
                targetArrayList.get((int)((tempDate.getTime()-start.getTime())/oneDayInMillis)).setCalorie(
                        targetArrayList.get((int)((tempDate.getTime()-start.getTime())/oneDayInMillis)).getCalorie()+weeklyCalories.get(i).getCalorie()
                );
            }

        }
        //Log.e("SAVED","CALORIES: ");
        //for(i = 0; i < targetArrayList.size();++i)Log.e("CONSUMED CAL CHECK",targetArrayList.get(i).getCalorie() + " - " + targetArrayList.get(i).getTime());


    }*/

    private static void setConsumedCaloriesForEachDay(ArrayList<IntakeFood> weeklyCalories, ArrayList<IntakeFood> targetArrayList) {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int i = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
        c.add(Calendar.DATE, -i - 7);
        Date start = c.getTime();
        c.add(Calendar.DATE, 6);
        Date end = c.getTime();
        //System.out.println(start + " - " + end);

        long oneDayInMillis = 86400000;
        long currentTimeInMillis = System.currentTimeMillis();
        long lastWeekMillis = currentTimeInMillis - (7 * 24 * 60 * 60 * 1000);
        Date tempDate = new Date();

        for (i = 0; i < 7; ++i) {
            IntakeFood food = new IntakeFood();
            food.setTime(lastWeekMillis + i * oneDayInMillis);
            food.setCalorie(0);
            targetArrayList.add(food);
        }
        //Log.e("START - END - CAL",start.toString() + "   -   " + end.toString());
        for (i = 0; i < weeklyCalories.size(); ++i) {
            tempDate.setTime(weeklyCalories.get(i).getTime());
            if (tempDate.getTime() >= lastWeekMillis) {
                //Log.e("Add cal",weeklyCalories.get(i).getCalorie()+"");
                targetArrayList.get((int) ((tempDate.getTime() - lastWeekMillis) / oneDayInMillis)).setCalorie(
                        targetArrayList.get((int) ((tempDate.getTime() - lastWeekMillis) / oneDayInMillis)).getCalorie() + weeklyCalories.get(i).getCalorie()
                );
            }

        }
        //Log.e("SAVED","CALORIES: ");
        //for(i = 0; i < targetArrayList.size();++i)Log.e("CONSUMED CAL CHECK",targetArrayList.get(i).getCalorie() + " - " + targetArrayList.get(i).getTime());


    }

    private static void setBurnedCaloriesForEachDay(ArrayList<StepCount> burnedCaloriesForEachDay, ArrayList<StepCount> stepsForEachDay, int calorieNeedForOneDay, ArrayList<Training> trainings) {
        for(int i = 0; i < stepsForEachDay.size(); ++i){
            burnedCaloriesForEachDay.add(new StepCount(stepsForEachDay.get(i).getTime(),(stepsForEachDay.get(i).getSteps()/20)+calorieNeedForOneDay));
        }

        long oneDayInMillis = 86400000;
        long currentTimeInMillis = System.currentTimeMillis();
        long lastWeekMillis = currentTimeInMillis - (7 * 24 * 60 * 60 * 1000);

        Date tempDate = new Date();

        for(int i = 0; i < trainings.size(); ++i){
            tempDate.setTime(trainings.get(i).getTimeTo());
            if(tempDate.getTime() > currentTimeInMillis){
                burnedCaloriesForEachDay.get((int) ((tempDate.getTime() - lastWeekMillis) / oneDayInMillis)).setSteps(
                        burnedCaloriesForEachDay.get((int) ((tempDate.getTime() - lastWeekMillis) / oneDayInMillis)).getSteps() + trainings.get(i).getBurnCalorie()
                );
            }
        }
    }

}
