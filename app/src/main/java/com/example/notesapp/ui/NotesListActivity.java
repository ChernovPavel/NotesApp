package com.example.notesapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.notesapp.R;
import com.example.notesapp.data.Note;
import com.example.notesapp.data.Repo;
import com.example.notesapp.data.inMemoryRepoImpl;
import com.example.notesapp.recycler.NotesAdapter;

public class NotesListActivity extends AppCompatActivity {

   private Repo repository = new inMemoryRepoImpl();
   private RecyclerView recyclerView;
   private NotesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        fillRepo();

        adapter = new NotesAdapter();
        adapter.setNotes(repository.getAll());

        recyclerView = findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
}