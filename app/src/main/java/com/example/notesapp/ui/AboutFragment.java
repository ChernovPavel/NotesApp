package com.example.notesapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.notesapp.R;

public class AboutFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            ((MainActivity) requireActivity()).getNavBar().setVisibility(View.GONE);
        }
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onStop() {
        ((MainActivity) requireActivity()).getNavBar().setVisibility(View.VISIBLE);
        super.onStop();
    }
}