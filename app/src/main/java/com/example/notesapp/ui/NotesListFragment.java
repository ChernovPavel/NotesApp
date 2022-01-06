package com.example.notesapp.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.R;
import com.example.notesapp.data.InMemoryRepoImpl;
import com.example.notesapp.data.Note;
import com.example.notesapp.data.Repo;
import com.example.notesapp.recycler.NotesAdapter;

public class NotesListFragment extends Fragment implements NotesAdapter.onNoteClickListener {

    private final Repo repository = InMemoryRepoImpl.getInstance();
    private RecyclerView recyclerView;
    private NotesAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (repository.getAll().size() == 0) fillRepo();

        adapter = new NotesAdapter();
        adapter.setNotes(repository.getAll());

        adapter.setOnNoteClickListener(this);

        recyclerView = view.findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void fillRepo() {
        repository.create(new Note("Title 1", "Description 1", Note.NoteImportance.LOW, "30.2.2019"));
        repository.create(new Note("Title 2", "Description 2", Note.NoteImportance.HIGH, "15.5.2022"));
        repository.create(new Note("Title 3", "Description 3", Note.NoteImportance.MEDIUM, "2.11.2017"));
        repository.create(new Note("Title 4", "Description 4", Note.NoteImportance.HIGH, "14.2.2015"));
        repository.create(new Note("Title 5", "Description 5", Note.NoteImportance.MEDIUM, "30.2.2019"));
        repository.create(new Note("Title 6", "Description 6", Note.NoteImportance.LOW, "15.2.2012"));
        repository.create(new Note("Title 7", "Description 7", Note.NoteImportance.HIGH, "16.12.2019"));
        repository.create(new Note("Title 8", "Description 8", Note.NoteImportance.MEDIUM, "30.2.2019"));
        repository.create(new Note("Title 9", "Description 9", Note.NoteImportance.MEDIUM, "30.11.2000"));
        repository.create(new Note("Title 10", "Description 10", Note.NoteImportance.MEDIUM, "30.2.2019"));
        repository.create(new Note("Title 11", "Description 11", Note.NoteImportance.HIGH, "23.1.2011"));
        repository.create(new Note("Title 12", "Description 12", Note.NoteImportance.HIGH, "6.2.2019"));
        repository.create(new Note("Title 13", "Description 13", Note.NoteImportance.LOW, "9.4.2019"));
        repository.create(new Note("Title 14", "Description 14", Note.NoteImportance.MEDIUM, "8.2.2012"));
        repository.create(new Note("Title 15", "Description 15", Note.NoteImportance.LOW, "30.6.2019"));
        repository.create(new Note("Title 16", "Description 16", Note.NoteImportance.MEDIUM, "30.2.2019"));
    }

    @Override
    public void onNoteClick(Note note) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, EditNoteFragment.getInstance(note))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        requireActivity().getMenuInflater().inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_create:
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new EditNoteFragment())
                        .addToBackStack(null)
                        .commit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}