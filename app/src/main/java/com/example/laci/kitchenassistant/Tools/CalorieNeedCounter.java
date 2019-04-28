package com.example.laci.kitchenassistant.Tools;

import android.util.Log;

import com.example.laci.kitchenassistant.BaseClasses.IntakeFood;
import com.example.laci.kitchenassistant.BaseClasses.StepCount;
import com.example.laci.kitchenassistant.BaseClasses.Training;
import com.example.laci.kitchenassistant.BaseClasses.User;
import com.example.laci.kitchenassistant.BaseClasses.WeeklyPersonalData;
import com.example.laci.kitchenassistant.BaseClasses.WeightHistory;
import com.example.laci.kitchenassistant.main.MainActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalorieNeedCounter {
    private static ArrayList<WeeklyPersonalData> weeklyPersonalData = new ArrayList<>();
    private static int calorieNeedForOneDay;

    public static void CountCalories(ArrayList<IntakeFood> foods, ArrayList<StepCount> steps, ArrayList<Training> trainings, User user){
        calorieNeedForOneDay = getCalorieNeedForOneDay(user);
        setWeeklyPersonalData(foods, steps, trainings, user);
        /*calculateHistoryWeeksGoal();
        countAveragesGlobalAverages();
        selectFoodsToEatPerDay();*/

    }



    private static void setWeeklyPersonalData(ArrayList<IntakeFood> foods, ArrayList<StepCount> steps, ArrayList<Training> trainings, User user){
        //Set all event dates
        ArrayList<Date> dates = new ArrayList<>();
        setAllEventDates(foods,steps, trainings, dates);

        //Set all week when happened something
        createAllWeekWhenHappenedSomething(dates);

        //Set average values to the weeklyPersonalData ArrayList
        for(int i = 0; i < weeklyPersonalData.size(); ++i){

            setWeeklyConsumedCalories(weeklyPersonalData.get(i).getStart(),
                    weeklyPersonalData.get(i).getEnd(),
                    foods,
                    weeklyPersonalData.get(i));

            setWeeklyBurnedCalories(weeklyPersonalData.get(i).getStart(),
                    weeklyPersonalData.get(i).getEnd(),
                    steps,
                    trainings,
                    weeklyPersonalData.get(i));
            setWeightChange(user, i);
            if( !weeklyPersonalData.get(i).isRealData() || !weeklyPersonalData.get(i).isWeightFound()){
                weeklyPersonalData.remove(i);
            }
        }
    }

    private static void setWeightChange(User user, int i){
        Date previousWeekStart = new Date(getTheStartOfDate(new Date(weeklyPersonalData.get(i).getStart().getTime()-100000)).getTime());
        Date previousWeekEnd = new Date(getTheEndOfDate(new Date(weeklyPersonalData.get(i).getStart().getTime()-100000)).getTime());
        int thisWeekWeight = -1, previousWeekWeight = -1;
        thisWeekWeight = getWeightFromAWeek(user.getWeightHistories(), weeklyPersonalData.get(i).getStart(), weeklyPersonalData.get(i).getEnd());
        previousWeekWeight = getWeightFromAWeek(user.getWeightHistories(), previousWeekStart, previousWeekEnd);
        if(thisWeekWeight != -1 && previousWeekWeight != -1){
            weeklyPersonalData.get(i).setWeightCHangeFromTheLastWeek(thisWeekWeight - previousWeekWeight);
            weeklyPersonalData.get(i).setWeightFound(true);
        }else{
            weeklyPersonalData.get(i).setWeightFound(false);
        }
    }

    private static int getWeightFromAWeek(ArrayList<WeightHistory> weights, Date start, Date end){
        WeightHistory weight = null;
        for(int i = 0; i < weights.size(); ++i){
            if(weights.get(i).getTimeMillis() >= start.getTime() && weights.get(i).getTimeMillis() <= end.getTime()){
                if(weight == null){
                    weight = weights.get(i);
                }else{
                    if(weights.get(i).getTimeMillis() < weight.getTimeMillis()){
                        weight = weights.get(i);
                    }
                }
            }
        }
        if(weight != null){
            return (int)(long)(weight.getWeight());
        }
        return -1;
    }

    private static void createAllWeekWhenHappenedSomething(ArrayList<Date> dates){
        for(int i = 0; i < dates.size(); ++i){
            if(!isExistsADate(dates.get(i))){
                weeklyPersonalData.add(new WeeklyPersonalData(
                        getTheStartOfDate(dates.get(i)),
                        getTheEndOfDate(dates.get(i)),
                        0,
                        0,
                        0
                ));
            }
        }
    }

    private static void setAllEventDates(ArrayList<IntakeFood> foods, ArrayList<StepCount> steps, ArrayList<Training> trainings, ArrayList<Date> dates){
        for(int i = 0; i < foods.size();++i)dates.add(new Date(foods.get(i).getTime()));
        for(int i = 0; i < steps.size();++i)dates.add(new Date(steps.get(i).getTime()));
        for(int i = 0; i < trainings.size();++i)dates.add(new Date(trainings.get(i).getTimeTo()));
    }

    private static boolean isExistsADate(Date date){
        for(int i = 0; i < weeklyPersonalData.size(); ++i){
            if(date.getTime() >= weeklyPersonalData.get(i).getStart().getTime() && date.getTime() <= weeklyPersonalData.get(i).getEnd().getTime()){
                return true;
            }
        }
        return false;
    }

    private static Date getTheStartOfDate(Date date){
        long millisPerWeek = 7 * 24 * 60 * 60 * 1000;
        return new Date(date.getTime() - (date.getTime()% millisPerWeek));
    }

    private static Date getTheEndOfDate(Date date){
        long millisPerWeek = 7 * 24 * 60 * 60 * 1000;
        return new Date((date.getTime() - (date.getTime() % millisPerWeek)) + (millisPerWeek-1));
    }

    /**
     * Set the average consumed calories for a given week
     * @param start - start date of the week
     * @param end - end date of the week
     * @param allCalories - the all consumed foods, what the user registered to the database
     * @param targetWeek - the object where we want to save the data
     */
    private static void setWeeklyConsumedCalories(Date start, Date end, ArrayList<IntakeFood> allCalories, WeeklyPersonalData targetWeek){
        //Set the consumed calories for each day to the weeklyFoodsForEachDay ArrayList
        ArrayList<IntakeFood> weeklyFoodsForEachDay = new ArrayList<>();

        Date tempDate = new Date();
        int i, badData = 0;
        long oneDayInMillis = 86400000;

        for(i = 0; i < 7; ++i){
            IntakeFood food = new IntakeFood();
            food.setTime(start.getTime()+i*oneDayInMillis);
            food.setCalorie(0);
            weeklyFoodsForEachDay.add(food);
        }

        for(i = 0; i < allCalories.size(); ++i){
            tempDate.setTime(allCalories.get(i).getTime());
            if(tempDate.getTime() >= start.getTime() && tempDate.getTime() <= end.getTime()){
                weeklyFoodsForEachDay.get((int)((tempDate.getTime()-start.getTime())/oneDayInMillis)).setCalorie(
                        weeklyFoodsForEachDay.get((int)((tempDate.getTime()-start.getTime())/oneDayInMillis)).getCalorie()+allCalories.get(i).getCalorie()
                );
            }

        }

        //Set the average consumed calories for this week, pulling out the days when the consumed calories were less than the minimum calorie requirement to keep in alive
        for(i = 0; i < weeklyFoodsForEachDay.size(); ++i){
            tempDate.setTime(weeklyFoodsForEachDay.get(i).getTime());
            if(tempDate.getTime() >= start.getTime() && tempDate.getTime() <= end.getTime()){
                if(weeklyFoodsForEachDay.get(i).getCalorie() < calorieNeedForOneDay){
                    badData += 1;
                }
                if((7-badData) < WeeklyPersonalData.getMinimumNumbersOfRealDataPerWeek())
                    targetWeek.setRealData(false);
                else
                    targetWeek.setAverageConsumedCalories(targetWeek.getAverageConsumedCalories() + weeklyFoodsForEachDay.get(i).getCalorie());
            }
        }

        //We got the summed value to this variable, what we have to divide the numbers of good days
        targetWeek.setAverageConsumedCalories(targetWeek.getAverageConsumedCalories()/(7-badData));
    }

    /**
     * Set the burned calories for a week to the targetWeek object
     * @param start - the start day of week
     * @param end - the end day of week
     * @param allSteps - the all steps from the downloading the app
     * @param trainings - the all documented trainings
     * @param targetWeek - the object where we want to save the data
     */
    private static void setWeeklyBurnedCalories(Date start, Date end, ArrayList<StepCount> allSteps,ArrayList<Training> trainings, WeeklyPersonalData targetWeek){
        //Set the steps for each day to the following arraylist
        ArrayList<StepCount> numberOfStepsForEachDayOnTHeTargetWeek = new ArrayList<>();
        int i, goodDaysOnThisWeek = 7;
        long oneDayInMillis = 86400000;
        Date tempDate = new Date();

        for(i = 0; i < 7; ++i){
            numberOfStepsForEachDayOnTHeTargetWeek.add(new StepCount(start.getTime()+i*oneDayInMillis,0));
        }

        for(i = 0; i < allSteps.size(); ++i){
            tempDate.setTime(allSteps.get(i).getTime());
            Log.e("STEPSCHECK","FOR - " + tempDate.toString() + " - " + allSteps.get(i).getSteps());
            if(tempDate.getTime() >= start.getTime() && tempDate.getTime() <= end.getTime()){

                numberOfStepsForEachDayOnTHeTargetWeek.get((int)((tempDate.getTime()-start.getTime())/oneDayInMillis)).setSteps(
                        numberOfStepsForEachDayOnTHeTargetWeek.get((int)((tempDate.getTime()-start.getTime())/oneDayInMillis)).getSteps()+allSteps.get(i).getSteps()
                );
            }
        }

        //If the steps was less than 1000, it means that the step counter wasn't turned on, on its day and we skip it from the counting.
        // transforming the steps to calories and adding the calorie need for one day to the burned calories
        for(i = 0; i < numberOfStepsForEachDayOnTHeTargetWeek.size(); ++i){
            if(numberOfStepsForEachDayOnTHeTargetWeek.get(i).getSteps() < 1000){
                --goodDaysOnThisWeek;
                continue;
            }
            numberOfStepsForEachDayOnTHeTargetWeek.get(i).setSteps(numberOfStepsForEachDayOnTHeTargetWeek.get(i).getSteps()/20);
            numberOfStepsForEachDayOnTHeTargetWeek.get(i).setSteps(numberOfStepsForEachDayOnTHeTargetWeek.get(i).getSteps()+calorieNeedForOneDay);
            targetWeek.setAverageBurnedCalories(targetWeek.getAverageBurnedCalories() + numberOfStepsForEachDayOnTHeTargetWeek.get(i).getSteps());
        }

        //Continue with the weekly trainings
        for(i = 0; i < trainings.size(); ++i){
            tempDate.setTime(trainings.get(i).getTimeTo());
            if(tempDate.getTime() >= start.getTime() && tempDate.getTime() <= end.getTime()){
                targetWeek.setAverageBurnedCalories( targetWeek.getAverageBurnedCalories() + trainings.get(i).getBurnCalorie());
            }
        }

        //Finally we have to divide the summed value for the active days of week
        targetWeek.setAverageBurnedCalories(targetWeek.getAverageBurnedCalories()/goodDaysOnThisWeek);
        if(goodDaysOnThisWeek < WeeklyPersonalData.getMinimumNumbersOfRealDataPerWeek())
            targetWeek.setRealData(false);

    }

    /**
     * It returns the personalized value of the calorie need for one day, followed by the users parameters
     * @param user
     * @return - calorie requirement for one day
     */
    private static int getCalorieNeedForOneDay(User user){
        int calorieNeed = 0;
        //if male
        if(user.getGender()==0){
            if(user.getAge() <= 18){
                calorieNeed = (int)17.5 * user.getWeight() + 651;
            }else {
                if(user.getAge() >= 31){
                    calorieNeed = (int)11.6 * user.getWeight() + 879;
                }else{
                    calorieNeed = (int)15.3 * user.getWeight() + 679;
                }
            }
        }else { //if female
            if(user.getAge() <= 18){
                calorieNeed = (int)12.2 * user.getWeight() + 746;
            }else {
                if(user.getAge() >= 31){
                    calorieNeed = (int)08.7 * user.getWeight() + 829;
                }else{
                    calorieNeed = (int)14.7 * user.getWeight() + 496;
                }
            }
        }
        return  calorieNeed;
    }

}