package com.example.edupro.ui.practice.writing.practice.write;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.edupro.R;


import com.example.edupro.ui.practice.writing.practice.WritingPracticeViewModel;

public class SampleAnswerFragment extends Fragment {
    private static final String TAG = "SampleAnswerFragment";
    private WritingPracticeViewModel writingPracticeViewModel;
    private TextView sampleAnswerTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View sampleAnswer = inflater.inflate(com.example.edupro.R.layout.fragment_writing_practice_best_answer, container, false);

        Fragment parentFragment = getParentFragment();
        if (parentFragment != null && parentFragment.getParentFragment() != null) {
            writingPracticeViewModel = new ViewModelProvider(parentFragment.getParentFragment()).get(WritingPracticeViewModel.class);
        }

        sampleAnswerTextView = sampleAnswer.findViewById(R.id.writing_practice_best_answer_text_view);

        observeAnyChange();

        return sampleAnswer;
    }

    private void observeAnyChange() {
        writingPracticeViewModel.getWritingDto().observe(getViewLifecycleOwner(), writingDto -> {
            if (writingDto != null) {
                String sampleAnswer = writingDto.getSampleAnswer();
                sampleAnswerTextView.setText(sampleAnswer.replace("\\n", "\n"));
            }
        });
    }
}
