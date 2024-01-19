package com.example.edupro.ui.practice.reading.practice.question;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.edupro.R;
import com.example.edupro.model.reading.Question;
import com.example.edupro.model.reading.ReadingDto;
import com.example.edupro.ui.practice.reading.practice.ReadingPracticeViewModel;

import java.util.ArrayList;

public class ReadingQuestionFragment extends Fragment {
    private static final String TAG = "ReadingQuestionFragment";
    private ReadingDto readingDto = new ReadingDto();
    private ReadingPracticeViewModel readingViewModel;
    //private ReadingQuestionViewModel readingQuestionViewModel;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View readingQuestion = inflater.inflate(R.layout.fragment_reading_practice_questions, container, false);
        readingViewModel = new ViewModelProvider(requireParentFragment()).get(ReadingPracticeViewModel.class);

        //readingQuestionViewModel = new ViewModelProvider(this).get(ReadingQuestionViewModel.class);
        //readingQuestionViewModel.init();
        ObserverAnyChange();
        return readingQuestion;
    }

    private void ObserverAnyChange() {
        readingViewModel.getReading().observe(getViewLifecycleOwner(), readingDto -> {
            this.readingDto = readingDto;

            ArrayList<Question> part1 = this.readingDto.getQuestions().get(0).getQuestions();
            ArrayList<Question> part2 = this.readingDto.getQuestions().get(1).getQuestions();

            //readingViewModel.setNumberOfQuestions(part1.size() + part2.size());

            handleQuestionShow(R.id.reading_practice_question_part1, this.readingDto.getType().get(0), part1,0);
            handleQuestionShow(R.id.reading_practice_question_part2, this.readingDto.getType().get(1), part2, part1.size());
        });
    }

    private void handleQuestionShow(int layoutId, Long type, ArrayList<Question> questions, int index) {
        Fragment fragmentLayout;
        if (type == 0) {
            fragmentLayout = new ReadingTFNGFragment(questions, index);
        } else if (type == 1) {
            fragmentLayout = new ReadingMCQFragment(questions, index);
        } else {
            fragmentLayout = new ReadingCompleteFormFragment();
        }
        getChildFragmentManager().beginTransaction()
                .replace(layoutId, fragmentLayout)
                .commit();
    }
}
