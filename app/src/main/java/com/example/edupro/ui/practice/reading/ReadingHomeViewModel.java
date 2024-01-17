package com.example.edupro.ui.practice.reading;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.edupro.data.repository.ReadingRepository;
import com.example.edupro.model.reading.ReadingDto;

import java.util.ArrayList;

public class ReadingHomeViewModel extends ViewModel {
    private final ReadingRepository readingRepository;
    public ReadingHomeViewModel() {
        readingRepository = ReadingRepository.getInstance();
    }
    public void getAllReading() {
        readingRepository.getAllReading();
    }
    public LiveData<ArrayList<ReadingDto>> getAllReadingList() {
        return readingRepository.getReadings();
    }
    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
