package com.example.smartstrengthlog.ui.ProgressTracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.smartstrengthlog.MainMenu;
import com.example.smartstrengthlog.R;
import com.example.smartstrengthlog.WorkoutSessionLog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import util.SmartStrengthLogAPI;

public class ExerciseSelectionProgress extends AppCompatActivity {

    private Button name_exercise1;
    private Button name_exercise2;
    private Button name_exercise3;

    private String workoutId;
    private String documentID;

    //Connection to Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_selection_progress);

        //Bind
        name_exercise1 = findViewById(R.id.ejercicio1_button);
        name_exercise2 = findViewById(R.id.ejercicio2_button);
        name_exercise3 = findViewById(R.id.ejercicio3_button);

        workoutId = (String) getIntent().getSerializableExtra("workoutId");

        //Buscamos el documento (workout) que tiene el id recibido.
        db.collection("Workout")
                .whereEqualTo("id", workoutId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                //Guardamos los nombres de los ejercicios
                                documentID = document.getId();

                                ArrayList<String> Ejercicios = (ArrayList<String>) document.get("exercises");
                                Log.d("EJERCICIOS", "Lista de ejericios:" +Ejercicios);
                                name_exercise1.setText(Ejercicios.get(0));
                                name_exercise2.setText(Ejercicios.get(1));
                                name_exercise3.setText(Ejercicios.get(2));

                            }
                        } else {
                            Log.d("DOCU", "Error getting documents: ", task.getException());
                        }
                    }
                });

        //Clickamos en el primer botón
        name_exercise1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Cambio de vista, recargamos para el siguiente ejercicio
                Intent intent = new Intent(ExerciseSelectionProgress.this,
                        PerformanceStats.class);
                intent.putExtra("workoutId", workoutId);
                intent.putExtra("documentID", documentID);
                intent.putExtra("exercise", name_exercise1.getText());
                intent.putExtra("num_ejercicio", "Ejercicio 1");
                startActivity(intent);

            }
        });

        //Clickamos en el segundo botón
        name_exercise2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Cambio de vista, recargamos para el siguiente ejercicio
                Intent intent = new Intent(ExerciseSelectionProgress.this,
                        PerformanceStats.class);
                intent.putExtra("workoutId", workoutId);
                intent.putExtra("documentID", documentID);
                intent.putExtra("exercise", name_exercise2.getText());
                intent.putExtra("num_ejercicio", "Ejercicio 2");
                startActivity(intent);

            }
        });

        //Clicaamos en el tercer botón
        name_exercise3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Cambio de vista, recargamos para el siguiente ejercicio
                Intent intent = new Intent(ExerciseSelectionProgress.this,
                        PerformanceStats.class);
                intent.putExtra("workoutId", workoutId);
                intent.putExtra("documentID", documentID);
                intent.putExtra("exercise", name_exercise3.getText());
                intent.putExtra("num_ejercicio", "Ejercicio 3");
                startActivity(intent);

            }
        });

    }
    @Override
    public void onBackPressed() {
        //Cambio de vista
        Intent intent = new Intent(this,
                MainMenu.class);
        SmartStrengthLogAPI smartStrengthLogAPI = new SmartStrengthLogAPI();
        intent.putExtra("username", smartStrengthLogAPI.getUsername());
        intent.putExtra("userId", smartStrengthLogAPI.getUserId());
        intent.putExtra("fragmentToLoad", "Performance");
        startActivity(intent);
    }
}