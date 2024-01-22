package com.example.edupro.ui.practice.speaking.practice;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.edupro.data.repository.SpeakingRepository;
import com.example.edupro.data.repository.WritingRepository;
import com.example.edupro.model.speaking.SpeakingDto;
import com.example.edupro.model.writing.WritingDto;

public class SpeakingPracticeViewModel extends ViewModel {
    private final SpeakingRepository speakingRepository = SpeakingRepository.getInstance();
    private final MutableLiveData<String> speakingId = new MutableLiveData<>("");
    private final MutableLiveData<SpeakingDto> speakingDto = new MutableLiveData<>(new SpeakingDto());
    private final MutableLiveData<String> currentAnswer = new MutableLiveData<>("");

    public void setSpeakingId(String id) {
        speakingId.setValue(id);
    }

    public void setSpeakingDto(SpeakingDto speakingDto) {
        this.speakingDto.setValue(speakingDto);
    }

    public void setCurrentAnswer(String currentAnswer) {
        this.currentAnswer.setValue(currentAnswer);
    }

    public MutableLiveData<SpeakingDto> getSpeakingDto() {
        speakingRepository.getSpeakingById(speakingId.getValue());
        return speakingRepository.getSpeaking();
    }

    public MutableLiveData<String> getSpeakingId() {
        return speakingId;
    }

    public MutableLiveData<String> getCurrentAnswer() {
        return currentAnswer;
    }

}