package com.example.smartstrengthlog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.smartstrengthlog.ui.ProgressTracking.RmCalculator;
import com.example.smartstrengthlog.ui.ProgressTracking.RoutineSelectionProgress;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.protobuf.StringValue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import models.Workout;
import ui.WorkoutRecyclerAdapter;

public class MainMenu extends AppCompatActivity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {

       // Toast.makeText(MainMenu.this, "INICIADO!", Toast.LENGTH_SHORT).show();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_workout, R.id.navigation_performance)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        //noWorkoutEntry = findViewById(R.id.list_no_workouts);
        //noWorkoutEntry.setVisibility(View.VISIBLE);

        workoutList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        //recyclerView.setHasFixedSize(true);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Gracias a la API podemos coger el usuario o el id cuando queramos
        //SmartStrengthLogAPI.getInstance().getUserId();
        //SmartStrengthLogAPI.getInstance().getUsername()

    }


    public void logOut (View view){

        startActivity(new Intent(this, LoginActivity.class));
        finish();

    }

    //Al clickar al botón de nuevo WK, vamos a esta pantalla
    public void createWorkout (View view){

        //Al ser un fragment, tenemos que referenciar el contexto, por eso es diferente.
        Intent myIntent = new Intent(this, NewWorkoutCreator.class);
        startActivity(myIntent);
        finish();

    }

    public void exerciseProgress(View view){
        //Cambio de vista
        Intent intent = new Intent(this,
                RoutineSelectionProgress.class);
        //intent.putExtra("workoutId", workout.getId());
        //Log.d("Clicked", "QUEREMOS PASAR el id:  "+ workout.getId());
        startActivity(intent);

    }

    public void calculate1RM(View view){
        //Cambio de vista
        Intent intent = new Intent(this,
                RmCalculator.class);
        startActivity(intent);

    }

    public void exportData(View view){

        StringBuilder data = new StringBuilder();
        data.append("Time,Distance");

        for (int i  =0; i<5; i++){
            data.append("\n" + String.valueOf(i)+","+String.valueOf(i*i));
        }

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


}