package com.example.notesapp.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AppExitFragment extends DialogFragment {

    public static final String TAG = "AppExitFragment";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder
                .setTitle("Are you sure you want to close the application?")
                .setPositiveButton("Yes", (dialogInterface, i) -> requireActivity().finish())
                .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
        return builder.create();
    }
}
