package com.example.laci.kitchenassistant.Tools;

import android.util.Log;

import com.example.laci.kitchenassistant.BaseClasses.IntakeFood;
import com.example.laci.kitchenassistant.BaseClasses.StepCount;
import com.example.laci.kitchenassistant.BaseClasses.Training;
import com.example.laci.kitchenassistant.BaseClasses.User;
import com.example.laci.kitchenassistant.BaseClasses.UserNeedPersonalInformations;
import com.example.laci.kitchenassistant.BaseClasses.WeeklyPersonalData;
import com.example.laci.kitchenassistant.BaseClasses.WeightHistory;
import com.example.laci.kitchenassistant.main.MainActivity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalorieNeedCounter {
    private static ArrayList<WeeklyPersonalData> weeklyPersonalData = new ArrayList<>();
    private static int calorieNeedForOneDay;
    private static int MINIMUM_CALORIE_INTAKE_FOR_A_DAY = 500;//recommended 1200
    private static int MINIMUM_CALORIE_BURN_FOR_A_DAY = 1;//recommended calorieNeedForOneDay + 100
    private static int MINIMUM_STEPS_FOR_A_DAY = 500;//recommended 1000
    private static int MINIMUM_VALID_DAYS_PER_WEEK = 1;// recommended 4
    private static int ONE_CALORIE_IN_STEPS = 20;// recommended [20,30]


    public static void CountCalories(ArrayList<IntakeFood> foods, ArrayList<StepCount> steps, ArrayList<Training> trainings, User user){
        calorieNeedForOneDay = getCalorieNeedForOneDay(user);
        //MINIMUM_CALORIE_BURN_FOR_A_DAY = calorieNeedForOneDay;
        setWeeklyPersonalData(foods, steps, trainings, user);
        filterWeeklyPersonalDataByWhatWeWant(user);
        setCalorieRequirements();
        //printWeeklyPersonalData();
        //printUserPersonalInformations();


        /*calculateHistoryWeeksGoal();
        countAveragesGlobalAverages();
        selectFoodsToEatPerDay();*/

    }




    private static void setCalorieRequirements(){
        int minBC=10000,minIC=10000, maxBC=-1, maxIC=-1, avgBC=0, avgIC=0;
        int burnedCalories, consumedCalories;
        for(int i = 0; i < weeklyPersonalData.size(); ++i){
            burnedCalories = weeklyPersonalData.get(i).getAverageBurnedCalories();
            consumedCalories = weeklyPersonalData.get(i).getAverageConsumedCalories();
            if( burnedCalories < minBC ){
                minBC = burnedCalories;
            }
            if( consumedCalories < minIC){
                minIC = consumedCalories;
            }
            if( burnedCalories > maxBC){
                maxBC = burnedCalories;
            }
            if( consumedCalories > maxIC){
                maxIC = consumedCalories;
            }
            avgBC += burnedCalories;
            avgIC += consumedCalories;
        }
        avgBC /= weeklyPersonalData.size();
        avgIC /= weeklyPersonalData.size();

        UserNeedPersonalInformations.setMinimumCalorieBurn(minBC);
        UserNeedPersonalInformations.setMinimumCalorieIntake(minIC);
        UserNeedPersonalInformations.setMaximumCalorieBurn(maxBC);
        UserNeedPersonalInformations.setMaximumCalorieIntake(maxIC);
        UserNeedPersonalInformations.setAverageCalorieBurn(avgBC);
        UserNeedPersonalInformations.setAverageCalorieIntake(avgIC);
    }

    /**
     * Removing weeks where the result was other than what is ours goal
     * @param user
     */
    private static void filterWeeklyPersonalDataByWhatWeWant(User user){
        int whatWeWant = whatWeWant(user);// 0 - keeping weight, 1 - gaining weight, -1 - losing weight
        for(int i = 0; i < weeklyPersonalData.size(); ++i){
            if(whatWeWant == -1){
                if(weeklyPersonalData.get(i).getWeightCHangeFromTheLastWeek() > -1){
                    weeklyPersonalData.remove(i);
                }
            }
            if(whatWeWant == 0){
                if(weeklyPersonalData.get(i).getWeightCHangeFromTheLastWeek() != 0){
                    weeklyPersonalData.remove(i);
                }
            }
            if(whatWeWant == 1){
                if(weeklyPersonalData.get(i).getWeightCHangeFromTheLastWeek() < 1){
                    weeklyPersonalData.remove(i);
                }
            }
        }
    }

    private static int whatWeWant(User user){
        if(user.getGoal_weight() < user.getWeight()) return -1;
        if(user.getGoal_weight() > user.getWeight()) return 1;
        return 0;
    }


    private static void printWeeklyPersonalData(){
        Log.e("WeeklyPersonalData","------------------------------------------------------------------------");
        for(int i = 0; i < weeklyPersonalData.size(); ++i){
            Log.e("WeeklyPersonalData", "Start: " + weeklyPersonalData.get(i).getStart().toString()
                    + ", End: " + weeklyPersonalData.get(i).getEnd().toString() + ", "
                    + "AvgBC: " + weeklyPersonalData.get(i).getAverageBurnedCalories()
                    + ", " + "AvgIC: "  + weeklyPersonalData.get(i).getAverageConsumedCalories()
                    + ", Weight: " + weeklyPersonalData.get(i).getWeightCHangeFromTheLastWeek()
                    + ", IsReal: " + weeklyPersonalData.get(i).isRealData()
                    + ", IsWeightFound: " + weeklyPersonalData.get(i).isWeightFound());
        }
    }

    private static void printUserPersonalInformations(){
        Log.e("WeeklyPersonalData","-------------------------------------------------------------------");
        Log.e("WeeklyPersonalData","MinBurn: " + UserNeedPersonalInformations.getMinimumCalorieBurn() +
                                            ", MinConsume: " + UserNeedPersonalInformations.getMinimumCalorieIntake() +
                                            ", MaxBurn: " + UserNeedPersonalInformations.getMaximumCalorieBurn() +
                                            ", MaxConsume: " + UserNeedPersonalInformations.getMaximumCalorieIntake() +
                                            ", AverageBurn: " + UserNeedPersonalInformations.getAverageCalorieBurn() +
                                            ", AverageConsume: " + UserNeedPersonalInformations.getAverageCalorieIntake());
    }


    private static void setWeeklyPersonalData(ArrayList<IntakeFood> foods, ArrayList<StepCount> steps, ArrayList<Training> trainings, User user){
        //Set all weeks when we registered an event from the user
        weeklyPersonalData.clear();
        DateFunctions.getAllWeekWhenHappenedSomething(foods, steps, trainings, weeklyPersonalData);
        //Checking events
        //Set average values to the weeklyPersonalData ArrayList

        for(int i = 0; i < weeklyPersonalData.size(); ){

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

            //Clear weeks, when we have not enough data to calculate a real data
            if( !(weeklyPersonalData.get(i).isRealData() && weeklyPersonalData.get(i).isWeightFound())){
                weeklyPersonalData.remove(i);
            }else{
                ++i;
            }
        }

    }

    private static void setWeightChange(User user, int i){
        Date previousWeekStart = new Date(DateFunctions.getTheStartOfDate(new Date(weeklyPersonalData.get(i).getStart().getTime()-100000)).getTime());
        Date previousWeekEnd = new Date(DateFunctions.getTheEndOfDate(new Date(weeklyPersonalData.get(i).getStart().getTime()-100000)).getTime());
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










    /**
     * Set the average consumed calories for a given week
     * @param start - start date of the week
     * @param end - end date of the week
     * @param allCalories - the all consumed foods, what the user registered to the database
     * @param targetWeek - the object where we want to save the data
     */
    public static void setWeeklyConsumedCalories(Date start, Date end, ArrayList<IntakeFood> allCalories, WeeklyPersonalData targetWeek){
        //Set the consumed calories for each day to the weeklyFoodsForEachDay ArrayList
        ArrayList<IntakeFood> weeklyFoodsForEachDay = new ArrayList<>();

        Date tempDate = new Date();
        int i, badData = 0, goodDays = 0;
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
            //Sum the consumed calories
            targetWeek.setAverageConsumedCalories(targetWeek.getAverageConsumedCalories() + weeklyFoodsForEachDay.get(i).getCalorie());
            tempDate.setTime(weeklyFoodsForEachDay.get(i).getTime());
            //Sum the bad days, when the consumed calories were less than the minimum
            if(weeklyFoodsForEachDay.get(i).getCalorie() < MINIMUM_CALORIE_INTAKE_FOR_A_DAY){
                badData += 1;
            }
        }
        goodDays = 7-badData;
        //If the good days were less then the minimum, then ths week is not a valid week
        if(goodDays < MINIMUM_VALID_DAYS_PER_WEEK)
            targetWeek.setRealData(false);


        //We got the summed value to the averageConsumedCalories variable, what we have to divide the numbers of good days
        if( goodDays > 0 ) {
            targetWeek.setAverageConsumedCalories(targetWeek.getAverageConsumedCalories() / goodDays);
        }
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
            if(tempDate.getTime() >= start.getTime() && tempDate.getTime() <= end.getTime()){

                numberOfStepsForEachDayOnTHeTargetWeek.get((int)((tempDate.getTime()-start.getTime())/oneDayInMillis)).setSteps(
                        numberOfStepsForEachDayOnTHeTargetWeek.get((int)((tempDate.getTime()-start.getTime())/oneDayInMillis)).getSteps()+allSteps.get(i).getSteps()
                );
            }
        }



        //If the steps was less than 1000, it means that the step counter wasn't turned on, on its day and we skip it from the counting.
        // transforming the steps to calories and adding the calorie need for one day to the burned calories
        for(i = 0; i < numberOfStepsForEachDayOnTHeTargetWeek.size(); ++i){
            if(numberOfStepsForEachDayOnTHeTargetWeek.get(i).getSteps() < MINIMUM_STEPS_FOR_A_DAY){
                --goodDaysOnThisWeek;
            }else{
                targetWeek.setAverageBurnedCalories(targetWeek.getAverageBurnedCalories()
                        + numberOfStepsForEachDayOnTHeTargetWeek.get(i).getSteps()/ONE_CALORIE_IN_STEPS
                        + calorieNeedForOneDay);
            }

        }
        //Continue with the weekly trainings
        for(i = 0; i < trainings.size(); ++i){
            tempDate.setTime(trainings.get(i).getTimeTo());
            if(tempDate.getTime() >= start.getTime() && tempDate.getTime() <= end.getTime()){
                targetWeek.setAverageBurnedCalories( targetWeek.getAverageBurnedCalories() + trainings.get(i).getBurnCalorie());
            }
        }

        //Finally we have to divide the summed value for the active days of week
        if(goodDaysOnThisWeek >= 1){
            targetWeek.setAverageBurnedCalories(targetWeek.getAverageBurnedCalories()/goodDaysOnThisWeek);
        }

        if(goodDaysOnThisWeek < MINIMUM_VALID_DAYS_PER_WEEK)
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
