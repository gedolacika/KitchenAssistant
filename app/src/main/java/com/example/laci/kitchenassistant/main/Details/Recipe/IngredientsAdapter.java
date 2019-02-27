package com.example.laci.kitchenassistant.main.Details.Recipe;

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

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder>  {
    private Context context;
    private ArrayList<BasicFoodQuantity> foods;

    public IngredientsAdapter(ArrayList<BasicFoodQuantity> foods, Context context){
        this.context = context;
        this.foods = foods;
    }
    @NonNull
    @Override
    public IngredientsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardview_details_ingredients,viewGroup,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull IngredientsAdapter.ViewHolder viewHolder, int i) {
        Glide.with(context).load(foods.get(i).getPicture()).into(viewHolder.image);
        viewHolder.name.setText(foods.get(i).getName());
        viewHolder.quantity.setText(String.valueOf(foods.get(i).getQuantity()) + "g");
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView name, quantity;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.cardview_details_ingredients_image);
            name = itemView.findViewById(R.id.cardview_details_ingredients_name);
            quantity = itemView.findViewById(R.id.cardview_details_ingredients_quantity);
        }
    }
}
