package com.example.edupro.ui.practice.writing.practice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.edupro.R;
import com.example.edupro.model.writing.WritingDto;
import com.example.edupro.ui.dialog.SweetAlertDialog;
import com.example.edupro.ui.practice.reading.practice.passage.ReadingPassageFragment;
import com.example.edupro.ui.practice.reading.practice.question.ReadingQuestionFragment;
import com.example.edupro.ui.practice.writing.practice.history.HistorySubmissionFragment;
import com.example.edupro.ui.practice.writing.practice.write.WriteAnswerFragment;
import com.example.edupro.viewmodel.UserViewModel;

public class WritingPracticeFragment extends Fragment {
    private static final String TAG = "WritingPracticeFragment";

    private WritingDto writingDto = new WritingDto();

    private WritingPracticeViewModel writingPracticeViewModel;

    private TextView writeAnswer;
    private String writingId;
    private TextView historySubmission;
    private AppCompatButton cancelButton;
    private UserViewModel userViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View writingPractice = inflater.inflate(R.layout.fragment_writing_practice, container, false);

        writingPracticeViewModel = new ViewModelProvider(this).get(WritingPracticeViewModel.class);
        if (getArguments() != null) {
            writingId = getArguments().getString("writingId");
            String answers = getArguments().getString("answers");
            if (answers != null && !answers.equals("")) {
                writingPracticeViewModel.setCurrentAnswer(answers);
            }
            writingPracticeViewModel.setWritingId(writingId);
        }

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        writingPracticeViewModel.init();
        observeAnyChange();

        writeAnswer = writingPractice.findViewById(R.id.writing_practice_question_button);
        historySubmission = writingPractice.findViewById(R.id.writing_practice_history_submission_button);

        handleSessionShow();

        handleCancel(writingPractice);
        return writingPractice;
    }

    private void handleCancel(View writingPractice) {
        Button cancel = writingPractice.findViewById(R.id.writing_practice_cancel_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setConfirmText("Yes")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                writingPracticeViewModel.saveAnswer(userViewModel.getUser().getValue().getId(), writingPracticeViewModel.getCurrentAnswer().getValue(), false);

                                Navigation.findNavController(writingPractice).navigate(R.id.navigation_practice_writing);
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .setCancelText("No")
                        .setCancelClickListener(null);
                sweetAlertDialog.show();
            }
        });
    }

    private void observeAnyChange() {
        writingPracticeViewModel.getWritingDto().observe(getViewLifecycleOwner(), writingDto -> {
            if (writingDto != null) {
                this.writingDto = writingDto;
                writingPracticeViewModel.setWritingDto(writingDto);
            }
        });
    }

    private void handleSessionShow() {
        writingPracticeViewModel.getIsWriteAnswerShow().observe(getViewLifecycleOwner(), isWriteAnswerShow -> {
            Fragment newFragment = isWriteAnswerShow ? new WriteAnswerFragment() : new HistorySubmissionFragment();
            if (isWriteAnswerShow) {
                writeAnswer.setBackgroundResource(R.drawable.reading_passage_button_selected);
                historySubmission.setBackgroundResource(R.drawable.reading_passage_button_unselected);
            } else {
                writeAnswer.setBackgroundResource(R.drawable.reading_passage_button_unselected);
                historySubmission.setBackgroundResource(R.drawable.reading_passage_button_selected);
            }
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.writing_practice_container, newFragment)
                    .commit();
        });
    }
}
