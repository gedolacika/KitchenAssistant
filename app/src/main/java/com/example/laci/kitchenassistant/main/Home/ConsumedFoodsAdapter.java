package com.example.laci.kitchenassistant.main.Home;

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
import com.example.laci.kitchenassistant.BaseClasses.IntakeFood;
import com.example.laci.kitchenassistant.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ConsumedFoodsAdapter extends RecyclerView.Adapter<ConsumedFoodsAdapter.ViewHolder> {
    private ArrayList<IntakeFood> foods;
    private Context context;
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat parser = new SimpleDateFormat("HH:mm:ss");

    public ConsumedFoodsAdapter(ArrayList<IntakeFood> foods, Context context){
        this.foods = foods;
        this.context = context;
    }

    @NonNull
    @Override
    public ConsumedFoodsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardview_consumed_food,viewGroup,false);
        return new ConsumedFoodsAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ConsumedFoodsAdapter.ViewHolder viewHolder, int i) {
        Glide.with(context).load(foods.get(i).getPicture()).into(viewHolder.image);
        viewHolder.calorie.setText(String.valueOf(foods.get(i).getCalorie()));
        viewHolder.fat.setText(String.valueOf(foods.get(i).getFat()));
        viewHolder.carbohydrate.setText(String.valueOf(foods.get(i).getCarbohydrate()));
        viewHolder.protein.setText(String.valueOf(foods.get(i).getProtein()));
        viewHolder.quantity.setText("Quantity: " + foods.get(i).getQuantity() + "g");
        viewHolder.date.setText(parser.format(new Date(foods.get(i).getTime())));
        viewHolder.name.setText(foods.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView calorie, fat, carbohydrate, protein, quantity, date, name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews(itemView);
        }

        private void initViews(View view){
            image = view.findViewById(R.id.cardview_consumed_food_recipe_image);
            calorie = view.findViewById(R.id.cardview_consumed_food_calorie);
            fat = view.findViewById(R.id.cardview_consumed_food_recipe_details_fat);
            carbohydrate = view.findViewById(R.id.cardview_consumed_food_recipe_details_carbohydrate);
            protein = view.findViewById(R.id.cardview_consumed_food_recipe_details_protein);
            quantity = view.findViewById(R.id.cardview_consumed_food_quantity);
            date = view.findViewById(R.id.cardview_consumed_food_recipe_quantity_title);
            name = view.findViewById(R.id.cardview_consumed_food_recipe_name);
        }
    }
}
