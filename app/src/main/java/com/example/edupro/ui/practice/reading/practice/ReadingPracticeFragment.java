package com.example.edupro.ui.practice.reading.practice;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edupro.R;
import com.example.edupro.model.reading.Question;
import com.example.edupro.model.reading.ReadingDto;
import com.example.edupro.ui.RecyclerViewClickInterface;
import com.example.edupro.ui.dialog.SweetAlertDialog;
import com.example.edupro.ui.practice.reading.practice.passage.ReadingPassageFragment;
import com.example.edupro.ui.practice.reading.practice.question.ReadingQuestionFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.Angle;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Position;
import nl.dionsegijn.konfetti.core.Spread;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.xml.KonfettiView;

public class ReadingPracticeFragment extends Fragment {
    private static final String TAG = "ReadingPracticeFragment";
    private String readingId;
    private ReadingDto readingDto = new ReadingDto();
    private ReadingPracticeViewModel readingViewModel;
    private TextView readingPassage;
    private TextView readingQuestion;
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
            readingId = getArguments().getString("readingId");
            readingViewModel.setReadingId(readingId);
        }

        ObserverAnyChange();

        handleSessionShow(readingPractice);
        handleBottomSheet();
        handleSubmit(readingPractice);

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

    private void handleSessionShow(View readingPractice) {
        readingPassage = readingPractice.findViewById(R.id.reading_practice_passage_button);
        readingPassage.setOnClickListener(v -> readingViewModel.setIsPassageShow(true));
        readingQuestion = readingPractice.findViewById(R.id.reading_practice_question_button);
        readingQuestion.setOnClickListener(v -> readingViewModel.setIsPassageShow(false));

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
                    questions.add("-");
                }
                readingQuestionAdapter = new ReadingQuestionListAdapter(questions, new RecyclerViewClickInterface() {
                    @Override
                    public void onItemClick(int position) {
                        readingViewModel.setIsPassageShow(false);
                        RecyclerView.LayoutManager layoutManager = readingQuestionRecyclerView.getLayoutManager();
                        if (layoutManager != null) {
                            layoutManager.smoothScrollToPosition(readingQuestionRecyclerView, null, position);
                        }
                    }
                    @Override
                    public void onLongItemClick(int position) {

                    }
                });
                readingQuestionRecyclerView.setAdapter(readingQuestionAdapter);
                for (int i = 0; i < numberOfQuestions; i++) {
                    int finalI = i;
                    readingViewModel.getAnswerAtIndex(i).observe(getViewLifecycleOwner(), answer -> {
                        if (answer != null && !answer.equals("-")) {
                            questions.set(finalI, "Q" + (finalI + 1));
                            ((ReadingQuestionListAdapter) readingQuestionAdapter).notifyItemChanged(finalI);
                        } else if (answer != null) {
                            questions.set(finalI, "-");
                            ((ReadingQuestionListAdapter) readingQuestionAdapter).notifyItemChanged(finalI);
                        }
                    });
                }
            }
        });
    }

    private void handleSubmit(View readingPractice) {
        Button submitButton = readingPractice.findViewById(R.id.reading_practice_submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pair<String, String> answersSelected = readingViewModel.getAnswersSelected();
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Answered: " + answersSelected.first + "\n" + "Unanswered: " + answersSelected.second)
                        .setConfirmText("Submit")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                readingViewModel.submitAnswer("1").observe(getViewLifecycleOwner(), isSubmit -> {
                                    if (isSubmit) {
                                        handleParadeAnimation(readingPractice);
                                        sDialog
                                                .setTitleText("Submitted!")
                                                .setContentText("Congratulate on finishing the test!")
                                                .setConfirmText("View Result")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                        handleSubmitted(readingPractice);
                                                        sDialog.dismissWithAnimation();
                                                    }
                                                })
                                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                    } else {
                                        sDialog
                                                .setTitleText("Loading")
                                                .changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
                                        sDialog
                                                .setCancelable(false);
                                        sDialog
                                                .getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                        //sDialog.show();
                                    }
                                });
                            }
                        })
                        .setCancelText("Cancel")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        });
                sweetAlertDialog.show();
            }
        });
    }

    private void handleSubmitted(View readingPractice) {
        String result = readingViewModel.getMark().getValue() +"/" + readingViewModel.getNumberOfQuestions().getValue();
        String part1Type = String.valueOf(readingDto.getQuestions().get(0).getType());
        String part2Type = String.valueOf(readingDto.getQuestions().get(1).getType());

        ArrayList<String> correctAnswersPart1 = new ArrayList<>();
        ArrayList<String> answersPart1 = new ArrayList<>();
        int part1Size = readingDto.getQuestions().get(0).getQuestions().size();
        for (int i = 0; i < part1Size; i++) {
            correctAnswersPart1.add(readingDto.getAnswers().get(i));
        }
        for (int i = 0; i < part1Size; i++) {
            answersPart1.add(readingViewModel.getAnswerAtIndex(i).getValue());
        }

        ArrayList<String> correctAnswersPart2 = new ArrayList<>();
        ArrayList<String> answersPart2 = new ArrayList<>();
        int part2Size = readingDto.getQuestions().get(1).getQuestions().size();
        for (int i = 0; i < part2Size; i++) {
            answersPart2.add(readingViewModel.getAnswerAtIndex(i + part1Size).getValue());
        }
        for (int i = 0; i < part2Size; i++) {
            correctAnswersPart2.add(readingDto.getAnswers().get(i + part1Size));
        }

        Bundle bundle = new Bundle();
        bundle.putString("readingId", readingId);
        bundle.putString("result", result);
        bundle.putString("part1", part1Type);
        bundle.putString("part2", part2Type);
        bundle.putStringArrayList("correctAnswersPart1", correctAnswersPart1);
        bundle.putStringArrayList("answersPart1", answersPart1);
        bundle.putStringArrayList("correctAnswersPart2", correctAnswersPart2);
        bundle.putStringArrayList("answersPart2", answersPart2);

        Navigation.findNavController(readingPractice).navigate(R.id.navigation_practice_reading_result, bundle);
    }

    private void handleParadeAnimation(View readingPractice) {
        KonfettiView konfettiView = readingPractice.findViewById(R.id.konfetti_reading_practice);
        Shape.DrawableShape drawableShape = null;

        final Drawable drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_heart);
        if (drawable != null) {
            drawableShape = new Shape.DrawableShape(
                    drawable,
                    true, true);
        }

        EmitterConfig emitterConfig = new Emitter(5, TimeUnit.SECONDS).perSecond(30);
        konfettiView.start(
                new PartyFactory(emitterConfig)
                        .angle(Angle.RIGHT - 45)
                        .spread(Spread.SMALL)
                        .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE, drawableShape))
                        .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                        .setSpeedBetween(10f, 30f)
                        .position(new Position.Relative(0.0, 0.5))
                        .build(),
                new PartyFactory(emitterConfig)
                        .angle(Angle.LEFT + 45)
                        .spread(Spread.SMALL)
                        .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE, drawableShape))
                        .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                        .setSpeedBetween(10f, 30f)
                        .position(new Position.Relative(1.0, 0.5))
                        .build());
    }
}
