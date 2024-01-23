package com.example.edupro.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.edupro.model.AnswerDto;
import com.example.edupro.model.reading.ReadingDto;
import com.example.edupro.service.RetrofitAPI;
import com.example.edupro.service.RetrofitClient;
import com.example.edupro.service.request.SpeakingRequestModel;
import com.example.edupro.service.response.SpeakingGradingReponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

import retrofit2.Call;

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

    public void getAnswerOfSkillByUserId(String userId, String skillId) {
        ArrayList<String> childrenIds = new ArrayList<>();
        childrenIds.add(userId);
        childrenIds.add(skillId);

        firebaseRepository.getAllDataOfChild(childrenIds, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<AnswerDto> answers = new ArrayList<>();
                if (dataSnapshot.getChildrenCount() == 0) {
                    firebaseRepository.getListData().setValue(answers);
                    return;
                }

                for (DataSnapshot answer : dataSnapshot.getChildren()) {
                    if (answer.getChildrenCount() == 0) {
                        continue;
                    }
                    for (DataSnapshot answerOfUser : answer.getChildren()) {
                        AnswerDto answerDto = AnswerDto.fromFirebaseData(answerOfUser);
                        answers.add(answerDto);
                    }
//                    AnswerDto answerDto = AnswerDto.fromFirebaseData(answer.getChildren());
//                    answers.add(answerDto);
                }
                firebaseRepository.getListData().setValue(answers);
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

    public MutableLiveData<ArrayList<AnswerDto>> getAnswers() {
        return firebaseRepository.getListData();
    }

    public MutableLiveData<Boolean> getStatusHandling() {
        return firebaseRepository.getStatusHandling();
    }

//    public void gradeAnswer(AnswerDto answerDto, List<QuestionAnswerPair> questionAnswerPairs, MutableLiveData<GradingResponse> gradingResponseLiveData) {
//        RetrofitAPI gradingApiService = RetrofitClient.getRetrofitClient().create(RetrofitAPI.class);
//
//        SpeakingRequestModel speakingRequestModel = new SpeakingRequestModel(answerDto.getAudioFile(), answerDto.getAnswer());
//        Call<SpeakingGradingReponse> call = gradingApiService.gradeSpeaking(answerDto, questionAnswerPairs)
//
//        call.enqueue(new Callback<GradingResponse>() {
//            @Override
//            public void onResponse(Call<GradingResponse> call, Response<GradingResponse> response) {
//                if (response.isSuccessful()) {
//                    gradingResponseLiveData.setValue(response.body());
//                } else {
//                    // Handle error response
//                    // You can set an error message or handle it in another way
//                    gradingResponseLiveData.setValue(null);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<GradingResponse> call, Throwable t) {
//                // Handle failure
//                // You can set an error message or handle it in another way
//                gradingResponseLiveData.setValue(null);
//            }
//        });
//    }
}
