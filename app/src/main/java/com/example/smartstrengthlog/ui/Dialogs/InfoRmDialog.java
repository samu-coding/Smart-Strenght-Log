package com.example.smartstrengthlog.ui.ProgressTracking;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class InfoRmDialog extends AppCompatDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Information")
                .setMessage("This is a calculator that uses the Epley & Welday Formula (very precise when 10 < reps < 15) to estimate your " +
                        "1RM (One-repetition maximum).\n 1RM in weight training is " +
                        "the maximum amount of weight that a person can possibly lift for one" +
                        " repetition. \n\nDISCLAIMER: This is just a theoretical approach for the user´s " +
                        "guidance. Smart Strength Log does not hold any responsibility over the usage of this information.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });



        return  builder.create();
    }
}
