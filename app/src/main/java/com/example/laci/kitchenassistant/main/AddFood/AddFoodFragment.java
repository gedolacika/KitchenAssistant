package com.example.laci.kitchenassistant.main.AddFood;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
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
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.laci.kitchenassistant.BaseClasses.BasicFood;
import com.example.laci.kitchenassistant.BaseClasses.BasicFoodQuantity;
import com.example.laci.kitchenassistant.BaseClasses.Recipe;
import com.example.laci.kitchenassistant.R;
import com.example.laci.kitchenassistant.Tools.FragmentNavigation;
import com.example.laci.kitchenassistant.Tools.Tools;
import com.example.laci.kitchenassistant.Tools.Validations;
import com.example.laci.kitchenassistant.firebase.RecipeHandler;
import com.example.laci.kitchenassistant.main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.google.android.gms.common.util.CollectionUtils.listOf;

public class AddFoodFragment extends Fragment {
    private TextInputEditText name,origin,preparation_time, portion,preparation,quantity_of_all_food,link;
    private Spinner difficulty;
    private SeekBar quantity_seekBar;
    private int quantity = 0, choosen_difficulty;
    private Spinner type, ingredient;
    private Button add_ingredient, add_picture, add_food;
    private RecyclerView ingredients_recyclerView, pictures_recyclerView;
    private ArrayAdapter<CharSequence> type_adapter, difficulty_adapter;
    private String choosen_type;
    private BasicFood chosen_basic_food;
    private AddFoodBasicFoodsSpinnerAdapter ingredient_spinner_adapter;
    private AddFoodIngredientAdapter ingredient_recyclerView_adapter;
    private AddFoodAddPictureAdapter add_picture_adapter;
    private ArrayList<BasicFoodQuantity> ingredients;
    private Context context;
    private ArrayList<byte[]> pictures_byte_byte;
    private ArrayList<String> pictures_byte_name,recipe_url_pictures;
    private static final int PICTURE_GALLERY = 0;
    private static final int PICTURE_CAMERA = 1;
    private static final int PICTURE_GALLERY_PERMISSION = 3;
    private static final int PICTURE_CAMERA_PERMISSION = 4;
    private View globalView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_food, container, false);
        globalView = view;
        ((MainActivity)getActivity()).setTitle("Add food");
        initViews(view);
        context = view.getContext();
        recipe_url_pictures = new ArrayList<>();
        setUpTypeSpinner(container.getContext());
        setUpIngredientSpinner(container.getContext());
        setUpIngredientRecyclerView(view.getContext());
        setUpPicturesRecyclerView(view.getContext());
        setUpListeners();
        setUpDifficultySpinner(view.getContext());



        return view;
    }

    private void setUpPicturesRecyclerView(Context context){
        pictures_byte_name = new ArrayList<>();
        pictures_byte_byte = new ArrayList<>();
        add_picture_adapter = new AddFoodAddPictureAdapter(context, recipe_url_pictures,pictures_byte_byte);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayout.HORIZONTAL);
        pictures_recyclerView.setLayoutManager(manager);
        pictures_recyclerView.setAdapter(add_picture_adapter);
    }

    private void setUpListeners(){
        quantity_seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener()
                {
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress,
                                                  boolean fromUser)
                    {
                        quantity = progress*10;
                        add_ingredient.setText("ADD " + quantity + "g");
                    }
                }
        );
        add_ingredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BasicFood new_basicFood = new BasicFood(chosen_basic_food.getName(),
                        chosen_basic_food.getPicture(),
                        chosen_basic_food.getCalorie()*quantity/100,
                        chosen_basic_food.getProtein()*quantity/100,
                        chosen_basic_food.getCarbohydrate()*quantity/100,
                        chosen_basic_food.getFat()*quantity/100);
                ingredients.add(new BasicFoodQuantity(new_basicFood,quantity));
                ingredient_recyclerView_adapter.notifyDataSetChanged();

                Toast.makeText(context,quantity + "g " + new_basicFood.getName() + " added to ingredients.",Toast.LENGTH_LONG).show();
            }
        });
        add_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!link.getText().toString().matches("")){
                    recipe_url_pictures.add(link.getText().toString());
                    add_picture_adapter.notifyDataSetChanged();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddFoodFragment.this.context);

                    builder.setMessage("Select the source of the picture.");

                    builder.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{
                                                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        PICTURE_GALLERY_PERMISSION);
                            } else {

                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, PICTURE_GALLERY);
                            }
                        }
                    });

                    builder.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{
                                                android.Manifest.permission.CAMERA,
                                                android.Manifest.permission.CAMERA},
                                        PICTURE_CAMERA_PERMISSION);
                            } else {

                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, PICTURE_CAMERA);
                            }

                        }
                    });

                    builder.show();
                }

            }
        });

        add_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //,origin,preparation_time, portion, difficulty, preparation,quantity_of_all_food
                boolean send_data = true;
                if(Validations.validateString(name.getText().toString()) != 0){
                    name.setError("Not a valid name");
                    send_data = false;
                }
                if(Validations.validateNumber(preparation_time.getText().toString()) != 0){
                    preparation_time.setError("Not a valid time.");
                    send_data = false;
                }
                if(Validations.validateNumber(portion.getText().toString()) != 0){
                    portion.setError("Not a valid number of servings.");
                    send_data = false;
                }
                /*if(Validations.validateNumber(difficulty.getText().toString()) != 0){
                    difficulty.setError("Not a valid difficulty. It could be 0-easy,1-medium,2-hard.");
                    send_data = false;
                }*/
                if(Validations.validateString(origin.getText().toString()) != 0){
                    origin.setError("Not a valid origin.");
                    send_data = false;
                }

                if(Validations.validateStringWithNewLine(preparation.getText().toString()) != 0){
                    preparation.setError("Not a valid description.");
                    send_data = false;
                }
                if(Validations.validateNumber(quantity_of_all_food.getText().toString()) != 0){
                    quantity_of_all_food.setError("Not a valid origin.");
                    send_data = false;
                }
                int calorie, protein, fat, carbohydrate,sugar,saturated;
                calorie = 0;
                protein = 0;
                fat = 0;
                carbohydrate = 0;
                sugar = 0;
                saturated = 0;
                for(int i = 0; i < ingredients.size(); ++i){
                    calorie += ingredients.get(i).getCalorie();
                    protein += ingredients.get(i).getProtein();
                    fat += ingredients.get(i).getFat();
                    carbohydrate += ingredients.get(i).getCarbohydrate();
                    sugar += ingredients.get(i).getSugar();
                    saturated += ingredients.get(i).getSaturated();
                }
                if(send_data){
                    Recipe upload_recipe = new Recipe(name.getText().toString(),
                            calorie,
                            protein,
                            carbohydrate,
                            fat,
                            ingredients,
                            preparation.getText().toString(),
                            origin.getText().toString(),
                            choosen_type,
                            FirebaseAuth.getInstance().getUid(),
                            Integer.parseInt(preparation_time.getText().toString()),
                            Integer.parseInt(portion.getText().toString()),
                            choosen_difficulty,
                            true,
                            recipe_url_pictures);
                    upload_recipe.setQuantity(Integer.parseInt(quantity_of_all_food.getText().toString()));
                    upload_recipe.setSaturated(saturated);
                    upload_recipe.setSugar(sugar);
                    RecipeHandler.uploadRecipe(upload_recipe,pictures_byte_name,pictures_byte_byte);
                    Toast.makeText(context,"The recipe uploaded to database.",Toast.LENGTH_LONG).show();
                    FragmentNavigation.loadHomeFragment(getActivity());
                }

            }
        });
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PICTURE_GALLERY:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    InputStream istream = null;
                    try {
                        istream = getContext().getContentResolver().openInputStream(uri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        pictures_byte_byte.add(Tools.getBytes(istream));
                        pictures_byte_name.add(String.valueOf(System.currentTimeMillis()));
                        add_picture_adapter.notifyDataSetChanged();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                break;
            case PICTURE_CAMERA:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    pictures_byte_byte.add(baos.toByteArray());
                    pictures_byte_name.add(String.valueOf(System.currentTimeMillis()));
                    add_picture_adapter.notifyDataSetChanged();


                }
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PICTURE_GALLERY_PERMISSION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, PICTURE_GALLERY);

                } else {
                    Toast.makeText(globalView.getContext(),"You can't upload picture from gallery if you don't enabled the access of store.",Toast.LENGTH_LONG).show();
                }
                break;
            case PICTURE_CAMERA_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, PICTURE_CAMERA);
                } else {
                    Toast.makeText(globalView.getContext(),"You can't upload picture from gallery if you don't enabled the access of camera.",Toast.LENGTH_LONG).show();
                }
                break;


        }
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
        ArrayList<BasicFood> foods = ((MainActivity)Objects.requireNonNull(getActivity())).basicFoods;
        for(int i = 0; i < ((MainActivity)getActivity()).recipes.size() ;++i)
        {
            ((MainActivity)getActivity()).recipes.get(i).setPicture(((MainActivity)getActivity()).recipes.get(i).getPictures().get(0));
            foods.add(((MainActivity)getActivity()).recipes.get(i));
        }
        ingredient_spinner_adapter = new AddFoodBasicFoodsSpinnerAdapter(context,foods);
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

    public void setUpDifficultySpinner(Context context){
        difficulty_adapter = ArrayAdapter.createFromResource(context,R.array.difficulty,android.R.layout.simple_spinner_item);
        difficulty_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficulty.setAdapter(difficulty_adapter);
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                choosen_difficulty = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                choosen_difficulty = 0;
            }
        });
    }

    public void initViews(View view){
        name = view.findViewById(R.id.add_food_name_editText);
        origin = view.findViewById(R.id.add_food_origin_editText);
        preparation_time = view.findViewById(R.id.add_food_preparation_time_editText);
        portion = view.findViewById(R.id.add_food_portion_editText);
        difficulty = view.findViewById(R.id.add_food_difficulty_spinner);
        quantity_seekBar = view.findViewById(R.id.add_food_ingredient_seekBar);
        preparation = view.findViewById(R.id.add_food_preparation_editText);
        quantity_of_all_food = view.findViewById(R.id.add_food_quantity_editText);
        type = view.findViewById(R.id.add_food_spinner_food_type);
        ingredient = view.findViewById(R.id.add_food_food_ingerdients_spinner);
        add_ingredient = view.findViewById(R.id.add_food_button_add_ingredient);
        add_picture = view.findViewById(R.id.add_food_add_image_button);
        add_food = view.findViewById(R.id.add_food_upload_food);
        ingredients_recyclerView = view.findViewById(R.id.add_food_add_ingredients_recyclerView);
        pictures_recyclerView = view.findViewById(R.id.add_food_add_images_recyclerView);
        link = view.findViewById(R.id.add_food_picture_link_editText);
    }

}
