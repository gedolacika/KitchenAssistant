package com.example.laci.kitchenassistant.main.Details.BasicFood;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.laci.kitchenassistant.BaseClasses.BasicFood;
import com.example.laci.kitchenassistant.BaseClasses.BasicFoodQuantity;
import com.example.laci.kitchenassistant.R;
import com.example.laci.kitchenassistant.Tools.FragmentNavigation;
import com.example.laci.kitchenassistant.Tools.Validations;
import com.example.laci.kitchenassistant.firebase.Account;
import com.example.laci.kitchenassistant.main.MainActivity;

import java.util.Objects;

public class BasicFoodDetailsFragment extends Fragment {
    private BasicFood food;
    private TextView name, calorie, fat, carbohydrate, protein;
    private ImageView imageView;
    private Button button;
    private TextInputEditText quantity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basic_food_details, container, false);
        setIndex();
        initViews(view);
        setValues(view.getContext());
        setListener();

        return view;
    }

    private void setListener(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Validations.validateNumber(Objects.requireNonNull(quantity.getText()).toString()) == 0){
                    BasicFoodQuantity for_upload = new BasicFoodQuantity();
                    for_upload.setCalorie(Integer.parseInt(quantity.getText().toString())*food.getCalorie()/100);
                    for_upload.setProtein(Integer.parseInt(quantity.getText().toString())*food.getProtein()/100);
                    for_upload.setCarbohydrate(Integer.parseInt(quantity.getText().toString())*food.getCarbohydrate()/100);
                    for_upload.setFat(Integer.parseInt(quantity.getText().toString())*food.getFat()/100);
                    for_upload.setSaturated(Integer.parseInt(quantity.getText().toString())*food.getSaturated()/100);
                    for_upload.setSugar(Integer.parseInt(quantity.getText().toString())*food.getSugar()/100);
                    for_upload.setName(food.getName());
                    for_upload.setPicture(food.getPicture());
                    for_upload.setQuantity(Integer.parseInt(quantity.getText().toString()));
                    Account.setIntakeFood(for_upload);
                    Toast.makeText(view.getContext(),"The upload was successfull!",Toast.LENGTH_LONG).show();
                    FragmentNavigation.loadFoodsFragment(Objects.requireNonNull(getActivity()));
                }
            }
        });

    }

    private void setValues(Context context) {
        name.setText(food.getName());
        fat.setText(String.valueOf(food.getFat()));
        calorie.setText(String.valueOf(food.getCalorie()));
        carbohydrate.setText(String.valueOf(food.getCarbohydrate()));
        protein.setText(String.valueOf(food.getProtein()));
        Glide.with(context).load(food.getPicture()).into(imageView);
    }

    private void initViews(View view) {
        name = view.findViewById(R.id.fragment_basic_food_details_name);
        calorie = view.findViewById(R.id.fragment_basic_food_details_calorie);
        fat = view.findViewById(R.id.cardview_consumed_food_details_fat);
        carbohydrate = view.findViewById(R.id.cardview_consumed_food_details_carbohydrate);
        protein = view.findViewById(R.id.cardview_consumed_food_details_protein);
        imageView = view.findViewById(R.id.fragment_basic_food_details_image);
        button = view.findViewById(R.id.fragment_basic_food_details_button);
        quantity = view.findViewById(R.id.fragment_basic_food_details_editText);
    }

    private void setIndex() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            int index = bundle.getInt("index", 0);
            food = ((MainActivity) Objects.requireNonNull(getActivity())).basicFoods.get(index);
        }

    }
}
