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

    private TextView emailUser;
    private String userId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);

        emailUser = root.findViewById(R.id.textView_email);
        String email = SmartStrengthLogAPI.getInstance().getUsername();
        emailUser.setText(email);

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

        userId = SmartStrengthLogAPI.getInstance().getUserId();
        //SmartStrengthLogAPI.getInstance().getUsername()
        Log.d("USUARIO", "BUSQUEDA DOCUMENTO DE USUARIO :" +userId);

        //Para poder obtener el contexto del Fragment, usamos get Activity.
        Context HomeFragmentContext = getActivity();


    }



}