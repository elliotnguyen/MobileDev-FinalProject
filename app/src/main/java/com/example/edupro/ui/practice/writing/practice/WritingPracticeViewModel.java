package com.example.edupro.ui.practice.writing.practice;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.edupro.data.repository.ReadingRepository;
import com.example.edupro.data.repository.WritingRepository;
import com.example.edupro.model.writing.WritingDto;

public class WritingPracticeViewModel extends ViewModel {
    private final WritingRepository writingRepository = WritingRepository.getInstance();
    private final MutableLiveData<String> writingId = new MutableLiveData<>("");
    private final MutableLiveData<WritingDto> writingDto = new MutableLiveData<>(new WritingDto());
    private final MutableLiveData<Boolean> isWriteAnswerShow = new MutableLiveData<>(true);
    private final MutableLiveData<Boolean> isSampleAnswerShow = new MutableLiveData<>(false);
    private final MutableLiveData<String> currentAnswer = new MutableLiveData<>("");
    public void setWritingId(String id) {
        writingId.setValue(id);
    }
    public void setWriteAnswerShow(boolean isWriteAnswerShow) {
        this.isWriteAnswerShow.setValue(isWriteAnswerShow);
    }
    public void setIsSampleAnswerShow(boolean isSampleAnswerShow) {
        this.isSampleAnswerShow.setValue(isSampleAnswerShow);
    }
    public void setWritingDto(WritingDto writingDto) {
        this.writingDto.setValue(writingDto);
    }
    public void setCurrentAnswer(String currentAnswer) {
        this.currentAnswer.setValue(currentAnswer);
    }
    public LiveData<Boolean> getIsWriteAnswerShow() {
        return isWriteAnswerShow;
    }
    public LiveData<Boolean> getIsSampleAnswerShow() {
        return isSampleAnswerShow;
    }
    public LiveData<WritingDto> getWritingDto() {
        writingRepository.getWritingById(writingId.getValue());
        return writingRepository.getWriting();
    }
    public LiveData<String> getWritingId() {
        return writingId;
    }
    public LiveData<String> getCurrentAnswer() {
        return currentAnswer;
    }
}
