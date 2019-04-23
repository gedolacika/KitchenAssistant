package com.example.laci.kitchenassistant.main.Fitness;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.laci.kitchenassistant.BaseClasses.TrainingBase;
import com.example.laci.kitchenassistant.R;

import java.util.ArrayList;

public class TrainingSpinnerAdapter extends ArrayAdapter<TrainingBase> {

    public TrainingSpinnerAdapter(Context context, ArrayList<TrainingBase> trainingBases){
        super(context, 0, trainingBases);
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView,  @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }

    private View initView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cardview_training_spinner,parent,false);
        }
        TextView textView = convertView.findViewById(R.id.cardview_training_spinner_textView);

        TrainingBase trainingBase = getItem(position);
        if(trainingBase != null){
            textView.setText(trainingBase.getName());
        }

        return convertView;
    }
}
