package com.example.notesapp.ui;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.notesapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DatePickerFragment extends Fragment {

    private DatePicker datePicker;
    private Button confirmDate;
    private String date;
    private OnConfirmDateBtnClickListener listener;

    //метод который проверяет что фрагмент из которого открылся данный дочерний фрагмент реализует интерфейс
    public void onAttachToParentFragment(Fragment fragment) {
        try {
            listener = (OnConfirmDateBtnClickListener) fragment;

        } catch (ClassCastException e) {
            throw new ClassCastException(
                    fragment.toString() + " must implement OnConfirmDateBtnListener");
        }
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //вызываем созданный выше метод
        onAttachToParentFragment(getParentFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_date_picker, container, false);
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull @org.jetbrains.annotations.NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        datePicker = view.findViewById(R.id.date_picker);
        confirmDate = view.findViewById(R.id.confirm_date);
        date = new SimpleDateFormat("d.M.yyyy").format(Calendar.getInstance().getTime());

        datePicker.setOnDateChangedListener((datePicker, year, month, dayOfMonth) ->
                date = String.format(Locale.ENGLISH, "%d.%d.%d", dayOfMonth, month + 1, year));

        confirmDate.setOnClickListener(view1 -> {
            getParentFragmentManager().popBackStack();
            listener.confirmDateBtnPressed(date);
        });
    }

    //коллбэк который имплементит EditNoteFragment чтобы там релизовать логику при подтверждении даты в этом врагменте датапикера
    interface OnConfirmDateBtnClickListener {
        void confirmDateBtnPressed(String date);
    }
}