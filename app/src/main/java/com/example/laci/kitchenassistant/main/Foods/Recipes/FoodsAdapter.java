package com.example.laci.kitchenassistant.main.Foods.Recipes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
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
import com.example.laci.kitchenassistant.Tools.FragmentNavigation;

import java.util.ArrayList;


public class FoodsAdapter extends RecyclerView.Adapter<FoodsAdapter.ViewHolder> {
    private ArrayList<Recipe>  recipes=null;
    private Context context;
    private FragmentActivity activity;

    public FoodsAdapter(ArrayList<Recipe> recipes, Context context, FragmentActivity activity)
    {
        this.recipes = recipes;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardview_food,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Glide.with(context).load(recipes.get(i).getPictures().get(0)).into(viewHolder.image);
        viewHolder.calorie.setText(String.valueOf(recipes.get(i).getCalorie()));
        viewHolder.fat.setText(String.valueOf(recipes.get(i).getFat()));
        viewHolder.carbohydrate.setText(String.valueOf(recipes.get(i).getCarbohydrate()));
        viewHolder.protein.setText(String.valueOf(recipes.get(i).getProtein()));
        viewHolder.name.setText(String.valueOf(recipes.get(i).getName()));
        viewHolder.type.setText(String.valueOf(recipes.get(i).getRecipeType()));
        viewHolder.index = i;

    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView calorie,fat, carbohydrate, protein,name,type;
        private ImageView image;
        private ConstraintLayout layout;
        private int index;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            calorie = itemView.findViewById(R.id.cardview_food_calorie_text7);
            fat = itemView.findViewById(R.id.fragment_food_details_fat);
            carbohydrate = itemView.findViewById(R.id.fragment_food_details_carbohydrate);
            protein = itemView.findViewById(R.id.fragment_food_details_protein);
            image = itemView.findViewById(R.id.cardview_food_image);
            layout = itemView.findViewById(R.id.cardview_food_constarint_layout);
            name = itemView.findViewById(R.id.cardview_food_name);
            type = itemView.findViewById(R.id.cardview_food_type);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("AAAAAAAAAAAA",index+"");
                    FragmentNavigation.loadDetailsFragment(activity,index);
                }
            });
        }
    }
}
