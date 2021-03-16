package com.example.smartstrengthlog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.smartstrengthlog.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Arrays;
import java.util.Collections;

import models.Workout;

public class MainMenu extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    FirebaseAuth myAuth = FirebaseAuth.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        writeDatabase();

    }

    private void writeDatabase() {

        Workout workout = new Workout();

        //Obtenemos el usuario
        workout.title = "Torso 1";
        workout.description ="Primer dia";
        workout.user = myAuth.getUid().toString();
        workout.id = "1234";

        workout.exercises = Arrays.asList("1", "2");

        //Path es la manera de identificar las características
        databaseReference = database.getReference(workout.user);
        databaseReference.setValue(workout);

    }

    public void logOut (View view){

        startActivity(new Intent(this, LoginActivity.class));
        finish();

    }

    public void createWorkout (View view){

        //Al ser un fragment, tenemos que referenciar el contexto, por eso es diferente.
        Log.i("ERROR","Boton clicado");
        
        Intent myIntent = new Intent(this, NewWorkoutCreator.class);
        startActivity(myIntent);
        finish();

    }

}