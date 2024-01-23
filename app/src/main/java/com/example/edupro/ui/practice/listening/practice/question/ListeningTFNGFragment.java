package com.example.edupro.ui.practice.listening.practice.question;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.edupro.databinding.FragmentListeningTFNGBinding;
import com.example.edupro.model.question.Question;
import com.example.edupro.model.question.TFNGQuestion;
import com.example.edupro.ui.practice.listening.practice.ListeningPracticeViewModel;
import com.example.edupro.ui.practice.reading.practice.question.MCQClickInterface;

import java.util.ArrayList;

public class ListeningTFNGFragment extends Fragment {

    private FragmentListeningTFNGBinding binding;
    private final int index;
    private final ArrayList<TFNGQuestion> tfngQuestions = new ArrayList<>();
    private ListeningPracticeViewModel listeningPracticeViewModel;

    public ListeningTFNGFragment(ArrayList<Question> tfngQuestions, int index) {
        for (Question question : tfngQuestions) {
            Log.d("TAG", "ListeningTFNGFragment: " + question.getContent());
            this.tfngQuestions.add((TFNGQuestion) question);
        }
        this.index = index;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListeningTFNGBinding.inflate(inflater, container, false);

        Fragment parentFragment = getParentFragment();

        if (parentFragment != null && parentFragment.getParentFragment() != null) {
            listeningPracticeViewModel = new ViewModelProvider(parentFragment.getParentFragment()).get(ListeningPracticeViewModel.class);
        }

        binding.listeningTfngRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        TFNGListAdapter tfngListAdapter = new TFNGListAdapter(tfngQuestions, this.index, listeningPracticeViewModel, new MCQClickInterface() {
            @Override
            public void onItemClick(int position, String option) {
                listeningPracticeViewModel.setAnswerAtIndex(position + index, option);
            }
        });
        binding.listeningTfngRecyclerView.setAdapter(tfngListAdapter);

        return binding.getRoot();
    }
}