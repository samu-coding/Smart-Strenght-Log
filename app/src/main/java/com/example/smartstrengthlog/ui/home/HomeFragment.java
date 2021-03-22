package com.example.smartstrengthlog.ui.home;

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
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstrengthlog.CreateAccountActivity;
import com.example.smartstrengthlog.MainMenu;
import com.example.smartstrengthlog.NewWorkoutCreator;
import com.example.smartstrengthlog.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FirebaseAuth firebaseAuth;
    private  FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private List<Workout> workoutList;
    private RecyclerView recyclerView;
    private WorkoutRecyclerAdapter workoutRecyclerAdapter;

    private CollectionReference collectionReference = db.collection("Workout");
    private TextView noWorkoutEntry;

    private String userId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);

        noWorkoutEntry = root.findViewById(R.id.list_no_workouts);

        workoutList = new ArrayList<>();

        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        return root;

    }

     //Here is recommended to query the storage reference
    @Override
    public void onStart() {
        super.onStart();

        //Para poder obtener el contexto del Fragment, usamos get Activity.
        Context HomeFragmentContext = getActivity();

        userId = SmartStrengthLogAPI.getInstance().getUserId();
        //SmartStrengthLogAPI.getInstance().getUsername()

        Log.d("USUARIO", "BUSQUEDA DOCUMENTO DE USUARIO :" +userId);


        collectionReference.whereEqualTo("user", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        //collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document :task.getResult()){

                        Workout workout = document.toObject(Workout.class);
                        workoutList.add(workout);
                        Log.d("DOCU", document.getId() + "-->" + document.getData());

                    }

                    Toast.makeText(HomeFragmentContext, "SI ENCUENTRA WK", Toast.LENGTH_SHORT).show();

                    //Invoke Recycler view

                    workoutRecyclerAdapter = new WorkoutRecyclerAdapter(HomeFragmentContext,
                            workoutList);
                    recyclerView.setAdapter(workoutRecyclerAdapter);
                    workoutRecyclerAdapter.notifyDataSetChanged();
                    /*
                    for(QueryDocumentSnapshot document :task.getResult()){
                        Log.d("DOCU", document.getId() + "-->" + document.getData());
                    }*/
                }else {
                    noWorkoutEntry.setVisibility(View.VISIBLE);
                    Toast.makeText(HomeFragmentContext, "NO ENCUENTRA WK", Toast.LENGTH_SHORT).show();
                    Log.d("DOCU","ERROR GETTING DOCUMENTS");
                }
            }
        });



    }

   /* public void createWorkout (View view){

        //Al ser un fragment, tenemos que referenciar el contexto, por eso es diferente.
        Log.i("ERROR","Boton clicado");

        Intent myIntent = new Intent(HomeFragment.this.getActivity(), NewWorkoutCreator.class);
        startActivity(myIntent);
        getActivity().finish();

    }*/


}