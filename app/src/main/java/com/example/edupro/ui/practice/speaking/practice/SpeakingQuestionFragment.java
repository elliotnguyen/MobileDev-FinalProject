package com.example.edupro.ui.practice.speaking.practice;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

import android.content.ContextWrapper;
import android.content.pm.PackageManager;
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

import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
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
import com.example.edupro.ui.practice.listening.practice.ListeningPracticeViewModel;
import com.example.edupro.ui.practice.listening.practice.question.ListeningQuestionFragment;

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
    private static final int SAMPLE_RATE = 16000;
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;

    private AudioRecord audioRecord;

    public static SpeakingQuestionFragment newInstance() {
        return new SpeakingQuestionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSpeakingQuestionBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(requireParentFragment()).get(SpeakingPracticeViewModel.class);
        outputFile = getOutputFilePath();
        observeAnyChange();

        chronometer = binding.chronometer;

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
        if (audioRecord == null) {
            requestAudioPermissions();
        } else {
            stopRecording();
        }
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

    private void startRecording() {
        binding.recordBtn.setBackgroundResource(R.drawable.ic_microphone_playing);
//            mediaRecorder = new MediaRecorder();
//            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//            mediaRecorder.setOutputFile(outputFile);
//
//            mediaRecorder.prepare();
//            mediaRecorder.start();
        int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT) * 3;

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, bufferSize);

            final byte[] buffer = new byte[bufferSize];

            audioRecord.startRecording();
            isRecording = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FileOutputStream outputStream = new FileOutputStream(outputFile);
                    int bytesRead;
                    int totalBytesRead = 0;

                    // Write WAV header with a placeholder for the total audio length
                    writeWavHeader(outputStream,  0);

                    // Recording loop
                    while (isRecording && audioRecord != null) {
                        bytesRead = audioRecord.read(buffer, 0, bufferSize);
                        if (bytesRead > 0) {
                            outputStream.write(buffer, 0, bytesRead);
                            totalBytesRead += bytesRead;
                        }
                    }

                    // Update the WAV header with the correct total audio length
                    writeWavHeader(outputStream, totalBytesRead + 44);

                    // Close the outputStream
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
            recordingStartTime = SystemClock.elapsedRealtime();
            chronometer.setBase(recordingStartTime);
            chronometer.start();
    }
    private void writeWavHeader(FileOutputStream outputStream, long totalDataLen) throws IOException {
        long longSampleRate = SAMPLE_RATE;
        int channelsMask = CHANNEL_CONFIG == AudioFormat.CHANNEL_CONFIGURATION_MONO ? 0x00000004 : 0x00000003;
        long byteRate = SAMPLE_RATE * CHANNEL_CONFIG * AUDIO_FORMAT / 8;

        byte[] header = new byte[44];

        // RIFF/WAVE header
        header[0] = 'R';
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xFF);
        header[5] = (byte) ((totalDataLen >> 8) & 0xFF);
        header[6] = (byte) ((totalDataLen >> 16) & 0xFF);
        header[7] = (byte) ((totalDataLen >> 24) & 0xFF);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';

        // 'fmt ' chunk
        int fmtLength = 16;
        header[16] = (byte)(fmtLength & 0xFF);
        header[17] = (byte)((fmtLength >> 8) & 0xFF);
        header[18] = 0;
        header[19] = 0;
        header[20] = 1;  // PCM format
        header[21] = 0;
        header[22] = (byte) CHANNEL_CONFIG;
        header[23] = 0;
        header[24] = (byte) (longSampleRate & 0xFF);
        header[25] = (byte) ((longSampleRate >> 8) & 0xFF);
        header[26] = (byte) ((longSampleRate >> 16) & 0xFF);
        header[27] = (byte) ((longSampleRate >> 24) & 0xFF);
        header[28] = (byte) (byteRate & 0xFF);
        header[29] = (byte) ((byteRate >> 8) & 0xFF);
        header[30] = (byte) ((byteRate >> 16) & 0xFF);
        header[31] = (byte) ((byteRate >> 24) & 0xFF);
        header[32] = (byte) (CHANNEL_CONFIG * AUDIO_FORMAT / 8);  // Block align
        header[33] = 0;
        header[34] = 16;  // Bits per sample

        // 'data' chunk
        long dataLen = totalDataLen - 36;  // 36 is the size of the header
        header[40] = (byte)(dataLen & 0xFF);
        header[41] = (byte)((dataLen >> 8) & 0xFF);
        header[42] = (byte)((dataLen >> 16) & 0xFF);
        header[43] = (byte)((dataLen >> 24) & 0xFF);

        outputStream.write(header, 0, 44);
    }





    private byte[] intToByteArray(int value) {
        return new byte[]{
                (byte) (value & 0xff),
                (byte) ((value >> 8) & 0xff),
                (byte) ((value >> 16) & 0xff),
                (byte) ((value >> 24) & 0xff)
        };
    }

    private byte[] shortToByteArray(short value) {
        return new byte[]{
                (byte) (value & 0xff),
                (byte) ((value >> 8) & 0xff)
        };
    }
    private void stopRecording() {
        if (audioRecord != null) {
            Log.d("TAG", "stopRecording: ");
            chronometer.stop();
            audioRecord.stop();
            handleStopRecordingUI();
            audioRecord.release();
            audioRecord = null;
        }
    }
    private void handleStopRecordingUI() {
        binding.recordBtn.setVisibility(View.GONE);
        binding.chronometer.setVisibility(View.GONE);
        binding.recordBtn.setBackgroundResource(R.drawable.ic_microphone);
        binding.doneRecordView.setVisibility(View.VISIBLE);
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

    private String getOutputFilePath() {
        ContextWrapper contextWrapper = new ContextWrapper(requireContext());
        File musicDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(musicDirectory, "recording.wav");
        return file.getAbsolutePath();
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

    private void gradingSpeaking() {
        String answer = outputFile;
        String sampleAnswer = speakingDto.getSampleAnswer();
        double score = 0;
        if (answer.equals(sampleAnswer)) {
            score = 10;
        }
//        mViewModel.setScore(score);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRecording();
    }
}