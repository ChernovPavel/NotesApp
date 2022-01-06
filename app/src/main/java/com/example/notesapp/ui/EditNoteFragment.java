package com.example.notesapp.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.notesapp.R;
import com.example.notesapp.data.InMemoryRepoImpl;
import com.example.notesapp.data.Note;
import com.example.notesapp.data.Repo;

public class EditNoteFragment extends Fragment implements DatePickerFragment.OnConfirmDateBtnClickListener {

    public static final String NOTE = "NOTE";
    Repo repository = InMemoryRepoImpl.getInstance();
    private EditText title;
    private EditText description;
    private Button selectDate;
    private Button saveNote;
    private Spinner spinner;
    private Integer choiceImportance;
    private TextView tvDate;
    private int id = -1;
    private Note note = null;
    private Controller controller;

    public static EditNoteFragment getInstance(Note note) {
        EditNoteFragment fragment = new EditNoteFragment();
        Bundle args = new Bundle();
        args.putSerializable(NOTE, note);
        fragment.setArguments(args);
        return fragment;
    }

    public static void hideSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void confirmDateBtnPressed(String date) {
        tvDate.setText(date);
    }

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
    public void onStop() {
        ((MainActivity) requireActivity()).getNavBar().setVisibility(View.VISIBLE);
        super.onStop();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            ((MainActivity) requireActivity()).getNavBar().setVisibility(View.GONE);
        }
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
        tvDate = view.findViewById(R.id.date_text);

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
            tvDate.setText(note.getDate());
        }

        saveNote.setOnClickListener(v -> {
            //если новая заметка то по клику создавать новую, если меняем существующую, то апдейтим старую
            if (note == null) {
                int newNoteId = repository
                        .create(new Note(
                                        title.getText().toString(),
                                        description.getText().toString(),
                                        Note.NoteImportance.values()[choiceImportance],
                                tvDate.getText().toString()
                                )
                        );
                note = repository.read(newNoteId);
            } else {
                note.setTitle(title.getText().toString());
                note.setDescription(description.getText().toString());
                note.setImportance(Note.NoteImportance.values()[choiceImportance]);
                note.setDate(tvDate.getText().toString());
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

        selectDate.setOnClickListener(v -> {
            //проверка, чтобы открывать только один nested фрагмент

            for (Fragment f : requireActivity().getSupportFragmentManager().getFragments()) {
                if (f.isVisible()) {
                    FragmentManager childFm = f.getChildFragmentManager();
                    if (childFm.getBackStackEntryCount() == 0) {
                        getChildFragmentManager()
                                .beginTransaction()
                                .replace(R.id.date_picker_fragment, new DatePickerFragment())
                                .addToBackStack(null)
                                .commit();
                    }
                }
            }
            hideSoftKeyboard(requireActivity());
        });
    }

    interface Controller {
        void saveButtonPressed();
    }
}