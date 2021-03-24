package com.example.smartstrengthlog;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
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
import util.WorkoutSessionAPI;

public class WorkoutSessionLog extends AppCompatActivity {

    private String currentUserId;
    private String currentUsername;

    private String workoutId;

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

    //ID del documentod el workout
    private String documentID;




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

        WorkoutSessionAPI workoutSessionAPI = WorkoutSessionAPI.getInstance();
        int numero_ej = workoutSessionAPI.getExerciseNumber();




        //We get the id of the workout

        //Bundle workoutId= getIntent().getExtras();
        //String id = workoutId.getString("workoutId");
        workoutId = (String) getIntent().getSerializableExtra("workoutId");
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

                                documentID = document.getId();
                                Log.d("DOCU", document.getId() + " => " + document.getData());
                                ArrayList<String> Ejercicios = (ArrayList<String>) document.get("exercises");
                                Log.d("DOCU","NOMBRE DEL EJERCICIOS ARRAY: "+Ejercicios);
                                name_ejercicio = Ejercicios.get(numero_ej);
                                Log.d("DOCU","NOMBRE DEL EJERCICIO: "+name_ejercicio);
                                name_exercise.setText(name_ejercicio);


                            }
                        } else {
                            Log.d("DOCU", "Error getting documents: ", task.getException());
                        }
                    }
                });






    }





    @RequiresApi(api = Build.VERSION_CODES.O)
    public void guardarSetsEjercicio(View view){

        WorkoutSessionAPI workoutSessionAPI = WorkoutSessionAPI.getInstance();

        Map<String, Object> set1 = new HashMap<>();
        set1.put("Exercise", name_ejercicio);
        set1.put("Set",1);
        set1.put("Reps", reps_set1.getText().toString());
        set1.put("Weight", weight_set1.getText().toString());
        set1.put("RIR", rir_set1.getText().toString());
        //set1.put("RPE", false);
        set1.put("Date", java.time.LocalDate.now().toString());

        Map<String, Object> set2 = new HashMap<>();
        set2.put("Exercise", name_ejercicio);
        set2.put("Set",2);
        set2.put("Reps", reps_set2.getText().toString());
        set2.put("Weight", weight_set2.getText().toString());
        set2.put("RIR", rir_set2.getText().toString());
        //set1.put("RPE", false);
        set2.put("Date", java.time.LocalDate.now().toString());

        Map<String, Object> set3 = new HashMap<>();
        set3.put("Exercise", name_ejercicio);
        set3.put("Set",3);
        set3.put("Reps", reps_set3.getText().toString());
        set3.put("Weight", weight_set3.getText().toString());
        set3.put("RIR", rir_set3.getText().toString());
        //set1.put("RPE", false);
        set3.put("Date", java.time.LocalDate.now().toString());

        Log.d("MAPA", "info: "+set1);

        int numero_ej = workoutSessionAPI.getExerciseNumber();

        //Dependiendo del ejericcio que sea, guardamos en el API como un ejercicio u otro
        switch(numero_ej)
        {
            case 0 :
                workoutSessionAPI.setSet1_E1(set1);
                workoutSessionAPI.setSet2_E1(set2);
                workoutSessionAPI.setSet3_E1(set3);
                break;

            case 1 :
                workoutSessionAPI.setSet1_E2(set1);
                workoutSessionAPI.setSet2_E2(set2);
                workoutSessionAPI.setSet3_E2(set3);
                break; // break es opcional

            case 2 :
                workoutSessionAPI.setSet1_E3(set1);
                workoutSessionAPI.setSet2_E3(set2);
                workoutSessionAPI.setSet3_E3(set3);

                break;


        }

        Log.d("NUM_EJERICIO", "valor numero_ej:  "+ numero_ej);
        workoutSessionAPI.setExerciseNumber(numero_ej+1);

        if (numero_ej >=2){

            saveSession(workoutSessionAPI);

        }

        else {

            //Cambio de vista
            Intent intent = new Intent(this,
                    WorkoutSessionLog.class);

            intent.putExtra("workoutId", workoutId);
            //Log.d("Clicked", "QUEREMOS PASAR el id:  "+ workout.getId());
            startActivity(intent);
        }


    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveSession(WorkoutSessionAPI workoutSessionAPI) {

       //CollectionReference collectionReference = db.collection("Workout").document(documentID).collection("History");

        //DocumentReference documentReference = db.whereEqualTo("id", workoutId).collection("History");

        saveSet(workoutSessionAPI.getSet1_E1(),"Ejercicio 1");
        saveSet(workoutSessionAPI.getSet2_E1(),"Ejercicio 1");
        saveSet(workoutSessionAPI.getSet3_E1(),"Ejercicio 1");

        saveSet(workoutSessionAPI.getSet1_E2(),"Ejercicio 2");
        saveSet(workoutSessionAPI.getSet2_E2(),"Ejercicio 2");
        saveSet(workoutSessionAPI.getSet3_E2(),"Ejercicio 2");

        saveSet(workoutSessionAPI.getSet1_E3(),"Ejercicio 3");
        saveSet(workoutSessionAPI.getSet2_E3(),"Ejercicio 3");
        saveSet(workoutSessionAPI.getSet3_E3(),"Ejercicio 3");

       // Map<String, Object> Entreno = new HashMap<>();
        //Entreno.put("Ejercicio1",workoutSessionAPI.getSet1_E1());
        //Log.d("Entreno", "info: "+Entreno);




        //Establecemos fecha al documento del entreno
        DocumentReference doc = db.collection("Workout").document(documentID).collection("History").document(java.time.LocalDate.now().toString());

        doc.update("date", java.time.LocalDate.now().toString())
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("UPDDATE", "ACTUALIZACIÓN EXISTOSA");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("UPDDATE", "ACTUALIZACIÓN FRACASADA");
            }
        });

        //Añadimos la fecha al documento del History:
        Map<String, Object> today = new HashMap<>();
        today.put("date", java.time.LocalDate.now().toString());
        db.collection("Workout").document(documentID).collection("History").document(java.time.LocalDate.now().toString())
                .set(today)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("UPDATE", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("UPDATE", "Error writing document", e);
                    }
                });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveSet(Map set, String numero_ejercicio){

        //Crear fecha del documento

        CollectionReference collectionReference = db.collection("Workout").document(documentID).collection("History").document(java.time.LocalDate.now().toString()).collection(numero_ejercicio);
        //Test
        collectionReference.add(set)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Toast.makeText(WorkoutSessionLog.this, "Saving Workout Session...", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(WorkoutSessionLog.this, MainMenu.class));
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d("CreateWokout","Error");

                    }
                });

    }

}