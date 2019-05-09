package com.example.laci.kitchenassistant.main.FoodRecommendation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.laci.kitchenassistant.BaseClasses.Recipe;
import com.example.laci.kitchenassistant.R;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {
    private ArrayList<Recipe> recipes;
    private Context context;

    public RecipeAdapter(ArrayList<Recipe> recipes, Context context){
        this.recipes = recipes;
        this.context = context;
    }

    @NonNull
    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardview_food_reccomends_recipe,viewGroup,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.ViewHolder viewHolder, int i) {
        Glide.with(context).load(recipes.get(i).getPictures().get(0)).into(viewHolder.imageView);
        viewHolder.name.setText(recipes.get(i).getName());
        viewHolder.calorie.setText(String.valueOf(recipes.get(i).getCalorie()));
        viewHolder.carbohydrate.setText(String.valueOf(recipes.get(i).getCarbohydrate()));
        viewHolder.protein.setText(String.valueOf(recipes.get(i).getProtein()));
        viewHolder.fat.setText(String.valueOf(recipes.get(i).getFat()));
        viewHolder.quantity.setText("Recommended quantity: " + recipes.get(i).getQuantity() + " g");
        /*Glide.with(context).load(bytePictures.get(i-urlPictures.size())).into(viewHolder.imageView);*/
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView name, calorie, protein, carbohydrate, fat, quantity;
        //private ConstraintLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews(itemView);
            /*layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO - Do this!!! - Navigate to details
                }
            });*/
        }

        private void initViews(View itemView){
            imageView = itemView.findViewById(R.id.cardview_food_recommend_recipe_image);
            name = itemView.findViewById(R.id.cardview_food_recommend_recipe_name);
            calorie = itemView.findViewById(R.id.cardview_food_recommend_recipe_calorie_text7);
            protein = itemView.findViewById(R.id.cardview_food_recommend_recipe_details_protein);
            carbohydrate = itemView.findViewById(R.id.cardview_food_recommend_recipe_details_carbohydrate);
            fat = itemView.findViewById(R.id.cardview_food_recommend_recipe_details_fat);
            quantity = itemView.findViewById(R.id.cardview_food_recommend_recipe_quantity_title);
            //layout = itemView.findViewById(R.id.cardview_food_recommend_constraint_layout_inner);
        }
    }
}
