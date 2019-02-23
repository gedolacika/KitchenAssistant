package com.example.laci.kitchenassistant.main.AddFood;

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

public class AddFoodIngredientAdapter extends RecyclerView.Adapter<AddFoodIngredientAdapter.ViewHolder> {
    private Context context;
    private ArrayList<BasicFoodQuantity> ingredients;
    private int current_id;

    public AddFoodIngredientAdapter(Context context, ArrayList<BasicFoodQuantity> ingredients){
        this.context = context;
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public AddFoodIngredientAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardview_add_food_ingredient_in_recycler_view,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddFoodIngredientAdapter.ViewHolder viewHolder, int i) {
        current_id = i;
        BasicFoodQuantity food = ingredients.get(i);
        viewHolder.name.setText(food.getName());
        viewHolder.calorie_count.setText(String.valueOf(food.getCalorie()));
        viewHolder.protein_count.setText(String.valueOf(food.getProtein()));
        viewHolder.fat_count.setText(String.valueOf(food.getFat()));
        viewHolder.carbohydrate_count.setText(String.valueOf(food.getCarbohydrate()));
        viewHolder.quantity_count.setText(String.valueOf(food.getQuantity()));

        Glide.with(context).load(food.getPicture()).into(viewHolder.picture);

        Glide.with(context).load(R.drawable.calorie_icon).into(viewHolder.calorie_image);
        Glide.with(context).load(R.drawable.protein_icon).into(viewHolder.protein_image);
        Glide.with(context).load(R.drawable.fat_icon).into(viewHolder.fat_image);
        Glide.with(context).load(R.drawable.carbohydrate_icon).into(viewHolder.carbohydrate_image);

    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView picture,calorie_image,protein_image, fat_image, carbohydrate_image,delete;
        private TextView name,calorie_count,protein_count, fat_count, carbohydrate_count,quantity_count;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            setViews(itemView);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ingredients.remove(current_id);
                    notifyDataSetChanged();
                }
            });
        }

        @SuppressLint("CutPasteId")
        private void setViews(View view){
            this.name = view.findViewById(R.id.cardview_add_food_ingredient_in_recycler_view_name);
            this.calorie_count = view.findViewById(R.id.cardview_add_food_ingredient_in_recycler_view_calorie_textView);
            this.protein_count = view.findViewById(R.id.cardview_add_food_ingredient_in_recycler_view_protein_textView);
            this.fat_count = view.findViewById(R.id.cardview_add_food_ingredient_in_recycler_view_fat_textView);
            this.carbohydrate_count = view.findViewById(R.id.cardview_add_food_ingredient_in_recycler_view_carbohydrate_textView);
            this.quantity_count = view.findViewById(R.id.cardview_add_food_ingredient_in_recycler_view_quantity);

            this.picture = view.findViewById(R.id.cardview_add_food_ingredient_in_recycler_view_ingredient_imageView);
            this.calorie_image = view.findViewById(R.id.cardview_add_food_ingredient_in_recycler_view_calorie_image);
            this.protein_image = view.findViewById(R.id.cardview_add_food_ingredient_in_recycler_view_protein_image);
            this.fat_image = view.findViewById(R.id.cardview_add_food_ingredient_in_recycler_view_fat_image);
            this.carbohydrate_image = view.findViewById(R.id.cardview_add_food_ingredient_in_recycler_view_carbohydrate_image);

            this.delete = view.findViewById(R.id.cardview_add_food_ingredient_in_recycler_view_thrash);
        }
    }
}
