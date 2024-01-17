package com.example.edupro.ui.practice.reading.practice.passage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edupro.R;
import com.example.edupro.model.ReadingDto;
import com.example.edupro.ui.practice.reading.practice.ReadingPracticeViewModel;

public class ReadingPassageFragment extends Fragment {
    private static final String TAG = "ReadingPassageFragment";
    private ReadingDto readingDto = null;
    private ReadingPracticeViewModel readingViewModel;
    private RecyclerView readingPassageRecyclerView;
    private RecyclerView.Adapter readingPassageAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View readingPassage = inflater.inflate(R.layout.fragment_reading_practice_passages, container, false);
        readingPassageRecyclerView = readingPassage.findViewById(R.id.reading_practice_passages_recycler_view);
        configureReadingPassageRecyclerView();
//        if (getArguments() != null) {
//            String readingId = getArguments().getString("readingId");
//            readingViewModel = new ReadingPracticeViewModel();
//            readingViewModel.getReadingById(readingId);
//        }
        readingViewModel = new ViewModelProvider(this).get(ReadingPracticeViewModel.class);
        ObserverAnyChange();
        return readingPassage;
    }

    private void configureReadingPassageRecyclerView() {
        readingPassageAdapter = new ReadingPassageListAdapter(readingDto.getContent());
        readingPassageRecyclerView.setAdapter(readingPassageAdapter);
    }

    private void ObserverAnyChange() {
        readingViewModel.getReading().observe(getViewLifecycleOwner(), readingDto -> {
            this.readingDto = readingDto;
            readingPassageAdapter.notifyDataSetChanged();
        });
    }
}