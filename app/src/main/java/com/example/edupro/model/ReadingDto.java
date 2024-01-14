package com.example.edupro.model;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class ReadingDto {
    private final String id;
    private final ArrayList<Integer> type;
    private final Integer topic;
    private final String title;
    private final String content;
    private final String questions;
    private final ArrayList<ArrayList<String>> answers;
    public ReadingDto() {
        id = "";
        type = null;
        topic = null;
        title = "";
        content = "";
        questions = "";
        answers = null;
    }

    public ReadingDto(String id, ArrayList<Integer> type, Integer topic, String title, String content, String questions, ArrayList<ArrayList<String>> answers) {
        this.id = id;
        this.type = type;
        this.topic = topic;
        this.title = title;
        this.content = content;
        this.questions = questions;
        this.answers = answers;
    }

    @SuppressWarnings("unchecked")
    public static ReadingDto fromFirebaseData(DataSnapshot dataSnapshot) {
        String id = (String) dataSnapshot.child("id").getValue();
        ArrayList<Integer> type = (ArrayList<Integer>) dataSnapshot.child("type").getValue();
        Integer topic = (Integer) dataSnapshot.child("topic").getValue();
        String title = (String) dataSnapshot.child("title").getValue();
        String content = (String) dataSnapshot.child("content").getValue();
        String questions = "";
        ArrayList<ArrayList<String>> answers = new ArrayList<>();
        for (DataSnapshot answerSection : dataSnapshot.child("answers").getChildren()) {
            ArrayList<String> answer = (ArrayList<String>) answerSection.getValue();
            answers.add(answer);
        }
        return new ReadingDto(id, type, topic, title, content, questions, answers);
    }

    public String getId() {
        return id;
    }

    public ArrayList<Integer> getType() {
        return type;
    }

    public Integer getTopic() {
        return topic;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getQuestions() {
        return questions;
    }

    public ArrayList<ArrayList<String>> getAnswers() {
        return answers;
    }
}
