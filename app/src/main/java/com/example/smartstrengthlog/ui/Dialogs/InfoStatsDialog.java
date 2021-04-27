package com.example.smartstrengthlog.ui.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class InfoStatsDialog extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Graph information")
                .setMessage("The purpose of this graph is to visually show  the user how their performance is" +
                        " of a certain exercise.  The numbers are obtained through a calculation" +
                        " of the estimated 1 RM based on your first set in every training session." +
                        "\n\nDISCLAIMER: This is just a theoretical approach for the user´s " +
                        "guidance. Smart Strength Log does not hold any responsibility over the usage of this information.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });



        return  builder.create();
    }
}
