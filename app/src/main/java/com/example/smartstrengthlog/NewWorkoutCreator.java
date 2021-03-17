package com.example.smartstrengthlog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class NewWorkoutCreator extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_workout_creator);
    }

    //Boton para crear el workout
    public void createWorkout(){

        Log.i("Create", "Workout");

    }

}

