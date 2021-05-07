package com.example.smartstrengthlog.ui.Events;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartstrengthlog.MainMenu;
import com.example.smartstrengthlog.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import util.SmartStrengthLogAPI;

public class CreateEvent extends AppCompatActivity {

    CalendarView calendarView;
    TextView myDate;
    EditText nameEvent;
    Button saveEvent;
    String date;

    //Connection to Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Events");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_calendar);

        calendarView = findViewById(R.id.calendarView);
        myDate = findViewById(R.id.myDate);
        nameEvent = findViewById(R.id.name_event);
        saveEvent = findViewById(R.id.button_save_Event);



        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date = (dayOfMonth) +"/" +month + "/" +year;
                myDate.setText(date);
            }
        });


        saveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nameEventString = nameEvent.getText().toString();
               if (!nameEventString.isEmpty()){

                   if (date != null){

                       saveEvent(nameEventString, date);

                       Intent intent = new Intent(CreateEvent.this,
                               MainMenu.class);
                       SmartStrengthLogAPI smartStrengthLogAPI = new SmartStrengthLogAPI();
                       intent.putExtra("username", smartStrengthLogAPI.getUsername());
                       intent.putExtra("userId", smartStrengthLogAPI.getUserId());
                       intent.putExtra("fragmentToLoad", "Performance");
                       startActivity(intent);
                   }
                   else{
                       Toast.makeText(getApplicationContext(),"Please select a date.",Toast.LENGTH_SHORT).show();
                   }
               }
               else{
                   Toast.makeText(getApplicationContext(),"Please name the event.",Toast.LENGTH_SHORT).show();
               }

                Log.d("VALUES", nameEventString + " "+ date);
            }
        });

    }

    public void saveEvent(String nameEvent, String date){

        String currentUserId = SmartStrengthLogAPI.getInstance().getUserId();

        Map<String, Object> evento = new HashMap<>();
        evento.put("nameEvent", nameEvent);
        evento.put("dateEvent", date);
        evento.put("user", currentUserId);

        collectionReference.add(evento)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("EVENT", "EVENT SAVED IN FIRESTORE!");
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
        intent.putExtra("fragmentToLoad", "Performance");
        startActivity(intent);
    }
}