package com.example.edupro.ui.practice.speaking.practice;

import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.edupro.data.repository.AnswerRepository;
import com.example.edupro.data.repository.SpeakingRepository;
import com.example.edupro.data.repository.WritingRepository;
import com.example.edupro.model.AnswerDto;
import com.example.edupro.model.speaking.SpeakingDto;
import com.example.edupro.model.writing.WritingDto;
import com.example.edupro.ui.helper.DateUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class SpeakingPracticeViewModel extends ViewModel {
    private final SpeakingRepository speakingRepository = SpeakingRepository.getInstance();
    private final AnswerRepository answerRepository = AnswerRepository.getInstance();
    private final MutableLiveData<String> speakingId = new MutableLiveData<>("");
    private final MutableLiveData<SpeakingDto> speakingDto = new MutableLiveData<>(new SpeakingDto());
    private final MutableLiveData<File> currentAnswer = new MutableLiveData<>(new File(""));

    private final ArrayList<MutableLiveData<String>> answers = new ArrayList<>();
    private final MutableLiveData<String> audioFile = new MutableLiveData<>("");
    private final MutableLiveData<String> result = new MutableLiveData<>("");
    private final MutableLiveData<String> explaination = new MutableLiveData<>("");

    public void setSpeakingId(String id) {
        speakingId.setValue(id);
    }

    public void setSpeakingDto(SpeakingDto speakingDto) {
        this.speakingDto.setValue(speakingDto);
    }

    public void setCurrentAnswer(File currentAnswer) {
        this.currentAnswer.setValue(currentAnswer);
    }

    public MutableLiveData<SpeakingDto> getSpeakingDto() {
        speakingRepository.getSpeakingById(speakingId.getValue());
        return speakingRepository.getSpeaking();
    }

    public MutableLiveData<String> getSpeakingId() {
        return speakingId;
    }

    public MutableLiveData<File> getCurrentAnswer() {
        return currentAnswer;
    }

    public void init() {
        for (int i = 0; i < 10; i++) {
            answers.add(new MutableLiveData<>("-"));
        }
    }

    public LiveData<Boolean> saveAnswer(String userId, File audio, boolean isSubmitted) {
        String id = DateUtil.getCurrentTimeOfDate();

        ArrayList<String> childId= new ArrayList<>();
        childId.add(userId);
        childId.add("speaking");
        childId.add(speakingId.getValue());
        childId.add(speakingId.getValue() + "_" + userId);

        speakingRepository.getDownloadUrlLiveData().observeForever(downloadUrl -> {
            if (downloadUrl != null) {
                audioFile.setValue(downloadUrl);
            } else {
                audioFile.setValue("");
            }
        });

        getResult(audio).observeForever(new Observer<Pair<String, String>>() {
            @Override
            public void onChanged(Pair<String, String> resultAndExplanation) {
                if (resultAndExplanation != null) {
                    String score = resultAndExplanation.first;
                    AnswerDto answerDto = new AnswerDto(id, "w" + speakingId.getValue(), userId, audioFile.getValue(), score, "", "", isSubmitted);
                    answerRepository.createAnswerByTestIdOfUserId(childId, answerDto);
                }
            }
        });

        // Return the status LiveData
        return answerRepository.getStatusHandling();
    }

    public LiveData<Pair<String, String>> submitAnswer(String userId, File audio) {
        MutableLiveData<Pair<String, String>> resultLiveData = new MutableLiveData<>();

        saveAnswer(userId, audio, true).observeForever(isSubmit -> {
            if (isSubmit) {
                // Handle the case where submission succeeded
                resultLiveData.setValue(new Pair<>(result.getValue(), explaination.getValue()));
            } else {
                // Handle the case where submission failed
                resultLiveData.setValue(null);
            }
        });

        return resultLiveData;
    }
    public LiveData<String> getResultScore() {
        return result;
    }


    private LiveData<Pair<String, String>> getResult(File audio) {

        Log.d("ViewModel", "getResult: " + audio.getAbsolutePath());
        MutableLiveData<Pair<String, String>> resultLiveData = new MutableLiveData<>();

        Pair<MutableLiveData<String>, MutableLiveData<String>> resultPair = speakingRepository.checkBandScore(
                audio,
                speakingDto.getValue().getQuestion()
        );

        resultPair.first.observeForever(score -> {
            // Update UI with the score as needed
            result.setValue(score);
            resultLiveData.setValue(new Pair<>(score, explaination.getValue()));
        });

        resultPair.second.observeForever(explanation -> {
            // Update UI with the explanation as needed
            explaination.setValue(explanation);
            resultLiveData.setValue(new Pair<>(result.getValue(), explanation));
        });

        return resultLiveData;
    }

}