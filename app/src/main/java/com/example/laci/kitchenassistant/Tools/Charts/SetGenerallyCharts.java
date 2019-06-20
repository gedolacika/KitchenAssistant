package com.example.laci.kitchenassistant.Tools.Charts;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.example.laci.kitchenassistant.R;
import com.example.laci.kitchenassistant.main.Fitness.ChartValueFormatter;
import com.example.laci.kitchenassistant.main.Fitness.XAxisValueFormatter;
import com.example.laci.kitchenassistant.main.MainActivity;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class SetGenerallyCharts {

    public static void setUpLineChart(Context context, LineChart chart, ArrayList<Entry> dataVals) {
        chart.clear();

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter( new XAxisValueFormatter());
        xAxis.setSpaceMin(2);

        LineDataSet lineDataSet = new LineDataSet(dataVals,"Steps");
        chart.getXAxis().setDrawGridLines(false);
        //if(controller != 3)
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setDrawValues(false);
        lineDataSet.setFillColor(ContextCompat.getColor(context,R.color.colorPrimary));
        lineDataSet.setColor(ContextCompat.getColor(context,R.color.colorPrimary));
        lineDataSet.setDrawCircles(false);
        chart.getDescription().setText("");
        chart.getLegend().setEnabled(false);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        LineData data = new LineData(dataSets);
        data.setValueFormatter(new ChartValueFormatter());
        chart.animateX(4000);
        chart.setData(data);
    }

    public static void setUpOneLineChartWithoutFill(Context context, LineChart chart, ArrayList<Entry> dataVals) {
        chart.clear();

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter( new XAxisValueFormatter());
        xAxis.setSpaceMin(2);

        LineDataSet lineDataSet = new LineDataSet(dataVals,"Burned calories");
        chart.getXAxis().setDrawGridLines(false);

        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawFilled(false);
        lineDataSet.setColor(ContextCompat.getColor(context,R.color.colorPrimary));
        lineDataSet.setDrawCircles(false);


        chart.getDescription().setText("");
        //chart.getLegend().setEnabled(false);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        LineData data = new LineData(dataSets);
        data.setValueFormatter(new ChartValueFormatter());
        chart.animateX(4000);
        chart.setData(data);
    }

    public static void setUpTwoLineChart(Context context, LineChart chart, ArrayList<Entry> steps, ArrayList<Entry> consumedCalories) {
        chart.clear();

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter( new XAxisValueFormatter());
        xAxis.setSpaceMin(2);

        LineDataSet lineDataSet = new LineDataSet(steps,"Burned calories");
        LineDataSet lineDataSet2 = new LineDataSet(consumedCalories,"Consumed calories");
        chart.getXAxis().setDrawGridLines(false);

        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawFilled(false);
        lineDataSet.setColor(ContextCompat.getColor(context,R.color.colorPrimary));
        lineDataSet.setDrawCircles(false);

        lineDataSet2.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSet2.setDrawFilled(true);
        lineDataSet2.setDrawValues(false);
        lineDataSet2.setDrawFilled(false);
        lineDataSet2.setColor(ContextCompat.getColor(context,R.color.colorButton));
        lineDataSet2.setDrawCircles(false);


        chart.getDescription().setText("");
        //chart.getLegend().setEnabled(false);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);
        dataSets.add(lineDataSet2);

        LineData data = new LineData(dataSets);
        data.setValueFormatter(new ChartValueFormatter());
        chart.animateX(4000);
        chart.setData(data);
    }

    public static void setUpPieChart(View view, PieChart pieChart, ArrayList<PieEntry> values, String centerText) {
        pieChart.clear();
        pieChart.setUsePercentValues(false);
        pieChart.getDescription().setEnabled(false);
        //pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationFrictionCoef(0.96f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setHoleRadius(65f);
        pieChart.setTransparentCircleRadius(30f);
        pieChart.setCenterText(centerText);
        pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(0f);
        pieChart.setCenterTextSize(14f);
        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);


        PieDataSet dataSet = new PieDataSet(values, "");
        dataSet.setSliceSpace(1.5f);
        dataSet.setSelectionShift(0f);
        dataSet.setColors(view.getResources().getColor(R.color.colorPrimary), view.getResources().getColor(R.color.colorPieAlt));

        PieData data = new PieData((dataSet));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.YELLOW);
        data.setDrawValues(false);

        pieChart.setData(data);


    }

}
