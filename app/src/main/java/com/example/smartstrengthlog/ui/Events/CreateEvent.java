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
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import models.Event;
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
                       //Cambio de actividad
                       Intent intent = new Intent(CreateEvent.this,
                               EventList.class);
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

        Event evento = new Event();
        evento.setNameEvent(nameEvent);
        evento.setDateEvent(date);
        evento.setUser(currentUserId);
        evento.setEventID(String.valueOf(Timestamp.now().getSeconds()));

        collectionReference.add(evento)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("EVENT", "Event saved.");
                    }
                });

    }

    @Override
    public void onBackPressed() {
        //Cambio de vista
        Intent intent = new Intent(this,
                EventList.class);
        startActivity(intent);
    }
}