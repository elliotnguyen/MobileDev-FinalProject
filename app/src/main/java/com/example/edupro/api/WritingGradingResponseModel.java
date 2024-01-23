package com.example.edupro.api;

import java.util.List;

public class WritingGradingResponseModel {
    private String explanation;
    private double bardGrade;
    private double modelGrade;
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
