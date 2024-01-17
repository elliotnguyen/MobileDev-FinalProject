package com.example.edupro.ui.practice.reading.practice.passage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edupro.R;
import com.example.edupro.model.reading.ReadingDto;
import com.example.edupro.ui.practice.reading.practice.ReadingPracticeViewModel;

public class ReadingPassageFragment extends Fragment {
    private static final String TAG = "ReadingPassageFragment";
    private ReadingDto readingDto = new ReadingDto();
    private TextView readingTitle;
    private ReadingPracticeViewModel readingViewModel;
    private RecyclerView readingPassageRecyclerView;
    private RecyclerView.Adapter readingPassageAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View readingPassage = inflater.inflate(R.layout.fragment_reading_practice_passages, container, false);

        readingTitle = readingPassage.findViewById(R.id.reading_practice_passage_title);

        readingPassageRecyclerView = readingPassage.findViewById(R.id.reading_practice_passages_recycler_view);
        readingPassageRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        readingViewModel = new ViewModelProvider(requireParentFragment()).get(ReadingPracticeViewModel.class);
//        readingViewModel.getReadingId().observe(getViewLifecycleOwner(), readingId -> {
//            if (readingId != null) {
//                readingViewModel.getReadingById(readingId);
//            }
//        });
        ObserverAnyChange();
        return readingPassage;
    }

    private void configureReadingPassageRecyclerView() {
        readingPassageAdapter = new ReadingPassageListAdapter(this.readingDto);
        readingPassageRecyclerView.setAdapter(readingPassageAdapter);
    }

    private void ObserverAnyChange() {
        readingViewModel.getReading().observe(getViewLifecycleOwner(), readingDto -> {
            this.readingDto = readingDto;
            readingTitle.setText(readingDto.getTitle());
            configureReadingPassageRecyclerView();
        });
    }
}