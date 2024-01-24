package com.example.edupro.api;


import com.google.gson.annotations.SerializedName;

public class SpeakingGradingResponseModel {
    @SerializedName("explaination")
    private String explaination;
    @SerializedName("grade")
    private double grade;

    public String getExplanation() {
        return explaination;
    }

    public void setExplanation(String explanation) {
        this.explaination = explanation;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }


}
