package com.example.edupro.ui.practice.writing.practice.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.edupro.R;
import com.example.edupro.databinding.WritingHistoryResultDetailBinding;
import com.example.edupro.model.MessageDao;
import com.example.edupro.service.GeminiService;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

public class HistorySubmissionDetailFragment extends Fragment {
    private static final String TAG = "HistorySubmissionDetailFragment";
    private WritingHistoryResultDetailBinding binding;
    private HistorySubmissionDetailViewModel historySubmissionDetailViewModel;
    private String content = "";
    private String grade = "";
    private String explanation = "";
    private String question = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = WritingHistoryResultDetailBinding.inflate(inflater, container, false);

        historySubmissionDetailViewModel = new ViewModelProvider(this).get(HistorySubmissionDetailViewModel.class);

        if (getArguments() != null) {
            question = getArguments().getString("question");
            content = getArguments().getString("your_answer");
            grade = getArguments().getString("grade");
            explanation = getArguments().getString("explanation");
            binding.writingResultYourAnswer.setText(content);
            binding.writingResultGrade.setText("Grade: " + grade);
            binding.writingResultExplanation.setText(explanation);
        }

        binding.writingResultBackButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigateUp();
        });

        observeAnyChange();

        handleExplanation();

        return binding.getRoot();
    }

    private void observeAnyChange() {
        historySubmissionDetailViewModel.getExplanationOfResult().observe(getViewLifecycleOwner(), s -> {
            binding.writingResultExplanation.setText(s);
        });
    }

    private void handleExplanation() {
        if (explanation.equals("")) {
            GeminiService geminiService = new GeminiService(getString(R.string.apiKey), "gemini-pro");

            String prompt = "Explanation why i have this grade on the IELTS Writing Test: " + binding.writingResultYourAnswer.getText().toString() + " and here is the question: " +
                    question + " and here is my answer: " + content + ". After that, fix the mistakes and write a better answer.";
            Content userMessage = new Content.Builder()
                    .addText(prompt)
                    .build();

            ListenableFuture<GenerateContentResponse> response = geminiService.getGenerativeModelFutures().generateContent(userMessage);
            Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
                @Override
                public void onSuccess(@Nullable GenerateContentResponse result) {
                    if (result == null) {
                        return;
                    }
                    String text = result.getText();
                    historySubmissionDetailViewModel.setExplanation(text);
                }

                @Override
                public void onFailure(Throwable t) {
                    t.printStackTrace();
                }
            }, ContextCompat.getMainExecutor(requireContext()));
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
