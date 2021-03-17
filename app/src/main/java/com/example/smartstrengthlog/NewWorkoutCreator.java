package com.example.smartstrengthlog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class NewWorkoutCreator extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_workout_creator);
    }

    //Boton para crear el workout





    public void createWorkoutButton(View view){

        startActivity(new Intent(this, MainMenu.class));
        finish();
        Toast.makeText(this, "Workout created!", Toast.LENGTH_LONG).show();

    }


}

