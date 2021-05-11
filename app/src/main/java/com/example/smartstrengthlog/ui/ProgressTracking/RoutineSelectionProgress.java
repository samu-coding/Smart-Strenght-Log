package com.example.smartstrengthlog.ui.ProgressTracking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartstrengthlog.MainMenu;
import com.example.smartstrengthlog.R;
import com.example.smartstrengthlog.WorkoutSessionLog;
import com.example.smartstrengthlog.ui.dashboard.DashboardFragment;
import com.example.smartstrengthlog.ui.dashboard.DashboardViewModel;
import com.example.smartstrengthlog.ui.home.HomeViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class RoutineSelectionProgress extends AppCompatActivity implements WorkoutRecyclerAdapter.OnWorkoutClickListener{

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<Workout> workoutList;
    private RecyclerView recyclerView;
    private WorkoutRecyclerAdapter workoutRecyclerAdapter;

    private CollectionReference collectionReference = db.collection("Workout");
    //private TextView noWorkoutEntry;

    private String userId;

    private DashboardViewModel dashboardViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine_selection_progress);

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
        Log.d("USUARIO", "BUSQUEDA DOCUMENTO DE USUARIO :" +userId);

        mostrarWorkouts(this);


    }

    @Override
    public void onWorkoutClick(int position) {
        WorkoutSessionAPI workoutSessionAPI = WorkoutSessionAPI.getInstance();
        workoutSessionAPI.setExerciseNumber(0);
        Workout workout = workoutList.get(position);

        //Cambio de vista
        Intent intent = new Intent(this,
                ExerciseSelectionProgress.class);
        intent.putExtra("workoutId", workout.getId());
        Log.d("Clicked", "QUEREMOS PASAR el id:  "+ workout.getId());

        startActivity(intent);

    }


    public void mostrarWorkouts(Context HomeFragmentContext){
        collectionReference.whereEqualTo("user", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){

                    for (QueryDocumentSnapshot document :task.getResult()){

                        Workout workout = document.toObject(Workout.class);
                        workoutList.add(workout);
                        Log.d("DOCU", document.getId() + "-->" + document.getData());

                    }

                    //Invoke Recycler view
                    workoutRecyclerAdapter = new WorkoutRecyclerAdapter(HomeFragmentContext,
                            workoutList, RoutineSelectionProgress.this::onWorkoutClick);
                    recyclerView.setAdapter(workoutRecyclerAdapter);
                    workoutRecyclerAdapter.notifyDataSetChanged();

                    if (workoutList.isEmpty()){

                        Log.d("DOCU","ERROR GETTING DOCUMENTS");

                    }


                }else {
                    Toast.makeText(HomeFragmentContext, "No workouts to show", Toast.LENGTH_SHORT).show();
                    Log.d("DOCU","ERROR GETTING DOCUMENTS");
                }
            }
        });
    }

}