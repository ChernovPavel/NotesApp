package com.example.notesapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.notesapp.R;

public class CancelDialogFragment extends DialogFragment {

    public static final String TAG = "CancelDialogFragment";
    Button button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View customView = inflater.inflate(R.layout.fragment_dialog_cancel_note, null);
        button = customView.findViewById(R.id.cancel_dialog_button);
        button.setOnClickListener(view -> {
            String text = customView.<EditText>findViewById(R.id.cancel_dialog_reason_text).getText().toString();
            ((MainActivity) requireActivity()).onDialogResult(text);
            dismiss();
        });
        return customView;
    }
}
