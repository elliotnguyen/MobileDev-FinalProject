package com.example.edupro.ui.practice.writing.practice.write;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.edupro.R;
import com.example.edupro.model.writing.WritingDto;
import com.example.edupro.ui.practice.writing.practice.WritingPracticeViewModel;

public class WriteAnswerFragment extends Fragment {
    private static final String TAG = "WriteAnswerFragment";
    private WritingDto writingDto = new WritingDto();
    private WritingPracticeViewModel writingPracticeViewModel;
    private TextView writingQuestion;

    private TextView writingAnswer;
    private RelativeLayout writingAnswerButton;
    private TextView sampleAnswer;
    private RelativeLayout sampleAnswerButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View writeAnswer = inflater.inflate(R.layout.fragment_writing_practice_question, container, false);

        writingQuestion = writeAnswer.findViewById(R.id.writing_practice_question);

        writingPracticeViewModel = new ViewModelProvider(requireParentFragment()).get(WritingPracticeViewModel.class);
        observeAnyChange();

//        writingAnswer = writeAnswer.findViewById(R.id.writing_practice_write_your_answer);
//        writingAnswer.setOnClickListener(v -> {
//            writingPracticeViewModel.setIsSampleAnswerShow(false);
//        });
//        sampleAnswer = writeAnswer.findViewById(R.id.writing_practice_best_answer);
//        sampleAnswer.setOnClickListener(v -> {
//            writingPracticeViewModel.setIsSampleAnswerShow(true);
//        });
        handleSessionShow(writeAnswer);

        return writeAnswer;
    }

    private void handleSessionShow(View writeAnswer) {
        writingAnswerButton = writeAnswer.findViewById(R.id.writing_practice_writeAnswerButton);
        writingAnswer = writeAnswer.findViewById(R.id.writing_practice_write_your_answer);
        writingAnswer.setOnClickListener(v -> {
            writingPracticeViewModel.setIsSampleAnswerShow(false);
        });
        sampleAnswerButton = writeAnswer.findViewById(R.id.writing_bestAnswerButton);
        sampleAnswer = writeAnswer.findViewById(R.id.writing_practice_best_answer);
        sampleAnswer.setOnClickListener(v -> {
            writingPracticeViewModel.setIsSampleAnswerShow(true);
        });

        writingPracticeViewModel.getIsSampleAnswerShow().observe(getViewLifecycleOwner(), isSampleAnswerShow -> {
            Fragment fragment = isSampleAnswerShow ? new SampleAnswerFragment() : new WritingAnswerFragment();

            if (isSampleAnswerShow) {
                writingAnswerButton.setBackgroundResource(R.drawable.fragment_writing_practice_write_answer_button_background);
                sampleAnswerButton.setBackgroundResource(R.drawable.fragment_writing_practice_best_answer_button_selected_background);
            } else {
                writingAnswerButton.setBackgroundResource(R.drawable.fragment_writing_practice_write_answer_button_selected_background);
                sampleAnswerButton.setBackgroundResource(R.drawable.fragment_writing_practice_best_answer_button_background);
            }

            getChildFragmentManager().beginTransaction()
                    .replace(R.id.writing_practice_write_answer_container, fragment)
                    .commit();
        });
    }

    private void observeAnyChange() {
        writingPracticeViewModel.getWritingDto().observe(getViewLifecycleOwner(), writingDto -> {
            if (writingDto != null) {
                this.writingDto = writingDto;
                writingQuestion.setText(this.writingDto.getQuestion());
            }
        });
    }
}
