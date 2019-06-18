package com.example.laci.kitchenassistant.main.Foods.BasicFoods;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.laci.kitchenassistant.R;
import com.example.laci.kitchenassistant.main.MainActivity;

public class FoodsBasicFoodsFragment extends Fragment {
    private RecyclerView recyclerView;
    private BasicFoodsAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_foods_basic_foods, container, false);
        ((MainActivity)getActivity()).setTitle("Basic foods");
        recyclerView = view.findViewById(R.id.fragment_foods_basic_foods_recyclerView);
        adapter = new BasicFoodsAdapter(((MainActivity)getActivity()).basicFoods,view.getContext(),getActivity());
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return view;
    }
}
