package com.example.smartstrengthlog.ui.ProgressTracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannelGroup;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.smartstrengthlog.LoginActivity;
import com.example.smartstrengthlog.MainMenu;
import com.example.smartstrengthlog.R;
import com.example.smartstrengthlog.WorkoutSessionLog;
import com.example.smartstrengthlog.ui.Dialogs.InfoStatsDialog;
import com.example.smartstrengthlog.ui.notifications.NotificationsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import util.MarcasAPI;
import util.SmartStrengthLogAPI;

import static java.lang.Integer.parseInt;

public class PerformanceStats extends AppCompatActivity {

    private String name_exercise;
    private String workoutId;
    private String documentID;

    private String ejercicio = "Ejercicio 1";

    private TextView name_exercise_Title;
    private TextView num_wk_grafica;
    private Button mostrar_grafica_boton;

    private Button botonInfo;


    //Connection to Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Guardado de marcas
    List<Integer> marcas =new ArrayList<>();
    List<Date> diaWKSession =new ArrayList<>();


    //test
    private int i =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance_stats);

        name_exercise_Title = findViewById(R.id.exercise_name_stats_Title);
        mostrar_grafica_boton = findViewById(R.id.show_graph_button);
        num_wk_grafica = findViewById(R.id.number_of_wk_graph);

        //Obtenemos los valores que vienen de la vista anterior
        workoutId = (String) getIntent().getSerializableExtra("workoutId");
        name_exercise = (String) getIntent().getSerializableExtra("exercise");
        documentID = (String) getIntent().getSerializableExtra("documentID");
        ejercicio = getIntent().getExtras().getString("num_ejercicio");

        name_exercise_Title.setText(name_exercise+ "\nPerformance:");

        botonInfo = findViewById(R.id.button_more_info_stats);
        botonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });


        MarcasAPI marcasAPI = MarcasAPI.getInstance();



        //Buscamos las marcas del primer set de cada entreno.
        Query query = db.collection("Workout").document(documentID).collection("History")
                .orderBy("date", Query.Direction.ASCENDING)
                .limit(5);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("SEARCH", document.getId() + " => " + document.getData());


                        Date dateaso = null;
                        try {
                            dateaso = new SimpleDateFormat("yyyy-MM-dd").parse(document.getId());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        diaWKSession.add(dateaso);
                        String auxID = document.getId();

                        //Subquery para los valores del primer set
                        Query subQuery1 = db.collection("Workout").document(documentID).collection("History")
                                .document(auxID).collection(ejercicio).whereEqualTo("Set",1);

                        subQuery1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {


                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        //Calculamos el 1RM del documento y lo añadimos a un ARRAY!
                                        //Fórmula de Epley Welday
                                        int reps = parseInt(document.get("Reps").toString());
                                        int weight = parseInt(document.get("Weight").toString());
                                        int oneRM = (int) (weight * (1+(0.033 * reps))); //Epley & Welday Formula
                                        marcas.add(oneRM);

                                    }
                                    //Log.d("MARCAS", marcas.toString());
                                    marcasAPI.setMarcas(marcas);
                                    marcasAPI.setDates(diaWKSession);

                                }

                                else {
                                    Log.d("MARCAS", "Error getting MARCAS: ", task.getException());
                                }
                            }
                        });

                    }

                } else {
                    Log.d("MARCAS", "Error getting documents de MARCAS: ", task.getException());
                }
            }

        });

        mostrar_grafica_boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearGraph(marcasAPI.getMarcas(), marcasAPI.getDates());
            }
        });



    }


    public void crearGraph(List<Integer> marcas, List<Date> diaWKSession){

        GraphView graph = (GraphView) findViewById(R.id.graph);
        DataPoint [] dataPoints = new DataPoint[marcas.size()];

        for (int i =0; i<marcas.size(); i++){
           dataPoints [i] = new DataPoint((i+1),marcas.get(i));
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);

        graph.getGridLabelRenderer().setNumHorizontalLabels(marcas.size());

        series.setColor(Color.BLUE);
        series.setThickness(7);
        graph.addSeries(series);
        num_wk_grafica.setText("Analysis of the last "+marcas.size()+" sessions.");
        Log.d("Dias", String.valueOf(diaWKSession));
        Log.d("Marcas", String.valueOf(marcas));
        Log.d("Points", String.valueOf(dataPoints));

    }

    public void openDialog(){
        InfoStatsDialog infoStatsDialog = new InfoStatsDialog();
        infoStatsDialog.show(getSupportFragmentManager(),"Dialog");

    }

    @Override
    public void onBackPressed() {
        //Cambio de vista
        Intent intent = new Intent(this,
                MainMenu.class);
        SmartStrengthLogAPI smartStrengthLogAPI = new SmartStrengthLogAPI();
        intent.putExtra("username", smartStrengthLogAPI.getUsername());
        intent.putExtra("userId", smartStrengthLogAPI.getUserId());
        intent.putExtra("fragmentToLoad", "Performance");
        startActivity(intent);
    }

}

