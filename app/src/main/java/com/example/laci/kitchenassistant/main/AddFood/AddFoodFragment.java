package com.example.laci.kitchenassistant.main.AddFood;

import android.content.Context;
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
import android.widget.Toast;

import com.example.laci.kitchenassistant.BaseClasses.BasicFood;
import com.example.laci.kitchenassistant.BaseClasses.BasicFoodQuantity;
import com.example.laci.kitchenassistant.R;
import com.example.laci.kitchenassistant.Tools.Validations;
import com.example.laci.kitchenassistant.main.MainActivity;

import java.util.ArrayList;
import java.util.Objects;

public class AddFoodFragment extends Fragment {
    private TextInputEditText name,origin,preparation_time, portion, difficulty,quantity,preparation,quantity_of_all_food;
    private Spinner type, ingredient;
    private Button add_ingredient, add_picture, add_food;
    private RecyclerView ingredients_recyclerView, pictures_recyclerView;
    private ArrayAdapter<CharSequence> type_adapter;
    private String choosen_type;
    private BasicFood chosen_basic_food;
    private AddFoodBasicFoodsSpinnerAdapter ingredient_spinner_adapter;
    private AddFoodIngredientAdapter ingredient_recyclerView_adapter;
    private ArrayList<BasicFoodQuantity> ingredients;
    private Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_food, container, false);
        initViews(view);
        context = view.getContext();
        setUpTypeSpinner(container.getContext());
        setUpIngredientSpinner(container.getContext());
        setUpIngredientRecyclerView(view.getContext());
        setUpListeners();




        return view;
    }

    private void setUpListeners(){
        add_ingredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Validations.validateQuantity(quantity.getText().toString()) == 0){
                    BasicFood new_basicFood = new BasicFood(chosen_basic_food.getName(),
                            chosen_basic_food.getPicture(),
                            chosen_basic_food.getCalorie()*Integer.parseInt(quantity.getText().toString())/100,
                            chosen_basic_food.getProtein()*Integer.parseInt(quantity.getText().toString())/100,
                            chosen_basic_food.getCarbohydrate()*Integer.parseInt(quantity.getText().toString())/100,
                            chosen_basic_food.getFat()*Integer.parseInt(quantity.getText().toString())/100);
                    ingredients.add(new BasicFoodQuantity(new_basicFood,Integer.parseInt(quantity.getText().toString())));
                    ingredient_recyclerView_adapter.notifyDataSetChanged();

                    Toast.makeText(context,quantity.getText() + "g " + new_basicFood.getName() + " added to ingredients.",Toast.LENGTH_LONG).show();
                    quantity.setText("");
                }
                else
                    quantity.setError("Not a valid number.");
            }
        });
    }

    private void setUpIngredientRecyclerView(Context context){
        ingredients = new ArrayList<>();
        ingredient_recyclerView_adapter = new AddFoodIngredientAdapter(context,ingredients);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayout.VERTICAL);
        ingredients_recyclerView.setLayoutManager(manager);
        ingredients_recyclerView.setAdapter(ingredient_recyclerView_adapter);
    }

    private void setUpIngredientSpinner(Context context){
        ingredient_spinner_adapter = new AddFoodBasicFoodsSpinnerAdapter(context,((MainActivity)Objects.requireNonNull(getActivity())).basicFoods);
        ingredient.setAdapter(ingredient_spinner_adapter);

        ingredient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                chosen_basic_food = (BasicFood)adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                chosen_basic_food = (BasicFood)adapterView.getItemAtPosition(0);
            }
        });
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
        ingredients_recyclerView = view.findViewById(R.id.add_food_add_ingredients_recyclerView);
        pictures_recyclerView = view.findViewById(R.id.add_food_add_images_recyclerView);
    }

}
