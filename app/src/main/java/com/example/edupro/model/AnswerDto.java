package com.example.edupro.model;

import com.google.firebase.database.DataSnapshot;

public class AnswerDto {
    private String id;
    private String testId;
    private String userId;
    private String answer;
    private String score;
    private String timeStamp;
    private String note;
    private Boolean isSubmitted;

    public AnswerDto() {
        id = "";
        testId = "";
        userId = "";
        answer = "";
        score = "";
        timeStamp = "";
        note = "";
        isSubmitted = false;
    }

    public AnswerDto(String id, String testId, String userId, String answer, String score, String timeStamp, String note, Boolean isSubmitted) {
        this.id = id;
        this.testId = testId;
        this.userId = userId;
        this.answer = answer;
        this.score = score;
        this.timeStamp = timeStamp;
        this.note = note;
        this.isSubmitted = isSubmitted;
    }

    public static AnswerDto fromFirebaseData(DataSnapshot dataSnapshot) {
        String id = dataSnapshot.getKey();
        String testId = (String) dataSnapshot.child("testId").getValue();
        String userId = (String) dataSnapshot.child("userId").getValue();
        String answer = (String) dataSnapshot.child("answer").getValue();
        String score = (String) dataSnapshot.child("score").getValue();
        String timeStamp = (String) dataSnapshot.child("timeStamp").getValue();
        String note = (String) dataSnapshot.child("note").getValue();
        Boolean isSubmitted = (Boolean) dataSnapshot.child("submitted").getValue();
        return new AnswerDto(id, testId, userId, answer, score, timeStamp, note, isSubmitted);
    }

    public String getId() {
        return id;
    }

    public String getTestId() {
        return testId;
    }

    public String getUserId() {
        return userId;
    }

    public String getAnswer() {
        return answer;
    }

    public String getScore() {
        return score;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getNote() {
        return note;
    }

    public Boolean getSubmitted() {
        return isSubmitted;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public void setNote (String note) {
        this.note = note;
    }

    public void setSubmitted(Boolean submitted) {
        isSubmitted = submitted;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getProgress() {
        int numOfQuestion = 0, numOfAnswer = 0;
        for (String i : answer.split(";")) {
            if (!i.equals("-")) {
                numOfAnswer++;
            }
            numOfQuestion++;
        }
        if (numOfQuestion == 0) {
            return 0;
        }
        return (int) Math.round((double) numOfAnswer / numOfQuestion * 100);
    }
}
