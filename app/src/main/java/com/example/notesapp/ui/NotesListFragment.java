package com.example.notesapp.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.notesapp.R;
import com.example.notesapp.data.Constants;
import com.example.notesapp.data.InMemoryRepoImpl;
import com.example.notesapp.data.Note;
import com.example.notesapp.data.Repo;
import com.example.notesapp.recycler.NotesAdapter;

public class NotesListFragment extends Fragment implements NotesAdapter.onNoteClickListener {

    private Repo repository = InMemoryRepoImpl.getInstance();
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
        repository.create(new Note("Title 1", "Description 1"));
        repository.create(new Note("Title 2", "Description 2"));
        repository.create(new Note("Title 3", "Description 3"));
        repository.create(new Note("Title 4", "Description 4"));
        repository.create(new Note("Title 5", "Description 5"));
        repository.create(new Note("Title 6", "Description 6"));
        repository.create(new Note("Title 7", "Description 7"));
        repository.create(new Note("Title 8", "Description 8"));
        repository.create(new Note("Title 9", "Description 9"));
        repository.create(new Note("Title 10", "Description 10"));
        repository.create(new Note("Title 11", "Description 11"));
        repository.create(new Note("Title 12", "Description 12"));
        repository.create(new Note("Title 13", "Description 13"));
        repository.create(new Note("Title 14", "Description 14"));
        repository.create(new Note("Title 15", "Description 15"));
        repository.create(new Note("Title 16", "Description 16"));
    }

    @Override
    public void onNoteClick(Note note) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, EditNoteFragment.getInstance(note))
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
                        .add(R.id.fragment_container, new EditNoteFragment())
                        .addToBackStack(null)
                        .commit();


                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}