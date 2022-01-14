package com.example.notesapp.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.notesapp.R;
import com.example.notesapp.data.Note;
import com.google.android.material.textfield.TextInputLayout;

public class NotesDialog extends DialogFragment {

    public static final String NOTE = "NOTE";
    NoteDialogController controller;
    Note note;

    public static NotesDialog getInstance(Note note) {
        NotesDialog dialog = new NotesDialog();
        Bundle args = new Bundle();
        args.putSerializable(NOTE, note);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        note = (Note) args.getSerializable(NOTE);

        String title = "";
        String description = "";

        if (note != null) {
            title = note.getTitle();
            description = note.getDescription();
        }

        View dialog = LayoutInflater.from(getContext()).inflate(R.layout.notes_dialog, null);

        TextInputLayout dialogTitle = dialog.findViewById(R.id.dialog_title);
        TextInputLayout dialogDescription = dialog.findViewById(R.id.dialog_description);

        dialogTitle.getEditText().setText(title);
        dialogDescription.getEditText().setText(description);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        String buttonText;

        if (note == null) {
            buttonText = "Create";
            builder.setTitle("Create note");
        } else {
            buttonText = "Modify";
            builder.setTitle("Modify note");
        }
        builder
                .setView(dialog)
                .setCancelable(true)
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel())
                .setPositiveButton(buttonText, (dialogInterface, i) -> {
                    if (note == null) {
                        controller.createNoteFromDialog(
                                dialogTitle.getEditText().getText().toString(),
                                dialogDescription.getEditText().getText().toString()
                        );
                    } else {
                        note.setTitle(dialogTitle.getEditText().getText().toString());
                        note.setDescription(dialogDescription.getEditText().getText().toString());
                        controller.updateNoteFromDialog(note);
                    }
                    dialogInterface.dismiss();
                })
        ;
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        this.controller = (NoteDialogController) context;
        super.onAttach(context);
    }

    interface NoteDialogController {
        void createNoteFromDialog(String title, String description);

        void updateNoteFromDialog(Note note);

    }
}
