package com.example.laci.kitchenassistant.Tools;

import android.util.Log;

import com.example.laci.kitchenassistant.BaseClasses.BasicFood;
import com.example.laci.kitchenassistant.BaseClasses.BasicFoodQuantity;
import com.example.laci.kitchenassistant.BaseClasses.IntakeFood;
import com.example.laci.kitchenassistant.BaseClasses.Recipe;
import com.example.laci.kitchenassistant.BaseClasses.User;
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
                    100
            ));
        }

        ArrayList<Recipe> tempRecipes = new ArrayList<>();
        ArrayList<BasicFoodQuantity> tempBasicFood = new ArrayList<>();

        for(int i = 0; i < recipes.size(); ++i){
            tempRecipes.add(new Recipe(recipes.get(i)));
        }
        for(int i = 0; i < basicFoodQuantities.size(); ++i){
            tempBasicFood.add(new BasicFoodQuantity(basicFoodQuantities.get(i)));
        }

        setBreakfasts(tempRecipes, consumedFoods);
        setSnacks(tempBasicFood,consumedFoods);

        tempBasicFood.clear();
        tempRecipes.clear();
        for(int i = 0; i < recipes.size(); ++i){
            tempRecipes.add(new Recipe(recipes.get(i)));
        }
        for(int i = 0; i < basicFoodQuantities.size(); ++i){
            tempBasicFood.add(new BasicFoodQuantity(basicFoodQuantities.get(i)));
        }

        setLunch(tempRecipes,consumedFoods);
        setAfternoonSnacks(tempBasicFood,consumedFoods);
        tempBasicFood.clear();
        tempRecipes.clear();
        for(int i = 0; i < recipes.size(); ++i){
            tempRecipes.add(new Recipe(recipes.get(i)));
        }
        setDinner(tempRecipes, consumedFoods);

        tempBasicFood.clear();
        tempRecipes.clear();

    }

    //----------MEAL SPECIFIED FUNCTIONS----------------------------------------------------------------------------------------------------------------

    //----------DINNER------------------------------------------------------------------------------
    private static void setDinner(ArrayList<Recipe> recipes, ArrayList<IntakeFood> consumedFoods){
        ArrayList<Recipe> dinners = new ArrayList<>();
        fillAllDinnerArray(dinners, recipes);
        filterRecipesArray(dinners, consumedFoods);
        int randomNumber, target_calorie;
        float rate;
        UserNeedPersonalInformations.getDinners().clear();

        if(avgIntake != 0 && UserNeedPersonalInformations.getWeeklyAverageConsumedCalories()  >= MINIMUM_AVG_CALORIE)
            rate = (float)(int)(avgIntake/UserNeedPersonalInformations.getWeeklyAverageConsumedCalories());
        else
            rate = 1;
        int oldQuantity;
        target_calorie = (int)((avgIntake * rate * DINNER_RATIO));
        for(int i = 0; i < NUMBER_OF_FOOD_RECOMMENDATIONS_FOR_EACH_MEAL; ++i){
            if(dinners.size() > 0){
                randomNumber = random.nextInt(dinners.size());
                Recipe tempRecipe = new Recipe(dinners.get(randomNumber));
                oldQuantity = tempRecipe.getQuantity();
                tempRecipe.setQuantity(target_calorie * tempRecipe.getQuantity() / tempRecipe.getCalorie());
                tempRecipe.setCalorie(target_calorie);
                tempRecipe.setProtein(tempRecipe.getProtein() * tempRecipe.getQuantity() / oldQuantity);
                tempRecipe.setCarbohydrate(tempRecipe.getCarbohydrate() * tempRecipe.getQuantity() / oldQuantity);
                tempRecipe.setFat(tempRecipe.getFat() * tempRecipe.getQuantity() / oldQuantity);
                UserNeedPersonalInformations.addToDinners(tempRecipe);
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

        int randomNumber, target_calorie;
        float rate;
        UserNeedPersonalInformations.getAfternoonSnacks().clear();

        if(avgIntake != 0 && UserNeedPersonalInformations.getWeeklyAverageConsumedCalories()  >= MINIMUM_AVG_CALORIE)
            rate = (float)(int)(avgIntake/UserNeedPersonalInformations.getWeeklyAverageConsumedCalories());
        else
            rate = 1;
        int oldQuantity;
        target_calorie = (int)((avgIntake * rate * AFTERNOON_SNACK_RATIO));
        for(int i = 0; i < NUMBER_OF_FOOD_RECOMMENDATIONS_FOR_EACH_MEAL; ++i){
            if(afternoonSnacks.size() > 0){
                randomNumber = random.nextInt(afternoonSnacks.size());
                BasicFoodQuantity tempBasicFood = new BasicFoodQuantity(afternoonSnacks.get(randomNumber));
                oldQuantity = tempBasicFood.getQuantity();
                tempBasicFood.setQuantity(target_calorie * tempBasicFood.getQuantity() / tempBasicFood.getCalorie());
                tempBasicFood.setCalorie(target_calorie);
                tempBasicFood.setProtein(tempBasicFood.getProtein() * tempBasicFood.getQuantity() / oldQuantity);
                tempBasicFood.setCarbohydrate(tempBasicFood.getCarbohydrate() * tempBasicFood.getQuantity() / oldQuantity);
                tempBasicFood.setFat(tempBasicFood.getFat() * tempBasicFood.getQuantity() / oldQuantity);
                UserNeedPersonalInformations.addToAfternoonSnacks(tempBasicFood);
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
        int randomNumber, target_calorie;
        float rate;
        UserNeedPersonalInformations.getLunches().clear();

        if(avgIntake != 0 && UserNeedPersonalInformations.getWeeklyAverageConsumedCalories()  >= MINIMUM_AVG_CALORIE)
            rate = (float)(int)(avgIntake/UserNeedPersonalInformations.getWeeklyAverageConsumedCalories());
        else
            rate = 1;
        int oldQuantity;
        target_calorie = (int)((avgIntake * rate * LUNCH_RATIO));
        for(int i = 0; i < NUMBER_OF_FOOD_RECOMMENDATIONS_FOR_EACH_MEAL; ++i){
            if(lunches.size() > 0){
                randomNumber = random.nextInt(lunches.size());
                Recipe tempRecipe = new Recipe(lunches.get(randomNumber));
                oldQuantity = tempRecipe.getQuantity();
                tempRecipe.setQuantity(target_calorie * tempRecipe.getQuantity() / tempRecipe.getCalorie());
                tempRecipe.setCalorie(target_calorie);
                tempRecipe.setProtein(tempRecipe.getProtein() * tempRecipe.getQuantity() / oldQuantity);
                tempRecipe.setCarbohydrate(tempRecipe.getCarbohydrate() * tempRecipe.getQuantity() / oldQuantity);
                tempRecipe.setFat(tempRecipe.getFat() * tempRecipe.getQuantity() / oldQuantity);
                UserNeedPersonalInformations.addToLunches(tempRecipe);
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

        int randomNumber, target_calorie;
        float rate;
        UserNeedPersonalInformations.getSnacks().clear();

        if(avgIntake != 0 && UserNeedPersonalInformations.getWeeklyAverageConsumedCalories()  >= MINIMUM_AVG_CALORIE)
            rate = (float)(int)(avgIntake/UserNeedPersonalInformations.getWeeklyAverageConsumedCalories());
        else
            rate = 1;
        int oldQuantity;
        target_calorie = (int)((avgIntake * rate * SNACK_RATIO));
        for(int i = 0; i < NUMBER_OF_FOOD_RECOMMENDATIONS_FOR_EACH_MEAL; ++i){
            if(snacks.size() > 0){
                randomNumber = random.nextInt(snacks.size());
                BasicFoodQuantity tempBasicFood = new BasicFoodQuantity(snacks.get(randomNumber));
                oldQuantity = tempBasicFood.getQuantity();
                tempBasicFood.setQuantity(target_calorie*tempBasicFood.getQuantity()/tempBasicFood.getCalorie());
                tempBasicFood.setCalorie(target_calorie);
                tempBasicFood.setProtein(tempBasicFood.getProtein() * tempBasicFood.getQuantity() / oldQuantity);
                tempBasicFood.setCarbohydrate(tempBasicFood.getCarbohydrate() * tempBasicFood.getQuantity() / oldQuantity);
                tempBasicFood.setFat(tempBasicFood.getFat() * tempBasicFood.getQuantity() / oldQuantity);
                UserNeedPersonalInformations.addToSnacks(tempBasicFood);
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
        int randomNumber, target_calorie;
        float rate;
        UserNeedPersonalInformations.getBreakfasts().clear();

        if(avgIntake != 0 && UserNeedPersonalInformations.getWeeklyAverageConsumedCalories() >= MINIMUM_AVG_CALORIE)
            rate = (float)(int)(avgIntake/UserNeedPersonalInformations.getWeeklyAverageConsumedCalories());
        else
            rate = (float)1.0;

        target_calorie = (int)((avgIntake * rate * BREAKFAST_RATIO));
        int oldQuantity;
        for(int i = 0; i < NUMBER_OF_FOOD_RECOMMENDATIONS_FOR_EACH_MEAL; ++i){
            randomNumber = random.nextInt(breakfasts.size());
            Recipe tempBreakfast = new Recipe(breakfasts.get(randomNumber));
            oldQuantity = tempBreakfast.getQuantity();
            tempBreakfast.setQuantity(target_calorie*oldQuantity/tempBreakfast.getCalorie());
            tempBreakfast.setCalorie(target_calorie);
            tempBreakfast.setProtein(tempBreakfast.getProtein() * tempBreakfast.getQuantity() / oldQuantity);
            tempBreakfast.setCarbohydrate(tempBreakfast.getCarbohydrate() * tempBreakfast.getQuantity() / oldQuantity);
            tempBreakfast.setFat(tempBreakfast.getFat() * tempBreakfast.getQuantity() / oldQuantity);
            UserNeedPersonalInformations.addToBreakfasts(tempBreakfast);
            breakfasts.remove(randomNumber);
        }

        breakfasts.clear();
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
