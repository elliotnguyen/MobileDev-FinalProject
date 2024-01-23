package com.example.edupro.model.speaking;

import android.util.Log;

import com.example.edupro.model.SkillDto;
import com.example.edupro.model.reading.QuestionSection;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class SpeakingDto extends SkillDto {
    private final String question;
    private final String sampleAnswer;

    public SpeakingDto() {
        super();
        question = "";
        sampleAnswer = "";
    }

    public SpeakingDto(String id, ArrayList<Long> type, long topic, String question, String sampleAnswer) {
        super(id, type, topic);
        this.question = question;
        this.sampleAnswer = sampleAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public String getSampleAnswer() {
        return sampleAnswer;
    }

    public static SpeakingDto fromFirebaseData(DataSnapshot dataSnapshot) {
        String id = (String) dataSnapshot.child("id").getValue();
        Log.d("id", id);
        ArrayList<Long> type = (ArrayList<Long>) dataSnapshot.child("type").getValue();
        long topic = (long) dataSnapshot.child("topic").getValue();
        String question = (String) dataSnapshot.child("question").getValue();
        String sampleAnswer = (String) dataSnapshot.child("sample_answer").getValue();
        return new SpeakingDto(id, type, topic, question, sampleAnswer);
    }

}
