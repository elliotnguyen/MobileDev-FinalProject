package com.example.edupro.ui.practice.listening.practice;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.example.edupro.R;
import com.example.edupro.data.repository.UserRepository;
import com.example.edupro.databinding.FragmentListeningPracticeBinding;
import com.example.edupro.model.User;
import com.example.edupro.model.listening.ListeningDto;
import com.example.edupro.model.question.Question;
import com.example.edupro.ui.RecyclerViewClickInterface;
import com.example.edupro.ui.dialog.SweetAlertDialog;
import com.example.edupro.ui.practice.listening.practice.question.ListeningQuestionFragment;
import com.example.edupro.ui.practice.reading.practice.ReadingQuestionListAdapter;
import com.example.edupro.viewmodel.UserViewModel;

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

public class ListeningPracticeFragment extends Fragment {

    private ListeningPracticeViewModel mViewModel;
    private FragmentListeningPracticeBinding binding;
    private ListeningDto listeningDto = new ListeningDto();
    private TextView listeningQuestions, listeningDicussion;
    private ReadingQuestionListAdapter readingQuestionAdapter;
    private RecyclerView readingQuestionRecyclerView;
    private UserViewModel userViewModel;
    private Chronometer chronometer;

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
        chronometer = binding.getRoot().findViewById(R.id.listening_practice_timer);
        chronometer.start();
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.initUser(new UserRepository.OnUserFetchedListener() {
            @Override
            public void onUserFetched(User user) {
                Log.d("ListeningPractice", "onUserFetched: " + user.getId());
            }

            @Override
            public void onError(Exception e) {

            }
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
        handleCancel(binding.getRoot());

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
                                mViewModel.submitAnswer(userViewModel.getUser().getValue().getId()).observe(getViewLifecycleOwner(), isSubmit -> {
                                    if (isSubmit) {
                                        handleParadeAnimation(binding.getRoot());
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
        String result = mViewModel.getResult().getValue().toString();
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
        Log.d("ListeningPractice", "handleSubmitted: " + listeningDto.getId());
        bundle.putString("result", result);
        bundle.putString("part1", part1Type);
        bundle.putString("part2", part2Type);
        bundle.putStringArrayList("correctAnswersPart1", correctAnswersPart1);
        bundle.putStringArrayList("answersPart1", answersPart1);
        bundle.putStringArrayList("correctAnswersPart2", correctAnswersPart2);
        bundle.putStringArrayList("answersPart2", answersPart2);

        Navigation.findNavController(binding.getRoot()).navigate(R.id.navigation_practice_listening_result, bundle);
    }

    private void handleCancel(View view) {
        Button cancel = view.findViewById(R.id.listening_practice_cancel_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setConfirmText("Yes")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                mViewModel.saveAnswer(userViewModel.getUser().getValue().getId(), false, "0");

                                Navigation.findNavController(binding.getRoot()).navigate(R.id.navigation_practice_listening);
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .setCancelText("No")
                        .setCancelClickListener(null);
                sweetAlertDialog.show();
            }
        });
    }

    private void handleParadeAnimation(View readingPractice) {
        KonfettiView konfettiView = readingPractice.findViewById(R.id.konfetti_listening_practice);
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