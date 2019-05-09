package com.example.laci.kitchenassistant.Tools;

import android.util.Log;

import com.example.laci.kitchenassistant.BaseClasses.BasicFood;
import com.example.laci.kitchenassistant.BaseClasses.BasicFoodQuantity;
import com.example.laci.kitchenassistant.BaseClasses.IntakeFood;
import com.example.laci.kitchenassistant.BaseClasses.Recipe;
import com.example.laci.kitchenassistant.BaseClasses.UserNeedPersonalInformations;

import java.util.ArrayList;
import java.util.Random;

public class MenuSetter {
    private static float BREAKFAST_RATIO = (float)(double)0.27;
    private static float SNACK_RATIO = (float)(double)0.20;
    private static float LUNCH_RATIO = (float)(double)0.20;
    private static float AFTERNOON_SNACK_RATIO = (float)(double)0.19;
    private static float DINNER_RATIO = (float)(double)0.14;
    private static int NUMBER_OF_FOOD_RECOMMENDATIONS_FOR_EACH_MEAL = 3;
    private static int NUMBER_OF_DAYS_BETWEEN_TWO_SAME_FOODS = 1; //Number of X days
    private static Random random = new Random();
    private static ArrayList<Recipe> consumedFoodsForTheLastXDays = new ArrayList<>();
    private static final int MINIMUM_AVG_CALORIE = 1000;
    private static int avgIntake;

    //Recommends - use a normal distribution
    public static void setMenu(ArrayList<Recipe> recipes, ArrayList<BasicFood> basicFoods, ArrayList<IntakeFood> consumedFoods){
        avgIntake = UserNeedPersonalInformations.getAverageCalorieIntake() > MINIMUM_AVG_CALORIE ? UserNeedPersonalInformations.getAverageCalorieIntake() : MINIMUM_AVG_CALORIE;
        UserNeedPersonalInformations.calculateWeeklyAverageConsumedCalories(consumedFoods);
        ArrayList<BasicFoodQuantity> basicFoodQuantities = new ArrayList<>();
        for(int i = 0; i < basicFoods.size(); ++i){
            basicFoodQuantities.add(new BasicFoodQuantity(
                    basicFoods.get(i),
                    0
            ));
        }

        setBreakfasts(recipes, consumedFoods);
        setSnacks(basicFoodQuantities,consumedFoods);
        setLunch(recipes,consumedFoods);
        setAfternoonSnacks(basicFoodQuantities,consumedFoods);
        setDinner(recipes, consumedFoods);



    }

    //----------MEAL SPECIFIED FUNCTIONS----------------------------------------------------------------------------------------------------------------

    //----------DINNER------------------------------------------------------------------------------
    private static void setDinner(ArrayList<Recipe> recipes, ArrayList<IntakeFood> consumedFoods){
        ArrayList<Recipe> dinners = new ArrayList<>();
        fillAllDinnerArray(dinners, recipes);
        filterRecipesArray(dinners, consumedFoods);
        int randomNumber, quantity;
        float rate;
        UserNeedPersonalInformations.getDinners().clear();

        if(avgIntake != 0 && UserNeedPersonalInformations.getWeeklyAverageConsumedCalories() != 0)
            rate = (float)(int)(avgIntake/UserNeedPersonalInformations.getWeeklyAverageConsumedCalories());
        else
            rate = 1;

        quantity = (int)((avgIntake * rate * DINNER_RATIO));
        for(int i = 0; i < NUMBER_OF_FOOD_RECOMMENDATIONS_FOR_EACH_MEAL; ++i){
            if(dinners.size() > 0){
                randomNumber = random.nextInt(dinners.size());
                dinners.get(randomNumber).setQuantity(quantity);
                UserNeedPersonalInformations.addToDinners(dinners.get(randomNumber));
                dinners.remove(randomNumber);
            }
        }

    }




    private static void fillAllDinnerArray(ArrayList<Recipe> breakfasts, ArrayList<Recipe> recipes){
        for(int i = 0; i < recipes.size(); ++i){
            if(recipes.get(i).getRecipeType().equals("Breakfast") ||
                    recipes.get(i).getRecipeType().equals("Fish") ||
                    recipes.get(i).getRecipeType().equals("Soup")){
                breakfasts.add(recipes.get(i));
            }
        }
    }


