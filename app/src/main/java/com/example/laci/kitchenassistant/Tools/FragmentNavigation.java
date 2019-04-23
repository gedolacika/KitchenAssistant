package com.example.laci.kitchenassistant.Tools;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.example.laci.kitchenassistant.R;
import com.example.laci.kitchenassistant.main.Account.AccountFragment;
import com.example.laci.kitchenassistant.main.AddFood.AddFoodFragment;
import com.example.laci.kitchenassistant.main.Details.BasicFood.BasicFoodDetailsFragment;
import com.example.laci.kitchenassistant.main.DiagramsFragment;
import com.example.laci.kitchenassistant.main.Fitness.FitnessFragment;
import com.example.laci.kitchenassistant.main.Details.Recipe.FoodDetailsFragment;
import com.example.laci.kitchenassistant.main.Foods.BasicFoods.FoodsBasicFoodsFragment;
import com.example.laci.kitchenassistant.main.Foods.Recipes.FoodsRecipesFragment;
import com.example.laci.kitchenassistant.main.FoodsAtHomeFragment;
import com.example.laci.kitchenassistant.main.Foods.FoodsFragment;
import com.example.laci.kitchenassistant.main.HistoryFragment;
import com.example.laci.kitchenassistant.main.Home.HomeFragment;

public class FragmentNavigation {
    private static Fragment fragment;

    public static void loadAccountFragment(FragmentActivity fragmentActivity) {
        fragment = fragmentActivity.getSupportFragmentManager().findFragmentByTag("AccountFragment");
        if (fragment == null) {
            fragment = new AccountFragment();
        }

        loadFragment(fragment, fragmentActivity);
    }

    public static void loadDiagramsFragment(FragmentActivity fragmentActivity) {
        fragment = fragmentActivity.getSupportFragmentManager().findFragmentByTag("DiagramsFragment");
        if (fragment == null) {
            fragment = new DiagramsFragment();
        }

        loadFragment(fragment, fragmentActivity);
    }

    public static void loadFitnessFragment(FragmentActivity fragmentActivity) {
        fragment = fragmentActivity.getSupportFragmentManager().findFragmentByTag("FitnessFragment");
        if (fragment == null) {
            fragment = new FitnessFragment();
        }

        loadFragment(fragment, fragmentActivity);
    }

    public static void loadFoodsAtHomeFragment(FragmentActivity fragmentActivity) {
        fragment = fragmentActivity.getSupportFragmentManager().findFragmentByTag("FoodsAtHomeFragment");
        if (fragment == null) {
            fragment = new FoodsAtHomeFragment();
        }

        loadFragment(fragment, fragmentActivity);
    }

    public static void loadFoodsFragment(FragmentActivity fragmentActivity) {
        fragment = fragmentActivity.getSupportFragmentManager().findFragmentByTag("FoodsFragment");
        if (fragment == null) {
            fragment = new FoodsFragment();
        }

        loadFragment(fragment, fragmentActivity);
    }

    public static void loadHistoryFragment(FragmentActivity fragmentActivity) {
        fragment = fragmentActivity.getSupportFragmentManager().findFragmentByTag("HistoryFragment");
        if (fragment == null) {
            fragment = new HistoryFragment();
        }

        loadFragment(fragment, fragmentActivity);
    }

    public static void loadHomeFragment(FragmentActivity fragmentActivity) {
        fragment = fragmentActivity.getSupportFragmentManager().findFragmentByTag("HomeFragment");
        if (fragment == null) {
            fragment = new HomeFragment();
        }

        loadFragment(fragment, fragmentActivity);
    }

    public static void loadAddFoodFragment(FragmentActivity fragmentActivity) {
        fragment = fragmentActivity.getSupportFragmentManager().findFragmentByTag("AddFoodFragment");
        if (fragment == null) {
            fragment = new AddFoodFragment();
        }

        loadFragment(fragment, fragmentActivity);
    }

    public static void loadDetailsFragment(FragmentActivity fragmentActivity, int index) {
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);

        fragment = fragmentActivity.getSupportFragmentManager().findFragmentByTag("DetailsFragment");
        if (fragment == null) {
            fragment = new FoodDetailsFragment();
        }
        fragment.setArguments(bundle);
        loadFragment(fragment, fragmentActivity);
    }

    public static void loadRecipesSubFragment(FragmentActivity fragmentActivity) {
        fragment = fragmentActivity.getSupportFragmentManager().findFragmentByTag("RecipesSubFragment");
        if (fragment == null) {
            fragment = new FoodsRecipesFragment();
        }

        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_foods_frameLayout, fragment, fragment.getClass().getSimpleName());
        ft.commit();
    }

    public static void loadBasicFoodsSubFragment(FragmentActivity fragmentActivity) {
        fragment = fragmentActivity.getSupportFragmentManager().findFragmentByTag("BasicFoodsSubFragment");
        if (fragment == null) {
            fragment = new FoodsBasicFoodsFragment();
        }

        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_foods_frameLayout, fragment, fragment.getClass().getSimpleName());
        ft.commit();
    }

    public static void loadBasicFoodsDetailsFragment(FragmentActivity fragmentActivity, int index) {
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        fragment = fragmentActivity.getSupportFragmentManager().findFragmentByTag("BasicFoodsDetailsFragment");
        if (fragment == null) {
            fragment = new BasicFoodDetailsFragment();
        }
        fragment.setArguments(bundle);
        loadFragment(fragment, fragmentActivity);
    }


    private static void loadFragment(Fragment fragment, FragmentActivity fragmentActivity) {
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_activity_content_frame_layout, fragment, fragment.getClass().getSimpleName());
        ft.commit();
    }

}
