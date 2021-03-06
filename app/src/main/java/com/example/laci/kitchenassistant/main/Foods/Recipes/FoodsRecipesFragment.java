package com.example.laci.kitchenassistant.main.Foods.Recipes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.laci.kitchenassistant.R;
import com.example.laci.kitchenassistant.main.MainActivity;


public class FoodsRecipesFragment extends Fragment {
    private RecyclerView recyclerView;
    private FoodsAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_foods_recipes, container, false);
        ((MainActivity)getActivity()).setTitle("Recipes");
        recyclerView = view.findViewById(R.id.fragment_foods_recipes_recyclerView);
        adapter = new FoodsAdapter(((MainActivity)getActivity()).recipes,view.getContext(),getActivity());
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        Log.e("SIZE",((MainActivity)getActivity()).recipes.size() + "");
        return view;
    }

}
