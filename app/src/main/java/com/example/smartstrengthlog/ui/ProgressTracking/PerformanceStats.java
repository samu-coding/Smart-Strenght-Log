package com.example.smartstrengthlog.ui.ProgressTracking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.smartstrengthlog.R;

public class PerformanceStats extends AppCompatActivity {

    private String name_exercise;

    private String workoutId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance_stats);


        workoutId = (String) getIntent().getSerializableExtra("workoutId");
        name_exercise = (String) getIntent().getSerializableExtra("exercise");

        Toast.makeText(this, workoutId+ " "+ name_exercise, Toast.LENGTH_SHORT).show();

    }
}