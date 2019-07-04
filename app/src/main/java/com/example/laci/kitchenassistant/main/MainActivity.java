package com.example.laci.kitchenassistant.main;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.laci.kitchenassistant.BaseClasses.BasicFood;
import com.example.laci.kitchenassistant.BaseClasses.IntakeFood;
import com.example.laci.kitchenassistant.BaseClasses.Recipe;
import com.example.laci.kitchenassistant.BaseClasses.StepCount;
import com.example.laci.kitchenassistant.BaseClasses.Training;
import com.example.laci.kitchenassistant.BaseClasses.TrainingBase;
import com.example.laci.kitchenassistant.BaseClasses.User;
import com.example.laci.kitchenassistant.R;
import com.example.laci.kitchenassistant.Tools.ActivityNavigation;
import com.example.laci.kitchenassistant.Tools.CalorieNeedCounter;
import com.example.laci.kitchenassistant.Tools.FragmentNavigation;
import com.example.laci.kitchenassistant.firebase.Account;
import com.example.laci.kitchenassistant.firebase.BasicFoodsHandler;
import com.example.laci.kitchenassistant.firebase.RecipeHandler;
import com.example.laci.kitchenassistant.firebase.RetrieveDataListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public User user;
    public ArrayList<BasicFood> basicFoods;
    public ArrayList<Recipe> recipes;
    public ArrayList<StepCount> dailyStepCounts;
    public ArrayList<StepCount> weeklyStepCounts = null;
    public ArrayList<IntakeFood> intookedFoods;
    private Context context;
    public ArrayList<TrainingBase> trainingBases ;
    public ArrayList<Training> trainings ;
    public ArrayList<StepCount> allSteps;
    public ArrayList<StepCount> stepsForEachDay;
    public ArrayList<StepCount> burnedCaloriesForEachDay;
    private ImageView profilePictureToNavigationBar, coverPhotoToNavigationBar;
    private TextView userName;
    private int finishedDownloads = 0;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        downloadInformations();



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void initViews(){
        profilePictureToNavigationBar = findViewById(R.id.main_sidebar_profile_picture);
        coverPhotoToNavigationBar = findViewById(R.id.main_sidebar_coverPhoto);
        userName = findViewById(R.id.main_sidebar_username);
        Account.downloadProfilePicture(profilePictureToNavigationBar,this);
        Account.downloadCoverPhoto(coverPhotoToNavigationBar,this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        initViews();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_account: FragmentNavigation.loadAccountFragment(this); break;
            case R.id.nav_diagram: FragmentNavigation.loadDiagramsFragment(this); break;
            case R.id.nav_fitness: FragmentNavigation.loadFitnessFragment(this); break;
            case R.id.nav_foods: FragmentNavigation.loadFoodsFragment(this); break;
            case R.id.nav_foods_at_home: FragmentNavigation.loadFoodRecommendationFragment(this); break;
            case R.id.nav_home: FragmentNavigation.loadHomeFragment(this); break;
            case R.id.nav_logout: FirebaseAuth.getInstance().signOut();
                ((ActivityManager)context.getSystemService(ACTIVITY_SERVICE))
                        .clearApplicationUserData();
                                    ActivityNavigation.navigateToSplash(this);
                                    break;
            case R.id.nav_add_food: FragmentNavigation.loadAddFoodFragment(this); break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void checkTheEndOfDownloads(){
        ++finishedDownloads;
        if(finishedDownloads == 9){
            CalorieNeedCounter.CountCalories(intookedFoods, allSteps, trainings, user);
            dialog.dismiss();
            FragmentNavigation.loadHomeFragment(this);
        }
    }

    public void downloadInformations(){
        dialog = new AlertDialog.Builder(this)
                .setTitle("Download")
                .setMessage("Downloading data from the server...")
                .show();
        BasicFoodsHandler.downloadBasicFoods(new RetrieveDataListener<ArrayList<BasicFood>>() {
            @Override
            public void onSuccess(ArrayList<BasicFood> data) {
                basicFoods = data;
                checkTheEndOfDownloads();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(context,message,Toast.LENGTH_LONG).show();
            }
        });
        RecipeHandler.downloadRecipes(new RetrieveDataListener<ArrayList<Recipe>>() {
            @Override
            public void onSuccess(ArrayList<Recipe> data) {
                recipes = data;
                checkTheEndOfDownloads();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(context,message,Toast.LENGTH_LONG).show();
            }
        });

        Account.downloadTrainingBase(new RetrieveDataListener<ArrayList<TrainingBase>>() {
            @Override
            public void onSuccess(ArrayList<TrainingBase> data) {
                trainingBases = data;
                checkTheEndOfDownloads();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(context,message,Toast.LENGTH_LONG).show();
            }
        });

        Account.downloadTraining(new RetrieveDataListener<ArrayList<Training>>() {
            @Override
            public void onSuccess(ArrayList<Training> data) {
                trainings = data;
                checkTheEndOfDownloads();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(context,message,Toast.LENGTH_LONG).show();
            }
        });

        Account.getUser(new RetrieveDataListener<User>() {
            @Override
            public void onSuccess(User data) {
                user = data;
                userName.setText(user.getName());
                checkTheEndOfDownloads();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(context,message,Toast.LENGTH_LONG).show();
            }
        });

        Account.getIntakeFood(new RetrieveDataListener<ArrayList<IntakeFood>>() {
            @Override
            public void onSuccess(ArrayList<IntakeFood> data) {
                intookedFoods = data;
                checkTheEndOfDownloads();
            }

            @Override
            public void onFailure(String message) {

            }
        });

        Account.downloadDailySteps(new RetrieveDataListener<ArrayList<StepCount>>() {
            @Override
            public void onSuccess(ArrayList<StepCount> data) {
                dailyStepCounts = data;
                checkTheEndOfDownloads();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        });

        Account.downloadAllSteps(new RetrieveDataListener<ArrayList<StepCount>>() {
            @Override
            public void onSuccess(ArrayList<StepCount> data) {
                allSteps = data;
                checkTheEndOfDownloads();

            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(context, message.toString(),Toast.LENGTH_LONG).show();
            }
        });

        Account.downloadWeeklySteps(new RetrieveDataListener<ArrayList<StepCount>>() {
            @Override
            public void onSuccess(ArrayList<StepCount> data) {
                weeklyStepCounts = data;
                checkTheEndOfDownloads();
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }
}
