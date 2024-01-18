package com.example.edupro.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.edupro.model.writing.WritingDto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WritingRepository {
    private final FirebaseRepository<WritingDto> firebaseRepository;

    private WritingRepository() {
        firebaseRepository = FirebaseRepository.getInstance(WritingDto.class, "writing");
    }

    public static WritingRepository getInstance() {
        return new WritingRepository();
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
}
