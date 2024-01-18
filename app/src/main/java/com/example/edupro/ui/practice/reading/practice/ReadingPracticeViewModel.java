package com.example.edupro.ui.practice.reading.practice;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.edupro.data.repository.AnswerRepository;
import com.example.edupro.data.repository.ReadingRepository;
import com.example.edupro.model.AnswerDto;
import com.example.edupro.model.reading.ReadingDto;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Objects;

public class ReadingPracticeViewModel extends ViewModel {
    private final ReadingRepository readingRepository = ReadingRepository.getInstance();
    private final AnswerRepository answerRepository = AnswerRepository.getInstance();
    private final MutableLiveData<ReadingDto> fixedReading = new MutableLiveData<>(new ReadingDto());
    private final MutableLiveData<String> readingId = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> isPassageShow = new MutableLiveData<>(true);
    //private final MutableLiveData<ReadingDto> reading = new MutableLiveData<>(new ReadingDto());
    private final MutableLiveData<Integer> numberOfQuestions = new MutableLiveData<>(0);
    private final ArrayList<MutableLiveData<String>> answers = new ArrayList<>();
    private final MutableLiveData<Integer> mark = new MutableLiveData<>(0);
    public void init() {
        for (int i = 0; i < 10; i++) {
            answers.add(new MutableLiveData<>(""));
        }
    }
    public void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions.setValue(numberOfQuestions);
    }
    public void setAnswerAtIndex(int index, String answer) {
        answers.get(index).setValue(answer);
    }
    public int getAnswersSelected() {
        int count = 0;
        for (MutableLiveData<String> answer : answers) {
            if (!Objects.equals(answer.getValue(), "")) {
                count++;
            }
        }
        return count;
    }

    public void setReadingId(String id) {
        readingId.setValue(id);
    }
    public void setIsPassageShow(Boolean isPassageShow) {
        this.isPassageShow.setValue(isPassageShow);
    }
//    public void setReading(ReadingDto reading) {
//        this.reading.setValue(reading);
//    }
    public void setFixedReading(ReadingDto reading) {
        this.fixedReading.setValue(reading);
    }
    public LiveData<String> getReadingId() {
        return readingId;
    }
    public LiveData<Boolean> getIsPassageShow() {
        return isPassageShow;
    }
    public LiveData<ReadingDto> getReading() {
        if (fixedReading.getValue() != null && fixedReading.getValue().getId().equals("")) {
            readingRepository.getReadingById(readingId.getValue());
            setFixedReading(readingRepository.getReading().getValue());
            return readingRepository.getReading();
        }
        return fixedReading;
//        readingRepository.getReadingById(readingId.getValue());
//        return readingRepository.getReading();
    }
//    private final ReadingRepository readingRepository = ReadingRepository.getInstance();

    public LiveData<String> getAnswerAtIndex(int index) {
        return answers.get(index);
    }
    public LiveData<Integer> getNumberOfQuestions() {
        return numberOfQuestions;
    }
    private String getCurrentAnswer() {
        StringBuilder answer = new StringBuilder();
        if (numberOfQuestions.getValue() != null) {
            for (int i = 0; i < numberOfQuestions.getValue(); i++) {
                if (answers.get(i).getValue() != null) {
                    answer.append(answers.get(i).getValue()).append(";");
                }
            }
        }
        return answer.toString();
    }
    public LiveData<Boolean> saveAnswer(String userId, Boolean isSubmitted) {
        String currentAnswer = getCurrentAnswer();
        String id = "";
        if (readingId.getValue() != null) {
            id = "r" + readingId.getValue() + "_" + userId;
        }
        ArrayList<String> childId= new ArrayList<>();
        childId.add(id);
        AnswerDto answerDto = new AnswerDto(id, "r" + readingId.getValue(), userId, currentAnswer, 0, "", "", isSubmitted);
        answerRepository.createAnswerByTestIdOfUserId(childId, answerDto);
        return answerRepository.getStatusHandling();
    }
    public LiveData<Boolean> submitAnswer(String userId) {
        return saveAnswer(userId, true);
    }
    public LiveData<Integer> getMark() {
        if (numberOfQuestions.getValue() != null) {
            int count = 0;
            for (int i = 0; i < numberOfQuestions.getValue(); i++) {
                if (answers.get(i).getValue() != null && fixedReading.getValue() != null) {
                    if (Objects.equals(answers.get(i).getValue(), fixedReading.getValue().getAnswers().get(i))) {
                        count++;
                    }
                }
            }
            mark.setValue(count);
        }
        return mark;
    }
}
