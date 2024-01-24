package com.example.edupro.ui.practice.writing.practice;

import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.edupro.data.repository.AnswerRepository;
import com.example.edupro.data.repository.ReadingRepository;
import com.example.edupro.data.repository.WritingRepository;
import com.example.edupro.model.AnswerDto;
import com.example.edupro.model.writing.WritingDto;
import com.example.edupro.ui.helper.DateUtil;

import java.util.ArrayList;

public class WritingPracticeViewModel extends ViewModel {
    private final WritingRepository writingRepository = WritingRepository.getInstance();
    private final MutableLiveData<String> writingId = new MutableLiveData<>("");
    private final MutableLiveData<WritingDto> writingDto = new MutableLiveData<>(new WritingDto());
    private final MutableLiveData<Boolean> isWriteAnswerShow = new MutableLiveData<>(true);
    private final MutableLiveData<Boolean> isSampleAnswerShow = new MutableLiveData<>(false);
    private final MutableLiveData<String> currentAnswer = new MutableLiveData<>("");
    private final MutableLiveData<String> result = new MutableLiveData<>("");
    private final MutableLiveData<String> explaination = new MutableLiveData<>("");
    private final ArrayList<MutableLiveData<String>> answers = new ArrayList<>();
    private final AnswerRepository answerRepository = AnswerRepository.getInstance();
    public void setWritingId(String id) {
        writingId.setValue(id);
    }
    public void setWriteAnswerShow(boolean isWriteAnswerShow) {
        this.isWriteAnswerShow.setValue(isWriteAnswerShow);
    }
    public void setIsSampleAnswerShow(boolean isSampleAnswerShow) {
        this.isSampleAnswerShow.setValue(isSampleAnswerShow);
    }
    public void setWritingDto(WritingDto writingDto) {
        this.writingDto.setValue(writingDto);
    }
    public void setCurrentAnswer(String currentAnswer) {
        this.currentAnswer.setValue(currentAnswer);
    }

    public LiveData<String> getResultScore() {
        return result;
    }
    public LiveData<Boolean> getIsWriteAnswerShow() {
        return isWriteAnswerShow;
    }
    public LiveData<Boolean> getIsSampleAnswerShow() {
        return isSampleAnswerShow;
    }
    public LiveData<WritingDto> getWritingDto() {
        writingRepository.getWritingById(writingId.getValue());
        return writingRepository.getWriting();
    }
    public LiveData<String> getWritingId() {
        return writingId;
    }
    public LiveData<String> getCurrentAnswer() {
        return currentAnswer;
    }

    public LiveData<Boolean> saveAnswer(String userId, String answer, boolean isSubmitted) {
        String id = DateUtil.getCurrentTimeOfDate();

        ArrayList<String> childId= new ArrayList<>();
        childId.add(userId);
        childId.add("writing");
        childId.add(writingId.getValue());
        childId.add(writingId.getValue() + "_" + userId);

        getResult().observeForever(new Observer<Pair<String, String>>() {
            @Override
            public void onChanged(Pair<String, String> resultAndExplanation) {
                if (resultAndExplanation != null) {
                    String score = resultAndExplanation.first;
                    Log.d("api", "onChanged: " + answer);
                    AnswerDto answerDto = new AnswerDto(id, "w" + writingId.getValue(), userId, currentAnswer.getValue(), score, "", "", isSubmitted);
                    answerRepository.createAnswerByTestIdOfUserId(childId, answerDto);
                }
            }
        });

        // Return the status LiveData
        return answerRepository.getStatusHandling();
    }

    public LiveData<Pair<String, String>> submitAnswer(String userId, String answer) {
        MutableLiveData<Pair<String, String>> resultLiveData = new MutableLiveData<>();

        saveAnswer(userId, answer, true).observeForever(isSubmit -> {
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



    private LiveData<Pair<String, String>> getResult() {
        MutableLiveData<Pair<String, String>> resultLiveData = new MutableLiveData<>();

        Pair<MutableLiveData<String>, MutableLiveData<String>> resultPair = writingRepository.checkBandScore(
                currentAnswer.getValue(),
                writingDto.getValue().getQuestion()
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

    public void init() {
        for (int i = 0; i < 10; i++) {
            answers.add(new MutableLiveData<>("-"));
        }
    }
}
