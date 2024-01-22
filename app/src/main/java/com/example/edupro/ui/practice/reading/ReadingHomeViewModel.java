package com.example.edupro.ui.practice.reading;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.edupro.data.repository.AnswerRepository;
import com.example.edupro.data.repository.ReadingRepository;
import com.example.edupro.model.AnswerDto;
import com.example.edupro.model.reading.ReadingDto;

import java.util.ArrayList;

public class ReadingHomeViewModel extends ViewModel {
    private final ReadingRepository readingRepository;
    private final AnswerRepository answerRepository;

    public ReadingHomeViewModel() {
        readingRepository = ReadingRepository.getInstance();
        answerRepository = AnswerRepository.getInstance();
    }
    public void getAllReading() {
        readingRepository.getAllReading();
    }

    public void getAllAnswerBySkillIdOfUserId(String userId, String skillId) {
        answerRepository.getAnswerOfSkillByUserId(userId, skillId);
    }

    public LiveData<ArrayList<AnswerDto>> getAllAnswersBySkillIdOfUserIdList() {
        return answerRepository.getAnswers();
    }

    public LiveData<ArrayList<ReadingDto>> getAllReadingList() {
        return readingRepository.getReadings();
    }

    public static Pair<ArrayList<ReadingDto>, ArrayList<AnswerDto>> getReadingsOfTopic(ArrayList<ReadingDto> readings, ArrayList<AnswerDto> answers, int topic) {
        ArrayList<ReadingDto> readingsOfTopic = new ArrayList<>();
        ArrayList<AnswerDto> answersOfTopic = new ArrayList<>();
        int idx = 0;
        for (ReadingDto reading : readings) {
            if (reading.getTopic() == topic) {
                readingsOfTopic.add(reading);
                if (answers.size() > idx) {
                    answersOfTopic.add(answers.get(idx));
                }
            }
            idx++;
        }
        return new Pair<>(readingsOfTopic, answersOfTopic);
    }
    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
