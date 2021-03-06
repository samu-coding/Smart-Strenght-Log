package com.example.smartstrengthlog.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstrengthlog.LoginActivity;
import com.example.smartstrengthlog.MainMenu;
import com.example.smartstrengthlog.R;
import com.example.smartstrengthlog.WorkoutSessionLog;
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
import java.util.Objects;

import models.Workout;
import ui.WorkoutRecyclerAdapter;
import util.SmartStrengthLogAPI;
import util.WorkoutSessionAPI;

public class DashboardFragment extends Fragment implements WorkoutRecyclerAdapter.OnWorkoutClickListener {


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<Workout> workoutList;
    private RecyclerView recyclerView;
    private WorkoutRecyclerAdapter workoutRecyclerAdapter;

    private CollectionReference collectionReference = db.collection("Workout");
    private TextView noWorkoutEntry;

    private String userId;

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);

        noWorkoutEntry = root.findViewById(R.id.list_no_workouts);

        workoutList = new ArrayList<>();

        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Context HomeFragmentContext = getActivity();
        mostrarWorkouts(HomeFragmentContext);

        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        userId = SmartStrengthLogAPI.getInstance().getUserId();

        Context HomeFragmentContext = getActivity();//Para poder obtener el contexto del Fragment
        mostrarWorkouts(HomeFragmentContext);
    }

    public void mostrarWorkouts(Context HomeFragmentContext){
        collectionReference.whereEqualTo("user", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){

                    for (QueryDocumentSnapshot document :task.getResult()){
                        Workout workout = document.toObject(Workout.class);
                        workoutList.add(workout);
                    }

                    //Recycler view
                    workoutRecyclerAdapter = new WorkoutRecyclerAdapter(HomeFragmentContext,
                            workoutList,DashboardFragment.this);
                    recyclerView.setAdapter(workoutRecyclerAdapter);
                    workoutRecyclerAdapter.notifyDataSetChanged();

                    if (workoutList.isEmpty()){
                        Log.d("DOCU","ERROR GETTING DOCUMENTS");
                    }


                }else {
                    noWorkoutEntry.setVisibility(View.VISIBLE);
                    Log.d("DOCU","ERROR GETTING DOCUMENTS");
                }
            }
        });
    }

    //Click de un workout
    @Override
    public void onWorkoutClick(int position) {

        WorkoutSessionAPI workoutSessionAPI = WorkoutSessionAPI.getInstance();
        workoutSessionAPI.setExerciseNumber(0);
        Workout workout = workoutList.get(position);

        //Cambio de vista
        Intent intent = new Intent(getActivity(),
                WorkoutSessionLog.class);
        intent.putExtra("workoutId", workout.getId());
        startActivity(intent);

    }
}