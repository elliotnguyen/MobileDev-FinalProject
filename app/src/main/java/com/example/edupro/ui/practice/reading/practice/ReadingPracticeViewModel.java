package com.example.edupro.ui.practice.reading.practice;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.edupro.data.repository.ReadingRepository;
import com.example.edupro.model.reading.ReadingDto;

import java.util.ArrayList;
import java.util.Objects;

public class ReadingPracticeViewModel extends ViewModel {
    private final MutableLiveData<String> readingId = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> isPassageShow = new MutableLiveData<>(true);
    private final MutableLiveData<ReadingDto> reading = new MutableLiveData<>(new ReadingDto());
    private final MutableLiveData<Integer> numberOfQuestions = new MutableLiveData<>(0);
    private final ArrayList<MutableLiveData<String>> answers = new ArrayList<>();
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
    public void setReading(ReadingDto reading) {
        this.reading.setValue(reading);
    }
    public LiveData<String> getReadingId() {
        return readingId;
    }
    public LiveData<Boolean> getIsPassageShow() {
        return isPassageShow;
    }
    public LiveData<ReadingDto> getReading() {
        readingRepository.getReadingById(readingId.getValue());
        return readingRepository.getReading();
    }
    private final ReadingRepository readingRepository = ReadingRepository.getInstance();

    public LiveData<String> getAnswerAtIndex(int index) {
        return answers.get(index);
    }
    public LiveData<Integer> getNumberOfQuestions() {
        return numberOfQuestions;
    }
}
