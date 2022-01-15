package com.example.notesapp.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.R;
import com.example.notesapp.data.Note;
import com.example.notesapp.data.PopupMenuItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NoteHolder> {

    private List<Note> notes = new ArrayList<>();
    private onNoteClickListener listener;
    private PopupMenuItemClickListener popupMenuListener;

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    //инициализируем listener из фрагмента который реализует интерфейс onNoteClickListener
    public void setOnNoteClickListener(onNoteClickListener listener) {
        this.listener = listener;
    }

    //аналогично слушателю выше, инициализируем слушатель нажатия на попап меню (который реализует интерфейс onNoteClickListener)
    public void setPopupMenuItemClickListener(PopupMenuItemClickListener popupMenuListener) {
        this.popupMenuListener = popupMenuListener;
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.note_item, parent, false);

        // listener который получили из фрагмента списка заметок передаем в конструктор холдера
        // в холдере вызываем на listener'e единственный метод onNoteClick и обрабатываем его во фрагменте со список заметок
        return new NoteHolder(view, listener, popupMenuListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        holder.bind(notes.get(position));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void delete(List<Note> all, int position) {
        this.notes = all;
        notifyItemRemoved(position);
    }

    // коллбэк нажатия на заметку. Его реализует фрагмент на котором отображаем все заметки (ресайклер)
    public interface onNoteClickListener {
        void onNoteClick(Note note);
    }
}
