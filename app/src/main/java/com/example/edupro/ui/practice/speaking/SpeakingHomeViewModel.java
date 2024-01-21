package com.example.edupro.ui.practice.speaking;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.edupro.data.repository.ListeningRepository;
import com.example.edupro.data.repository.SpeakingRepository;
import com.example.edupro.model.listening.ListeningDto;
import com.example.edupro.model.speaking.SpeakingDto;

import java.util.ArrayList;

public class SpeakingHomeViewModel extends ViewModel {
    private SpeakingRepository speakingRepository;

    public SpeakingHomeViewModel() {
        speakingRepository = SpeakingRepository.getInstance();
    }
    public void init() {
        speakingRepository.getAllSpeaking();
    }

    public LiveData<ArrayList<SpeakingDto>> getAllSpeakingList() {
        return speakingRepository.getSpeakings();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}