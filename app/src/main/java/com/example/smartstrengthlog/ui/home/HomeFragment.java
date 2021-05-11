package com.example.smartstrengthlog.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import com.example.smartstrengthlog.WorkoutSessionLog;
import com.example.smartstrengthlog.ui.dashboard.DashboardFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.Workout;
import ui.WorkoutRecyclerAdapter;
import util.SmartStrengthLogAPI;

import static java.lang.Integer.parseInt;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Workout");

    private TextView emailUser;
    private String userId;

    //Last WK
    private TextView lastWK1;
    private TextView lastWK2;
    private TextView lastWK3;

    List<String> wkTitles =new ArrayList<>();
    List<String> wkDates =new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);

        //LastWK
        lastWK1 = root.findViewById(R.id.last_1_WK);
        lastWK2 = root.findViewById(R.id.last_2_WK);
        lastWK3 = root.findViewById(R.id.last_3_WK);

        userId = SmartStrengthLogAPI.getInstance().getUserId();

        consultaLastWK();

        emailUser = root.findViewById(R.id.textView_email);
        Log.d("EMAILonCreate", "" +userId);
        String email = SmartStrengthLogAPI.getInstance().getUsername();
        emailUser.setText(email);

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        userId = SmartStrengthLogAPI.getInstance().getUserId();
        Context HomeFragmentContext = getActivity();
    }

    public void consultaLastWK(){

        userId = SmartStrengthLogAPI.getInstance().getUserId();
        Log.d("USERIDconsulta","ID:"+userId);

        Query query = db.collection("Workout")
                .whereEqualTo("user", userId)
                .orderBy("LastWorkout", Query.Direction.DESCENDING)
                .limit(3);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        wkTitles.add(document.get("title").toString());
                        wkDates.add(document.get("LastWorkout").toString());

                    }

                    if (wkTitles.isEmpty())
                    {

                    }
                    else{
                        lastWK1.setText(wkTitles.get(0) + " - "+ wkDates.get(0));
                        if(wkTitles.size()==2){
                            lastWK2.setText(wkTitles.get(1) + " - "+ wkDates.get(1));
                        }
                        else if(wkTitles.size()==3){
                            lastWK2.setText(wkTitles.get(1) + " - "+ wkDates.get(1));
                            lastWK3.setText(wkTitles.get(2) + " - "+ wkDates.get(2));
                        }
                    }

                }

                else {
                    Log.d("MARCAS", "Error getting MARCAS: ", task.getException());
                }
            }
        });

    }

}