package com.example.edupro.api;

public class WritingGradingRequestModel {
    private   String question_data;
    private  String answer_data;
    public WritingGradingRequestModel() {
    }
    public WritingGradingRequestModel(String question, String answer) {
        this.question_data = question;
        this.answer_data = answer;
    }

    public void setAnswer(String answer) {
        this.answer_data = answer;
    }

    public void setQuestion(String question) {
        this.question_data = question;
    }

    public String getAnswer() {
        return answer_data;
    }

    public String getQuestion() {
        return question_data;
    }
}
