package com.example.edupro.ui.practice.reading;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.edupro.data.repository.ReadingRepository;
import com.example.edupro.model.ReadingDto;

import java.util.ArrayList;

public class ReadingViewModel extends ViewModel {
    private final ReadingRepository readingRepository;
    public ReadingViewModel() {
        readingRepository = ReadingRepository.getInstance();
    }
    public void getAllReading() {
        readingRepository.getAllReading();
    }
    public LiveData<ArrayList<ReadingDto>> getAllReadingList() {
        return readingRepository.getReadings();
    }
    public void getReadingById(String id) {
        readingRepository.getReadingById(id);
    }
    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
