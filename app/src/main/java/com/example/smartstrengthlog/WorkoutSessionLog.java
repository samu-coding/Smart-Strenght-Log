package com.example.smartstrengthlog;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartstrengthlog.ui.Dialogs.InfoRmDialog;
import com.example.smartstrengthlog.ui.Dialogs.InfoWKSessionRIR;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Workout;
import util.SmartStrengthLogAPI;
import util.WorkoutSessionAPI;

public class WorkoutSessionLog extends AppCompatActivity {

    private String workoutId;

    //Connection to Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private TextView name_exercise;
    private TextView exerciseNotes;

    private EditText reps_set1;
    private EditText reps_set2;
    private EditText reps_set3;

    private EditText weight_set1;
    private EditText weight_set2;
    private EditText weight_set3;

    private EditText rir_set1;
    private EditText rir_set2;
    private EditText rir_set3;

    private Button botonSiguienteEjercicio;

    //Datos dobtenidos del documento
    private String name_ejercicio;
    private String notesThisExercise;

    //ID del documentod el workout
    private String documentID;


    //Marcas del entrenamiento anterior
    private TextView prev_reps_set1;
    private TextView prev_reps_set2;
    private TextView prev_reps_set3;

    private TextView prev_weight_set1;
    private TextView prev_weight_set2;
    private TextView prev_weight_set3;

    private TextView prev_rir_set1;
    private TextView prev_rir_set2;
    private TextView prev_rir_set3;

    //Info
    private View RIRInfo;

