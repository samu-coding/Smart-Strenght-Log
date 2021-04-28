package com.example.smartstrengthlog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import models.Workout;
import util.SmartStrengthLogAPI;

public class NewWorkoutCreator extends AppCompatActivity {

    private EditText titleEditText;
    private EditText descriptionEditText;

    private EditText exerciseEditText0;
    private EditText numberOfSetsEditText0;
    private EditText notesExerciseEditText0;
    private EditText exerciseEditText1;
    private EditText numberOfSetsEditText1;
    private EditText notesExerciseEditText1;
    private EditText exerciseEditText2;
    private EditText numberOfSetsEditText2;
    private EditText notesExerciseEditText2;

    private TextView currentUserTextView;

    private String currentUserId;
    private String currentUsername;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    //Connection to Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Workout");
    //private CollectionReference collectionReference = db.collection("Users").document(this.currentUsername).collection("Workout");

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_workout_creator);
        
        //collectionReference
        firebaseAuth = FirebaseAuth.getInstance();
        titleEditText = findViewById(R.id.title_workout);
        descriptionEditText = findViewById(R.id.description_workout);

        //Ejercicios
        exerciseEditText0 = findViewById(R.id.exercise_workout0);
        numberOfSetsEditText0 = findViewById(R.id.number_sets_workout0);
        notesExerciseEditText0 = findViewById(R.id.description_exercise_workout0);
        exerciseEditText1 = findViewById(R.id.exercise_workout1);
        numberOfSetsEditText1 = findViewById(R.id.number_sets_workout1);
        notesExerciseEditText1 = findViewById(R.id.description_exercise_workout1);
        exerciseEditText2 = findViewById(R.id.exercise_workout2);
        numberOfSetsEditText2 = findViewById(R.id.number_sets_workout2);
        notesExerciseEditText2 = findViewById(R.id.description_exercise_workout2);

        Log.d("Usuario","TESTING LOG D");

        if (SmartStrengthLogAPI.getInstance() != null){
            currentUserId = SmartStrengthLogAPI.getInstance().getUserId();
            currentUsername = SmartStrengthLogAPI.getInstance().getUsername();
            Log.d("Usuario","User: " +currentUserId);

        }

        authStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null){

                }else{

                }
            }
        };


    }

    @Override
    protected void onStart() {
        super.onStart();
        user = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuth !=null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    //Boton para crear el workout

    

    public void createWorkoutButton(View view){

        //Save the Workout
        saveWokout();

    }

    private void saveWokout() {

        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        String exercise0 = exerciseEditText0.getText().toString().trim();
        String descriptionExercise0 = notesExerciseEditText0.getText().toString().trim();
        String numberOfSets0_str = numberOfSetsEditText0.getText().toString();
        String exercise1 = exerciseEditText1.getText().toString().trim();
        String descriptionExercise1 = notesExerciseEditText1.getText().toString().trim();
        String numberOfSets1_str = numberOfSetsEditText1.getText().toString();
        String exercise2 = exerciseEditText2.getText().toString().trim();
        String descriptionExercise2 = notesExerciseEditText2.getText().toString().trim();
        String numberOfSets2_str = numberOfSetsEditText2.getText().toString();

        //Creamos las Listas de Ejercicios y sus sets
        List<String> exercises = new ArrayList<String>();
        List<String> notesExercises = new ArrayList<String>();
        List<Integer> sets = new ArrayList<Integer>();




        if (!title.isEmpty() && !description.isEmpty() && !exercise0.isEmpty() && !numberOfSets0_str.isEmpty() && !exercise1.isEmpty() && !numberOfSets1_str.isEmpty() && !exercise2.isEmpty() && !numberOfSets2_str.isEmpty()){
            //Guardamos la info del workout en FireStore

            //Añadimos ejercicios y sets
            exercises.add(exercise0);
            exercises.add(exercise1);
            exercises.add(exercise2);
            notesExercises.add(descriptionExercise0);
            notesExercises.add(descriptionExercise1);
            notesExercises.add(descriptionExercise2);

            //Una vez sabemos que no está vacío el campo, lo convertimos a entero.
            int numberOfSets0 = Integer.parseInt(numberOfSets0_str);
            int numberOfSets1 = Integer.parseInt(numberOfSets1_str);
            int numberOfSets2 = Integer.parseInt(numberOfSets2_str);

            sets.add(numberOfSets0);
            sets.add(numberOfSets1);
            sets.add(numberOfSets2);


            //Create a Workout Object
            Workout workout = new Workout();
            workout.setId(String.valueOf(Timestamp.now().getSeconds()));
            workout.setUser(SmartStrengthLogAPI.getInstance().getUserId());
            workout.setTitle(title);
            workout.setDescription(description);
            workout.setExercises(exercises);
            workout.setSets(sets);
            workout.setNotes(notesExercises);

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
            Toast.makeText(this, "Fill al the information first.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        //Cambio de vista
        Intent intent = new Intent(this,
                MainMenu.class);
        SmartStrengthLogAPI smartStrengthLogAPI = new SmartStrengthLogAPI();
        intent.putExtra("username", smartStrengthLogAPI.getUsername());
        intent.putExtra("userId", smartStrengthLogAPI.getUserId());
        startActivity(intent);
    }


}

