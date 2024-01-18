package com.example.edupro.model.writing;

import com.example.edupro.model.SkillDto;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class WritingDto extends SkillDto {
    private final String question;
    private final String sampleAnswer;

    public WritingDto() {
        super();
        question = "";
        sampleAnswer = "";
    }

    public WritingDto(String id, ArrayList<Long> type, long topic, String question, String sampleAnswer) {
        super(id, type, topic);
        this.question = question;
        this.sampleAnswer = sampleAnswer;
    }

    public static WritingDto fromFirebaseData(DataSnapshot dataSnapshot) {
        String id = (String) dataSnapshot.child("id").getValue();
        ArrayList<Long> type = (ArrayList<Long>) dataSnapshot.child("type").getValue();
        long topic = (long) dataSnapshot.child("topic").getValue();
        String question = (String) dataSnapshot.child("question").getValue();
        String sampleAnswer = (String) dataSnapshot.child("sample_answer").getValue();
        return new WritingDto(id, type, topic, question, sampleAnswer);
    }

    public String getQuestion() {
        return question;
    }

    public String getSampleAnswer() {
        return sampleAnswer;
    }
}
