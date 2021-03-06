package com.example.notesapp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.google.gson.GsonBuilder;

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

    public static final String KEY = "KEY";
    private static final String ID_NOTE = "ID_NOTE";
    private SharedPreferences prefs = null;

    public static EditNoteFragment getInstance(Note note) {
        EditNoteFragment fragment = new EditNoteFragment();
        Bundle args = new Bundle();
        args.putSerializable(NOTE, note);
        fragment.setArguments(args);
        return fragment;
    }

    //скрывание клавиатуры
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
        //проверяем что активити инмплементит интерфейс Controller. Который является коллбеком для нажатия кнопки "сохранить" в этом фрагменте
        if (context instanceof Controller) {
            this.controller = (Controller) context;
        } else {
            throw new IllegalStateException("activity must be implement Controller");
        }
        super.onAttach(context);
    }

    //снова показывать нижнее меню когда этот фрагмент закроется. Потому что при открытии этого фрагмента нижнее меню скрываем
    @Override
    public void onStop() {
        ((MainActivity) requireActivity()).getNavBar().setVisibility(View.VISIBLE);
        super.onStop();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //скрываем нижнее меню при открытии этого фрагмента (потому что оно не нужно)
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            ((MainActivity) requireActivity()).getNavBar().setVisibility(View.GONE);
        }

        //чтобы фрагмент имел доступ к настройкам главного меню (для скрывания ненужных кнопок)
        setHasOptionsMenu(true);

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
        prefs = getActivity().getPreferences(Context.MODE_PRIVATE);

        //стандартное добавление спинера на фрагмент
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

                String jsonNotes = new GsonBuilder().create().toJson(repository.getAll());
                prefs.edit().putString(KEY, jsonNotes).apply();
                prefs.edit().putInt(ID_NOTE, repository.getCounter()).apply();

            } else {
                note.setTitle(title.getText().toString());
                note.setDescription(description.getText().toString());
                note.setImportance(Note.NoteImportance.values()[choiceImportance]);
                note.setDate(tvDate.getText().toString());
                repository.update(note);

                String jsonNotes = new GsonBuilder().create().toJson(repository.getAll());
                prefs.edit().putString(KEY, jsonNotes).apply();
            }

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .remove(this)
                    .commit();

            // вызвваем коллбэк
            controller.saveButtonPressed();
            hideSoftKeyboard(requireActivity());
        });

        //слушатель выбора элемента на спиннере
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

    //скрывать кнопки создания заметки в меню
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuItem item = menu.findItem(R.id.main_create);
        if (item != null) {
            item.setVisible(false);
            menu.findItem(R.id.dialog_create).setVisible(false);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    interface Controller {
        void saveButtonPressed();
    }
}