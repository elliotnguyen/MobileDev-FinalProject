package com.example.edupro.service.response;

import com.google.gson.annotations.SerializedName;

public class SpeakingGradingReponse {
    @SerializedName("grade")
    private double grade;

    @SerializedName("explaination")
    private String explaination;

    public double getGrade() {
        return grade;
    }

    public String getExplaination() {
        return explaination;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public void setExplaination(String explaination) {
        this.explaination = explaination;
    }

    public SpeakingGradingReponse(double grade, String explaination) {
        this.grade = grade;
        this.explaination = explaination;
    }
}
