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
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartstrengthlog.MainMenu;
import com.example.smartstrengthlog.R;

import util.SmartStrengthLogAPI;

public class EventCalendar extends AppCompatActivity {

    CalendarView calendarView;
    TextView myDate;
    EditText nameEvent;
    Button saveEvent;
    String date;


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

                       Intent intent = new Intent(EventCalendar.this,
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
}