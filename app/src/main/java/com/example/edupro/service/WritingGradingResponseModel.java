package com.example.edupro.service;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WritingGradingResponseModel {
    @SerializedName("explanation")
    private String explanation;
    @SerializedName("bard_grade")
    private double bardGrade;
    @SerializedName("model_grade")
    private double modelGrade;
    @SerializedName("warnings")
    private List<String> warnings;

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public double getBardGrade() {
        return bardGrade;
    }

    public void setBardGrade(double bardGrade) {
        this.bardGrade = bardGrade;
    }

    public double getModelGrade() {
        return modelGrade;
    }

    public void setModelGrade(double modelGrade) {
        this.modelGrade = modelGrade;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }
}
