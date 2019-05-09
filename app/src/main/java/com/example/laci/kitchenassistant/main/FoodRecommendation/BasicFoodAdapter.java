package com.example.laci.kitchenassistant.main.FoodRecommendation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.laci.kitchenassistant.BaseClasses.BasicFoodQuantity;
import com.example.laci.kitchenassistant.R;

import java.util.ArrayList;

public class BasicFoodAdapter extends RecyclerView.Adapter<BasicFoodAdapter.ViewHolder> {
    private ArrayList<BasicFoodQuantity> basicFoodQuantities;
    private Context context;

    public BasicFoodAdapter(ArrayList<BasicFoodQuantity> basicFoodQuantities, Context context){
        this.basicFoodQuantities = basicFoodQuantities;
        this.context = context;
    }

    @NonNull
    @Override
    public BasicFoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardview_food_reccomends_recipe,viewGroup,false);
        return new BasicFoodAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Glide.with(context).load(basicFoodQuantities.get(i).getPicture()).into(viewHolder.imageView);
        viewHolder.name.setText(basicFoodQuantities.get(i).getName());
        viewHolder.calorie.setText(String.valueOf(basicFoodQuantities.get(i).getCalorie()));
        viewHolder.carbohydrate.setText(String.valueOf(basicFoodQuantities.get(i).getCarbohydrate()));
        viewHolder.protein.setText(String.valueOf(basicFoodQuantities.get(i).getProtein()));
        viewHolder.fat.setText(String.valueOf(basicFoodQuantities.get(i).getFat()));
        viewHolder.quantity.setText("Recommended quantity: " + basicFoodQuantities.get(i).getQuantity() + " g");
    }



    @Override
    public int getItemCount() {
        return basicFoodQuantities.size();
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
