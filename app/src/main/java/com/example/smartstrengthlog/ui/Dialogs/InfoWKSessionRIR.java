package com.example.smartstrengthlog.ui.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class InfoWKSessionRIR extends AppCompatDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Information")
                .setMessage("RIR (Repetitions In Reserve): Term used to represent the number of " +
                        "remaining repetitions that are not performed in a set before reaching muscle " +
                        "failure. Therefore, a RIR 2 indicates that the person could have performed " +
                        "2 more repetitions and then he would have reached muscle failure.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return  builder.create();
    }
}