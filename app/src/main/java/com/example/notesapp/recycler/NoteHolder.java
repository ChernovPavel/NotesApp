package com.example.notesapp.recycler;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.R;
import com.example.notesapp.data.Note;

public class NoteHolder extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {

    private final TextView title;
    private final TextView description;
    private ImageView itemMenu;
    private Note note;
    private PopupMenu popupMenu;

    public NoteHolder(@NonNull View itemView, NotesAdapter.onNoteClickListener listener) {
        super(itemView);
        title = itemView.findViewById(R.id.note_title);
        description = itemView.findViewById(R.id.note_description);
        itemMenu = itemView.findViewById(R.id.item_menu);
        itemView.setOnClickListener(view -> listener.onNoteClick(note));

        popupMenu = new PopupMenu(itemView.getContext(), itemMenu);
        popupMenu.inflate(R.menu.context);

        itemMenu.setOnClickListener(v -> popupMenu.show());

        popupMenu.setOnMenuItemClickListener(this);

    }

    void bind(Note note) {
        this.note = note;
        title.setText(note.getTitle());
        description.setText(note.getDescription());

    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.context_delete:
                //TODO
                return true;
            case R.id.context_modify:
                //TODO
                return true;
            default:
                return false;
        }

    }
}
