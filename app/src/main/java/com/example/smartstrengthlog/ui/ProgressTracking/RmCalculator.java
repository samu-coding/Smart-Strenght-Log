package com.example.smartstrengthlog.ui.ProgressTracking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartstrengthlog.R;
import com.example.smartstrengthlog.ui.Dialogs.InfoRmDialog;

public class RmCalculator extends AppCompatActivity {

    private Button botonInfo;
    private Button botonCalcular1RM;

    private EditText reps;
    private EditText weight;
    private TextView rmcalculado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rm_calculator);

        reps = findViewById(R.id.reps_rm);
        weight = findViewById(R.id.weight_rm);

        rmcalculado = findViewById(R.id.result_rm_calculation);

        botonCalcular1RM = findViewById(R.id.button_calculate_rm);

        botonCalcular1RM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String repes = String.valueOf(reps.getText());
                String peso = String.valueOf(weight.getText());

                if (!repes.isEmpty() && !peso.isEmpty()) {

                    float RM1 = (float) (Float.parseFloat(peso) * (1 + (0.033 * Float.parseFloat(repes))));
                    rmcalculado.setText("Your 1RM is: "+RM1+ " kg.");
                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);

                } else {
                    Toast.makeText(RmCalculator.this, "Please, fill all the fields.", Toast.LENGTH_LONG).show();
                }
            }

        });

        botonInfo = findViewById(R.id.button_more_info_rm);
        botonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    public void openDialog(){
        InfoRmDialog infoRmDialog = new InfoRmDialog();
        infoRmDialog.show(getSupportFragmentManager(),"Dialog");

    }

}