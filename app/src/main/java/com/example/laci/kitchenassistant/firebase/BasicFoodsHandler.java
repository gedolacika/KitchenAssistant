package com.example.laci.kitchenassistant.firebase;

import android.support.annotation.NonNull;

import com.example.laci.kitchenassistant.BaseClasses.BasicFood;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class BasicFoodsHandler {
    private static String BASICFOOD_CALORIE = "Calorie";
    private static String BASICFOOD_NAME = "Name";
    private static String BASICFOOD_CARBOHYDRATE = "Carbohydrate";
    private static String BASICFOOD_PROTEIN = "Protein";
    private static String BASICFOOD_FAT = "Fat";
    private static String BASICFOOD_IMAGE = "i";
    private static String BASICFOOD_SUGAR = "Sugar";
    private static String BASICFOOD_SATURATED = "Saturated";
    private static String BASICFOOD_TYPE = "type";

    private static String DATABASE_BASE_PATH = "BasicFoods";

    public static void downloadBasicFoods(final RetrieveDataListener<ArrayList<BasicFood>> listener){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(DATABASE_BASE_PATH);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<BasicFood> basicFoods = new ArrayList<>();
                for(DataSnapshot current : dataSnapshot.getChildren()){
                    BasicFood current_basic_food = new BasicFood();
                    if(current.child(BASICFOOD_NAME).exists())
                        current_basic_food.setName(Objects.requireNonNull(current.child(BASICFOOD_NAME).getValue()).toString());
                    if(current.child(BASICFOOD_CALORIE).exists())
                        current_basic_food.setCalorie(Integer.parseInt(current.child(BASICFOOD_CALORIE).getValue().toString()));
                    else current_basic_food.setCalorie(0);

                    if(current.child(BASICFOOD_CARBOHYDRATE).exists())
                        current_basic_food.setCarbohydrate(Integer.parseInt(current.child(BASICFOOD_CARBOHYDRATE).getValue().toString()));
                    else current_basic_food.setCarbohydrate(0);

                    if(current.child(BASICFOOD_PROTEIN).exists())
                        current_basic_food.setProtein(Integer.parseInt(current.child(BASICFOOD_PROTEIN).getValue().toString()));
                    else current_basic_food.setProtein(0);

                    if(current.child(BASICFOOD_FAT).exists())
                        current_basic_food.setFat(Integer.parseInt(current.child(BASICFOOD_FAT).getValue().toString()));
                    else current_basic_food.setFat(0);

                    if(current.child(BASICFOOD_SUGAR).exists())
                        current_basic_food.setSugar(Integer.parseInt(current.child(BASICFOOD_SUGAR).getValue().toString()));
                    else current_basic_food.setSugar(0);

                    if(current.child(BASICFOOD_SATURATED).exists())
                        current_basic_food.setSaturated(Integer.parseInt(current.child(BASICFOOD_SATURATED).getValue().toString()));
                    else current_basic_food.setSaturated(0);


                    if(current.child(BASICFOOD_IMAGE).exists())
                        current_basic_food.setPicture(current.child(BASICFOOD_IMAGE).getValue().toString());

                    if(current.child(BASICFOOD_TYPE).exists())
                        current_basic_food.setType(current.child(BASICFOOD_TYPE).getValue().toString());
                    else
                        current_basic_food.setType("base");

                    basicFoods.add(current_basic_food);
                }
                listener.onSuccess(basicFoods);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure(databaseError.getMessage());
            }
        });
    }
}
