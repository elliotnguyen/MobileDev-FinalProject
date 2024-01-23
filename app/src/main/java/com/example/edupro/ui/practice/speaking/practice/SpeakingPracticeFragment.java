package com.example.edupro.ui.practice.speaking.practice;

import static android.content.ContentValues.TAG;

import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.edupro.R;
import com.example.edupro.databinding.FragmentSpeakingPracticeBinding;
import com.example.edupro.model.speaking.SpeakingDto;
import com.example.edupro.model.writing.WritingDto;
import com.example.edupro.ui.dialog.SweetAlertDialog;
import com.example.edupro.ui.practice.writing.practice.WritingPracticeViewModel;

import java.util.ArrayList;

public class SpeakingPracticeFragment extends Fragment {
    private FragmentSpeakingPracticeBinding binding;
    private SpeakingPracticeViewModel mViewModel;
    private SpeakingDto speakingDto = new SpeakingDto();

    private SpeakingPracticeViewModel speakingPracticeViewModel;

    public static SpeakingPracticeFragment newInstance() {
        return new SpeakingPracticeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSpeakingPracticeBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(SpeakingPracticeViewModel.class);
        String speakingId = getArguments().getString("speakingId");
        mViewModel.setSpeakingId(speakingId);
        observeAnyChange();


        handleOpenQuestion();


        return binding.getRoot();
    }

    private void handleOpenQuestion() {
        Fragment speakingQuestionFragment = new SpeakingQuestionFragment();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.speaking_practice_container, speakingQuestionFragment)
                .commit();

    }

    private void handleSubmit(View readingPractice) {
        Button submitButton = readingPractice.findViewById(R.id.speaking_practice_submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pair<String, String> answersSelected = mViewModel.getAnswersSelected();
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Answered: " + answersSelected.first + "\n" + "Unanswered: " + answersSelected.second)
                        .setConfirmText("Submit")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                mViewModel.submitAnswer("1").observe(getViewLifecycleOwner(), isSubmit -> {
                                    if (isSubmit) {
                                        //handleParadeAnimation(readingPractice);
                                        sDialog
                                                .setTitleText("Submitted!")
                                                .setContentText("Congratulate on finishing the test!")
                                                .setConfirmText("View Result")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                        //handleSubmitted(readingPractice);
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

    private void handleSubmitted() {
//        String result = mViewModel.getMark().getValue() +"/" + mViewModel.getNumberOfQuestions().getValue();
//        String question = mViewModel.getSpeakingDto().getValue().getQuestion();
//
//        String answerPath =
//        int part1Size = speakingDto.getQuestions().get(0).getQuestions().size();
//        for (int i = 0; i < part1Size; i++) {
//            correctAnswersPart1.add(readingDto.getAnswers().get(i));
//        }
//        for (int i = 0; i < part1Size; i++) {
//            answersPart1.add(readingViewModel.getAnswerAtIndex(i).getValue());
//        }
//
//        ArrayList<String> correctAnswersPart2 = new ArrayList<>();
//        ArrayList<String> answersPart2 = new ArrayList<>();
//        int part2Size = readingDto.getQuestions().get(1).getQuestions().size();
//        for (int i = 0; i < part2Size; i++) {
//            answersPart2.add(readingViewModel.getAnswerAtIndex(i + part1Size).getValue());
//        }
//        for (int i = 0; i < part2Size; i++) {
//            correctAnswersPart2.add(readingDto.getAnswers().get(i + part1Size));
//        }
//
//        Bundle bundle = new Bundle();
//        bundle.putString("readingId", readingId);
//        bundle.putString("result", result);
//        bundle.putString("part1", part1Type);
//        bundle.putString("part2", part2Type);
//        bundle.putStringArrayList("correctAnswersPart1", correctAnswersPart1);
//        bundle.putStringArrayList("answersPart1", answersPart1);
//        bundle.putStringArrayList("correctAnswersPart2", correctAnswersPart2);
//        bundle.putStringArrayList("answersPart2", answersPart2);
//
//        Navigation.findNavController(readingPractice).navigate(R.id.navigation_practice_reading_result, bundle);
    }

    private void observeAnyChange() {
        mViewModel.getSpeakingDto().observe(getViewLifecycleOwner(), speakingDto -> {
            if (speakingDto != null) {
                this.speakingDto = speakingDto;
                Log.d(TAG, "observeAnyChange: " + speakingDto.getQuestion());
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SpeakingPracticeViewModel.class);
        // TODO: Use the ViewModel
    }

}