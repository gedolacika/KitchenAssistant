package com.example.laci.kitchenassistant.main.Foods.BasicFoods;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.laci.kitchenassistant.BaseClasses.BasicFood;
import com.example.laci.kitchenassistant.R;
import com.example.laci.kitchenassistant.Tools.FragmentNavigation;

import java.util.ArrayList;

public class BasicFoodsAdapter extends RecyclerView.Adapter<BasicFoodsAdapter.ViewHolder> {

    private ArrayList<BasicFood> foods;
    private Context context;
    private FragmentActivity activity;

    public BasicFoodsAdapter(ArrayList<BasicFood> foods, Context context,FragmentActivity activity) {
        this.foods = foods;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public BasicFoodsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardview_food_basic_food,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BasicFoodsAdapter.ViewHolder viewHolder, int i) {
        Glide.with(context).load(foods.get(i).getPicture()).into(viewHolder.imageView);
        viewHolder.name.setText(foods.get(i).getName());
        viewHolder.calorie.setText(String.valueOf(foods.get(i).getCalorie()));
        viewHolder.fat.setText(String.valueOf(foods.get(i).getFat()));
        viewHolder.protein.setText(String.valueOf(foods.get(i).getProtein()));
        viewHolder.carbohydrate.setText(String.valueOf(foods.get(i).getCarbohydrate()));
        viewHolder.index = i;
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name, calorie, fat, protein, carbohydrate;
        private ImageView imageView;
        private ConstraintLayout layout;
        private int index;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews(itemView);



            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentNavigation.loadBasicFoodsDetailsFragment(activity,index);
                }
            });

        }

        private void initViews(View itemView){
            name = itemView.findViewById(R.id.cardview_food_basic_food_name);
            calorie = itemView.findViewById(R.id.cardview_food_basic_food_calorie);
            fat = itemView.findViewById(R.id.fragment_food_details_fat);
            protein = itemView.findViewById(R.id.fragment_food_details_protein);
            carbohydrate = itemView.findViewById(R.id.fragment_food_details_carbohydrate);
            imageView = itemView.findViewById(R.id.cardview_food_basic_food_image);
            layout = itemView.findViewById(R.id.cardview_food_basic_food_layout_inner);
        }
    }
}
