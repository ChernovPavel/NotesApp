package com.example.notesapp.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.R;
import com.example.notesapp.data.InMemoryRepoImpl;
import com.example.notesapp.data.Note;
import com.example.notesapp.data.PopupMenuItemClickListener;
import com.example.notesapp.data.Repo;
import com.example.notesapp.recycler.NoteHolder;
import com.example.notesapp.recycler.NotesAdapter;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class NotesListFragment extends Fragment implements NotesAdapter.onNoteClickListener, PopupMenuItemClickListener {

    public static final String KEY = "KEY";
    private static final String ID_NOTE = "ID_NOTE";
    public static ArrayList<Note> notesList = new ArrayList<>();
    private final Repo repository = InMemoryRepoImpl.getInstance();
    private NotesAdapter adapter;
    private SharedPreferences prefs = null;

    // метод который нужен чтобы использовать адаптер в главной активити и через объект адаптера вызвать setNotes()
    NotesAdapter getAdapter() {
        return adapter;
    }

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

        adapter = new NotesAdapter();

        adapter.setOnNoteClickListener(this);
        adapter.setPopupMenuItemClickListener(this);

        RecyclerView recyclerView = view.findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);

        prefs = getActivity().getPreferences(Context.MODE_PRIVATE);

        //получаем строку со всеми заметками из префов
        String savedNotes = prefs.getString(KEY, null);

        //получаем каунтер из префов и устанавливаем его в репозиторий. Иначе при закрытии аппа каунтер
        //обнулится и id заметок будут повторяться
        int counterFromPrefs = prefs.getInt(ID_NOTE, 0);
        repository.setCounter(counterFromPrefs);

        if (savedNotes == null || savedNotes.isEmpty()) {
            Toast.makeText(getContext(), "Пустой список", Toast.LENGTH_SHORT).show();
        } else {
            try {

                //приводим строку к листу объектов заметок
                Type type = new TypeToken<ArrayList<Note>>() {
                }.getType();

                notesList = new GsonBuilder().create().fromJson(savedNotes, type);
            } catch (JsonSyntaxException e) {
                Toast.makeText(getContext(), "Ошибка трансформации", Toast.LENGTH_SHORT).show();
            }
        }
        adapter.setNotes(notesList);
        if (repository.getAll().isEmpty()) repository.fill(notesList);

        // реализация удаления элемента путем свайпа влево или вправо на нем
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(0, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                NoteHolder holder = (NoteHolder) viewHolder;
                Note note = holder.getNote();
                repository.delete(note.getId());
                adapter.delete(repository.getAll(), position);

                //переводим в json-сторку все заметки и сохраняем их в префы
                String jsonNotes = new GsonBuilder().create().toJson(repository.getAll());
                prefs.edit().putString(KEY, jsonNotes).apply();

            }
        });
        helper.attachToRecyclerView(recyclerView);
    }

    /*
        Этот фрагмент реализует интерфейс и является слушателем клика по холдеру.
        А так как интерфейс создан в классе адаптера, то адаптер получает объект слушателя и передает его
        в конструктор холдера и холдер уже при нажатии на элемент вызывает коллбек тут (во фрагменте)
        и фрагмент решает что делать по этому клику
    */
    @Override
    public void onNoteClick(Note note) {
        // запустить фрагмент изменения заметки
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, EditNoteFragment.getInstance(note))
                .addToBackStack(null)
                .commit();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void click(int command, Note note, int position) {
        switch (command) {
            // реализация слушателя при нажатии на пункты контекстного меню на каждом элементе
            case R.id.context_delete:
                repository.delete(note.getId());

                // говорим адаптеру чтобы он удалил заметку из массива и оповестил всех наблюдателей что элемент удален
                adapter.delete(repository.getAll(), position);
                //переводим в json-сторку все заметки и сохраняем их в префы
                String jsonNotes = new GsonBuilder().create().toJson(repository.getAll());
                prefs.edit().putString(KEY, jsonNotes).apply();
                return;

            case R.id.context_modify:
                NotesDialog.getInstance(note).show(requireActivity().getSupportFragmentManager(), NotesDialog.NOTE);
        }
    }
}