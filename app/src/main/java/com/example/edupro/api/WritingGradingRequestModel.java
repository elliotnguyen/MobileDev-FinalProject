package com.example.edupro.api;

public class WritingGradingRequestModel {
    private   String question;
    private  String answer;

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getQuestion() {
        return question;
    }
}
