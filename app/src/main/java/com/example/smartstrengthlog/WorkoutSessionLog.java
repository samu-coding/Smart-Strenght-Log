package com.example.smartstrengthlog;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Workout;
import util.SmartStrengthLogAPI;

public class WorkoutSessionLog extends AppCompatActivity {

    private String currentUserId;
    private String currentUsername;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    //
    private String documentoWorkout;

    //Connection to Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    //private CollectionReference collectionReference = db.collection("Users").document(this.currentUsername).collection("Workout");

    private TextView name_exercise;

    private EditText reps_set1;
    private EditText reps_set2;
    private EditText reps_set3;

    private EditText weight_set1;
    private EditText weight_set2;
    private EditText weight_set3;

    private EditText rir_set1;
    private EditText rir_set2;
    private EditText rir_set3;

    //Datos dobtenidos del documento
    private String name_ejercicio;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_session_log);

        //Name Exercise
        name_exercise = findViewById(R.id.exercise_name);

        //Reps
        reps_set1 = findViewById(R.id.reps_set_1);
        reps_set2 = findViewById(R.id.reps_set_2);
        reps_set3 = findViewById(R.id.reps_set_3);

        //weight
        weight_set1 = findViewById(R.id.weight_set_1);
        weight_set2 = findViewById(R.id.weight_set_2);
        weight_set3 = findViewById(R.id.weight_set_3);

        //RIR
        rir_set1 = findViewById(R.id.rir_1);
        rir_set2 = findViewById(R.id.rir_2);
        rir_set3 = findViewById(R.id.rir_3);



        //We get the id of the workout

        //Bundle workoutId= getIntent().getExtras();
        //String id = workoutId.getString("workoutId");
        String workoutId = (String) getIntent().getSerializableExtra("workoutId");
        Log.d("DOCU","ID DEL WK: "+workoutId);

        //Buscamos el documento (workout) que tiene el id recibido.
        db.collection("Workout")
                .whereEqualTo("id", workoutId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("DOCU", document.getId() + " => " + document.getData());
                                ArrayList<String> Ejercicios = (ArrayList<String>) document.get("exercises");
                                Log.d("DOCU","NOMBRE DEL EJERCICIOS ARRAY: "+Ejercicios);
                                name_ejercicio = Ejercicios.get(0);
                                Log.d("DOCU","NOMBRE DEL EJERCICIO: "+name_ejercicio);
                                name_exercise.setText(name_ejercicio);


                            }
                        } else {
                            Log.d("DOCU", "Error getting documents: ", task.getException());
                        }
                    }
                });






    }


    public void aniadirHistory(){

    }


   @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveSession() {
       CollectionReference collectionReference = db.collection("Workout").document(documentoWorkout).collection("History");




       /*
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String exercise0 = exerciseEditText0.getText().toString().trim();
        int numberOfSets0 = Integer.parseInt(numberOfSetsEditText0.getText().toString());
        String exercise1 = exerciseEditText1.getText().toString().trim();
        int numberOfSets1 = Integer.parseInt(numberOfSetsEditText1.getText().toString());
        String exercise2 = exerciseEditText2.getText().toString().trim();
        int numberOfSets2 = Integer.parseInt(numberOfSetsEditText2.getText().toString());

        //Creamos las Listas de Ejercicios y sus sets
        List<String> exercises = new ArrayList<String>();
        List<Integer> sets = new ArrayList<Integer>();

        //Añadimos ejercicios y sets
        exercises.add(exercise0);
        exercises.add(exercise1);
        exercises.add(exercise2);
        sets.add(numberOfSets0);
        sets.add(numberOfSets1);
        sets.add(numberOfSets2);*/



       /*
        Map<String, Object> set1 = new HashMap<>();
        set1.put("Exercise", name_ejercicio);
        set1.put("Reps", reps_set1);
        set1.put("Weight", weight_set1);
        set1.put("RIR", rir_set1);
        //set1.put("RPE", false);
        set1.put("Date", java.time.LocalDate.now());
        collectionReference.document("SF").set(set1);*/

        /*
        if (!TextUtils.isEmpty(title)){
            //Guardamos la info del workout en FireStore
            //.child("Workout" + Timestamp.now().getSeconds());)

            //Create a Workout Object
            Workout workout = new Workout();
            workout.setId(String.valueOf(Timestamp.now().getSeconds()));
            workout.setUser(SmartStrengthLogAPI.getInstance().getUserId());
            workout.setTitle(title);
            workout.setDescription(description);
            workout.setExercises(exercises);
            workout.setSets(sets);


            //Invoke our CollectionReference
            collectionReference.add(workout)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            startActivity(new Intent(NewWorkoutCreator.this, MainMenu.class));
                            finish();
                            Toast.makeText(NewWorkoutCreator.this, "Workout created!", Toast.LENGTH_LONG).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Log.d("CreateWokout","Error");

                        }
                    });


            //Save a Workout instance


        }else{
            Toast.makeText(this, "A tittle is required!", Toast.LENGTH_SHORT).show();
        }
*/
    }

}