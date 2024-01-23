package com.example.edupro.service;

import com.example.edupro.model.speaking.SpeakingDto;
import com.example.edupro.service.request.SpeakingRequestModel;
import com.example.edupro.service.response.SpeakingGradingReponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitAPI {
    @POST("api/speaking/grade")
    Call<List<SpeakingGradingReponse>> gradeSpeaking(@Body SpeakingRequestModel speakingRequestModel);
}