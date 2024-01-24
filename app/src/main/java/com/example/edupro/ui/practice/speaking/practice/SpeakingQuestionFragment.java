package com.example.edupro.ui.practice.speaking.practice;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.Manifest;
import android.widget.Chronometer;
import android.widget.Toast;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.ExecuteCallback;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.arthenica.mobileffmpeg.FFmpegExecution;
import com.example.edupro.R;
import com.example.edupro.databinding.FragmentListeningQuestionBinding;
import com.example.edupro.databinding.FragmentSpeakingQuestionBinding;
import com.example.edupro.model.listening.ListeningDto;
import com.example.edupro.model.speaking.SpeakingDto;
import com.example.edupro.ui.dialog.SweetAlertDialog;
import com.example.edupro.ui.practice.listening.practice.ListeningPracticeViewModel;
import com.example.edupro.ui.practice.listening.practice.question.ListeningQuestionFragment;
import com.example.edupro.utils.FFmpegUtils;
import com.example.edupro.viewmodel.UserViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SpeakingQuestionFragment extends Fragment {
    private static final int PERMISSION_REQUEST_CODE = 1234;
    ;
    private SpeakingDto speakingDto = new SpeakingDto();
    private FragmentSpeakingQuestionBinding binding;
    private SpeakingPracticeViewModel mViewModel;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private MediaRecorder mediaRecorder;
    private String outputFile;
    private Chronometer chronometer;
    private boolean isRecording = false;
    private long recordingStartTime = 0;
    private UserViewModel userViewModel;
    private File answerFile;
    public static SpeakingQuestionFragment newInstance() {
        return new SpeakingQuestionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSpeakingQuestionBinding.inflate(inflater, container, false);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        mViewModel = new ViewModelProvider(requireParentFragment()).get(SpeakingPracticeViewModel.class);
        outputFile = getOutputFilePath();
        observeAnyChange();

        chronometer = binding.chronometer;
        chronometer.setBase(SystemClock.elapsedRealtime());
        binding.recordBtn.setOnClickListener(v -> onRecordButtonClicked());

        binding.recordAgainTv.setOnClickListener(v -> handleRecordUI());

        binding.playBtn.setOnClickListener(v -> playRecordedAudio());

        return binding.getRoot();
    }

    private void handleRecordUI() {
        binding.doneRecordView.setVisibility(View.GONE);
        binding.recordBtn.setVisibility(View.VISIBLE);
        binding.chronometer.setVisibility(View.VISIBLE);
        binding.chronometer.setBase(recordingStartTime);
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

    private void startRecording() {
        binding.recordBtn.setBackgroundResource(R.drawable.ic_microphone_playing);
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setOutputFile(outputFile);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            chronometer.start();
            Toast.makeText(requireContext(), "Recording started", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getOutputFilePath() {
        ContextWrapper contextWrapper = new ContextWrapper(requireContext());
        File musicDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(musicDirectory, "recording.mp3");
        answerFile = file;
        return file.getAbsolutePath();
    }
    private void stopRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            chronometer.stop();
            handleStopRecordingUI();
            Toast.makeText(requireContext(), "Recording Completed", Toast.LENGTH_LONG).show();
            gradingSpeaking();
        }
    }
    private void handleStopRecordingUI() {
        binding.recordBtn.setVisibility(View.GONE);
        binding.chronometer.setVisibility(View.GONE);
        binding.recordBtn.setBackgroundResource(R.drawable.ic_microphone);
        binding.doneRecordView.setVisibility(View.VISIBLE);
    }

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
    private void requestAudioPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(requireContext(), "Please grant permissions to record audio", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        PERMISSION_REQUEST_CODE);
            }
        }
        // if permission already granted
        else if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            startRecording();
        }
    }
    private void playRecordedAudio() {
        if (isPlaying) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            isPlaying = false;
            binding.playBtn.setBackgroundResource(R.drawable.ic_play_rounded);
            binding.playingStatus.setText("Listen to your answer");
            return;
        }
        try {
            binding.playBtn.setBackgroundResource(R.drawable.ic_pause);
            binding.playingStatus.setText("Stop Playing");
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(outputFile);
            mediaPlayer.prepare();
            mediaPlayer.start();
            isPlaying = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // Playback completed, update UI
                isPlaying = false;
                binding.playBtn.setBackgroundResource(R.drawable.ic_play_rounded);
                binding.playingStatus.setText("Listen to your answer");
            }
        });
    }
    private void gradingSpeaking() {
        FFmpegUtils.convertAacToMp3(outputFile, outputFile);
        File file = new File(outputFile);
        mViewModel.setCurrentAnswer(file);
        String question = speakingDto.getQuestion();
        binding.saveAnswerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setConfirmText("Submit")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                mViewModel.submitAnswer(userViewModel.getUser().getValue().getId(), file).observe(getViewLifecycleOwner(), resultPair -> {
                                    if (resultPair != null) {
                                        // Handle UI updates after successful submission
                                        String score = resultPair.first;
                                        String explaination = resultPair.second;

                                        sDialog
                                                .setTitleText("Submitted!")
                                                .setContentText("Congratulate on finishing the test!\nScore: " + score)
                                                .setConfirmText("View Result")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                        handleSubmitted(explaination);
                                                        sDialog.dismissWithAnimation();
                                                    }
                                                })
                                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                    } else {
                                        // Handle the case where submission failed
                                        sDialog
                                                .setTitleText("Loading")
                                                .changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
                                        sDialog
                                                .setCancelable(false);
                                        sDialog
                                                .getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
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

    private void handleSubmitted(String explaination) {
        Bundle bundle = new Bundle();
        bundle.putString("speakingId", mViewModel.getSpeakingId().getValue());
        bundle.putString("explaination", explaination);
        bundle.putString("score", mViewModel.getResultScore().getValue());
        Navigation.findNavController(binding.getRoot()).navigate(R.id.navigation_practice_speaking_result, bundle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRecording();
    }
}