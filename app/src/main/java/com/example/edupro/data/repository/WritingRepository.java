package com.example.edupro.data.repository;

import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.edupro.service.RetrofitClient;
import com.example.edupro.service.ServerAPIService;
import com.example.edupro.service.WritingGradingRequestModel;
import com.example.edupro.service.WritingGradingResponseModel;
import com.example.edupro.model.writing.WritingDto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WritingRepository {
    private final FirebaseRepository<WritingDto> firebaseRepository;

    private WritingRepository() {
        firebaseRepository = FirebaseRepository.getInstance(WritingDto.class, "writing");
    }

    public static WritingRepository instance;

    public static WritingRepository getInstance() {
        if (instance == null) {
            instance = new WritingRepository();
        }
        return instance;
    }

    public void getAllWriting() {
        firebaseRepository.getAllData(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<WritingDto> writings = new ArrayList<>();
                for (DataSnapshot writing : dataSnapshot.getChildren()) {
                    WritingDto writingDto = WritingDto.fromFirebaseData(writing);
                    writings.add(writingDto);
                }
                firebaseRepository.getListData().setValue(writings);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
            }
        });
    }

    public void getWritingById(String id) {
        firebaseRepository.getDataById(id, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                WritingDto writingDto = WritingDto.fromFirebaseData(dataSnapshot);
                firebaseRepository.getData().setValue(writingDto);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
            }
        });
    }

    public MutableLiveData<ArrayList<WritingDto>> getWritings() {
        return firebaseRepository.getListData();
    }

    public MutableLiveData<WritingDto> getWriting() {
        return firebaseRepository.getData();
    }

    public Pair<MutableLiveData<String>, MutableLiveData<String>> checkBandScore(String answer, String question) {
        final MutableLiveData<String> scoreLiveData = new MutableLiveData<>();
        final MutableLiveData<String> explanationLiveData = new MutableLiveData<>();

        ServerAPIService apiService = RetrofitClient.createService();
        WritingGradingRequestModel requestModel = new WritingGradingRequestModel(question, answer);
        Call<WritingGradingResponseModel> call = apiService.gradeWritingAPI(requestModel);

        call.enqueue(new Callback<WritingGradingResponseModel>() {
            @Override
            public void onResponse(Call<WritingGradingResponseModel> call, Response<WritingGradingResponseModel> response) {
                if (response.isSuccessful()) {
                    WritingGradingResponseModel responseModel = response.body();
                    double model_score = responseModel.getModelGrade();
                    double bard_score = responseModel.getBardGrade();

                    String explanation = responseModel.getExplaination();
                    Log.d("api", "onResponse: " + explanation);
                    List<String> warnings = responseModel.getWarnings();

                    double score = getScore(model_score, bard_score);

                    scoreLiveData.setValue(String.valueOf(score));
                    explanationLiveData.setValue(responseModel.getExplaination());
                    Log.d("api score", String.valueOf(score));
                } else {
                    // Handle the error
                    Log.d("api2", "fail22");
                }
            }

            @Override
            public void onFailure(Call<WritingGradingResponseModel> call, Throwable t) {
                Log.e(" failed", "API call failed: " + t.getMessage());
            }
        });

        return new Pair<>(scoreLiveData, explanationLiveData);
    }

    private double getScore(double model_score, double bard_score) {
        if (bard_score > 10) {
            return model_score;
        } else {
            // return higher score
            return model_score > bard_score ? model_score : bard_score;
        }
    }

}
