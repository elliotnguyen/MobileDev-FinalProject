package com.example.edupro.model.question;

import com.example.edupro.model.question.Question;

import java.util.ArrayList;

public class MCQQuestion extends Question {
    private ArrayList<String> options = new ArrayList<>();
    public MCQQuestion(String content, ArrayList<String> options) {
        super(content);
        this.options = options;
    }
    public ArrayList<String> getOptions() {
        return options;
    }
}
