package com.example.edupro.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
public interface  ServerAPIService {
    @POST("/api/writting/grade")
    Call<WritingGradingResponseModel> postData(@Body WritingGradingRequestModel request);
}
