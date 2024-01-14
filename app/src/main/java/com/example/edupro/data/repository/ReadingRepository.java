package com.example.edupro.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.edupro.model.ReadingDto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReadingRepository {
    private final MutableLiveData<ReadingDto> mReading;
    private final MutableLiveData<ArrayList<ReadingDto>> mReadings;
    static FirebaseDatabase database = FirebaseDatabase.getInstance();
    static DatabaseReference myRef = database.getReference("reading");
    public static ReadingRepository instance;
    private ReadingRepository() {
        mReading = new MutableLiveData<>();
        mReadings = new MutableLiveData<>();
    }
    public static ReadingRepository getInstance() {
        if (instance == null) {
            instance = new ReadingRepository();
        }
        return instance;
    }
    public void getAllReading() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<ReadingDto> readings = new ArrayList<>();
                for (DataSnapshot reading : dataSnapshot.getChildren()) {
                    ReadingDto readingDto = ReadingDto.fromFirebaseData(reading);
                    readings.add(readingDto);
                }
                mReadings.setValue(readings);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
            }
        });
    }
    public MutableLiveData<ArrayList<ReadingDto>> getReadings() {
        return mReadings;
    }
    public void getReadingById(String id) {
        myRef.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ReadingDto readingDto = ReadingDto.fromFirebaseData(dataSnapshot);
                mReading.setValue(readingDto);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
            }
        });
    }
    public MutableLiveData<ReadingDto> getReading() {
        return mReading;
    }
}