    //----------AFTERNOON SNACK---------------------------------------------------------------------
    private static void setAfternoonSnacks(ArrayList<BasicFoodQuantity> recipes, ArrayList<IntakeFood> consumedFoods){
        ArrayList<BasicFoodQuantity> afternoonSnacks = new ArrayList<>();
        fillAllAfternoonSnackArray(afternoonSnacks, recipes);
        filterBasicFoodQuantityArray(afternoonSnacks, consumedFoods);

        int randomNumber, quantity;
        float rate;
        UserNeedPersonalInformations.getAfternoonSnacks().clear();

        if(avgIntake != 0 && UserNeedPersonalInformations.getWeeklyAverageConsumedCalories() != 0)
            rate = (float)(int)(avgIntake/UserNeedPersonalInformations.getWeeklyAverageConsumedCalories());
        else
            rate = 1;

        quantity = (int)((avgIntake * rate * AFTERNOON_SNACK_RATIO));
        for(int i = 0; i < NUMBER_OF_FOOD_RECOMMENDATIONS_FOR_EACH_MEAL; ++i){
            if(afternoonSnacks.size() > 0){
                randomNumber = random.nextInt(afternoonSnacks.size());
                afternoonSnacks.get(randomNumber).setQuantity(quantity);
                UserNeedPersonalInformations.addToAfternoonSnacks(afternoonSnacks.get(randomNumber));
                afternoonSnacks.remove(randomNumber);
            }
        }
    }

    private static void fillAllAfternoonSnackArray(ArrayList<BasicFoodQuantity> snack, ArrayList<BasicFoodQuantity> basicFoods){
        for(int i = 0; i < basicFoods.size(); ++i){

            if(basicFoods.get(i).getType() != null){
                if(basicFoods.get(i).getType().equals("snack") ||
                        basicFoods.get(i).getType().equals("Snack") ||
                        basicFoods.get(i).getType().equals("fruit") ||
                        basicFoods.get(i).getType().equals("seed")){
                    snack.add(basicFoods.get(i));
                }
            }
        }
    }

    //----------LUNCH-------------------------------------------------------------------------------
    private static void setLunch(ArrayList<Recipe> recipes, ArrayList<IntakeFood> consumedFoods){
        ArrayList<Recipe> lunches = new ArrayList<>();
        fillAllLunchArray(lunches, recipes);
        filterRecipesArray(lunches, consumedFoods);
        int randomNumber, quantity;
        float rate;
        UserNeedPersonalInformations.getLunches().clear();

        if(avgIntake != 0 && UserNeedPersonalInformations.getWeeklyAverageConsumedCalories() != 0)
            rate = (float)(int)(avgIntake/UserNeedPersonalInformations.getWeeklyAverageConsumedCalories());
        else
            rate = 1;

        quantity = (int)((avgIntake * rate * LUNCH_RATIO));
        for(int i = 0; i < NUMBER_OF_FOOD_RECOMMENDATIONS_FOR_EACH_MEAL; ++i){
            if(lunches.size() > 0){
                randomNumber = random.nextInt(lunches.size());
                lunches.get(randomNumber).setQuantity(quantity);
                UserNeedPersonalInformations.addToLunches(lunches.get(randomNumber));
                lunches.remove(randomNumber);
            }

        }
    }




    private static void fillAllLunchArray(ArrayList<Recipe> breakfasts, ArrayList<Recipe> recipes){
        for(int i = 0; i < recipes.size(); ++i){
            if(recipes.get(i).getRecipeType().equals("Main course") ||
                    recipes.get(i).getRecipeType().equals("Fish") ||
                    recipes.get(i).getRecipeType().equals("Soup")){
                breakfasts.add(recipes.get(i));
            }
        }
    }

    //----------SNACK-------------------------------------------------------------------------------
    private static void setSnacks(ArrayList<BasicFoodQuantity> recipes, ArrayList<IntakeFood> consumedFoods){
        ArrayList<BasicFoodQuantity> snacks = new ArrayList<>();
        fillAllSnackArray(snacks, recipes);
        filterBasicFoodQuantityArray(snacks, consumedFoods);

        int randomNumber, quantity;
        float rate;
        UserNeedPersonalInformations.getSnacks().clear();

        if(avgIntake != 0 && UserNeedPersonalInformations.getWeeklyAverageConsumedCalories() != 0)
            rate = (float)(int)(avgIntake/UserNeedPersonalInformations.getWeeklyAverageConsumedCalories());
        else
            rate = 1;

        quantity = (int)((avgIntake * rate * SNACK_RATIO));
        for(int i = 0; i < NUMBER_OF_FOOD_RECOMMENDATIONS_FOR_EACH_MEAL; ++i){
            if(snacks.size() > 0){
                randomNumber = random.nextInt(snacks.size());
                snacks.get(randomNumber).setQuantity(quantity);
                UserNeedPersonalInformations.addToSnacks(snacks.get(randomNumber));
                snacks.remove(randomNumber);
            }
        }
    }

