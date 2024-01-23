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
import com.example.edupro.model.question.MCQQuestion;
import com.example.edupro.model.question.Question;
import com.example.edupro.ui.practice.reading.practice.ReadingPracticeViewModel;

import java.util.ArrayList;

public class ReadingMCQFragment extends Fragment {
    private static final String TAG = "ReadingMCQFragment";
    private final int index;
    private final ArrayList<MCQQuestion> mcqQuestions = new ArrayList<>();
    private ReadingPracticeViewModel readingPracticeViewModel;
    public ReadingMCQFragment(ArrayList<Question> mcqQuestions, int index) {
        for (Question question : mcqQuestions) {
            this.mcqQuestions.add((MCQQuestion) question);
        }
        this.index = index;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View readingMCQ = inflater.inflate(R.layout.fragment_reading_mcq, container, false);

        Fragment parentFragment = getParentFragment();
        if (parentFragment != null && parentFragment.getParentFragment() != null) {
            readingPracticeViewModel = new ViewModelProvider(parentFragment.getParentFragment()).get(ReadingPracticeViewModel.class);
        }

        RecyclerView mcqRecyclerView = readingMCQ.findViewById(R.id.reading_mcq_recycler_view);
        mcqRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        MCQListAdapter mcqListAdapter = new MCQListAdapter(mcqQuestions, this.index, readingPracticeViewModel, new MCQClickInterface() {
            @Override
            public void onItemClick(int position, String option) {
                readingPracticeViewModel.setAnswerAtIndex(position + index, option);
            }
        });
        mcqRecyclerView.setAdapter(mcqListAdapter);
        return readingMCQ;
    }
}
