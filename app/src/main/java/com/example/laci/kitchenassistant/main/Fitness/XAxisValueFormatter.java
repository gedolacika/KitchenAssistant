package com.example.laci.kitchenassistant.main.Fitness;

import android.annotation.SuppressLint;

import com.bumptech.glide.util.Util;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class XAxisValueFormatter implements IAxisValueFormatter {

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        Date date = new Date((long) value);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
        return getDay(date) + " " + dateFormat.format(date);
    }

    private String getDay(Date date) {
        switch(date.getDay()){
            case 1: return "Mo";
            case 2: return "Tu";
            case 3: return "We";
            case 4: return "Th";
            case 5: return "Fr";
            case 6: return "Sa";
            case 7: return "Su";
        }
        return "";
    }
}