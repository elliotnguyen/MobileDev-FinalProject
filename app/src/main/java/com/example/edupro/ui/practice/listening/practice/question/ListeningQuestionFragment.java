package com.example.edupro.ui.practice.listening.practice.question;

import androidx.lifecycle.ViewModelProvider;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.example.edupro.R;
import com.example.edupro.databinding.FragmentListeningQuestionBinding;
import com.example.edupro.model.listening.ListeningDto;
import com.example.edupro.model.question.Question;
import com.example.edupro.ui.practice.listening.practice.ListeningPracticeViewModel;
import com.example.edupro.ui.practice.reading.practice.question.ReadingCompleteFormFragment;

import java.util.ArrayList;
import java.util.Locale;

public class ListeningQuestionFragment extends Fragment {
    private ListeningDto listeningDto = new ListeningDto();
    private FragmentListeningQuestionBinding binding;
    private ListeningPracticeViewModel mViewModel;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;

    public static ListeningQuestionFragment newInstance() {
        return new ListeningQuestionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentListeningQuestionBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(requireParentFragment()).get(ListeningPracticeViewModel.class);
        observeAnyChange();

        return binding.getRoot();
    }

    private void handleSeekbar() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        String audio = listeningDto.getAudio();
        Log.d("TAG", "handleSeekbar: " + audio);
        try {
            mediaPlayer.setDataSource(audio);
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                binding.mSeekBar.setMax(mediaPlayer.getDuration());
                binding.totalTime.setText(formatTime(mediaPlayer.getDuration()));
            }
        });

        binding.playIcon.setOnClickListener(v -> {
            if (!isPlaying) {
                mediaPlayer.start();
                isPlaying = true;
                updateSeekBar();
                binding.playIcon.setImageResource(R.drawable.ic_pause);
            } else {
                mediaPlayer.pause();
                isPlaying = false;
                binding.playIcon.setImageResource(R.drawable.ic_play_audio);
            }
        });

        binding.mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress = i;
                if (b) {
                    mediaPlayer.seekTo(progress);
                    binding.mSeekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mediaPlayer.pause();
                isPlaying = false;
                binding.playIcon.setImageResource(R.drawable.ic_play_audio);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(progress);
                binding.mSeekBar.setProgress(progress);
                mediaPlayer.start();
                isPlaying = true;
                binding.curTime.setText(formatTime(progress));
                binding.playIcon.setImageResource(R.drawable.ic_pause);
            }
        });

        mediaPlayer.setOnCompletionListener(mediaPlayer -> {
            isPlaying = false;
            binding.playIcon.setImageResource(R.drawable.ic_play_audio);
        });


    }

    private String formatTime(int milliseconds) {
        int seconds = milliseconds / 1000;
        int minutes = seconds / 60;
        seconds %= 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }
    private void updateSeekBar() {
        // Update SeekBar and time every second
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    binding.mSeekBar.setProgress(currentPosition);
                    binding.curTime.setText(formatTime(currentPosition));
                    updateSeekBar();
                }
            }
        }, 0);
    }

    private void observeAnyChange() {
        mViewModel.getListening().observe(getViewLifecycleOwner(), listeningDto -> {
            if (listeningDto != null) {
                this.listeningDto = listeningDto;
                handleSeekbar();
                ArrayList<Question> part1 = this.listeningDto.getQuestions().get(0).getQuestions();
                ArrayList<Question> part2 = this.listeningDto.getQuestions().get(1).getQuestions();
                handleQuestionShow(R.id.listening_practice_question_part1, this.listeningDto.getType().get(0), part1, 0);
                handleQuestionShow(R.id.listening_practice_question_part2, this.listeningDto.getType().get(1), part2, part1.size());
            }
        });
    }

    private void handleQuestionShow(int layoutId, Long type, ArrayList<Question> questions, int index) {
        Fragment fragmentLayout;
        if (type == 0) {
            Log.d("TAG", "handleQuestionShow: " + questions.size());
            fragmentLayout = new ListeningTFNGFragment(questions, index);
        } else if (type == 1) {
            fragmentLayout = new ListeningMCQFragment(questions, index);
        } else {
            fragmentLayout = new ReadingCompleteFormFragment();
        }
        getChildFragmentManager().beginTransaction()
                .replace(layoutId, fragmentLayout)
                .commit();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ListeningPracticeViewModel.class);
        // TODO: Use the ViewModel
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}



