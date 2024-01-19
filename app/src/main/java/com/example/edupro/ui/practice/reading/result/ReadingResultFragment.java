package com.example.edupro.ui.practice.reading.result;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edupro.R;
import com.example.edupro.ui.dialog.SweetAlertDialog;

import java.util.ArrayList;

public class ReadingResultFragment extends Fragment {
    private static final String TAG = "ReadingResultFragment";
    private String readingId;
    private String result;
    private String part1Type;
    private String part2Type;
    private ArrayList<String> answersPart1 = new ArrayList<>();
    private ArrayList<String> correctAnswersPart1 = new ArrayList<>();
    private ArrayList<String> answersPart2 = new ArrayList<>();
    private ArrayList<String> correctAnswersPart2 = new ArrayList<>();
    private RecyclerView part1ResultRecyclerView;
    private RecyclerView.Adapter part1ResultAdapter;
    private RecyclerView part2ResultRecyclerView;
    private RecyclerView.Adapter part2ResultAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View readingResultView = inflater.inflate(R.layout.fragment_reading_result, container, false);

        if (getArguments() != null) {
            readingId = getArguments().getString("readingId");
            result = getArguments().getString("result");

            part1Type = getArguments().getString("part1");
            part2Type = getArguments().getString("part2");


            correctAnswersPart1 = getArguments().getStringArrayList("correctAnswersPart1");
            correctAnswersPart2 = getArguments().getStringArrayList("correctAnswersPart2");

            answersPart1 = getArguments().getStringArrayList("answersPart1");
            answersPart2 = getArguments().getStringArrayList("answersPart2");
        }

        TextView resultTextView = readingResultView.findViewById(R.id.reading_practice_result_text);
        String resultText = "Your Result: " + result;
        resultTextView.setText(resultText);

        handleRetry(readingResultView);
        handleCancel(readingResultView);

        part1ResultRecyclerView = readingResultView.findViewById(R.id.reading_result_part1_recycler_view);
        handleResultShow(part1ResultRecyclerView, answersPart1, correctAnswersPart1, part1Type);

        part2ResultRecyclerView = readingResultView.findViewById(R.id.reading_result_part2_recycler_view);
        handleResultShow(part2ResultRecyclerView, answersPart2, correctAnswersPart2, part2Type);

        return readingResultView;
    }

    private void handleResultShow(RecyclerView partResultRecyclerView, ArrayList<String> answers, ArrayList<String> correctAnswers, String part) {
        partResultRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        RecyclerView.Adapter partResultAdapter = new ReadingResultListAdapter(answers, correctAnswers, Integer.parseInt(part));
        partResultRecyclerView.setAdapter(partResultAdapter);
    }

    private void handleRetry(View readingResultView) {
        AppCompatButton retryButton = readingResultView.findViewById(R.id.reading_practice_result_retry_button);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setConfirmText("Yes")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                Bundle bundle = new Bundle();
                                bundle.putString("readingId", readingId);
                                Navigation.findNavController(readingResultView).navigate(R.id.navigation_practice_reading_practice, bundle);
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .setCancelText("No")
                        .setCancelClickListener(null);
                sweetAlertDialog.show();
            }
        });
    }

    private void handleCancel(View readingResultView) {
        AppCompatButton cancelButton = readingResultView.findViewById(R.id.reading_practice_result_cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setConfirmText("Yes")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                Navigation.findNavController(readingResultView).navigate(R.id.navigation_practice_reading);
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .setCancelText("No")
                        .setCancelClickListener(null);
                sweetAlertDialog.show();
            }
        });
    }
}
