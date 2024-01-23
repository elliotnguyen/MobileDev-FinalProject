package com.example.edupro.ui.practice.speaking.practice;

import static android.content.ContentValues.TAG;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.edupro.R;
import com.example.edupro.databinding.FragmentSpeakingPracticeBinding;
import com.example.edupro.model.speaking.SpeakingDto;
import com.example.edupro.model.writing.WritingDto;
import com.example.edupro.ui.practice.writing.practice.WritingPracticeViewModel;

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