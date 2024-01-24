package com.example.edupro.ui.practice.listening;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.edupro.data.repository.AnswerRepository;
import com.example.edupro.data.repository.ListeningRepository;
import com.example.edupro.model.AnswerDto;
import com.example.edupro.model.listening.ListeningDto;

import java.util.ArrayList;

public class ListeningHomeViewModel extends ViewModel {
    private ListeningRepository listeningRepository;
    private AnswerRepository answerRepository;

    public  ListeningHomeViewModel() {
        listeningRepository = ListeningRepository.getInstance();
        answerRepository = AnswerRepository.getInstance();
    }
    public void getAllAnswerBySkillIdOfUserId(String userId, String skillId) {
        answerRepository.getAnswerOfSkillByUserId(userId, skillId);
    }

    public LiveData<ArrayList<AnswerDto>> getAllAnswersBySkillIdOfUserIdList() {
        return answerRepository.getAnswers();
    }

    public void init() {
        listeningRepository.getAllListening();
    }

    public LiveData<ArrayList<ListeningDto>> getAllListeningList() {
        return listeningRepository.getListenings();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

}