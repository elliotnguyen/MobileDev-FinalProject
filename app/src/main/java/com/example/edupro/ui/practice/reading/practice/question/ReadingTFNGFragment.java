package com.example.edupro.ui.practice.reading.practice.question;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edupro.R;
import com.example.edupro.model.question.Question;
import com.example.edupro.model.question.TFNGQuestion;
import com.example.edupro.ui.practice.reading.practice.ReadingPracticeViewModel;

import java.util.ArrayList;

public class ReadingTFNGFragment extends Fragment {
    private static final String TAG = "ReadingTFNGFragment";
    private final int index;
    private final ArrayList<TFNGQuestion> tfngQuestions = new ArrayList<>();

    private ReadingPracticeViewModel readingPracticeViewModel;
    public ReadingTFNGFragment(ArrayList<Question> tfngQuestions, int index) {
        for (Question question : tfngQuestions) {
            this.tfngQuestions.add((TFNGQuestion) question);
        }
        this.index = index;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View readingTFNG = inflater.inflate(R.layout.fragment_reading_tfng, container, false);


        Fragment parentFragment = getParentFragment();
        if (parentFragment != null && parentFragment.getParentFragment() != null) {
            readingPracticeViewModel = new ViewModelProvider(parentFragment.getParentFragment()).get(ReadingPracticeViewModel.class);
        }

        RecyclerView tfngRecyclerView = readingTFNG.findViewById(R.id.reading_tfng_recycler_view);
        tfngRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        TFNGListAdapter tfngListAdapter = new TFNGListAdapter(tfngQuestions, this.index, readingPracticeViewModel, new MCQClickInterface() {
            @Override
            public void onItemClick(int position, String option) {
                readingPracticeViewModel.setAnswerAtIndex(position + index, option);
            }
        });
        tfngRecyclerView.setAdapter(tfngListAdapter);
        return readingTFNG;
    }
}
