package com.example.laci.kitchenassistant.firebase;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.laci.kitchenassistant.BaseClasses.BasicFoodQuantity;
import com.example.laci.kitchenassistant.BaseClasses.Recipe;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class RecipeHandler {
    private static String RECIPE_NAME = "Name";
    private static String RECIPE_PREPARATION_TIME = "Preparation_time";
    private static String RECIPE_PORTION = "Portion";
    private static String RECIPE_DIFFICULTY = "Difficulty";
    private static String RECIPE_ORIGIN = "Origin";
    private static String RECIPE_TYPE = "Type";
    private static String RECIPE_UPLOADER_ID = "UploaderID";
    private static String RECIPE_VISIBILITY = "Visibility";

    private static String RECIPE_INGREDIENTS = "Ingredients";
    private static String RECIPE_INGREDIENT_NAME = "Name";
    private static String RECIPE_INGREDIENT_QUANTITY = "Quantity";
    private static String RECIPE_INGREDIENT_PICTURE = "Quantity";
    private static String RECIPE_PREPARATION = "Preparation";

     static String RECIPE_CALORIE = "Calorie";
    private static String RECIPE_CARBOHYDRATE = "Carbohydrate";
    private static String RECIPE_PROTEIN = "Protein";
    private static String RECIPE_FAT = "Fat";
    private static String RECIPE_QUANTITY = "Quantity";

    private static String RECIPE_PICTURES_URL = "Picture URL";
    private static String UPLOAD_SUCCESS = "SUCCESS";
    private static String DATABASE_REFERENCE_BASE_PATH = "Recipes/";


    //PATH
    private static final String IMAGES_STORAGE_BASE_PATH = "images/recipes/";

    public static void downloadRecipes(RetrieveDataListener<ArrayList<Recipe>> listener){

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(IMAGES_STORAGE_BASE_PATH);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Recipe> recipes = new ArrayList<Recipe>();
                for(DataSnapshot current : dataSnapshot.getChildren()){
                    Recipe recipe = new Recipe();

                    if(current.child(RECIPE_NAME).exists())
                        recipe.setName(current.child(RECIPE_NAME).getValue().toString());
                    if(current.child(RECIPE_CALORIE).exists())
                        recipe.setCalorie(Integer.parseInt(current.child(RECIPE_CALORIE).getValue().toString()));
                    if(current.child(RECIPE_CARBOHYDRATE).exists())
                        recipe.setCarbohydrate(Integer.parseInt(current.child(RECIPE_CARBOHYDRATE).getValue().toString()));
                    if(current.child(RECIPE_PROTEIN).exists())
                        recipe.setProtein(Integer.parseInt(current.child(RECIPE_PROTEIN).getValue().toString()));
                    if(current.child(RECIPE_FAT).exists())
                        recipe.setFat(Integer.parseInt(current.child(RECIPE_FAT).getValue().toString()));

                    if(current.child(RECIPE_PREPARATION).exists())
                        recipe.setPreparation(current.child(RECIPE_PREPARATION).getValue().toString());
                    if(current.child(RECIPE_ORIGIN).exists())
                        recipe.setOrigin(current.child(RECIPE_ORIGIN).getValue().toString());
                    if(current.child(RECIPE_TYPE).exists())
                        recipe.setRecipeType(current.child(RECIPE_TYPE).getValue().toString());
                    if(current.child(RECIPE_UPLOADER_ID).exists())
                        recipe.setUploaderID(current.child(RECIPE_UPLOADER_ID).getValue().toString());

                    if(current.child(RECIPE_PREPARATION_TIME).exists())
                        recipe.setPreparation_time(Integer.parseInt(current.child(RECIPE_PREPARATION_TIME).getValue().toString()));
                    if(current.child(RECIPE_PORTION).exists())
                        recipe.setPortion(Integer.parseInt(current.child(RECIPE_PORTION).getValue().toString()));
                    if(current.child(RECIPE_DIFFICULTY).exists())
                        recipe.setDifficulty(Integer.parseInt(current.child(RECIPE_DIFFICULTY).getValue().toString()));
                    if(current.child(RECIPE_QUANTITY).exists())
                        recipe.setQuantity(Integer.parseInt(current.child(RECIPE_QUANTITY).getValue().toString()));

                    recipe.setVisibility(Boolean.parseBoolean(current.child(RECIPE_VISIBILITY).getValue().toString()));
                    if(current.child(RECIPE_PICTURES_URL).exists()){

                        for(DataSnapshot current_pictures : current.child(RECIPE_PICTURES_URL).getChildren()){
                            recipe.addPicture(current_pictures.getValue().toString());
                        }
                    }

                    if(current.child(RECIPE_INGREDIENTS).exists()){
                        for(DataSnapshot current_ingredients : current.child(RECIPE_INGREDIENTS).getChildren()){
                            recipe.addBasicFood(new BasicFoodQuantity(current_ingredients.child(RECIPE_INGREDIENT_NAME).getValue().toString(),
                                    current_ingredients.child(RECIPE_INGREDIENT_PICTURE).getValue().toString(),
                                    Integer.parseInt(current_ingredients.child(RECIPE_INGREDIENT_QUANTITY).getValue().toString())));
                            }
                    }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public static void uploadRecipe(final Recipe recipe, final ArrayList<String> storageReferences, final ArrayList<byte[]> pictures){
        if(storageReferences.size() > 0){
            uploadPhoto(pictures.get(pictures.size() - 1),
                    IMAGES_STORAGE_BASE_PATH + storageReferences.get(storageReferences.size() - 1) + ".jpg",
                    new RetrieveDataListener<String>() {
                        @Override
                        public void onSuccess(String data) {
                            FirebaseStorage.getInstance().getReference().child(IMAGES_STORAGE_BASE_PATH + storageReferences.get(storageReferences.size() - 1) + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageURL = uri.toString();
                                    recipe.addPicture(imageURL);
                                    storageReferences.remove(storageReferences.get(storageReferences.size()-1));
                                    pictures.remove(pictures.get(pictures.size()-1));
                                    uploadRecipe(recipe,storageReferences,pictures);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });
                        }

                        @Override
                        public void onFailure(String message) {

                        }
                    });

        }else{
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(DATABASE_REFERENCE_BASE_PATH + recipe.getName());
            if(!recipe.getName().isEmpty())
                databaseReference.child(RECIPE_NAME).setValue(recipe.getName());
            if(recipe.getCalorie() != -1)
                databaseReference.child(RECIPE_CALORIE).setValue(recipe.getCalorie());
            if(recipe.getCarbohydrate() != -1)
                databaseReference.child(RECIPE_CARBOHYDRATE).setValue(recipe.getCarbohydrate());
            if(recipe.getProtein() != -1)
                databaseReference.child(RECIPE_PROTEIN).setValue(recipe.getProtein());
            if(recipe.getFat() != -1)
                databaseReference.child(RECIPE_FAT).setValue(recipe.getFat());

            if(recipe.getPreparation() != null)
                databaseReference.child(RECIPE_PREPARATION).setValue(recipe.getPreparation());
            if(recipe.getOrigin() != null)
                databaseReference.child(RECIPE_ORIGIN).setValue(recipe.getOrigin());
            if(recipe.getRecipeType() != null)
                databaseReference.child(RECIPE_TYPE).setValue(recipe.getRecipeType());
            if(recipe.getUploaderID() != null)
                databaseReference.child(RECIPE_UPLOADER_ID);

            if(recipe.getPreparation_time() != -1)
                databaseReference.child(RECIPE_PREPARATION_TIME).setValue(recipe.getPreparation_time());
            if(recipe.getPortion() != -1)
                databaseReference.child(RECIPE_PORTION).setValue(recipe.getPortion());
            if(recipe.getDifficulty() != -1)
                databaseReference.child(RECIPE_DIFFICULTY).setValue(recipe.getDifficulty());
            if(recipe.getQuantity() >= 0)
                databaseReference.child(RECIPE_QUANTITY).setValue(recipe.getQuantity());

            databaseReference.child(RECIPE_VISIBILITY).setValue(recipe.isVisibility());

            for(int i = 0; i < recipe.getPictures().size() ; ++i){
                databaseReference.child(RECIPE_PICTURES_URL).child(String.valueOf(i)).setValue(recipe.getPictures().get(i));
            }


            for(int i = 0; i < recipe.getIngredients().size();++i){
                databaseReference.child(RECIPE_INGREDIENTS).child(String.valueOf(i)).child(RECIPE_INGREDIENT_NAME).setValue(recipe.getIngredients().get(i).getName());
                databaseReference.child(RECIPE_INGREDIENTS).child(String.valueOf(i)).child(RECIPE_INGREDIENT_QUANTITY).setValue(recipe.getIngredients().get(i).getQuantity());
                databaseReference.child(RECIPE_INGREDIENTS).child(String.valueOf(i)).child(RECIPE_INGREDIENT_PICTURE).setValue(recipe.getIngredients().get(i).getPicture());
            }

        }
    }



    public static void uploadPhoto(byte[] picture, String path, final RetrieveDataListener<String> listener){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(path);

        UploadTask uploadTask = storageReference.putBytes(picture);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onFailure(e.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                listener.onSuccess(UPLOAD_SUCCESS);
            }
        });
    }



}
