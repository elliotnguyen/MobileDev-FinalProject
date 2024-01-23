package com.example.edupro.ui.practice.speaking.practice;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.edupro.data.repository.AnswerRepository;
import com.example.edupro.data.repository.SpeakingRepository;
import com.example.edupro.data.repository.WritingRepository;
import com.example.edupro.model.AnswerDto;
import com.example.edupro.model.speaking.SpeakingDto;
import com.example.edupro.model.writing.WritingDto;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class SpeakingPracticeViewModel extends ViewModel {
    private final SpeakingRepository speakingRepository = SpeakingRepository.getInstance();
    private final AnswerRepository answerRepository = AnswerRepository.getInstance();
    private final MutableLiveData<String> speakingId = new MutableLiveData<>("");
    private final MutableLiveData<SpeakingDto> speakingDto = new MutableLiveData<>(new SpeakingDto());
    private final MutableLiveData<String> currentAnswer = new MutableLiveData<>("");

    private final ArrayList<MutableLiveData<String>> answers = new ArrayList<>();
    private final MutableLiveData<File> audioFile = new MutableLiveData<>();

    public void setSpeakingId(String id) {
        speakingId.setValue(id);
    }

    public void setSpeakingDto(SpeakingDto speakingDto) {
        this.speakingDto.setValue(speakingDto);
    }

    public void setCurrentAnswer(String currentAnswer) {
        this.currentAnswer.setValue(currentAnswer);
    }

    public MutableLiveData<SpeakingDto> getSpeakingDto() {
        speakingRepository.getSpeakingById(speakingId.getValue());
        return speakingRepository.getSpeaking();
    }

    public MutableLiveData<String> getSpeakingId() {
        return speakingId;
    }

    public MutableLiveData<String> getCurrentAnswer() {
        return currentAnswer;
    }

    public void init() {
        for (int i = 0; i < 10; i++) {
            answers.add(new MutableLiveData<>("-"));
        }
    }
//    private String getAnswerFilePath(String audioFile) {
//        File file = new File(audioFile);
//        return file.getAbsolutePath();
//    }
//    public LiveData<Boolean> saveAnswer(String userId, Boolean isSubmitted) {
//        String answer = getAnswerFilePath(currentAnswer.getValue());
//        String id = "";
//        if (speakingId.getValue() != null) {
//            id = "s" + speakingId.getValue() + "_" + userId;
//        }
//        ArrayList<String> childId= new ArrayList<>();
//        childId.add(id);
//        AnswerDto answerDto = new AnswerDto(id, "s" + speakingId.getValue(), userId, answer, 0, "", "", isSubmitted);
//        answerRepository.createAnswerByTestIdOfUserId(childId, answerDto);
//        return answerRepository.getStatusHandling();
//    }
//
//    public LiveData<Boolean> submitAnswer(String userId) {
//        return saveAnswer(userId, true);
//    }
//
//    public LiveData<Integer> getMark() {
//       return null;
//    }
//
//    public Pair<String, String> getAnswersSelected() {
////        int count = 0;
////        for (MutableLiveData<String> answer : answers) {
////            if (!Objects.equals(answer.getValue(), "-")) {
////                count++;
////            }
////        }
////        if (numberOfQuestions.getValue() == null) {
////            return new Pair<>("0", "0");
////        }
////        return new Pair<>(String.valueOf(count), String
////                .valueOf(numberOfQuestions.getValue() - count));
//        return null;
//    }

}