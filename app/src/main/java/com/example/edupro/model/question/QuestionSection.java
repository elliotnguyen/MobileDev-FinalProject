package com.example.edupro.model.question;

import com.example.edupro.model.question.Question;
import com.google.firebase.database.DataSnapshot;

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
    public static ArrayList<QuestionSection> handleQuestions(DataSnapshot dataSnapshot) {
        ArrayList<QuestionSection> questions = new ArrayList<>();
        for (DataSnapshot questionSection : dataSnapshot.child("questions").getChildren()) {
            long type = (long) questionSection.child("type").getValue();
            ArrayList<Question> questionList = new ArrayList<>();
            for (DataSnapshot question : questionSection.child("questions").getChildren()) {
                String content = (String) question.child("content").getValue();
                if (type == 0) {
                    questionList.add(new TFNGQuestion(content));
                } else if (type == 1) {
                    ArrayList<String> options = new ArrayList<>();
                    for (DataSnapshot option : question.child("choices").getChildren()) {
                        String optionContent = (String) option.getValue();
                        options.add(optionContent);
                    }
                    questionList.add(new MCQQuestion(content, options));
                }
            }
            questions.add(new QuestionSection(type, questionList));
        }
        return questions;
    }
}
