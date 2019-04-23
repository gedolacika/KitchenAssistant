package com.example.laci.kitchenassistant.firebase;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.laci.kitchenassistant.BaseClasses.BasicFood;
import com.example.laci.kitchenassistant.BaseClasses.BasicFoodQuantity;
import com.example.laci.kitchenassistant.BaseClasses.IntakeFood;
import com.example.laci.kitchenassistant.BaseClasses.StepCount;
import com.example.laci.kitchenassistant.BaseClasses.Training;
import com.example.laci.kitchenassistant.BaseClasses.TrainingBase;
import com.example.laci.kitchenassistant.BaseClasses.User;
import com.example.laci.kitchenassistant.BaseClasses.WeightHistory;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Objects;

public class Account {
    private static String USER_PATH = "users/" + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    private static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(USER_PATH);

    public static String PROFILE_PICTURE_PATH = "images/users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + ".jpg";
    public static String COVER_PHOTO_PATH = "images/users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "_cover.jpg";

    //ACCOUNT --------------------------------------------------------------------------------------------

    private static String ACCOUNT_AGE = "Age";
    private static String ACCOUNT_NAME = "Name";
    private static String ACCOUNT_WEIGHT = "Weight";
    private static String ACCOUNT_WEIGHT_WEIGHT = "Weight";
    private static String ACCOUNT_WEIGHT_TIME = "Time";
    private static String ACCOUNT_HEIGHT = "Height";
    private static String ACCOUNT_CALORIE_REQUIREMENT = "CalorieRequirement";
    private static String LOSE_KEEP_GAIN_WEIGHT = "LoseKeepGainWeight";
    private static String ACCOUNT_GENDER = "Gender";
    private static String ACCOUNT_GOAL_WEIGHT = "GoalWeight";

    //INTAKE FOOD ---------------------------------------------------------------------------------------

    private static String ACCOUNT_INTAKE_FOOD_BASE_PATH = "IntakeFoods";
    private static String ACCOUNT_INTAKE_FOOD_CALORIE = "Calorie";
    private static String ACCOUNT_INTAKE_FOOD_FAT = "Fat";
    private static String ACCOUNT_INTAKE_FOOD_PROTEIN = "Protein";
    private static String ACCOUNT_INTAKE_FOOD_CARBOHYDRATE = "Carbohydrate";
    private static String ACCOUNT_INTAKE_FOOD_SUGAR = "Sugar";
    private static String ACCOUNT_INTAKE_FOOD_SATURATED = "Saturated";
    private static String ACCOUNT_INTAKE_FOOD_TIME = "Time";
    private static String ACCOUNT_INTAKE_FOOD_NAME = "Name";
    private static String ACCOUNT_INTAKE_FOOD_QUANTITY = "Quantity";
    private static String ACCOUNT_INTAKE_FOOD_IMAGE = "Picture";

    //STEPS ----------------------------------------------------------------------------------------------

    private static String ACCOUNT_STEP_BASE = "Steps";
    private static String ACCOUNT_STEP_TIME = "Time";
    private static String ACCOUNT_STEP_STEP = "Step";

    //TRAINING --------------------------------------------------------------------------------------------

    private static String ACCOUNT_TRAINING_BASE = "Trainings";
    private static String ACCOUNT_TRAINING_NAME = "Name";
    private static String ACCOUNT_TRAINING_DURATION = "Duration";
    private static String ACCOUNT_TRAINING_TIME = "Time";
    private static String ACCOUNT_TRAINING_CALORIE = "Calorie";

