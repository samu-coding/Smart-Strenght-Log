package com.example.smartstrengthlog.ui.ProgressTracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.smartstrengthlog.MainMenu;
import com.example.smartstrengthlog.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import util.SmartStrengthLogAPI;

public class ExportData extends AppCompatActivity {

    //Connection to Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String name_exercise;
    private String workoutId;
    private String documentID;
    private int set= 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_data);

        //Obtenemos los valores que vienen de la vista anterior
        workoutId = (String) getIntent().getSerializableExtra("workoutID");
        //name_exercise = (String) getIntent().getSerializableExtra("exercise");
        documentID = (String) getIntent().getSerializableExtra("documentID");
        //buttonExport();
    }

    public void buttonExport(View view){
        StringBuilder data = new StringBuilder();
        data.append(" ,Set,Repetitions,Weight,Reps In Reserve"); //Title


        //Obtención del documento
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
                                Log.d("DOCID",""+documentID);
                                exportar(data, "Ejercicio 1", 1, documentID);
                            }
                        } else {
                            Log.d("DOCU", "Error getting documents: ", task.getException());
                        }
                    }
                });
        ;
    }


    public void exportar (StringBuilder data, String Ejercicio, int set, String documentID){

        //Buscamos el documento con fecha más reciente
        Query query = db.collection("Workout").document(documentID).collection("History")
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(1);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("SEARCH", document.getId() + " => " + document.getData());
                        String auxID = document.getId();

                        //Subquery para los valores del primer set
                        Query subQuery1 = db.collection("Workout").document(documentID).collection("History")
                                .document(auxID).collection(Ejercicio).whereEqualTo("Set",set);

                        subQuery1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {
                                    if (task.isComplete()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {


                                            String repes = document.get("Reps").toString();
                                            String peso = document.get("Weight").toString();
                                            String rir = document.get("RIR").toString();
                                            String name_ejercicio = document.get("Exercise").toString();

                                            Log.d("INFO SET", "Ejercicio: " + Ejercicio + ", Set: " + set);
                                            data.append("\n" + name_ejercicio + "," + "SET "+set + "," + repes + "," + peso + "," + rir);

                                            if (set ==1 ){
                                                exportar(data, Ejercicio,2, documentID);

                                            }
                                            else if (set == 2){
                                                exportar(data, Ejercicio,3, documentID);
                                            }
                                            else{

                                                switch (Ejercicio){

                                                    case "Ejercicio 1": exportar(data,"Ejercicio 2", 1, documentID);
                                                        break;

                                                    case "Ejercicio 2": exportar(data,"Ejercicio 3", 1, documentID);
                                                        break;

                                                    case "Ejercicio 3": Finalexport(data);
                                                        break;

                                                }

                                            }


                                        }
                                    } else {
                                        Log.d("SEARCH", "Error getting documents: ", task.getException());
                                    }
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


    public void Finalexport (StringBuilder data){

        try {
            //save file
            FileOutputStream out = openFileOutput("WorkoutData.csv", Context.MODE_PRIVATE);
            out.write(data.toString().getBytes());
            out.close();

            //export
            Context context = getApplicationContext();
            File filelocation = new File (getFilesDir(), "WorkoutData.csv");
            Uri path = FileProvider.getUriForFile(context, "com.example.smartstrenghtlog.fileprovider", filelocation);
            Intent fileIntent = new Intent (Intent.ACTION_SEND);
            fileIntent.setType("text/csv");
            fileIntent.putExtra(fileIntent.EXTRA_SUBJECT, "Data");
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
            startActivity(Intent.createChooser(fileIntent,"Send mail"));


        }catch (Exception e){
            e.printStackTrace();
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