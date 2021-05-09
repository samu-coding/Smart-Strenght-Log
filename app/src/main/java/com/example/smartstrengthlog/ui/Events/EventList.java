package com.example.smartstrengthlog.ui.Events;

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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartstrengthlog.MainMenu;
import com.example.smartstrengthlog.R;
import com.example.smartstrengthlog.ui.dashboard.DashboardViewModel;
import com.example.smartstrengthlog.ui.home.HomeViewModel;
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

import models.Event;
import ui.EventRecyclerAdapter;
import util.SmartStrengthLogAPI;

public class EventList extends AppCompatActivity implements EventRecyclerAdapter.OnEventClickListener {

    private HomeViewModel homeViewModel;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private List<Event> allEventsList;
    private RecyclerView recyclerView;
    private EventRecyclerAdapter eventRecyclerAdapter;

    private CollectionReference collectionReference = db.collection("Events");

    private String userId;

    private DashboardViewModel dashboardViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        final TextView textView = findViewById(R.id.text_dashboard);


        allEventsList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerViewEvents);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Para poder obtener el contexto del Fragment, usamos get Activity.
        mostrarEventos(this);


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

        mostrarEventos(this);


    }
    
    public void mostrarEventos(Context HomeFragmentContext) {
        collectionReference.whereEqualTo("user", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            //collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){

                    for (QueryDocumentSnapshot document :task.getResult()){

                        Event event = document.toObject(Event.class);
                        allEventsList.add(event);
                        Log.d("DOCU", document.getId() + "-->" + document.getData());

                    }

                    //Invoke Recycler view
                    eventRecyclerAdapter = new EventRecyclerAdapter(HomeFragmentContext,
                            allEventsList, EventList.this);
                    recyclerView.setAdapter(eventRecyclerAdapter);
                    eventRecyclerAdapter.notifyDataSetChanged();

                    if (allEventsList.isEmpty()){

                        //Toast.makeText(HomeFragmentContext, "NO ENCUENTRA WK", Toast.LENGTH_SHORT).show();
                        Log.d("DOCU","ERROR GETTING DOCUMENTS");

                    }


                }else {
                    Toast.makeText(HomeFragmentContext, "No workouts to show", Toast.LENGTH_SHORT).show();
                    Log.d("DOCU","ERROR GETTING DOCUMENTS");
                }
            }
        });


    }

    public void createEvent(View view){

        Intent intent = new Intent(this,
                CreateEvent.class);
        startActivity(intent);
    }

    @Override
    public void onEventClick(int position) {
        //DELETE THE EVENT -> MARK AS DONE
        //Log.d("Clicked", "onWorkoutClick: " + position);

        Event event = allEventsList.get(position);
        String eventId = event.getEventID();

        //Dialogo
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Mark as done:");
        alertDialog.setMessage("Are you sure you want to mark this event as done? This action will DELETE the event.");
        //alertDialog.setIcon(int )
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                deleteEvent(eventId);

            }

            private void deleteEvent(String eventId) {

                //Toast.makeText(getApplicationContext(),"Deleting event: "+eventId, Toast.LENGTH_SHORT).show();
                db.collection("Events")
                        .whereEqualTo("eventID", eventId)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        //Guardamos el ID del documento
                                        String documentID = document.getId();

                                        //Borramos el documento obtenido
                                        db.collection("Events")
                                                .document(documentID)
                                                .delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d("DELETE", "DocumentSnapshot successfully deleted!");
                                                        Intent intent = new Intent(EventList.this,
                                                                EventList.class);
                                                        startActivity(intent);
                                                        //onBackPressed();


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
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
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