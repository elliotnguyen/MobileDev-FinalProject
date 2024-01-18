package com.example.edupro.ui.practice.writing.practice.write;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.material.textfield.TextInputEditText;

public class WritingAnswerFragment extends Fragment {
    private static final String TAG = "WritingAnswerFragment";
    private WritingPracticeViewModel writingPracticeViewModel;
    private TextView wordCountTextView;
    private TextInputEditText writingAnswerEditText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View writingAnswer = inflater.inflate(R.layout.fragment_writing_practice_question_write, container, false);

        Fragment parentFragment = getParentFragment();
        if (parentFragment != null && parentFragment.getParentFragment() != null) {
            writingPracticeViewModel = new ViewModelProvider(parentFragment.getParentFragment()).get(WritingPracticeViewModel.class);
        }

        //observeAnyChange();

        handleWritingAnswer(writingAnswer);

        return writingAnswer;
    }

//    private void observeAnyChange() {
//        writingPracticeViewModel.getCurrentAnswer().observe(getViewLifecycleOwner(), currentAnswer -> {
//            if (currentAnswer != null) {
//                writingAnswerEditText.setText(currentAnswer);
//            }
//        });
//    }

    private void handleWritingAnswer(View writingAnswer) {
        writingAnswerEditText = writingAnswer.findViewById(R.id.writing_practice_question_write_edit_text);
        writingAnswerEditText.setText(writingPracticeViewModel.getCurrentAnswer().getValue());

        wordCountTextView = writingAnswer.findViewById(R.id.writing_practice_count_words_2);
        if (writingAnswerEditText.getText() != null) {
            int wordCount = countWords(writingAnswerEditText.getText().toString());
            wordCountTextView.setText(String.valueOf(wordCount));
        }
        else {
            wordCountTextView.setText("0");
        }

        writingAnswerEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                String text = writingAnswerEditText.getText().toString();
//                String[] words = text.trim().split("\\s+");
//
//                int wordCount = words.length;
                int wordCount = countWords(text);
                wordCountTextView.setText(String.valueOf(wordCount));
                writingPracticeViewModel.setCurrentAnswer(text);
            }
        });
    }

    private int countWords(String s) {
        String trim = s.trim();
        if (trim.isEmpty())
            return 0;
        return trim.split("\\s+").length;
    }
}
