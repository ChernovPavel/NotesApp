package com.example.notesapp.data;

import java.util.ArrayList;

public interface Repo {

    int create(Note note);

    Note read(int id);

    void update(Note note);

    void delete(int id);

    void fill(ArrayList<Note> notes);

    ArrayList<Note> getAll();
}
