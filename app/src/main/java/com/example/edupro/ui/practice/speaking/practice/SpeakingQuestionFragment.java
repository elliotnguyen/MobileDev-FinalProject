package com.example.edupro.ui.practice.speaking.practice;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.Manifest;
import android.widget.Toast;

import com.example.edupro.R;
import com.example.edupro.databinding.FragmentListeningQuestionBinding;
import com.example.edupro.databinding.FragmentSpeakingQuestionBinding;
import com.example.edupro.model.listening.ListeningDto;
import com.example.edupro.model.speaking.SpeakingDto;
import com.example.edupro.ui.practice.listening.practice.ListeningPracticeViewModel;
import com.example.edupro.ui.practice.listening.practice.question.ListeningQuestionFragment;

import java.io.IOException;

public class SpeakingQuestionFragment extends Fragment {
    private static final int PERMISSION_REQUEST_CODE = 1234; ;
    private SpeakingDto speakingDto = new SpeakingDto();
    private FragmentSpeakingQuestionBinding binding;
    private SpeakingPracticeViewModel mViewModel;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private MediaRecorder mediaRecorder;
    private String outputFile;

    public static SpeakingQuestionFragment newInstance() {
        return new SpeakingQuestionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSpeakingQuestionBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(requireParentFragment()).get(SpeakingPracticeViewModel.class);
        outputFile = requireContext().getExternalCacheDir().getAbsolutePath() + "/recording.3gp";

        observeAnyChange();

        binding.recordBtn.setOnClickListener(v -> onRecordButtonClicked());

        return binding.getRoot();
    }

    private void observeAnyChange() {
        mViewModel.getSpeakingDto().observe(getViewLifecycleOwner(), speakingDto -> {
            if (speakingDto != null) {
                this.speakingDto = speakingDto;
                binding.questionPrompt.setText(speakingDto.getQuestion());
            }
        });
    }
    private void onRecordButtonClicked() {
        if (mediaRecorder == null) {
            requestAudioPermissions();
        } else {
            stopRecording();
        }
    }

    private void requestAudioPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                    Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(requireContext(), "Please grant permissions to record audio", Toast.LENGTH_LONG).show();

                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        PERMISSION_REQUEST_CODE);

            } else {
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        PERMISSION_REQUEST_CODE);
            }
        }
        //If permission is granted, then go ahead recording audio
        else if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {

            //Go ahead with recording audio now
            startRecording();
        }
    }

    //Handling callback
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startRecording();
                } else {
                    Toast.makeText(requireContext(), "Permissions Denied to record audio", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
    private void startRecording() {
        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(outputFile);

            mediaRecorder.prepare();
            mediaRecorder.start();

            // Update UI or show recording indicator
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;


        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRecording();
    }
}