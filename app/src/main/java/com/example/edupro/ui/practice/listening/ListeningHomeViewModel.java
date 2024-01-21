package com.example.edupro.ui.practice.listening;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.edupro.data.repository.ListeningRepository;
import com.example.edupro.model.listening.ListeningDto;
import com.example.edupro.model.reading.ReadingDto;

import java.util.ArrayList;

public class ListeningHomeViewModel extends ViewModel {
    private ListeningRepository listeningRepository;

    public ListeningHomeViewModel() {
        listeningRepository = ListeningRepository.getInstance();
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