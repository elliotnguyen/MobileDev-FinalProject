package com.example.edupro.ui.practice.listening.practice.question;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.edupro.R;
import com.example.edupro.databinding.FragmentListeningMCQBinding;
import com.example.edupro.databinding.FragmentListeningTFNGBinding;
import com.example.edupro.model.reading.MCQQuestion;
import com.example.edupro.model.reading.Question;
import com.example.edupro.model.reading.TFNGQuestion;
import com.example.edupro.ui.practice.listening.practice.ListeningPracticeViewModel;
import com.example.edupro.ui.practice.reading.practice.ReadingPracticeViewModel;
import com.example.edupro.ui.practice.reading.practice.question.MCQClickInterface;

import java.util.ArrayList;

public class ListeningMCQFragment extends Fragment {

    private FragmentListeningMCQBinding binding;
    private final int index;
    private final ArrayList<MCQQuestion> mcqQuestions = new ArrayList<>();
    private ListeningPracticeViewModel listeningPracticeViewModel;

    public ListeningMCQFragment(ArrayList<Question> mcqQuestions, int index) {
        for (Question question : mcqQuestions) {
            this.mcqQuestions.add((MCQQuestion) question);
        }
        this.index = index;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListeningMCQBinding.inflate(inflater, container, false);

        Fragment parentFragment = getParentFragment();

        if (parentFragment != null && parentFragment.getParentFragment() != null) {
            listeningPracticeViewModel = new ViewModelProvider(parentFragment.getParentFragment()).get(ListeningPracticeViewModel.class);
        }

        binding.listeningMcqRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        MCQListAdapter mcqListAdapter = new MCQListAdapter(mcqQuestions, this.index, listeningPracticeViewModel, new MCQClickInterface() {
            @Override
            public void onItemClick(int position, String option) {
                listeningPracticeViewModel.setAnswerAtIndex(position + index, option);
            }
        });
        binding.listeningMcqRecyclerView.setAdapter(mcqListAdapter);

        return binding.getRoot();
    }
}