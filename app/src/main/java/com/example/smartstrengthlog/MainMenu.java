package com.example.smartstrengthlog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.smartstrengthlog.ui.ProgressTracking.RmCalculator;
import com.example.smartstrengthlog.ui.ProgressTracking.RoutineSelectionExportData;
import com.example.smartstrengthlog.ui.ProgressTracking.RoutineSelectionProgress;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import models.Workout;
import ui.WorkoutRecyclerAdapter;
import util.SmartStrengthLogAPI;

public class MainMenu extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private  FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private List<Workout> workoutList;
    private RecyclerView recyclerView;
    private WorkoutRecyclerAdapter workoutRecyclerAdapter;

    private CollectionReference collectionReference = db.collection("Workout");
    private TextView noWorkoutEntry;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

       // Toast.makeText(MainMenu.this, "INICIADO!", Toast.LENGTH_SHORT).show();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_workout, R.id.navigation_performance)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        //noWorkoutEntry = findViewById(R.id.list_no_workouts);
        //noWorkoutEntry.setVisibility(View.VISIBLE);

        workoutList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
      }

    public void logOut (View view){

        startActivity(new Intent(this, LoginActivity.class));
        finish();

    }

    //Al clickar al botón de nuevo WK, vamos a esta pantalla
    public void createWorkout (View view){

        //Al ser un fragment, tenemos que referenciar el contexto, por eso es diferente.
        Intent myIntent = new Intent(this, NewWorkoutCreator.class);
        startActivity(myIntent);
        finish();

    }

    public void exerciseProgress(View view){
        //Cambio de vista
        Intent intent = new Intent(this,
                RoutineSelectionProgress.class);
        startActivity(intent);

    }

    public void calculate1RM(View view){
        //Cambio de vista
        Intent intent = new Intent(this,
                RmCalculator.class);
        startActivity(intent);

    }

    public void exportData(View view){

        Intent intent = new Intent(this,
                RoutineSelectionExportData.class);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}