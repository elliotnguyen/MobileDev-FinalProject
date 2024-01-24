package com.example.edupro.data.repository;

import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.edupro.api.RetrofitClient;
import com.example.edupro.api.ServerAPIService;
import com.example.edupro.api.SpeakingGradingResponseModel;
import com.example.edupro.model.speaking.SpeakingDto;
import com.example.edupro.model.writing.WritingDto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpeakingRepository {
    private final String TAG = "SpeakingRepository";
    private final FirebaseRepository<SpeakingDto> firebaseRepository;
    private MutableLiveData<String> downloadUrlLiveData = new MutableLiveData<>();

    public LiveData<String> getDownloadUrlLiveData() {
        return downloadUrlLiveData;
    }
    public SpeakingRepository() {
        this.firebaseRepository = FirebaseRepository.getInstance(SpeakingDto.class, "speaking");
    }

    public static SpeakingRepository instance;

    public static SpeakingRepository getInstance() {
        if (instance == null) {
            instance = new SpeakingRepository();
        }
        return instance;
    }

    public void getAllSpeaking() {
        firebaseRepository.getAllData(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<SpeakingDto> speakings = new ArrayList<>();
                for (DataSnapshot speaking : snapshot.getChildren()) {
                    SpeakingDto speakingDto = SpeakingDto.fromFirebaseData(speaking);
                    speakings.add(speakingDto);
                }
                firebaseRepository.getListData().setValue(speakings);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getSpeakingById(String id) {
        Log.d(TAG, "getSpeakingById: " + id);
        firebaseRepository.getDataById(id, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SpeakingDto speakingDto = SpeakingDto.fromFirebaseData(snapshot);
                firebaseRepository.getData().setValue(speakingDto);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public MutableLiveData<ArrayList<SpeakingDto>> getSpeakings() {
        return firebaseRepository.getListData();
    }

    public MutableLiveData<SpeakingDto> getSpeaking() {
        return firebaseRepository.getData();
    }

    public void upLoadAudioFileToStorage(File audioFile, String speakingId, String userId) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference audioRef = storageRef.child("speaking_" + speakingId + "_" + userId + ".mp3");

        UploadTask uploadTask = audioRef.putFile(android.net.Uri.fromFile(audioFile));
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            Log.d(TAG, "upLoadAudioFileToStorage: " + taskSnapshot.getMetadata().getPath());

            // Get the download URL of the uploaded file
            audioRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String downloadUrl = uri.toString();

                // Add the download URL to the MutableLiveData
                downloadUrlLiveData.setValue(downloadUrl);
            }).addOnFailureListener(e -> {
                Log.d(TAG, "getDownloadUrl: " + e.getMessage());
            });
        }).addOnFailureListener(e -> {
            Log.d(TAG, "upLoadAudioFileToStorage: " + e.getMessage());
        });
    }


    public Pair<MutableLiveData<String>, MutableLiveData<String>> checkBandScore(File audioFile, String questionText) {
        final MutableLiveData<String> score = new MutableLiveData<>("");
        final MutableLiveData<String> explaination = new MutableLiveData<>("");
        RequestBody questionBody = RequestBody.create(MediaType.parse("text/plain"), questionText);
        RequestBody fileBody = RequestBody.create(MediaType.parse("audio/mp3"), audioFile);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", audioFile.getName(), fileBody);


        ServerAPIService apiService = RetrofitClient.createService();

        Call<SpeakingGradingResponseModel> call = apiService.gradeSpeakingAPI(questionBody, filePart);
        call.enqueue(new Callback<SpeakingGradingResponseModel>() {
            @Override
            public void onResponse(Call<SpeakingGradingResponseModel> call, Response<SpeakingGradingResponseModel> response) {
                if (response.isSuccessful()) {
                    SpeakingGradingResponseModel gradingResponse = response.body();
                    Log.d(TAG, "onResponse: " + gradingResponse);
                    double grade = gradingResponse.getGrade();
                    String explanation = gradingResponse.getExplanation();

                    score.setValue(String.valueOf(grade));
                    explaination.setValue(explanation);
                    Log.d(TAG, "onResponse1 " + grade);
                } else {
                    Log.d(TAG, "onResponse2: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<SpeakingGradingResponseModel> call, Throwable t) {
                Log.e(TAG, "API call failed: " + t.getMessage());
            }
        });

        return new Pair<>(new MutableLiveData<>(""), new MutableLiveData<>(""));
    }


}
