package com.example.edupro.model.listening;

import com.example.edupro.model.SkillDto;
import com.example.edupro.model.reading.QuestionSection;

import java.util.ArrayList;
import java.util.List;

public class ListeningDto extends SkillDto {
    private final String audio;
    private final ArrayList<QuestionSection> questions;
    private final ArrayList<String> answers;
    private final String title;

    public ListeningDto() {
        super();
        audio = "";
        questions = new ArrayList<>();
        answers = new ArrayList<>();
        title = "";
    }

    public ListeningDto(String id, ArrayList<Long> type, long topic, String audio, ArrayList<QuestionSection> questions, ArrayList<String> answers, String title) {
        super(id, type, topic);
        this.audio = audio;
        this.questions = questions;
        this.answers = answers;
        this.title = title;
    }

    public String getAudio() {
        return audio;
    }

    public ArrayList<QuestionSection> getQuestions() {
        return questions;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public String getTitle() {
        return title;
    }

}