package com.example.edupro.ui.practice.reading;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.edupro.R;
import com.example.edupro.model.ReadingDto;

import java.util.ArrayList;

public class ReadingFragment extends Fragment {
    private static final String TAG = "ReadingFragment";
    private ReadingViewModel readingViewModel;
    private ArrayList<ReadingDto> readings = new ArrayList<>();
    public static ReadingFragment newInstance() {
        return new ReadingFragment();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View readingHome = inflater.inflate(R.layout.fragment_reading_home, container, false);

        readingViewModel = new ReadingViewModel();
        readingViewModel.getAllReading();
        ObserverAnyChange();
        return readingHome;
    }
    private void ObserverAnyChange() {
        readingViewModel.getAllReadingList().observe(getViewLifecycleOwner(), readingDtos -> {
            readings.clear();
            readings.addAll(readingDtos);
        });
    }
}
