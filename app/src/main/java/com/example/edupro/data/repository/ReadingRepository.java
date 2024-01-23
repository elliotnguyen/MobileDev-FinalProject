package com.example.edupro.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.edupro.model.reading.ReadingDto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReadingRepository {
    private final FirebaseRepository<ReadingDto> firebaseRepository;

    private ReadingRepository() {
        firebaseRepository = FirebaseRepository.getInstance(ReadingDto.class, "reading");
    }

    public static ReadingRepository instance;
    public static ReadingRepository getInstance() {
        if (instance == null) {
            instance = new ReadingRepository();
        }
        return instance;
    }

    public void getAllReading() {
        firebaseRepository.getAllData(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<ReadingDto> readings = new ArrayList<>();
                for (DataSnapshot reading : dataSnapshot.getChildren()) {
                    ReadingDto readingDto = ReadingDto.fromFirebaseData(reading);
                    readings.add(readingDto);
                }
                firebaseRepository.getListData().setValue(readings);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
            }
        });
    }

    public void getReadingById(String id) {
        firebaseRepository.getDataById(id, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ReadingDto readingDto = ReadingDto.fromFirebaseData(dataSnapshot);
                firebaseRepository.getData().setValue(readingDto);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
            }
        });
    }

    public MutableLiveData<ArrayList<ReadingDto>> getReadings() {
        return firebaseRepository.getListData();
    }

    public MutableLiveData<ReadingDto> getReading() {
        return firebaseRepository.getData();
    }
}
