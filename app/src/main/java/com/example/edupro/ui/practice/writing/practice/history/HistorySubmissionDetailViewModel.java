package com.example.edupro.ui.practice.writing.practice.history;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HistorySubmissionDetailViewModel extends ViewModel {
    private static final String TAG = "HistorySubmissionDetailViewModel";
    private MutableLiveData<String> explanation = new MutableLiveData<>("");

    public void setExplanation(String explanation) {
        this.explanation.setValue(explanation);
    }

    public LiveData<String> getExplanationOfResult() {
        return explanation;
    }
}
