package com.example.notesapp.data;

import java.util.ArrayList;

public class InMemoryRepoImpl implements Repo {

    private static InMemoryRepoImpl repo;
    private ArrayList<Note> notes = new ArrayList<>();
    private int counter = 0;

    private InMemoryRepoImpl() {

    }

    public static Repo getInstance() {
        if (repo == null) {
            repo = new InMemoryRepoImpl();
        }
        return repo;
    }

    @Override
    public int create(Note note) {
        int id = counter++;
        note.setId(id);
        notes.add(note);
        return id;
    }

    @Override
    public Note read(int id) {
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getId() == id)
                return notes.get(i);
        }
        return null;
    }

    @Override
    public void update(Note note) {
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getId().equals(note.getId())) {
                notes.set(i, note);
                break;
            }
        }
    }

    @Override
    public void delete(int id) {
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getId() == id) {
                notes.remove(i);
                break;
            }
        }
    }

    @Override
    public ArrayList<Note> getAll() {
        return notes;
    }

    @Override
    public void fill(ArrayList<Note> notes) {
        this.notes = notes;
    }
}
