package com.example.smartstrengthlog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartstrengthlog.ui.Events.EventCalendar;
import com.example.smartstrengthlog.ui.ProgressTracking.RmCalculator;
import com.example.smartstrengthlog.ui.ProgressTracking.RoutineSelectionExportData;
import com.example.smartstrengthlog.ui.ProgressTracking.RoutineSelectionProgress;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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

        String fragmentRequired = getIntent().getExtras().getString("fragmentToLoad");


        //Cambio al fragment deseado
        if (fragmentRequired != null){

            switch (fragmentRequired){

                case "Performance":
                    navController.navigate(R.id.navigation_performance);
                    break;
                case "Workout":
                    navController.navigate(R.id.navigation_workout);
                    break;

            }


        }

        workoutList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
      }

    public void logOut (View view){

        startActivity(new Intent(this, LoginActivity.class));
        finish();

    }

    //Al clicar el botón de nuevo WK, vamos a esta pantalla
    public void createWorkout (View view){

        //Al ser un fragment, tenemos que referenciar el contexto, por eso es diferente.
        Intent myIntent = new Intent(this, NewWorkoutCreator.class);
        startActivity(myIntent);
        finish();
    }

    public void deleteWorkout (View view){

        //Al ser un fragment, tenemos que referenciar el contexto, por eso es diferente.
        Intent myIntent = new Intent(this, DeleteWorkout.class);
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

    public void gotoEvents(View view){

        Intent intent = new Intent(this,
                EventCalendar.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {

    }

    public void aboutInfo(View view){
        //Dialogo
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("About Smart Stregth Log");
        alertDialog.setMessage("Smart Stegth Log v1.0\nLast update: 25/04/2021\n\n Developed by: Samuel Soria\n Contact: samuelsoria.pinar@gmail.com ");
        //alertDialog.setIcon(int )
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }


}