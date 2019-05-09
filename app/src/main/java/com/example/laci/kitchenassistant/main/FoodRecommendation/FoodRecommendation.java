package com.example.laci.kitchenassistant.main.FoodRecommendation;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.laci.kitchenassistant.BaseClasses.BasicFoodQuantity;
import com.example.laci.kitchenassistant.BaseClasses.Recipe;
import com.example.laci.kitchenassistant.BaseClasses.StepCount;
import com.example.laci.kitchenassistant.BaseClasses.UserNeedPersonalInformations;
import com.example.laci.kitchenassistant.R;
import com.example.laci.kitchenassistant.Tools.CalorieNeedCounter;
import com.example.laci.kitchenassistant.Tools.MenuSetter;
import com.example.laci.kitchenassistant.firebase.Account;
import com.example.laci.kitchenassistant.firebase.RetrieveDataListener;
import com.example.laci.kitchenassistant.main.MainActivity;

import java.util.ArrayList;

public class FoodRecommendation extends Fragment {
    private RecyclerView breakfast, snack, lunch, afternoonSnack, dinner;
    private RecipeAdapter breakfastAdapter, lunchAdapter, dinnerAdapter;
    private BasicFoodAdapter snackAdapter, afternoonSnackAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_food_recommendation, container, false);
        initViews(view);

        /*breakfast = view.findViewById(R.id.fragment_food_recommends_breakfast_recyclerView);
        breakfastAdapter = new RecipeAdapter( UserNeedPersonalInformations.getBreakfasts(), view.getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayout.HORIZONTAL);
        breakfast.setAdapter(breakfastAdapter);
        breakfast.setLayoutManager(manager);*/


        breakfastAdapter = new RecipeAdapter( UserNeedPersonalInformations.getBreakfasts(), view.getContext());
        snackAdapter = new BasicFoodAdapter( UserNeedPersonalInformations.getSnacks(),view.getContext());
        lunchAdapter = new RecipeAdapter( UserNeedPersonalInformations.getLunches(), view.getContext());
        afternoonSnackAdapter = new BasicFoodAdapter( UserNeedPersonalInformations.getAfternoonSnacks(), view.getContext());
        dinnerAdapter = new RecipeAdapter(  UserNeedPersonalInformations.getDinners(), view.getContext());

        setUpRecipeRecyclerView(view.getContext(),breakfast, breakfastAdapter, UserNeedPersonalInformations.getBreakfasts());
        setUpBasicFoodRecyclerView(view.getContext(),snack, snackAdapter, UserNeedPersonalInformations.getSnacks());
        setUpRecipeRecyclerView(view.getContext(),lunch, lunchAdapter, UserNeedPersonalInformations.getLunches());
        setUpBasicFoodRecyclerView(view.getContext(), afternoonSnack, afternoonSnackAdapter, UserNeedPersonalInformations.getAfternoonSnacks());
        setUpRecipeRecyclerView(view.getContext(),dinner, dinnerAdapter, UserNeedPersonalInformations.getDinners());

        Account.downloadAllSteps(new RetrieveDataListener<ArrayList<StepCount>>() {
            @Override
            public void onSuccess(ArrayList<StepCount> data) {
                ((MainActivity)getActivity()).allSteps = data;
                CalorieNeedCounter.CountCalories(((MainActivity)getActivity()).intookedFoods, ((MainActivity)getActivity()).allSteps, ((MainActivity)getActivity()).trainings, ((MainActivity)getActivity()).user);
                MenuSetter.setMenu(((MainActivity)getActivity()).recipes,
                        ((MainActivity)getActivity()).basicFoods,
                        ((MainActivity)getActivity()).intookedFoods);

                breakfastAdapter.notifyDataSetChanged();
                snackAdapter.notifyDataSetChanged();
                lunchAdapter.notifyDataSetChanged();
                afternoonSnackAdapter.notifyDataSetChanged();
                dinnerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(container.getContext(), message.toString(),Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    private void setUpRecipeRecyclerView(Context context, RecyclerView recyclerView, RecipeAdapter adapter, ArrayList<Recipe> arrayList){
        adapter = new RecipeAdapter(arrayList,context);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayout.HORIZONTAL);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
    }

    private void setUpBasicFoodRecyclerView(Context context, RecyclerView recyclerView, BasicFoodAdapter adapter, ArrayList<BasicFoodQuantity> arrayList){
        adapter = new BasicFoodAdapter(arrayList,context);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayout.HORIZONTAL);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
    }

    private void initViews(View view){
        breakfast = view.findViewById(R.id.fragment_food_recommends_breakfast_recyclerView);
        snack = view.findViewById(R.id.fragment_food_recommends_snack_recyclerView);
        lunch = view.findViewById(R.id.fragment_food_recommends_lunch_recyclerView);
        afternoonSnack = view.findViewById(R.id.fragment_food_recommends_afternoon_snack_recyclerView);
        dinner = view.findViewById(R.id.fragment_food_recommends_dinner_recyclerView);
    }

}
