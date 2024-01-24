package com.example.edupro.service;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface  ServerAPIService {
    @POST("/api/writting/grade")
    Call<WritingGradingResponseModel> gradeWritingAPI(@Body WritingGradingRequestModel request);


    @Multipart
    @POST("/api/speaking/grade")
    Call<SpeakingGradingResponseModel> gradeSpeakingAPI(
            @Part("question") RequestBody question,
            @Part MultipartBody.Part file
    );
}
