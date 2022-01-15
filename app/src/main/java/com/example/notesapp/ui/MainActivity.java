package com.example.notesapp.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.notesapp.R;
import com.example.notesapp.data.InMemoryRepoImpl;
import com.example.notesapp.data.Note;
import com.example.notesapp.data.Repo;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements EditNoteFragment.Controller, NotesDialog.NoteDialogController {

    private final Repo repository = InMemoryRepoImpl.getInstance();
    private BottomNavigationView bottomNavigationView;
    private NotesListFragment notesListFragment;

    public BottomNavigationView getNavBar() {
        return bottomNavigationView;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        bottomNavigationView = findViewById(R.id.main_bottom_navigation);

        // выбор фрагмента по нажатию на нижнее навигационное меню
        bottomNavigationView.setOnItemSelectedListener(item -> {

            Fragment fragment;

            switch (item.getItemId()) {
                case R.id.bottom_menu_settings:
                    fragment = new SettingsFragment();
                    break;
                case R.id.bottom_menu_calendar:
                    fragment = new CalendarFragment();
                    break;
                default:
                    fragment = new NotesListFragment();
                    break;
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        });

        NotesListFragment notesListFragment = new NotesListFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, notesListFragment, "notesListFragment")
                .commit();
    }

    //отобразить фрагмент со списком заметок при нажатии кнопки сохранить во фрагменте изменения заметки
    @Override
    public void saveButtonPressed() {
        NotesListFragment notesListFragment = new NotesListFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, notesListFragment)
                .commit();
    }

    //создание заметки когда в диалоге-фрагменте нажали сохранить
    @Override
    public void createNoteFromDialog(String title, String description) {
        String date = new SimpleDateFormat("d.M.yyyy", Locale.ENGLISH).format(Calendar.getInstance().getTime());
        repository.create(new Note(title, description, Note.NoteImportance.MEDIUM, date));

        //получаем фрагмент и из него вызываем getAdapter() чтобы обновить список заметок и уведомить всех подписчиков
        notesListFragment = (NotesListFragment) getSupportFragmentManager().findFragmentByTag("notesListFragment");
        if (notesListFragment != null) {
            notesListFragment.getAdapter().setNotes(repository.getAll());
        }
    }

    //аналогично методу выше только это про изменение заметки
    @Override
    public void updateNoteFromDialog(Note note) {
        repository.update(note);
        if (notesListFragment != null) {
            notesListFragment.getAdapter().setNotes(repository.getAll());
        }
    }
}