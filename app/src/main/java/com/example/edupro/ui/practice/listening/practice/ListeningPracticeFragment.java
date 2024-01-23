package com.example.edupro.ui.practice.listening.practice;

import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.edupro.R;
import com.example.edupro.databinding.FragmentListeningPracticeBinding;
import com.example.edupro.model.listening.ListeningDto;
import com.example.edupro.model.question.Question;
import com.example.edupro.ui.RecyclerViewClickInterface;
import com.example.edupro.ui.dialog.SweetAlertDialog;
import com.example.edupro.ui.practice.listening.practice.question.ListeningQuestionFragment;
import com.example.edupro.ui.practice.reading.practice.ReadingQuestionListAdapter;

import java.util.ArrayList;

public class ListeningPracticeFragment extends Fragment {

    private ListeningPracticeViewModel mViewModel;
    private FragmentListeningPracticeBinding binding;
    private ListeningDto listeningDto = new ListeningDto();
    private TextView listeningQuestions, listeningDicussion;
    private ReadingQuestionListAdapter readingQuestionAdapter;
    private RecyclerView readingQuestionRecyclerView;


    public static ListeningPracticeFragment newInstance() {
        return new ListeningPracticeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentListeningPracticeBinding.inflate(inflater, container, false);
        binding.getRoot().findViewById(R.id.listening_practice_cancel_button).setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        readingQuestionRecyclerView = binding.getRoot().findViewById(R.id.practice_question_recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 6);
        readingQuestionRecyclerView.setLayoutManager(gridLayoutManager);

        mViewModel = new ViewModelProvider(this).get(ListeningPracticeViewModel.class);
        mViewModel.init();

        if (getArguments() != null) {
            String listeningId = getArguments().getString("listeningId");
            mViewModel.setListeningId(listeningId);
        }

        observeAnyChange();
        handleSessionShow();
        handleBottomSheet();
        handleSubmit();

        return binding.getRoot();
    }

    private void observeAnyChange() {
        mViewModel.getListening().observe(getViewLifecycleOwner(), listeningDto -> {
            if (listeningDto != null) {
                this.listeningDto = listeningDto;
                ArrayList<Question> firstSection = listeningDto.getQuestions().get(0).getQuestions();
                ArrayList<Question> secondSection = listeningDto.getQuestions().get(1).getQuestions();

                int numberOfQuestions = firstSection.size() + secondSection.size();
                mViewModel.setNumberOfQuestions(numberOfQuestions);
            }
        });
    }

    private void handleSessionShow() {
//        listeningQuestions = binding.getRoot().findViewById(R.id.listening_practice_question_button);
//        listeningQuestions.setBackgroundResource(R.drawable.reading_passage_button_selected);
//        listeningQuestions.setOnClickListener(v -> mViewModel.setIsQuestionShow(true));
//
//        listeningDicussion = binding.getRoot().findViewById(R.id.listening_practice_discussion_button);
//        listeningDicussion.setOnClickListener(v -> mViewModel.setIsQuestionShow(false));
//
//        mViewModel.getIsQuestionShow().observe(getViewLifecycleOwner(), isQuestionShow -> {
//            Fragment newFragment = isQuestionShow ? new ListeningQuestionFragment() : new ListeningDicussionFragment();
//            listeningQuestions.setBackgroundResource(isQuestionShow ?
//                    R.drawable.reading_passage_button_selected : R.drawable.reading_passage_button_unselected);
//            listeningDicussion.setBackgroundResource(isQuestionShow ?
//                    R.drawable.reading_passage_button_unselected : R.drawable.reading_passage_button_selected);
//            getChildFragmentManager().beginTransaction()
//                    .replace(R.id.listening_practice_container, newFragment)
//                    .commit();
//        });
        Fragment newFragment = new ListeningQuestionFragment();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.listening_practice_container, newFragment)
                .commit();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ListeningPracticeViewModel.class);
        // TODO: Use the ViewModel
    }

    private void handleBottomSheet() {
        mViewModel.getNumberOfQuestions().observe(getViewLifecycleOwner(), numberOfQuestions -> {
            if (numberOfQuestions != 0) {
                ArrayList<String> questions = new ArrayList<>();
                for (int i = 0; i < numberOfQuestions; i++) {
                    questions.add("-");
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
                    mViewModel.getAnswerAtIndex(i).observe(getViewLifecycleOwner(), answer -> {
                        if (answer != null && !answer.equals("-")) {
                            questions.set(finalI, "Q" + (finalI + 1));
                            ((ReadingQuestionListAdapter) readingQuestionAdapter).notifyItemChanged(finalI);
                        }
                    });
                }
            }
        });
    }

    private void handleSubmit() {
        Button submitButton = binding.getRoot().findViewById(R.id.listening_practice_submit_button);
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
                                                        handleSubmitted();
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
        String result = mViewModel.getMark().getValue() +"/" + mViewModel.getNumberOfQuestions().getValue();
        String part1Type = String.valueOf(listeningDto.getQuestions().get(0).getType());
        String part2Type = String.valueOf(listeningDto.getQuestions().get(1).getType());

        ArrayList<String> correctAnswersPart1 = new ArrayList<>();
        ArrayList<String> answersPart1 = new ArrayList<>();
        int part1Size = listeningDto.getQuestions().get(0).getQuestions().size();
        for (int i = 0; i < part1Size; i++) {
            correctAnswersPart1.add(listeningDto.getAnswers().get(i));
        }
        for (int i = 0; i < part1Size; i++) {
            answersPart1.add(mViewModel.getAnswerAtIndex(i).getValue());
        }

        ArrayList<String> correctAnswersPart2 = new ArrayList<>();
        ArrayList<String> answersPart2 = new ArrayList<>();
        int part2Size = listeningDto.getQuestions().get(1).getQuestions().size();
        for (int i = 0; i < part2Size; i++) {
            answersPart2.add(mViewModel.getAnswerAtIndex(i + part1Size).getValue());
        }
        for (int i = 0; i < part2Size; i++) {
            correctAnswersPart2.add(listeningDto.getAnswers().get(i + part1Size));
        }

        Bundle bundle = new Bundle();
        bundle.putString("listeningId", listeningDto.getId());
        bundle.putString("result", result);
        bundle.putString("part1", part1Type);
        bundle.putString("part2", part2Type);
        bundle.putStringArrayList("correctAnswersPart1", correctAnswersPart1);
        bundle.putStringArrayList("answersPart1", answersPart1);
        bundle.putStringArrayList("correctAnswersPart2", correctAnswersPart2);
        bundle.putStringArrayList("answersPart2", answersPart2);

        Navigation.findNavController(binding.getRoot()).navigate(R.id.navigation_practice_reading_result, bundle);
    }

}