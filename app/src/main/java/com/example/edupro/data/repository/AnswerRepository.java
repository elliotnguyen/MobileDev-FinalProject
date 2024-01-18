package com.example.edupro.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.edupro.model.AnswerDto;
import com.example.edupro.model.reading.ReadingDto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AnswerRepository {
    private final FirebaseRepository<AnswerDto> firebaseRepository;
    private AnswerRepository() {
        firebaseRepository = FirebaseRepository.getInstance(AnswerDto.class, "answer");
    }
    public static AnswerRepository instance;
    public static AnswerRepository getInstance() {
        if (instance == null) {
            instance = new AnswerRepository();
        }
        return instance;
    }
    public void getAnswerByTestIdOfUserId(String testId, String userId) {
        firebaseRepository.getDataById(testId + "_" + userId, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AnswerDto answerDto = AnswerDto.fromFirebaseData(dataSnapshot);
                firebaseRepository.getData().setValue(answerDto);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
            }
        });
    }
    public void createAnswerByTestIdOfUserId(ArrayList<String> childrenIds, AnswerDto answerDto) {
        firebaseRepository.createDataById(childrenIds, answerDto, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    firebaseRepository.getStatusHandling().setValue(true);
                } else {
                    firebaseRepository.getStatusHandling().setValue(false);
                }
            }
        });
    }
    public void updateAnswerByTestIdOfUserId(ArrayList<String> childrenIds, String answer) {
        firebaseRepository.updateDataOfChild(childrenIds, answer, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    firebaseRepository.getStatusHandling().setValue(true);
                } else {
                    firebaseRepository.getStatusHandling().setValue(false);
                }
            }
        });
    }
    public MutableLiveData<AnswerDto> getAnswer() {
        return firebaseRepository.getData();
    }
    public MutableLiveData<Boolean> getStatusHandling() {
        return firebaseRepository.getStatusHandling();
    }
}
