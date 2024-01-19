package com.example.edupro.model.reading;

import java.util.ArrayList;

public class QuestionSection {
    private long type = 0;
    private ArrayList<Question> questions = new ArrayList<>();
    public QuestionSection(long type, ArrayList<Question> questions) {
        this.type = type;
        this.questions = questions;
    }
    public long getType() {
        return type;
    }
    public ArrayList<Question> getQuestions() {
        return questions;
    }
}
