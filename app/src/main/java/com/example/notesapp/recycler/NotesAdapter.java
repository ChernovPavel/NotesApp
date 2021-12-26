package com.example.notesapp.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.R;
import com.example.notesapp.data.Note;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NoteHolder> {

    private List<Note> notes = new ArrayList<>();

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public interface onNoteClickListener {
        void onNoteClick(Note note);
    }

    private onNoteClickListener listener;

    public void setOnNoteClickListener(onNoteClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.note_item, parent, false);
        return new NoteHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        holder.bind(notes.get(position));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }
}
