package com.example.edupro.ui.practice.speaking.result;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.edupro.R;
import com.example.edupro.databinding.FragmentSpeakingResultBinding;
import com.example.edupro.databinding.FragmentWritingResultBinding;
import com.example.edupro.ui.dialog.SweetAlertDialog;

public class SpeakingResultFragment extends Fragment {


    private FragmentSpeakingResultBinding binding;
    private String explaination;
    private String speakingId;
    private String score;
    public SpeakingResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSpeakingResultBinding.inflate(inflater, container, false);

        if (getArguments() != null) {
            explaination = getArguments().getString("explaination");
            speakingId = getArguments().getString("speakingId");
            score = getArguments().getString("score");
        }

        handleShowingResult();
        handleRetry(binding.getRoot());
        handleCancel(binding.getRoot());
        return binding.getRoot();

    }

    private void handleShowingResult() {
        binding.speakingPracticeResultExplaination.setText(explaination);
        binding.speakingPracticeResultText.setText("Your Result: " + score + "/9.0");
    }

    private void handleRetry(View view) {
        AppCompatButton retryButton = view.findViewById(R.id.speaking_practice_result_retry_button);
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
                                bundle.putString("speakingId", speakingId);
                                Navigation.findNavController(view).navigate(R.id.navigation_practice_speaking_practice, bundle);
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
        AppCompatButton cancelButton = view.findViewById(R.id.speaking_practice_result_cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setConfirmText("Yes")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                Navigation.findNavController(view).navigate(R.id.navigation_practice_speaking);
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