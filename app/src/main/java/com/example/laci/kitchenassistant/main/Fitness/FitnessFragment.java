package com.example.laci.kitchenassistant.main.Fitness;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laci.kitchenassistant.BaseClasses.StepCount;
import com.example.laci.kitchenassistant.BaseClasses.Training;
import com.example.laci.kitchenassistant.BaseClasses.TrainingBase;
import com.example.laci.kitchenassistant.R;
import com.example.laci.kitchenassistant.Tools.CalorieNeedCounter;
import com.example.laci.kitchenassistant.Tools.Charts.PersonalizeChart;
import com.example.laci.kitchenassistant.Tools.Charts.SetGenerallyCharts;
import com.example.laci.kitchenassistant.Tools.Tools;
import com.example.laci.kitchenassistant.firebase.Account;
import com.example.laci.kitchenassistant.main.MainActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class FitnessFragment extends Fragment {

    private PieChart pieChart;
    private LineChart stepsLineChart, trainingAllLineChart;
    private RadioButton steps, calories, training, all;
    private TextView addTrainingTextView, calorieBurnedBySteps, calorieBurnedByTrainings, calorieBurnedWithKeepAlive, calorieBurnedAll;
    private Spinner addTrainingSpinner;
    private SeekBar addTrainingSeekBar;
    private Button addTrainingButton;
    private TrainingSpinnerAdapter training_spinner_adapter;
    private TrainingBase choosen_training;
    private int trainMinValue=0;
    private int ONE_CALORIE_IN_STEP = 20;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_fitness, container, false);
        initViews(view);
        ((MainActivity)getActivity()).setTitle("Fitness");
        PersonalizeChart.setStepsPieChart(view,pieChart, ((MainActivity)Objects.requireNonNull(getActivity())).dailyStepCounts, 10000);

        SetGenerallyCharts.setUpLineChart(getContext(),stepsLineChart,setStepsToArrayList(new ArrayList<Entry>(),1));
        SetGenerallyCharts.setUpLineChart(getContext(),trainingAllLineChart,setTrainingsAllArrayList(new ArrayList<Entry>(),3));
        setListeners(view);
        setUpTrainingsSpinner(view.getContext());

        return view;
    }

    /**
     * Set an array list with point to the chart - only the trainings and all calorie burnes
     * @param dataVals - ArrayList what we have to create before we call the function
     * @param controller - if 3 we show the calorie burn by trainings
     *                   if 4 we show the all weekly calorie burn (keep alive, steps, trainings)
     * @return the arraylist which contains the values to the chart
     */
    private ArrayList<Entry> setTrainingsAllArrayList(ArrayList<Entry> dataVals, int controller){
            if(controller == 3){
                long time = System.currentTimeMillis()-604800000;
                for(int i = 0; i < ((MainActivity)getActivity()).trainings.size(); ++i){
                    Date date = (new Date(((MainActivity)getActivity()).trainings.get(i).getTimeTo()));
                        while(time < date.getTime()){
                            dataVals.add(new Entry(time,0));
                            time += 300000;
                        }
                        dataVals.add(new Entry( date.getTime(),((MainActivity)getActivity()).trainings.get(i).getBurnCalorie()));
                    }
            }else{
                int calorieNeedForFiveMinutes = getCalorieNeedForFiveMinutes();
                int stepAdd, trainingAdd, stepIndex=0, trainingIndex=0;
                Date currentDate = new Date(System.currentTimeMillis());
                Training currentTraining=null;
                StepCount currentStepCount=null;
                if(((MainActivity)getActivity()).trainings.size() > 0){
                    currentTraining = ((MainActivity)getActivity()).trainings.get(0);
                }
                if(((MainActivity)getActivity()).weeklyStepCounts.size() > 0){
                    currentStepCount = ((MainActivity)getActivity()).weeklyStepCounts.get(0);
                }


                for(long i = (currentDate.getTime()-604800000); i <= currentDate.getTime(); i += 300000){
                    stepAdd = 0;
                    trainingAdd = 0;
                    if(currentStepCount != null){
                        if(currentStepCount.getTime() <= i && currentStepCount.getTime() >= (i-300000)){
                            stepAdd = currentStepCount.getSteps();
                            if(((MainActivity)getActivity()).weeklyStepCounts.size() > (stepIndex+1)){
                                stepIndex++;
                                currentStepCount = ((MainActivity)getActivity()).weeklyStepCounts.get(stepIndex);
                            }else{
                                currentStepCount = null;
                            }
                        }
                    }

                    if(currentTraining != null){
                        if(i <= currentTraining.getTimeTo() && i >= (currentTraining.getTimeTo()-(currentTraining.getDuration()*60000))){
                            trainingAdd = currentTraining.getBurnCalorie()/currentTraining.getDuration()*5;
                            if((i+300000)> currentTraining.getTimeTo() && (trainingIndex+1) < ((MainActivity)getActivity()).trainings.size()){
                                trainingIndex++;
                                currentTraining = ((MainActivity)getActivity()).trainings.get(trainingIndex);
                            }else{
                                currentTraining = null;
                            }
                        }
                    }

                    dataVals.add(new Entry( i,(calorieNeedForFiveMinutes+stepAdd+trainingAdd)));

                }
            }
            return dataVals;
        }


    /**
     * We create an ArrayList to the steps chart
     * @param dataVals - values
     * @param controller - if 1 we show steps
     *                   - if 2 we show the calorie burned by steps
     * @return the ArrayList which contains values  to the LineChart
     */
    private ArrayList<Entry> setStepsToArrayList(ArrayList<Entry> dataVals, int controller){
        Date currentDate = new Date(System.currentTimeMillis());
        if(controller <= 2 && controller >= 1){
            for(int i = 0; i < ((MainActivity)getActivity()).weeklyStepCounts.size(); ++i){
                Date date = (new Date(((MainActivity)getActivity()).weeklyStepCounts.get(i).getTime()));
                if( date.getTime() >= (currentDate.getTime()-604800000)) {
                    if(controller == 1){
                        dataVals.add(new Entry( date.getTime(),((MainActivity)getActivity()).weeklyStepCounts.get(i).getSteps()));
                    }else {
                        dataVals.add(new Entry( date.getTime(),((MainActivity)getActivity()).weeklyStepCounts.get(i).getSteps()/20));
                    }
                }
            }
        }
        return dataVals;
    }

    /**
     * We calculate the calorie requirements to keep alive for 5 minutes
     * @return - personalized calorie requirements for 5 minutes
     */
    private int getCalorieNeedForFiveMinutes(){
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


    private void setListeners(View view){
        steps.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    calories.setChecked(false);
                    SetGenerallyCharts.setUpLineChart(getContext(),stepsLineChart,setStepsToArrayList(new ArrayList<Entry>(),1));
                }else {
                    calories.setChecked(true);
                    SetGenerallyCharts.setUpLineChart(getContext(),stepsLineChart,setStepsToArrayList(new ArrayList<Entry>(),2));
                }
            }
        });

        calories.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    steps.setChecked(false);
                    SetGenerallyCharts.setUpLineChart(getContext(),stepsLineChart,setStepsToArrayList(new ArrayList<Entry>(),2));
                }else {
                    steps.setChecked(true);
                    SetGenerallyCharts.setUpLineChart(getContext(),stepsLineChart,setStepsToArrayList(new ArrayList<Entry>(),1));
                }
            }
        });

        training.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    all.setChecked(false);
                    SetGenerallyCharts.setUpLineChart(getContext(),trainingAllLineChart,setTrainingsAllArrayList(new ArrayList<Entry>(),3));
                }else {
                    all.setChecked(true);
                    SetGenerallyCharts.setUpLineChart(getContext(),trainingAllLineChart,setTrainingsAllArrayList(new ArrayList<Entry>(),4));
                }
            }
        });

        all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    training.setChecked(false);
                    SetGenerallyCharts.setUpLineChart(getContext(),trainingAllLineChart,setTrainingsAllArrayList(new ArrayList<Entry>(),4));
                }else {
                    training.setChecked(true);
                    SetGenerallyCharts.setUpLineChart(getContext(),trainingAllLineChart,setTrainingsAllArrayList(new ArrayList<Entry>(),3));
                }
            }
        });

        addTrainingSeekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener()
                {
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress,
                                                  boolean fromUser)
                    {
                        trainMinValue = progress;
                        addTrainingTextView.setText(String.valueOf(progress)+" min - " + String.valueOf((int)((float)choosen_training.getBurnCalorie()/10*progress))+ "ckal" );
                    }
                }
        );

        addTrainingButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onClick(View view) {
                if(trainMinValue > 0){
                    int calorieBurned = (int)((float)choosen_training.getBurnCalorie()/10*trainMinValue);
                    long time = System.currentTimeMillis();
                    Log.e("Time - Calorie", time + "  " + calorieBurned);
                    Account.saveTraining(new Training(
                            new TrainingBase(
                                    choosen_training.getName(),
                                    calorieBurned
                            ),
                            trainMinValue,
                            time
                    ));
                    Toast.makeText(view.getContext(),"You succesfull registered the training!",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(view.getContext(),"You can't register training with 0 minutes!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void initViews(View view) {
        pieChart = view.findViewById(R.id.fragment_fitness_step_chart);
        stepsLineChart = view.findViewById(R.id.fragment_fitness_steps_chart);
        steps = view.findViewById(R.id.fragment_fitness_show_steps);
        calories = view.findViewById(R.id.fragment_fitness_show_calories);
        addTrainingButton = view.findViewById(R.id.fragment_fitness_training_button);
        addTrainingTextView = view.findViewById(R.id.fragment_fitness_training_ckal_per_min);
        addTrainingSpinner = view.findViewById(R.id.fragment_fitness_training_spinner);
        addTrainingSeekBar = view.findViewById(R.id.fragment_fitness_training_seekBar);
        trainingAllLineChart = view.findViewById(R.id.fragment_fitness_chart_training_all);
        training = view.findViewById(R.id.fragment_fitness_show_trainings);
        all = view.findViewById(R.id.fragment_fitness_show_all_burn);
        //calorieBurnedBySteps, calorieBurnedByTrainings, calorieBurnedWithKeepAlive, calorieBurnedAll
        calorieBurnedBySteps = view.findViewById(R.id.fragment_fitness_steps_textView);
        calorieBurnedByTrainings = view.findViewById(R.id.fragment_fitness_trainings_textView);
        calorieBurnedWithKeepAlive = view.findViewById(R.id.fragment_fitness_alive_textView);
        calorieBurnedAll = view.findViewById(R.id.fragment_fitness_all_burn_textView);

        int allBurnedKcals = getDailyBurnedCaloriesBySteps();
        int temp;
        calorieBurnedBySteps.setText("Step: " + allBurnedKcals + "kcal");


        temp = getDailyBurnedCaloriesByTrainings();
        calorieBurnedByTrainings.setText("Training: " + temp + "kcal");
        allBurnedKcals += temp;

        temp = getDailyKeepAlive();
        calorieBurnedWithKeepAlive.setText("Base: " + temp + "kcal");

        allBurnedKcals += temp;

        calorieBurnedAll.setText("All: " + allBurnedKcals + "kcal");


    }

    public int getDailyKeepAlive(){
        long oneDayInMillis = 86400000;
        long oneDayCalorieRequirement = (long)(CalorieNeedCounter.getBaseCalorieNeedForOneDay(((MainActivity)getActivity()).user));
        long today = System.currentTimeMillis() % oneDayInMillis;

        return (int)((oneDayCalorieRequirement*today)/oneDayInMillis);
    }

    public int getDailyBurnedCaloriesByTrainings(){
        int sum = 0;
        Date tempDate;
        Date today = new Date();
        for(int i =0; i < ((MainActivity)getActivity()).trainings.size(); ++i){
            tempDate = new Date(((MainActivity)getActivity()).trainings.get(i).getTimeTo());
            if(today.getDay() == tempDate.getDay() &&
                today.getMonth() == tempDate.getMonth() &&
                today.getYear() == tempDate.getYear()){
                sum += ((MainActivity)getActivity()).trainings.get(i).getBurnCalorie();
            }
        }
        return sum;
    }

    public int getDailyBurnedCaloriesBySteps(){
        int step = 0;
        for(int i = 0; i < ((MainActivity)getActivity()).dailyStepCounts.size(); ++i){
            step += ((MainActivity)getActivity()).dailyStepCounts.get(i).getSteps();
        }
        return (int)((step + Tools.getStepsFromService(getContext()))/ONE_CALORIE_IN_STEP);
    }

    public void setUpTrainingsSpinner(Context context){
        ArrayList<TrainingBase> trainingBases = ((MainActivity)Objects.requireNonNull(getActivity())).trainingBases;

        training_spinner_adapter = new TrainingSpinnerAdapter(context,trainingBases);
        addTrainingSpinner.setAdapter(training_spinner_adapter);

        addTrainingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                choosen_training = (TrainingBase)adapterView.getItemAtPosition(i);
                addTrainingTextView.setText(String.valueOf(trainMinValue)+" min - " + String.valueOf((int)((float)choosen_training.getBurnCalorie()/10*trainMinValue))+ "ckal" );
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                choosen_training = (TrainingBase)adapterView.getItemAtPosition(0);
                addTrainingTextView.setText(String.valueOf(trainMinValue)+" min - " + String.valueOf((int)((float)choosen_training.getBurnCalorie()/10*trainMinValue))+ "ckal" );
            }
        });
    }

}