    public static void saveTraining(Training training) {
        databaseReference.child(ACCOUNT_TRAINING_BASE).child(String.valueOf(training.getTimeTo())).child(ACCOUNT_TRAINING_NAME).setValue(training.getName());
        databaseReference.child(ACCOUNT_TRAINING_BASE).child(String.valueOf(training.getTimeTo())).child(ACCOUNT_TRAINING_DURATION).setValue(training.getDuration());
        databaseReference.child(ACCOUNT_TRAINING_BASE).child(String.valueOf(training.getTimeTo())).child(ACCOUNT_TRAINING_TIME).setValue(training.getTimeTo());
        databaseReference.child(ACCOUNT_TRAINING_BASE).child(String.valueOf(training.getTimeTo())).child(ACCOUNT_TRAINING_CALORIE).setValue(training.getBurnCalorie());
    }

    public static void downloadTraining(final RetrieveDataListener<ArrayList<Training>> listener) {
        databaseReference.child(ACCOUNT_TRAINING_BASE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Training> trainings = new ArrayList<>();
                String name;
                long time;
                int calorieBurned, duration;
                for(DataSnapshot current : dataSnapshot.getChildren()){
                    if(current.child(ACCOUNT_TRAINING_NAME).exists()) name = current.child(ACCOUNT_TRAINING_NAME).getValue().toString();
                    else name = "Nothing";
                    if(current.child(ACCOUNT_TRAINING_CALORIE).exists()) calorieBurned = Integer.parseInt(current.child(ACCOUNT_TRAINING_CALORIE).getValue().toString());
                    else calorieBurned = -10;
                    if(current.child(ACCOUNT_TRAINING_DURATION).exists())duration = Integer.parseInt(current.child(ACCOUNT_TRAINING_DURATION).getValue().toString());
                    else duration = -10;
                    if(current.child(ACCOUNT_TRAINING_TIME).exists())time = Long.parseLong(current.child(ACCOUNT_TRAINING_TIME).getValue().toString());
                    else time = -1;
                    Training training = new Training(
                            new TrainingBase(
                                    name,
                                    calorieBurned
                            ),
                            duration,
                            time
                    );
                    trainings.add(training);
                }
                listener.onSuccess(trainings);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure(databaseError.getMessage());
            }
        });
    }

    public static void downloadTrainingBase(final RetrieveDataListener<ArrayList<TrainingBase>> listener) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Trainings");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<TrainingBase> trainings = new ArrayList<>();
                for(DataSnapshot current : dataSnapshot.getChildren()){
                    TrainingBase training = new TrainingBase(
                                    current.child(ACCOUNT_TRAINING_NAME).getValue().toString(),
                                    Integer.parseInt(current.child(ACCOUNT_TRAINING_CALORIE).getValue().toString())
                            );
                    trainings.add(training);
                }
                listener.onSuccess(trainings);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure(databaseError.getMessage());
            }
        });
    }

    public static void saveSteps(StepCount stepCount) {
        long current_day = stepCount.getTime() - (stepCount.getTime() % (24 * 60 * 60 * 1000));
        if (stepCount.getTime() != -1)
            databaseReference.child(ACCOUNT_STEP_BASE).child(String.valueOf(current_day)).child(String.valueOf(stepCount.getTime())).child(ACCOUNT_STEP_TIME).setValue(stepCount.getTime());
        if (stepCount.getSteps() != -1)
            databaseReference.child(ACCOUNT_STEP_BASE).child(String.valueOf(current_day)).child(String.valueOf(stepCount.getTime())).child(ACCOUNT_STEP_STEP).setValue(stepCount.getSteps());
    }

    public static void downloadDailySteps(final RetrieveDataListener<ArrayList<StepCount>> listener) {
        long time_in_millis = System.currentTimeMillis();
        long current_day = time_in_millis - (time_in_millis % (24 * 60 * 60 * 1000));
        databaseReference.child(ACCOUNT_STEP_BASE).child(String.valueOf(current_day)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<StepCount> stepCounts = new ArrayList<>();
                for (DataSnapshot current : dataSnapshot.getChildren()) {
                    long time = -1;
                    int steps = -1;
                    if (current.child(ACCOUNT_STEP_TIME).exists())
                        time = Long.parseLong(Objects.requireNonNull(current.child(ACCOUNT_STEP_TIME).getValue()).toString());
                    if (current.child(ACCOUNT_STEP_STEP).exists())
                        steps = Integer.parseInt(Objects.requireNonNull(current.child(ACCOUNT_STEP_STEP).getValue()).toString());
                    StepCount stepCount = new StepCount(time, steps);
                    if (time > 0 && steps > 0) stepCounts.add(stepCount);
                    Log.e("Download", "Cica");
                }
                listener.onSuccess(stepCounts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure(databaseError.getMessage());
            }
        });
    }

    public static void downloadWeeklySteps(final RetrieveDataListener<ArrayList<StepCount>> listener) {
        long time_in_millis = System.currentTimeMillis();
        final long current_day = time_in_millis - (time_in_millis % (24 * 60 * 60 * 1000));
        databaseReference.child(ACCOUNT_STEP_BASE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<StepCount> stepCounts = new ArrayList<>();
                long thisWeekStartPoint = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000);
                for (DataSnapshot currentFields : dataSnapshot.getChildren()) {
                    for (DataSnapshot current : currentFields.getChildren()) {
                        long time = -1;
                        int steps = -1;
                        if (current.child(ACCOUNT_STEP_TIME).exists())
                            time = Long.parseLong(Objects.requireNonNull(current.child(ACCOUNT_STEP_TIME).getValue()).toString());
                        if (current.child(ACCOUNT_STEP_STEP).exists())
                            steps = Integer.parseInt(Objects.requireNonNull(current.child(ACCOUNT_STEP_STEP).getValue()).toString());
                        if (time >= thisWeekStartPoint) {
                            StepCount stepCount = new StepCount(time, steps);
                            if (time > 0 && steps > 0) stepCounts.add(stepCount);
                        }
                    }
                }

                listener.onSuccess(stepCounts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure(databaseError.getMessage());
            }
        });
    }


    public static void setIntakeFood(BasicFoodQuantity food) {
        long id = System.currentTimeMillis();
        databaseReference.child(ACCOUNT_INTAKE_FOOD_BASE_PATH).child(String.valueOf(id)).child(ACCOUNT_INTAKE_FOOD_TIME).setValue(id);
        databaseReference.child(ACCOUNT_INTAKE_FOOD_BASE_PATH).child(String.valueOf(id)).child(ACCOUNT_INTAKE_FOOD_NAME).setValue(food.getName());
        databaseReference.child(ACCOUNT_INTAKE_FOOD_BASE_PATH).child(String.valueOf(id)).child(ACCOUNT_INTAKE_FOOD_IMAGE).setValue(food.getPicture());
        databaseReference.child(ACCOUNT_INTAKE_FOOD_BASE_PATH).child(String.valueOf(id)).child(ACCOUNT_INTAKE_FOOD_QUANTITY).setValue(food.getQuantity());
        if (food.getCalorie() > 0)
            databaseReference.child(ACCOUNT_INTAKE_FOOD_BASE_PATH).child(String.valueOf(id)).child(ACCOUNT_INTAKE_FOOD_CALORIE).setValue(food.getCalorie());
        if (food.getFat() > 0)
            databaseReference.child(ACCOUNT_INTAKE_FOOD_BASE_PATH).child(String.valueOf(id)).child(ACCOUNT_INTAKE_FOOD_FAT).setValue(food.getFat());
        if (food.getProtein() > 0)
            databaseReference.child(ACCOUNT_INTAKE_FOOD_BASE_PATH).child(String.valueOf(id)).child(ACCOUNT_INTAKE_FOOD_PROTEIN).setValue(food.getProtein());
        if (food.getCarbohydrate() > 0)
            databaseReference.child(ACCOUNT_INTAKE_FOOD_BASE_PATH).child(String.valueOf(id)).child(ACCOUNT_INTAKE_FOOD_CARBOHYDRATE).setValue(food.getCarbohydrate());
        if (food.getSugar() > 0)
            databaseReference.child(ACCOUNT_INTAKE_FOOD_BASE_PATH).child(String.valueOf(id)).child(ACCOUNT_INTAKE_FOOD_SUGAR).setValue(food.getSugar());
        if (food.getSaturated() > 0)
            databaseReference.child(ACCOUNT_INTAKE_FOOD_BASE_PATH).child(String.valueOf(id)).child(ACCOUNT_INTAKE_FOOD_SATURATED).setValue(food.getSaturated());
    }

    public static void getIntakeFood(final RetrieveDataListener<ArrayList<IntakeFood>> listener) {
        databaseReference.child(ACCOUNT_INTAKE_FOOD_BASE_PATH).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<IntakeFood> intakeFoods = new ArrayList<>();
                for (DataSnapshot current : dataSnapshot.getChildren()) {
                    IntakeFood current_food = new IntakeFood();
                    if (current.child(ACCOUNT_INTAKE_FOOD_TIME).exists())
                        current_food.setTime(Long.parseLong(current.child(ACCOUNT_INTAKE_FOOD_TIME).getValue().toString()));
                    else current_food.setTime(0);
                    if (current.child(ACCOUNT_INTAKE_FOOD_CALORIE).exists())
                        current_food.setCalorie(Integer.parseInt(current.child(ACCOUNT_INTAKE_FOOD_CALORIE).getValue().toString()));
                    else current_food.setCalorie(0);
                    if (current.child(ACCOUNT_INTAKE_FOOD_FAT).exists())
                        current_food.setFat(Integer.parseInt(current.child(ACCOUNT_INTAKE_FOOD_FAT).getValue().toString()));
                    else current_food.setFat(0);
                    if (current.child(ACCOUNT_INTAKE_FOOD_PROTEIN).exists())
                        current_food.setProtein(Integer.parseInt(current.child(ACCOUNT_INTAKE_FOOD_PROTEIN).getValue().toString()));
                    else current_food.setProtein(0);
                    if (current.child(ACCOUNT_INTAKE_FOOD_CARBOHYDRATE).exists())
                        current_food.setCarbohydrate(Integer.parseInt(current.child(ACCOUNT_INTAKE_FOOD_CARBOHYDRATE).getValue().toString()));
                    else current_food.setCarbohydrate(0);
                    if (current.child(ACCOUNT_INTAKE_FOOD_SUGAR).exists())
                        current_food.setSugar(Integer.parseInt(current.child(ACCOUNT_INTAKE_FOOD_SUGAR).getValue().toString()));
                    else current_food.setSugar(0);
                    if (current.child(ACCOUNT_INTAKE_FOOD_SATURATED).exists())
                        current_food.setSaturated(Integer.parseInt(current.child(ACCOUNT_INTAKE_FOOD_SATURATED).getValue().toString()));
                    else current_food.setSaturated(0);
                    intakeFoods.add(current_food);
                }
                listener.onSuccess(intakeFoods);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure(databaseError.getMessage());
            }
        });
    }

    public static void setUser(User user) {
        if (user.getName() != null) databaseReference.child(ACCOUNT_NAME).setValue(user.getName());
        if (user.getAge() != -1) databaseReference.child(ACCOUNT_AGE).setValue(user.getAge());
        if (user.getCalorie_requirement() != -1)
            databaseReference.child(ACCOUNT_CALORIE_REQUIREMENT).setValue(user.getCalorie_requirement());
        if (user.getHeight() != -1)
            databaseReference.child(ACCOUNT_HEIGHT).setValue(user.getHeight());
        if (user.getGoal_weight() != -1)
            databaseReference.child(ACCOUNT_GOAL_WEIGHT).setValue(user.getGoal_weight());

        if (user.getWeight() != -1) {
            String id = String.valueOf(System.currentTimeMillis());
            databaseReference.child(ACCOUNT_WEIGHT).child(id).child(ACCOUNT_WEIGHT_WEIGHT).setValue(user.getWeight());
            databaseReference.child(ACCOUNT_WEIGHT).child(id).child(ACCOUNT_WEIGHT_TIME).setValue(System.currentTimeMillis());
        }

        databaseReference.child(ACCOUNT_GENDER).setValue(user.getGender());
    }

    public static void getUser(final RetrieveDataListener<User> listener) {

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = new User();
                if (dataSnapshot.child(ACCOUNT_NAME).exists())
                    user.setName(dataSnapshot.child(ACCOUNT_NAME).getValue().toString());
                if (dataSnapshot.child(ACCOUNT_AGE).exists())
                    user.setAge(Integer.parseInt(dataSnapshot.child(ACCOUNT_AGE).getValue().toString()));
                if (dataSnapshot.child(ACCOUNT_CALORIE_REQUIREMENT).exists())
                    user.setCalorie_requirement(Integer.parseInt(dataSnapshot.child(ACCOUNT_CALORIE_REQUIREMENT).getValue().toString()));
                if (dataSnapshot.child(ACCOUNT_HEIGHT).exists())
                    user.setHeight(Integer.parseInt(dataSnapshot.child(ACCOUNT_HEIGHT).getValue().toString()));
                if (dataSnapshot.child(ACCOUNT_GOAL_WEIGHT).exists())
                    user.setGoal_weight(Integer.parseInt(dataSnapshot.child(ACCOUNT_GOAL_WEIGHT).getValue().toString()));
                if(dataSnapshot.child(ACCOUNT_GENDER).exists())
                    user.setGender(Integer.parseInt(dataSnapshot.child(ACCOUNT_GENDER).getValue().toString()));

                if (dataSnapshot.child(ACCOUNT_WEIGHT).exists()) {
                    ArrayList<WeightHistory> weightHistories = new ArrayList<>();
                    for (DataSnapshot current : dataSnapshot.child(ACCOUNT_WEIGHT).getChildren()) {
                        Long weight, time;
                        weight = Long.parseLong(current.child(ACCOUNT_WEIGHT_WEIGHT).getValue().toString());
                        time = Long.parseLong(current.child(ACCOUNT_WEIGHT_TIME).getValue().toString());
                        weightHistories.add(new WeightHistory(weight, time));
                    }
                    ArrayList<WeightHistory> reversedArray = new ArrayList<>();
                    for (int i = weightHistories.size() - 1; i >= 0; --i) {
                        reversedArray.add(weightHistories.get(i));
                    }
                    user.setWeightHistories(reversedArray);
                }
                // user.setWeight(Integer.parseInt(dataSnapshot.child(ACCOUNT_WEIGHT).getValue().toString()));

                listener.onSuccess(user);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure(databaseError.getMessage());
            }
        });

    }

    public static void uploadProfilePicture(byte[] picture, final RetrieveDataListener<String> listener) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(PROFILE_PICTURE_PATH);

        UploadTask uploadTask = storageReference.putBytes(picture);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onFailure(e.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                listener.onSuccess(taskSnapshot.toString());
            }
        });
    }

    public static void uploadCoverPhoto(byte[] picture, final RetrieveDataListener<String> listener) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(COVER_PHOTO_PATH);

        UploadTask uploadTask = storageReference.putBytes(picture);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onFailure(e.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                listener.onSuccess(taskSnapshot.toString());
            }
        });
    }

    public static void downloadProfilePicture(final ImageView profilePicture, final Context context) {
        FirebaseStorage.getInstance().getReference().child(PROFILE_PICTURE_PATH).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageURL = uri.toString();
                Glide.with(context).
                        load(imageURL).
                        apply(RequestOptions.circleCropTransform()).
                        into(profilePicture);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    public static void downloadCoverPhoto(final ImageView coverPhoto, final Context context) {

        FirebaseStorage.getInstance().getReference().child(COVER_PHOTO_PATH).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageURL = uri.toString();
                Glide.with(context).
                        load(imageURL).
                        into(coverPhoto);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }


}
