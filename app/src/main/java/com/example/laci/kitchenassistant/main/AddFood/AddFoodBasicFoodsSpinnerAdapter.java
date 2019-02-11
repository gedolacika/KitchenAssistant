package com.example.laci.kitchenassistant.main.AddFood;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.laci.kitchenassistant.BaseClasses.BasicFood;
import com.example.laci.kitchenassistant.R;

import java.util.ArrayList;

public class AddFoodBasicFoodsSpinnerAdapter extends ArrayAdapter<BasicFood> {

    public AddFoodBasicFoodsSpinnerAdapter(Context context,ArrayList<BasicFood> basicFoods){
        super(context,0,basicFoods);

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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cardview_add_food_ingredient,parent,false);
        }
        ImageView imageView = convertView.findViewById(R.id.cardview_add_food_ingredient_imageView);
        TextView textView = convertView.findViewById(R.id.cardview_add_food_ingredient_textView);

        BasicFood basicFood = getItem(position);
        if(basicFood != null){
            textView.setText(basicFood.getName());
            Glide.with(getContext()).load(basicFood.getPicture()).into(imageView);
        }

        return convertView;
    }
}
