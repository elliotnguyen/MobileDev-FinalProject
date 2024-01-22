package com.example.edupro.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.edupro.model.listening.ListeningDto;
import com.example.edupro.model.reading.ReadingDto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ListeningRepository {
    private final FirebaseRepository<ListeningDto> firebaseRepository;

    private ListeningRepository() {
        firebaseRepository = FirebaseRepository.getInstance(ListeningDto.class,"listening");
    }

    public static ListeningRepository instance;

    public static ListeningRepository getInstance() {
        if (instance == null) {
            instance = new ListeningRepository();
        }
        return instance;
    }

    public void getAllListening() {
        firebaseRepository.getAllData(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<ListeningDto> listenings = new ArrayList<>();
                for (DataSnapshot listening : snapshot.getChildren()) {
                    ListeningDto listeningDto = ListeningDto.fromFirebaseData(listening);
                    listenings.add(listeningDto);
                }
                firebaseRepository.getListData().setValue(listenings);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ListeningRepository", "onCancelled: " + error.getMessage());
            }
        });
    }

    public void getListeningById(String id) {
        Log.d("ListeningRepository", "getListeningById: " + id);
        firebaseRepository.getDataById(id, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ListeningDto listeningDto = ListeningDto.fromFirebaseData(snapshot);
                firebaseRepository.getData().setValue(listeningDto);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ListeningRepository", "onCancelled: " + error.getMessage());
            }
        });
    }

    public MutableLiveData<ListeningDto> getListening() {
        return firebaseRepository.getData();
    }

    public MutableLiveData<ArrayList<ListeningDto>> getListenings() {
        return firebaseRepository.getListData();
    }
}
