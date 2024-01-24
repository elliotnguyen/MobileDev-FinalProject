package com.example.edupro.ui.practice.writing.result;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.edupro.R;
import com.example.edupro.databinding.FragmentWritingResultBinding;
import com.example.edupro.ui.dialog.SweetAlertDialog;

public class WritingResultFragment extends Fragment {
    private FragmentWritingResultBinding binding;
    private String explaination;
    private String writingId;
    private String score;
    public WritingResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWritingResultBinding.inflate(inflater, container, false);

        if (getArguments() != null) {
            explaination = getArguments().getString("explaination");
            writingId = getArguments().getString("writingId");
            score = getArguments().getString("score");
        }

        handleShowingResult();
        handleRetry(binding.getRoot());
        handleCancel(binding.getRoot());
        return binding.getRoot();

    }

    private void handleShowingResult() {
        binding.writingPracticeResultExplaination.setText(explaination);
        binding.writingPracticeResultText.setText("Your Result: " + score + "/9.0");
    }

    private void handleRetry(View view) {
        AppCompatButton retryButton = view.findViewById(R.id.writing_practice_result_retry_button);
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
                                bundle.putString("writingId", writingId);
                                Navigation.findNavController(view).navigate(R.id.navigation_practice_writing_practice, bundle);
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .setCancelText("No")
                        .setCancelClickListener(null);
                sweetAlertDialog.show();
            }
        });
    }

    private void handleCancel(View view) {
        AppCompatButton cancelButton = view.findViewById(R.id.writing_practice_result_cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setConfirmText("Yes")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                Navigation.findNavController(view).navigate(R.id.navigation_practice_writing);
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