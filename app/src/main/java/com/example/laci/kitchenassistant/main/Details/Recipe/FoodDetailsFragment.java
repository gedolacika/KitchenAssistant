package com.example.laci.kitchenassistant.main.Details.Recipe;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laci.kitchenassistant.BaseClasses.BasicFoodQuantity;
import com.example.laci.kitchenassistant.BaseClasses.Recipe;
import com.example.laci.kitchenassistant.R;
import com.example.laci.kitchenassistant.Tools.FragmentNavigation;
import com.example.laci.kitchenassistant.firebase.Account;
import com.example.laci.kitchenassistant.main.MainActivity;

import java.util.Objects;

public class FoodDetailsFragment extends Fragment {
    private int quantity_int;
    private TextView name, type, origin, servings, time, difficulty, preparation, calorie, protein, fat, carbohydrate, quantity,quantity_serving;
    private RecyclerView pictures, ingredients;
    private Recipe food;
    private PicturesAdapter pictures_adapter;
    private IngredientsAdapter ingredientsAdapter;
    private Button button;
    private TextInputEditText editText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_details, container, false);

        setIndex();
        initViews(view);
        setPictures(view);
        setIngredients(view);
        setOtherParameters();
        setListener(view);
        ((MainActivity)getActivity()).setTitle(food.getName());
        return view;
    }

    private void setListener(View mainView){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity_int = Integer.valueOf(editText.getText().toString());

                BasicFoodQuantity for_upload = new BasicFoodQuantity();
                for_upload.setCalorie(quantity_int* food.getCalorie()/food.getQuantity());
                for_upload.setProtein(quantity_int* food.getProtein()/food.getQuantity());
                for_upload.setCarbohydrate(quantity_int* food.getCarbohydrate()/food.getQuantity());
                for_upload.setFat(quantity_int* food.getFat()/food.getQuantity());
                for_upload.setSaturated(quantity_int* food.getSaturated()/food.getQuantity());
                for_upload.setSugar(quantity_int* food.getSugar()/food.getQuantity());
                for_upload.setName(food.getName());
                for_upload.setPicture(food.getPictures().get(0));
                for_upload.setQuantity(quantity_int);
                Account.setIntakeFood(for_upload);
                Toast.makeText(view.getContext(),"The upload was successfull!",Toast.LENGTH_LONG).show();
                FragmentNavigation.loadFoodsFragment(Objects.requireNonNull(getActivity()));
            }
        });
    }

    private void setIngredients(View view){
        ingredientsAdapter = new IngredientsAdapter(food.getIngredients(),view.getContext());
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        ingredients.setLayoutManager(manager);
        ingredients.setAdapter(ingredientsAdapter);
        ingredientsAdapter.notifyDataSetChanged();
    }

    private void setPictures(View view) {
        pictures_adapter = new PicturesAdapter(food.getPictures(),view.getContext());
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        pictures.setLayoutManager(manager);
        pictures.setAdapter(pictures_adapter);
        pictures_adapter.notifyDataSetChanged();
    }

    @SuppressLint("SetTextI18n")
    private void setOtherParameters() {
        name.setText(food.getName());
        type.setText(food.getRecipeType());
        origin.setText(food.getOrigin());
        servings.setText(String.valueOf(food.getPortion()));
        time.setText(food.getPreparation_time() + " m");

        preparation.setText(food.getPreparation());
        calorie.setText(String.valueOf(food.getCalorie()));
        carbohydrate.setText(String.valueOf(food.getCarbohydrate()));
        fat.setText(String.valueOf(food.getFat()));
        protein.setText(String.valueOf(food.getProtein()));
        quantity.setText(String.valueOf(food.getQuantity())+"g");
        quantity_serving.setText(String.valueOf(food.getQuantity()/ food.getPortion()) + "g");



        switch(food.getDifficulty())
        {
            case 0: difficulty.setText("Low");break;
            case 1: difficulty.setText("Medium");break;
            case 2: difficulty.setText("Hard");break;
        }
    }

    private void initViews(View view) {
        name = view.findViewById(R.id.fragment_food_details_name);
        type = view.findViewById(R.id.fragment_food_details_type);
        origin = view.findViewById(R.id.fragment_food_details_origin);
        servings = view.findViewById(R.id.fragment_food_details_servings);
        time = view.findViewById(R.id.fragment_food_details_preparation_time);
        difficulty = view.findViewById(R.id.fragment_food_details_difficulty);
        preparation = view.findViewById(R.id.fragment_food_details_preparation);
        pictures = view.findViewById(R.id.fragment_food_details_pictures_recyclerView);
        ingredients = view.findViewById(R.id.fragment_food_details_ingredients);
        calorie = view.findViewById(R.id.fragment_food_details_calorie);
        protein = view.findViewById(R.id.cardview_food_recommend_recipe_details_protein);
        fat = view.findViewById(R.id.cardview_food_recommend_recipe_details_fat);
        carbohydrate = view.findViewById(R.id.cardview_food_recommend_recipe_details_carbohydrate);
        quantity = view.findViewById(R.id.fragment_food_details_quantity);
        quantity_serving = view.findViewById(R.id.fragment_food_details_quantity_per_serving);
        button = view.findViewById(R.id.fragment_food_details_button);
        editText = view.findViewById(R.id.fragment_food_details_quantity_editText);

    }

    private void setIndex() {
        Bundle bundle = this.getArguments();
        int index=0;
        if (bundle != null) {
            index = bundle.getInt("index", 0);
            Log.e("WE GOT: ",index+"");
        }
        food = ((MainActivity) getActivity()).recipes.get(index);
        Log.e("RECIPE NAME:", food.getName());
    }

}