    //Usado para las queries del entrenamiento anterior
    private String ejercicio = "Ejercicio 1";



    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_session_log);

        //Name Exercise
        name_exercise = findViewById(R.id.exercise_name);
        exerciseNotes = findViewById(R.id.exercise_notes);

        botonSiguienteEjercicio = findViewById(R.id.button_next_exercise);

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

        //Marcas anteriores
        //Reps anteriores
        prev_reps_set1 = findViewById(R.id.prev_reps_set1);
        prev_reps_set2 = findViewById(R.id.prev_reps_set2);
        prev_reps_set3 = findViewById(R.id.prev_reps_set3);

        //Weight anteriores
        prev_weight_set1 = findViewById(R.id.prev_weight_set_1);
        prev_weight_set2 = findViewById(R.id.prev_weight_set_2);
        prev_weight_set3 = findViewById(R.id.prev_weight_set_3);

        //RIR anteriores
        prev_rir_set1 = findViewById(R.id.prev_rir_1);
        prev_rir_set2 = findViewById(R.id.prev_rir_2);
        prev_rir_set3 = findViewById(R.id.prev_rir_3);

        RIRInfo = findViewById(R.id.RIR_info);
        RIRInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogRIR();
            }
        });

        //Invocamos nuestra API donde guardamos temporalmente la información del entreno
        WorkoutSessionAPI workoutSessionAPI = WorkoutSessionAPI.getInstance();
        int numero_ej = workoutSessionAPI.getExerciseNumber();


        //Obtenemos el ID del workout obtenido en la Vista anterior
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
                                Log.d("DOCU", documentID + " => " + document.getData());
                                ArrayList<String> Ejercicios = (ArrayList<String>) document.get("exercises");
                                name_ejercicio = Ejercicios.get(numero_ej);
                                name_exercise.setText(name_ejercicio); //Mostramos el correspontiente


                                //Guardamos las notas de los ejericios
                                ArrayList<String> notes = (ArrayList<String>) document.get("notes");

                                if (notes != null){
                                    notesThisExercise =  notes.get(numero_ej);
                                    exerciseNotes.setText(notesThisExercise); //Mostramos la correspontiente
                                }



                                inforPrevSessio();


                            }
                        } else {
                            Log.d("DOCU", "Error getting documents: ", task.getException());
                        }
                    }
                });

        if(numero_ej == 2) {
            botonSiguienteEjercicio.setText("SAVE SESSION");
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void guardarSetsEjercicio(View view){

        //Guardamos la información en la API

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
                break;

            case 2 :
                workoutSessionAPI.setSet1_E3(set1);
                workoutSessionAPI.setSet2_E3(set2);
                workoutSessionAPI.setSet3_E3(set3);

                break;


        }

        //Incrementamos el numero del ejercicio
        workoutSessionAPI.setExerciseNumber(numero_ej+1);

        //Si ya se han hecho todos, guardamos en Firestore
        if (numero_ej >=2){

            saveSession(workoutSessionAPI);

        }

        else {

            //Cambio de vista, recargamos para el siguiente ejercicio
            Intent intent = new Intent(this,
                    WorkoutSessionLog.class);
            intent.putExtra("workoutId", workoutId);
            startActivity(intent);
        }


    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveSession(WorkoutSessionAPI workoutSessionAPI) {

        //Guardamos todos los sets
        saveSet(workoutSessionAPI.getSet1_E1(),"Ejercicio 1");
        saveSet(workoutSessionAPI.getSet2_E1(),"Ejercicio 1");
        saveSet(workoutSessionAPI.getSet3_E1(),"Ejercicio 1");

        saveSet(workoutSessionAPI.getSet1_E2(),"Ejercicio 2");
        saveSet(workoutSessionAPI.getSet2_E2(),"Ejercicio 2");
        saveSet(workoutSessionAPI.getSet3_E2(),"Ejercicio 2");

        saveSet(workoutSessionAPI.getSet1_E3(),"Ejercicio 3");
        saveSet(workoutSessionAPI.getSet2_E3(),"Ejercicio 3");
        saveSet(workoutSessionAPI.getSet3_E3(),"Ejercicio 3");


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

        // Guardar la fecha del ultimo WK en el documento de entrenamientos
        DocumentReference subQueryDate = db.collection("Workout").document(documentID);
        subQueryDate
                .update("LastWorkout", LocalDate.now().toString())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("FECHA LW", "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FECHA LW", "Error updating document", e);
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

    //Funcion usada para buscar la info del levantamiento de la sesión anterior y aplicarlo en los TextViews
    public void inforPrevSessio(){

        //Obtenemos el numero del ejercicio y nombramos así la variable que hará referencia al id del documento
        WorkoutSessionAPI workoutSessionAPI =WorkoutSessionAPI.getInstance();
        int numero_ej = workoutSessionAPI.getExerciseNumber();
        switch(numero_ej)
        {
            case 0 :
                ejercicio ="Ejercicio 1";
                break;

            case 1 :
                ejercicio ="Ejercicio 2";
                break; // break es opcional

            case 2 :
                ejercicio ="Ejercicio 3";
                break;


        }

        //Buscamos el documento con fecha más reciente
        Query query = db.collection("Workout").document(documentID).collection("History")
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(1);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("SEARCH", document.getId() + " => " + document.getData());
                        String auxID = document.getId();

                        //Subquery para los valores del primer set
                        Query subQuery1 = db.collection("Workout").document(documentID).collection("History")
                                .document(auxID).collection(ejercicio).whereEqualTo("Set",1);

                        subQuery1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        prev_reps_set1.setText(document.get("Reps").toString());
                                        prev_weight_set1.setText(document.get("Weight").toString());
                                        prev_rir_set1.setText(document.get("RIR").toString());

                                    }
                                } else {
                                    Log.d("SEARCH", "Error getting documents: ", task.getException());
                                }


                            }
                        });

                        //Subquery para los valores del segundo set
                        Query subQuery2 = db.collection("Workout").document(documentID).collection("History")
                                .document(auxID).collection(ejercicio).whereEqualTo("Set",2);

                        subQuery2.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        prev_reps_set2.setText(document.get("Reps").toString());
                                        prev_weight_set2.setText(document.get("Weight").toString());
                                        prev_rir_set2.setText(document.get("RIR").toString());

                                    }
                                } else {
                                    Log.d("SEARCH", "Error getting documents: ", task.getException());
                                }


                            }
                        });

                        //Subquery para los valores del tercer set
                        Query subQuery3 = db.collection("Workout").document(documentID).collection("History")
                                .document(auxID).collection(ejercicio).whereEqualTo("Set",3);

                        subQuery3.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d("SEARCH-2", "SET 1 INFO: " + document.getData());
                                        prev_reps_set3.setText(document.get("Reps").toString());
                                        prev_weight_set3.setText(document.get("Weight").toString());
                                        prev_rir_set3.setText(document.get("RIR").toString());

                                    }
                                } else {
                                    Log.d("SEARCH", "Error getting documents: ", task.getException());
                                }


                            }
                        });




                    }
                } else {
                    Log.d("SEARCH", "Error getting documents: ", task.getException());
                }


            }
        });



    }

    public void openDialogRIR(){
        InfoWKSessionRIR infoWKSessionRIR = new InfoWKSessionRIR();
        infoWKSessionRIR.show(getSupportFragmentManager(),"Dialog");

    }

}