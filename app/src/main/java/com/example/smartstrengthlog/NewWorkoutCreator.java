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

import org.w3c.dom.Text;

import models.Workout;
import util.SmartStrengthLogAPI;

public class NewWorkoutCreator extends AppCompatActivity {

    private EditText titleEditText;
    private EditText descriptionEditText;
    private EditText exerciseEditText;
    private EditText numberOfSetsEditText;

    private TextView currentUserTextView;

    private String currentUserId;
    private String currentUsername;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    //Connection to Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Bundle extras = getIntent().getExtras();


    //FirebaseUser user = firebaseAuth.getCurrentUser();
    //String currentUserId = user.getUid();

    //Obtenemos el Id del usuario
    //updateUI(user);

    //Firestore creation
    //assert user != null;

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
        exerciseEditText = findViewById(R.id.exercise_workout);
        numberOfSetsEditText = findViewById(R.id.number_sets_workout);

        Log.d("Usuario","TESTING LOG D");

        if (SmartStrengthLogAPI.getInstance() != null){
            currentUserId = SmartStrengthLogAPI.getInstance().getUserId();
            currentUsername = SmartStrengthLogAPI.getInstance().getUsername();
            Log.d("Usuario","User: " +currentUserId);


            //_____________!!!!!!!!!!!!!!!
            //Necesario??? ->No funciona
            //currentUserTextView.setText(currentUsername);
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
        String exercise = exerciseEditText.getText().toString().trim();
        int numberOfSets = Integer.parseInt(numberOfSetsEditText.getText().toString().trim());

        if (!TextUtils.isEmpty(title)){
            //Guardamos la info del workout en FireStore
            //.child("Workout" + Timestamp.now().getSeconds());)

            //Create a Workout Object
            Workout workout = new Workout();
            workout.setTitle(title);
            workout.setDescription(description);
            workout.setExercises(exercise);
            workout.setNumberOfSets(numberOfSets);
            workout.setId(String.valueOf(Timestamp.now().getSeconds()));
            workout.setUser(SmartStrengthLogAPI.getInstance().getUserId());

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

    }


}

