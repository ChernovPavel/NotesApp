package com.example.notesapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.notesapp.R;
import com.example.notesapp.data.InMemoryRepoImpl;
import com.example.notesapp.data.Note;
import com.example.notesapp.data.Repo;

import java.util.Date;

public class EditNoteFragment extends Fragment {

    private EditText title;
    private EditText description;
    private Button selectDate;
    private Button saveNote;
    private Spinner spinner;
    private Integer choiceImportance;
    private TextView date;

    private int id = -1;
    private Note note = null;
    Repo repository = InMemoryRepoImpl.getInstance();

    public static final String NOTE = "NOTE";

    public static EditNoteFragment getInstance(Note note) {
        EditNoteFragment fragment = new EditNoteFragment();
        Bundle args = new Bundle();
        args.putSerializable(NOTE, note);
        fragment.setArguments(args);
        return fragment;
    }

    interface Controller {
        void saveButtonPressed();
    }

    private Controller controller;

    @Override
    public void onAttach(@NonNull Context context) {
        if (context instanceof Controller) {
            this.controller = (Controller) context;
        } else {
            throw new IllegalStateException("activity must be implement Controller");
        }
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        title = view.findViewById(R.id.edit_note_title);
        description = view.findViewById(R.id.edit_note_description);
        selectDate = view.findViewById(R.id.fragment_edit_note_select_data_btn);
        saveNote = view.findViewById(R.id.edit_note_update);
        spinner = view.findViewById(R.id.fragment_edit_note_spinner);
        date = view.findViewById(R.id.date_text);

        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(requireContext(), R.array.notes_importance, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Bundle args = getArguments();
        if (args != null && args.containsKey(NOTE)) {
            note = (Note) getArguments().getSerializable(NOTE);
            id = note.getId();
            title.setText(note.getTitle());
            description.setText(note.getDescription());
            spinner.setSelection(note.getImportance().ordinal());
        }

        saveNote.setOnClickListener(v -> {
            if (note == null) {
                int newNoteId = repository
                        .create(new Note(
                                        title.getText().toString(),
                                        description.getText().toString(),
                                        Note.NoteImportance.values()[choiceImportance],
                                        new Date()
                                )
                        );
                note = repository.read(newNoteId);
            } else {
                note.setTitle(title.getText().toString());
                note.setDescription(description.getText().toString());
                note.setImportance(Note.NoteImportance.values()[choiceImportance]);
                repository.update(note);
            }

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .remove(this)
                    .commit();

            controller.saveButtonPressed();
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                choiceImportance = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getChildFragmentManager()
                        .beginTransaction()
                        .add(R.id.date_picker_fragment, new DatePickerFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}