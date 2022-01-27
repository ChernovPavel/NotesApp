package com.example.notesapp.data;

import java.io.Serializable;

public class Note implements Serializable {
    private Integer id;
    private String title;
    private String description;
    private NoteImportance importance;
    private String date;

    public Note(String title, String description, NoteImportance importance, String date) {
        this.title = title;
        this.description = description;
        this.importance = importance;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public NoteImportance getImportance() {
        return importance;
    }

    public void setImportance(NoteImportance importance) {
        this.importance = importance;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public enum NoteImportance {
        HIGH,
        MEDIUM,
        LOW
    }
}
