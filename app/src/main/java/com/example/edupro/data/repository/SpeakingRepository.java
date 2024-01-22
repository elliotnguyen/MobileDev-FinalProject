package com.example.edupro.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.edupro.model.speaking.SpeakingDto;
import com.example.edupro.model.writing.WritingDto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SpeakingRepository {
    private final String TAG = "SpeakingRepository";
    private final FirebaseRepository<SpeakingDto> firebaseRepository;

    public SpeakingRepository() {
        this.firebaseRepository = FirebaseRepository.getInstance(SpeakingDto.class, "speaking");
    }

    public static SpeakingRepository getInstance() {
        return new SpeakingRepository();
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
}
