package com.example.notesapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notesapp.R;
import com.example.notesapp.data.Constants;
import com.example.notesapp.data.InMemoryRepoImpl;
import com.example.notesapp.data.Note;
import com.example.notesapp.data.Repo;

public class EditNoteActivity extends AppCompatActivity {

    private EditText title;
    private EditText description;
    private Button saveNote;
    private int id = -1;
    private Note note = null;
    Repo repository = InMemoryRepoImpl.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        title = findViewById(R.id.edit_note_title);
        description = findViewById(R.id.edit_note_description);
        saveNote = findViewById(R.id.edit_note_update);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Constants.NOTE)) {
            note = (Note) intent.getSerializableExtra(Constants.NOTE);
            id = note.getId();
            title.setText(note.getTitle());
            description.setText(note.getDescription());
        }

        saveNote.setOnClickListener(view -> {
            if (note == null) {
                int newNoteId = repository.create(new Note(title.getText().toString(), description.getText().toString()));
                note = repository.read(newNoteId);
            } else {
                note.setTitle(title.getText().toString());
                note.setDescription(description.getText().toString());
                repository.update(note);
            }

            Intent intentForNotesListActivity = new Intent(this, NotesListActivity.class);
            intentForNotesListActivity.putExtra(Constants.NOTE, note);
            startActivity(intentForNotesListActivity);
        });
    }
}
