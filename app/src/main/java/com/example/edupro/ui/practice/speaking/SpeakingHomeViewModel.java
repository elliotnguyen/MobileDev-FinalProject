package com.example.edupro.ui.practice.speaking;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.edupro.data.repository.AnswerRepository;
import com.example.edupro.data.repository.ListeningRepository;
import com.example.edupro.data.repository.SpeakingRepository;
import com.example.edupro.model.AnswerDto;
import com.example.edupro.model.listening.ListeningDto;
import com.example.edupro.model.speaking.SpeakingDto;

import java.util.ArrayList;

public class SpeakingHomeViewModel extends ViewModel {
    private SpeakingRepository speakingRepository;
    private AnswerRepository answerRepository;

    public SpeakingHomeViewModel() {
        answerRepository = AnswerRepository.getInstance();
        speakingRepository = SpeakingRepository.getInstance();
    }
    public void init() {
        speakingRepository.getAllSpeaking();
    }

    public void getAllAnswerBySkillIdOfUserId(String userId, String skillId) {
        answerRepository.getAnswerOfSkillByUserId(userId, skillId);
    }

    public LiveData<ArrayList<AnswerDto>> getAllAnswersBySkillIdOfUserIdList() {
        return answerRepository.getAnswers();
    }

    public LiveData<ArrayList<SpeakingDto>> getAllSpeakingList() {
        return speakingRepository.getSpeakings();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}