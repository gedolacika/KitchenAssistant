package com.example.laci.kitchenassistant.main.Foods;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.laci.kitchenassistant.R;
import com.example.laci.kitchenassistant.Tools.FragmentNavigation;
import com.example.laci.kitchenassistant.main.MainActivity;

import java.util.Objects;

public class FoodsFragment extends Fragment {

    private Button recipes, basicfoods;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_foods, container, false);
        recipes = view.findViewById(R.id.fragment_foods_recipe);
        basicfoods = view.findViewById(R.id.fragment_foods_basic_foods);
        FragmentNavigation.loadRecipesSubFragment(Objects.requireNonNull(getActivity()));
        recipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentNavigation.loadRecipesSubFragment(Objects.requireNonNull(getActivity()));
            }
        });
        basicfoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentNavigation.loadBasicFoodsSubFragment(Objects.requireNonNull(getActivity()));
            }
        });


        return view;
    }

}
