package com.example.laci.kitchenassistant.Tools;

import android.util.Log;

import com.example.laci.kitchenassistant.BaseClasses.IntakeFood;
import com.example.laci.kitchenassistant.BaseClasses.StepCount;
import com.example.laci.kitchenassistant.BaseClasses.Training;
import com.example.laci.kitchenassistant.BaseClasses.WeeklyPersonalData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DateFunctions {

    public static void getAllWeekWhenHappenedSomething(ArrayList<IntakeFood> foods, ArrayList<StepCount> steps, ArrayList<Training> trainings, ArrayList<WeeklyPersonalData> weeklyPersonalData) {
        ArrayList<Date> dates = new ArrayList<>();
        //Create to an arraylist all dates when we registered step, food or training
        createAllEventDates(foods, steps, trainings, dates);
        //Create weeks
        createAllWeekWhenHappenedSomething(dates,weeklyPersonalData);

    }

    public static boolean isEqualsDates(Date date1, Date date2){
        if(date1.getYear() == date2.getYear() &&
                date1.getMonth() == date2.getMonth() &&
                date1.getDay() == date2.getDay()){
            return true;
        }
        return false;
    }

    public static void createAllWeekWhenHappenedSomething(ArrayList<Date> dates, ArrayList<WeeklyPersonalData> weeklyPersonalData){
        for(int i = 0; i < dates.size(); ++i){
            if(!isExistsADate(dates.get(i), weeklyPersonalData)){
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

    public static void createAllEventDates(ArrayList<IntakeFood> foods, ArrayList<StepCount> steps, ArrayList<Training> trainings, ArrayList<Date> dates){
        for(int i = 0; i < foods.size();++i)dates.add(new Date(foods.get(i).getTime()));
        for(int i = 0; i < steps.size();++i)dates.add(new Date(steps.get(i).getTime()));
        for(int i = 0; i < trainings.size();++i)dates.add(new Date(trainings.get(i).getTimeTo()));
    }

    public static boolean isExistsADate(Date date, ArrayList<WeeklyPersonalData> weeklyPersonalData){
        for(int i = 0; i < weeklyPersonalData.size(); ++i){
            if(isRegisteredTheWeek(date,weeklyPersonalData.get(i).getStart(), weeklyPersonalData.get(i).getEnd())){
                return true;
            }
        }
        return false;
    }

    public static boolean isRegisteredTheWeek(Date checkDate, Date startOfWeek, Date endOfWeek){
        return checkDate.after(startOfWeek) && checkDate.before(endOfWeek);
    }

    public static Date getTheStartOfDate(Date incomingDate){
        Date inputDate = getStartOfDay(incomingDate);
        Calendar c = Calendar.getInstance();
        c.setTime(inputDate);
        while(c.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY){
            c.add(Calendar.DATE,-1);
        }
        Date start = c.getTime();
        return start;
    }

    public static Date getTheEndOfDate(Date incomingDate){
        Date inputDate = getEndOfDay(incomingDate);
        Calendar c = Calendar.getInstance();
        c.setTime(inputDate);
        while(c.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY){
            c.add(Calendar.DATE,1);
        }
        Date end = c.getTime();
        return end;
    }

    public static Date getStartOfDay(Date date){
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        return date;
    }

    private static Date getEndOfDay(Date date){
        date.setHours(23);
        date.setMinutes(59);
        date.setSeconds(59);
        return date;
    }


}
