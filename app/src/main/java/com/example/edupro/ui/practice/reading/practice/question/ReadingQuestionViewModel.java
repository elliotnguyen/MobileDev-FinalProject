package com.example.edupro.ui.practice.reading.practice.question;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Objects;

public class ReadingQuestionViewModel extends ViewModel {
    private int numberOfQuestions = 0;
    private ArrayList<MutableLiveData<String>> answers = new ArrayList<>();

    public void init() {
        for (int i = 0; i < 10; i++) {
            answers.add(new MutableLiveData<>(""));
        }
    }

    public void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }

    public ArrayList<MutableLiveData<String>> getAnswers() {
        return answers;
    }
    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setAnswerAtIndex(int index, String answer) {
        answers.get(index).setValue(answer);
    }

    public int getAnswersSelected() {
        int count = 0;
        for (MutableLiveData<String> answer : answers) {
            if (!Objects.equals(answer.getValue(), "")) {
                count++;
            }
        }
        return count;
    }
}
