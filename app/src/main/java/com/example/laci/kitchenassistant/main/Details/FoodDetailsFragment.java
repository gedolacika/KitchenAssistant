package com.example.laci.kitchenassistant.main.Details;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.laci.kitchenassistant.BaseClasses.Recipe;
import com.example.laci.kitchenassistant.R;
import com.example.laci.kitchenassistant.main.MainActivity;

public class FoodDetailsFragment extends Fragment {
    private int index;
    private TextView name, type, origin, servings, time, difficulty, preparation, calorie, protein, fat, carbohydrate;
    private RecyclerView pictures, ingredients;
    private Recipe recipe;
    private PicturesAdapter pictures_adapter;
    private IngredientsAdapter ingredientsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_details, container, false);

        setIndex();
        initViews(view);
        setPictures(view);
        setIngredients(view);
        setOtherParameters();
        //setListener();

        return view;
    }

    private void setIngredients(View view){
        ingredientsAdapter = new IngredientsAdapter(recipe.getIngredients(),view.getContext());
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        ingredients.setLayoutManager(manager);
        ingredients.setAdapter(ingredientsAdapter);
        ingredientsAdapter.notifyDataSetChanged();
    }

    private void setPictures(View view) {
        pictures_adapter = new PicturesAdapter(recipe.getPictures(),view.getContext());
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        pictures.setLayoutManager(manager);
        pictures.setAdapter(pictures_adapter);
        pictures_adapter.notifyDataSetChanged();
    }

    private void setOtherParameters() {
        name.setText(recipe.getName());
        type.setText(recipe.getRecipeType());
        origin.setText(recipe.getOrigin());
        servings.setText(String.valueOf(recipe.getPortion()));
        time.setText(recipe.getPreparation_time() + " m");
        difficulty.setText(String.valueOf(recipe.getDifficulty()));
        preparation.setText(recipe.getPreparation());
        calorie.setText(String.valueOf(recipe.getCalorie()));
        carbohydrate.setText(String.valueOf(recipe.getCarbohydrate()));
        fat.setText(String.valueOf(recipe.getFat()));
        protein.setText(String.valueOf(recipe.getProtein()));
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
        protein = view.findViewById(R.id.fragment_food_details_protein);
        fat = view.findViewById(R.id.fragment_food_details_fat);
        carbohydrate = view.findViewById(R.id.fragment_food_details_carbohydrate);
    }

    private void setIndex() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            int index = bundle.getInt("index", 0);
        }
        recipe = ((MainActivity) getActivity()).recipes.get(index);
    }

}
