package com.example.laci.kitchenassistant.main.AddFood;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.laci.kitchenassistant.BaseClasses.BasicFood;
import com.example.laci.kitchenassistant.R;
import com.example.laci.kitchenassistant.main.MainActivity;

public class AddFoodFragment extends Fragment {
    private TextInputEditText name,origin,preparation_time, portion, difficulty,quantity,preparation,quantity_of_all_food;
    private Spinner type, ingredient;
    private Button add_ingredient, add_picture, add_food;
    private RecyclerView ingredients, pictures;
    private ArrayAdapter<CharSequence> type_adapter;
    private String choosen_type;
    private BasicFood chosen_basic_food;
    private AddFoodBasicFoodsSpinnerAdapter ingredient_adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_food, container, false);
        initViews(view);
        setUpTypeSpinner(container.getContext());

        ingredient_adapter = new AddFoodBasicFoodsSpinnerAdapter(view.getContext(),((MainActivity)getActivity()).basicFoods);
        ingredient.setAdapter(ingredient_adapter);

        ingredient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                BasicFood selectedBasicFood = (BasicFood)adapterView.getItemAtPosition(i);
                chosen_basic_food = selectedBasicFood;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                chosen_basic_food = (BasicFood)adapterView.getItemAtPosition(0);
            }
        });



        return view;
    }

    public void setUpTypeSpinner(Context context){
        type_adapter = ArrayAdapter.createFromResource(context,R.array.type_names,android.R.layout.simple_spinner_item);
        type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(type_adapter);
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                choosen_type = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                choosen_type = "Breakfast";
            }
        });
    }

    public void initViews(View view){
        name = view.findViewById(R.id.add_food_name_editText);
        origin = view.findViewById(R.id.add_food_origin_editText);
        preparation_time = view.findViewById(R.id.add_food_preparation_time_editText);
        portion = view.findViewById(R.id.add_food_portion_editText);
        difficulty = view.findViewById(R.id.add_food_difficulty_editText);
        quantity = view.findViewById(R.id.add_food_ingredient_quantity_editText);
        preparation = view.findViewById(R.id.add_food_preparation_editText);
        quantity_of_all_food = view.findViewById(R.id.add_food_quantity_editText);
        type = view.findViewById(R.id.add_food_spinner_food_type);
        ingredient = view.findViewById(R.id.add_food_food_ingerdients_spinner);
        add_ingredient = view.findViewById(R.id.add_food_button_add_ingredient);
        add_picture = view.findViewById(R.id.add_food_add_image_button);
        add_food = view.findViewById(R.id.add_food_upload_food);
        ingredients = view.findViewById(R.id.add_food_add_ingredients_recyclerView);
        pictures = view.findViewById(R.id.add_food_add_images_recyclerView);
    }

}