    private static void fillAllSnackArray(ArrayList<BasicFoodQuantity> snack, ArrayList<BasicFoodQuantity> basicFoods){
        for(int i = 0; i < basicFoods.size(); ++i){

            if(basicFoods.get(i).getType() != null){
                if(basicFoods.get(i).getType().equals("snack") ||
                        basicFoods.get(i).getType().equals("Snack") ||
                        basicFoods.get(i).getType().equals("fruit") ||
                        basicFoods.get(i).getType().equals("seed")){
                    snack.add(basicFoods.get(i));
                }
            }
        }
    }

    //----------BREAKFAST---------------------------------------------------------------------------
    private static void setBreakfasts(ArrayList<Recipe> recipes, ArrayList<IntakeFood> consumedFoods){
        ArrayList<Recipe> breakfasts = new ArrayList<>();
        fillAllBreakfastArray(breakfasts, recipes);
        filterRecipesArray(breakfasts, consumedFoods);
        int randomNumber, quantity;
        float rate;
        UserNeedPersonalInformations.getBreakfasts().clear();

        if(avgIntake != 0 && UserNeedPersonalInformations.getWeeklyAverageConsumedCalories() != 0)
            rate = (float)(int)(avgIntake/UserNeedPersonalInformations.getWeeklyAverageConsumedCalories());
        else
            rate = 1;

        quantity = (int)((avgIntake * rate * BREAKFAST_RATIO));
        for(int i = 0; i < NUMBER_OF_FOOD_RECOMMENDATIONS_FOR_EACH_MEAL; ++i){
            randomNumber = random.nextInt(breakfasts.size());
            breakfasts.get(randomNumber).setQuantity(quantity);
            UserNeedPersonalInformations.addToBreakfasts(breakfasts.get(randomNumber));
            breakfasts.remove(randomNumber);
        }
    }




    private static void fillAllBreakfastArray(ArrayList<Recipe> breakfasts, ArrayList<Recipe> recipes){
        for(int i = 0; i < recipes.size(); ++i){
            if(recipes.get(i).getRecipeType().equals("Breakfast")){
                breakfasts.add(recipes.get(i));
            }
        }
    }


//--------------UNIVERSAL FUNCTIONS---------------------------------------------------------------------------------------------------------------------
    private static void filterRecipesArray(ArrayList<Recipe> recipes, ArrayList<IntakeFood> consumedFoods){
        long lastXDaysInMillis = System.currentTimeMillis() - (NUMBER_OF_DAYS_BETWEEN_TWO_SAME_FOODS * 24 * 60 * 60 * 1000);
        for(int i = 0; i < recipes.size(); ++i){
            for(int j = 0; j < consumedFoods.size(); ++j){
                if(consumedFoods.get(j).getTime() > lastXDaysInMillis){
                    if(recipes.get(i).getName().equals(consumedFoods.get(j).getName()) && recipes.size() > 1){
                        recipes.remove(i);
                    }
                }
            }
        }
    }

    private static void filterBasicFoodQuantityArray(ArrayList<BasicFoodQuantity> basicFoodQuantities, ArrayList<IntakeFood> consumedFoods){
        long lastXDaysInMillis = System.currentTimeMillis() - (NUMBER_OF_DAYS_BETWEEN_TWO_SAME_FOODS * 24 * 60 * 60 * 1000);
        for(int i = 0; i < basicFoodQuantities.size(); ++i){
            for(int j = 0; j < consumedFoods.size(); ++j){
                if(consumedFoods.get(j).getTime() > lastXDaysInMillis){
                    if(basicFoodQuantities.get(i).getName().equals(consumedFoods.get(j).getName()) && basicFoodQuantities.size() > 1){
                        basicFoodQuantities.remove(i);
                    }
                }
            }
        }
    }

}
