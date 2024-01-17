package com.example.edupro.ui.practice.reading.practice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edupro.R;
import com.example.edupro.model.reading.Question;
import com.example.edupro.model.reading.ReadingDto;
import com.example.edupro.ui.RecyclerViewClickInterface;
import com.example.edupro.ui.practice.reading.practice.passage.ReadingPassageFragment;
import com.example.edupro.ui.practice.reading.practice.question.ReadingQuestionFragment;

import java.util.ArrayList;

public class ReadingPracticeFragment extends Fragment {
    private static final String TAG = "ReadingPracticeFragment";
    private ReadingDto readingDto = new ReadingDto();
    private ReadingPracticeViewModel readingViewModel;
    private TextView readingPassage;
    private TextView readingQuestion;
    private Button submitButton;
    private RecyclerView readingQuestionRecyclerView;
    private RecyclerView.Adapter readingQuestionAdapter;
    public static ReadingPracticeFragment newInstance() {
        return new ReadingPracticeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View readingPractice = inflater.inflate(R.layout.fragment_reading_practice, container, false);

        readingQuestionRecyclerView = readingPractice.findViewById(R.id.practice_question_recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 6);
        readingQuestionRecyclerView.setLayoutManager(gridLayoutManager);

        readingViewModel = new ViewModelProvider(this).get(ReadingPracticeViewModel.class);
        readingViewModel.init();

        if (getArguments() != null) {
            String readingId = getArguments().getString("readingId");
            readingViewModel.setReadingId(readingId);
        }

        ObserverAnyChange();

        readingPassage = readingPractice.findViewById(R.id.reading_practice_passage_button);
        readingPassage.setOnClickListener(v -> readingViewModel.setIsPassageShow(true));
        readingQuestion = readingPractice.findViewById(R.id.reading_practice_question_button);
        readingQuestion.setOnClickListener(v -> readingViewModel.setIsPassageShow(false));

        handleSessionShow();
        handleBottomSheet();

        submitButton = readingPractice.findViewById(R.id.reading_practice_submit_button);
        submitButton.setOnClickListener(v -> handleSubmit());

        return readingPractice;
    }

    private void ObserverAnyChange() {
        readingViewModel.getReading().observe(getViewLifecycleOwner(), readingDto -> {
            this.readingDto = readingDto;
            ArrayList<Question> part1 = this.readingDto.getQuestions().get(0).getQuestions();
            ArrayList<Question> part2 = this.readingDto.getQuestions().get(1).getQuestions();

            readingViewModel.setNumberOfQuestions(part1.size() + part2.size());
        });
    }

    private void handleSessionShow() {
        readingViewModel.getIsPassageShow().observe(getViewLifecycleOwner(), isPassageShow -> {
            Fragment newFragment = isPassageShow ? new ReadingPassageFragment() : new ReadingQuestionFragment();
            if (isPassageShow) {
                readingPassage.setBackgroundResource(R.drawable.reading_passage_button_selected);
                readingQuestion.setBackgroundResource(R.drawable.reading_passage_button_unselected);
            } else {
                readingPassage.setBackgroundResource(R.drawable.reading_passage_button_unselected);
                readingQuestion.setBackgroundResource(R.drawable.reading_passage_button_selected);
            }
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.reading_practice_passage_container, newFragment)
                    .commit();
        });
    }

    private void handleBottomSheet() {
        readingViewModel.getNumberOfQuestions().observe(getViewLifecycleOwner(), numberOfQuestions -> {
            if (numberOfQuestions != 0) {
                ArrayList<String> questions = new ArrayList<>();
                for (int i = 0; i < numberOfQuestions; i++) {
                    questions.add("");
                }
                readingQuestionAdapter = new ReadingQuestionListAdapter(questions, new RecyclerViewClickInterface() {
                    @Override
                    public void onItemClick(int position) {

                    }

                    @Override
                    public void onLongItemClick(int position) {

                    }
                });
                readingQuestionRecyclerView.setAdapter(readingQuestionAdapter);
                for (int i = 0; i < numberOfQuestions; i++) {
                    int finalI = i;
                    readingViewModel.getAnswerAtIndex(i).observe(getViewLifecycleOwner(), answer -> {
                        if (answer != null && !answer.equals("")) {
                            questions.set(finalI, "Q" + (finalI + 1));
                            ((ReadingQuestionListAdapter) readingQuestionAdapter).notifyItemChanged(finalI);
                        }
                    });
                }
            }
        });
    }

    private void handleSubmit() {

    }
}
