package com.example.edupro.service.request;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SpeakingRequestModel {
    private final MultipartBody.Part audioFile;
    private final String answer;

    public SpeakingRequestModel(MultipartBody.Part audioFile, String answer) {
        this.audioFile = audioFile;
        this.answer = answer;
    }

    public MultipartBody.Part getAudioFile() {
        return audioFile;
    }

    public String getAnswer() {
        return answer;
    }
}
