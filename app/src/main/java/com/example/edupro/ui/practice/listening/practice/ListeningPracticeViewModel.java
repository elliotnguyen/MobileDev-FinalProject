package com.example.edupro.ui.practice.listening.practice;

import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.edupro.data.repository.AnswerRepository;
import com.example.edupro.data.repository.ListeningRepository;
import com.example.edupro.model.AnswerDto;
import com.example.edupro.model.listening.ListeningDto;
import com.example.edupro.ui.helper.DateUtil;

import java.util.ArrayList;
import java.util.Objects;

public class ListeningPracticeViewModel extends ViewModel {
    private static final String TAG = "ListeningPracticeViewMo";
    private final ListeningRepository listeningRepository = ListeningRepository.getInstance();
    private final AnswerRepository answerRepository = AnswerRepository.getInstance();
    private final ArrayList<MutableLiveData<String>> answers = new ArrayList<>();
    private final MutableLiveData<String> result = new MutableLiveData<>("");
    private final MutableLiveData<Integer> numberOfQuestions = new MutableLiveData<>(0);
    private final MutableLiveData<Boolean> isQuestionShow = new MutableLiveData<>(true);
    private final MutableLiveData<String> listeningId = new MutableLiveData<>("");
    private MutableLiveData<ListeningDto> fixedListening = new MutableLiveData<>(new ListeningDto());

    public void init() {
        for (int i = 0; i < 10; i++) {
            answers.add(new MutableLiveData<>("-"));
        }
    }

    // setter and getter
    public void setfixedListening(ListeningDto listeningDto) {
        fixedListening.setValue(listeningDto);
    }
    public void setListeningId(String id) {
        listeningId.setValue(id);
    }
    public void setIsQuestionShow(Boolean isQuestionShow) {
        this.isQuestionShow.setValue(isQuestionShow);
    }

    public void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions.setValue(numberOfQuestions);
    }
    public void setAnswerAtIndex(int index, String answer) {
        answers.get(index).setValue(answer);
    }

    public LiveData<ListeningDto> getListening() {
        if (fixedListening.getValue() != null && fixedListening.getValue().getId().equals("")) {
            Log.d(TAG, "getListening: " + listeningId.getValue());
            listeningRepository.getListeningById(listeningId.getValue());
            return fixedListening = listeningRepository.getListening();
        }
        return fixedListening;
    }


    public LiveData<Boolean> getIsQuestionShow() {
        return isQuestionShow;
    }
    public Pair<String, String> getAnswersSelected() {
        int count = 0;
        for (MutableLiveData<String> answer : answers) {
            if (!Objects.equals(answer.getValue(), "-")) {
                count++;
            }
        }
        if (numberOfQuestions.getValue() == null) {
            return new Pair<>("0", "0");
        }
        return new Pair<>(String.valueOf(count), String
                .valueOf(numberOfQuestions.getValue() - count));
    }

    public MutableLiveData<String> getListeningId() {
        return listeningId;
    }

    public LiveData<String> getAnswerAtIndex(int index) {
        return answers.get(index);
    }

    public LiveData<Integer> getNumberOfQuestions() {
        return numberOfQuestions;
    }

    private String getCurrentAnswer() {
        StringBuilder answer = new StringBuilder();
        if (numberOfQuestions.getValue() != null) {
            for (int i = 0; i < numberOfQuestions.getValue(); i++) {
                if (answers.get(i).getValue() != null) {
                    answer.append(answers.get(i).getValue()).append(";");
                }
            }
        }
        return answer.toString();
    }

    public LiveData<Boolean> submitAnswer(String userId) {
        return saveAnswer(userId, true, getResult().getValue().toString());
    }

    public LiveData<Boolean> saveAnswer(String userId, Boolean isSubmitted, String result) {
        String currentAnswer = getCurrentAnswer();
        String id = DateUtil.getCurrentTimeOfDate();

        ArrayList<String> childId= new ArrayList<>();
        childId.add(userId);
        childId.add("listening");
        childId.add(listeningId.getValue());
        childId.add(listeningId.getValue() + "_" + userId);

        AnswerDto answerDto = new AnswerDto(id, "l" + listeningId.getValue(), userId, currentAnswer, result, "", "", isSubmitted);
        answerRepository.createAnswerByTestIdOfUserId(childId, answerDto);
        return answerRepository.getStatusHandling();
    }

    public LiveData<String> getResult() {
        if (numberOfQuestions.getValue() != null) {
            int count = 0;
            for (int i = 0; i < numberOfQuestions.getValue(); i++) {
                if (answers.get(i).getValue() != null && fixedListening.getValue() != null) {
                    if (Objects.equals(answers.get(i).getValue(), fixedListening.getValue().getAnswers().get(i))) {
                        count++;
                    }
                }
            }
            String result = String.valueOf(count) +"/" + getNumberOfQuestions().getValue();
            this.result.setValue(result);
        }
        return this.result;
    }

}