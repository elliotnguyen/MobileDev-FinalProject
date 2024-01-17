package com.example.edupro.ui.practice.reading.practice;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.edupro.data.repository.ReadingRepository;
import com.example.edupro.model.ReadingDto;

public class ReadingPracticeViewModel extends ViewModel {
    private MutableLiveData<String> readingId = new MutableLiveData<>("");
    public void setReadingId(String id) {
        readingId.setValue(id);
    }
    public LiveData<String> getReadingId() {
        return readingId;
    }
    private final ReadingRepository readingRepository;
    public ReadingPracticeViewModel() {
        readingRepository = ReadingRepository.getInstance();
    }
    public void getReadingById(String id) {
        readingRepository.getReadingById(id);
    }
    public LiveData<ReadingDto> getReading() {
        return readingRepository.getReading();
    }
}
