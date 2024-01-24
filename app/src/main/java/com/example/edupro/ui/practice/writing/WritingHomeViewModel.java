package com.example.edupro.ui.practice.writing;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.edupro.data.repository.AnswerRepository;
import com.example.edupro.data.repository.ReadingRepository;
import com.example.edupro.data.repository.WritingRepository;
import com.example.edupro.model.AnswerDto;
import com.example.edupro.model.reading.ReadingDto;
import com.example.edupro.model.writing.WritingDto;

import java.util.ArrayList;

public class WritingHomeViewModel extends ViewModel {
    private final WritingRepository writingRepository;
    private final AnswerRepository answerRepository;

    public WritingHomeViewModel() {
        writingRepository = WritingRepository.getInstance();
        answerRepository = AnswerRepository.getInstance();
    }
    public void getAllWriting() {
        writingRepository.getAllWriting();
    }

    public void getAllAnswerBySkillIdOfUserId(String userId, String skillId) {
        answerRepository.getAnswerOfSkillByUserId(userId, skillId);
    }

    public LiveData<ArrayList<AnswerDto>> getAllAnswersBySkillIdOfUserIdList() {
        return answerRepository.getAnswers();
    }

    public LiveData<ArrayList<WritingDto>> getAllWritingList() {
        return writingRepository.getWritings();
    }

    public static Pair<ArrayList<WritingDto>, ArrayList<AnswerDto>> getWritingsOfTopic(ArrayList<WritingDto> writings, ArrayList<AnswerDto> answers, int topic) {
        ArrayList<WritingDto> writingsOfTopic = new ArrayList<>();
        ArrayList<AnswerDto> answersOfTopic = new ArrayList<>();
        int idx = 0;
        for (WritingDto writing : writings) {
            if (writing.getTopic() == topic) {
                writingsOfTopic.add(writing);
                if (answers.size() > idx) {
                    answersOfTopic.add(answers.get(idx));
                }
            }
            idx++;
        }
        return new Pair<>(writingsOfTopic, answersOfTopic);
    }
    @Override
    protected void onCleared() {
        super.onCleared();
    }
}