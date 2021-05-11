package com.example.smartstrengthlog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartstrengthlog.ui.ProgressTracking.ExportData;
import com.example.smartstrengthlog.ui.ProgressTracking.RoutineSelectionExportData;
import com.example.smartstrengthlog.ui.dashboard.DashboardViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import models.Workout;
import ui.WorkoutRecyclerAdapter;
import util.SmartStrengthLogAPI;
import util.WorkoutSessionAPI;

public class DeleteWorkout extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<Workout> workoutList;
    private RecyclerView recyclerView;
    private WorkoutRecyclerAdapter workoutRecyclerAdapter;
    private CollectionReference collectionReference = db.collection("Workout");
        private String userId;
    private DashboardViewModel dashboardViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_workout);

        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        final TextView textView = findViewById(R.id.text_dashboard);


        workoutList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Para poder obtener el contexto del Fragment, usamos get Activity.
        mostrarWorkouts(this);

        dashboardViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
    }

    public void onStart() {
        super.onStart();
        userId = SmartStrengthLogAPI.getInstance().getUserId();
        Log.d("USUARIO", "BUSQUEDA DOCUMENTO DE USUARIO :" + userId);
        mostrarWorkouts(this);

    }

    public void onWorkoutClick1(int position) {
        //Log.d("Clicked", "onWorkoutClick: " + position);
        WorkoutSessionAPI workoutSessionAPI = WorkoutSessionAPI.getInstance();
        workoutSessionAPI.setExerciseNumber(0);

        Workout workout = workoutList.get(position);
        String workoutId = workout.getId();

         //Dialogo
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Confirm Delete:");
        alertDialog.setMessage("Are you sure you want to delete this workout routine and ALL it´s sessions information?");
        //alertDialog.setIcon(int )
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(getApplicationContext(),"Deleting workout...", Toast.LENGTH_SHORT).show();

                deleteWorkout(workoutId);

            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();

    }

    private void deleteWorkout(String workoutId) {

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
                                String documentID = document.getId();


                                //Borramos el documento obtenido
                                db.collection("Workout")
                                        .document(documentID)
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("DELETE", "DocumentSnapshot successfully deleted!");
                                                onBackPressed();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("DELETE", "Error deleting document", e);
                                                onBackPressed();
                                            }
                                        });
                            }
                        } else {
                            Log.d("DOCU", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    public void mostrarWorkouts(Context HomeFragmentContext) {
        collectionReference.whereEqualTo("user", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Workout workout = document.toObject(Workout.class);
                        workoutList.add(workout);
                        Log.d("DOCU", document.getId() + "-->" + document.getData());

                    }

                    //Invoke Recycler view
                    workoutRecyclerAdapter = new WorkoutRecyclerAdapter(HomeFragmentContext,
                            workoutList, DeleteWorkout.this::onWorkoutClick1);
                    recyclerView.setAdapter(workoutRecyclerAdapter);
                    workoutRecyclerAdapter.notifyDataSetChanged();

                    if (workoutList.isEmpty()) {

                        Log.d("DOCU", "ERROR GETTING DOCUMENTS");

                    }


                } else {
                    Toast.makeText(HomeFragmentContext, "No workouts to show", Toast.LENGTH_SHORT).show();
                    Log.d("DOCU", "ERROR GETTING DOCUMENTS");
                }
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
        intent.putExtra("fragmentToLoad", "Workout");
        startActivity(intent);
    }

}