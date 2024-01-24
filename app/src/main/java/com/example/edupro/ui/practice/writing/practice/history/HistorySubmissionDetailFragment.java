package com.example.edupro.ui.practice.writing.practice.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.edupro.databinding.WritingHistoryResultDetailBinding;

public class HistorySubmissionDetailFragment extends Fragment {
    private static final String TAG = "HistorySubmissionDetailFragment";
    private WritingHistoryResultDetailBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = WritingHistoryResultDetailBinding.inflate(inflater, container, false);

        if (getArguments() != null) {
            String content = getArguments().getString("your_answer");
            String grade = getArguments().getString("grade");
            String explanation = getArguments().getString("explanation");
            binding.writingResultYourAnswer.setText(content);
            binding.writingResultGrade.setText(grade);
            binding.writingResultExplanation.setText(explanation);
        }

        binding.writingResultBackButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigateUp();
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
