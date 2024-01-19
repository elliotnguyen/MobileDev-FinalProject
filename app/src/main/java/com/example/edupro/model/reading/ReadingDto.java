package com.example.edupro.model.reading;

import com.example.edupro.model.SkillDto;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class ReadingDto extends SkillDto {
    private final String title;
    private final ArrayList<String> content;
    private final ArrayList<QuestionSection> questions;
    private final ArrayList<String> answers;
    public ReadingDto() {
        super();
        title = "";
        content = new ArrayList<>();
        questions = new ArrayList<>();
        answers = new ArrayList<>();
    }

    public ReadingDto(String id, ArrayList<Long> type, long topic, String title, ArrayList<String> content, ArrayList<QuestionSection> questions, ArrayList<String> answers) {
        super(id, type, topic);
        this.title = title;
        this.content = content;
        this.questions = questions;
        this.answers = answers;
    }

    public static ReadingDto fromFirebaseData(DataSnapshot dataSnapshot) {
        String id = (String) dataSnapshot.child("id").getValue();
        ArrayList<Long> type = (ArrayList<Long>) dataSnapshot.child("type").getValue();
        long topic = (long) dataSnapshot.child("topic").getValue();
        String title = (String) dataSnapshot.child("title").getValue();

        ArrayList<String> content = new ArrayList<>();
        for (DataSnapshot contentSection : dataSnapshot.child("content").getChildren()) {
            String contentPart = (String) contentSection.getValue();
            content.add(contentPart);
        }
        ArrayList<QuestionSection> questions = handleQuestions(dataSnapshot);
        ArrayList<String> answers = new ArrayList<>();
        for (DataSnapshot answerSection : dataSnapshot.child("answers").getChildren()) {
            String answer = (String) answerSection.getValue();
            answers.add(answer);
        }
        return new ReadingDto(id, type, topic, title, content, questions, answers);
    }

    private static ArrayList<QuestionSection> handleQuestions(DataSnapshot dataSnapshot) {
        ArrayList<QuestionSection> questions = new ArrayList<>();
        for (DataSnapshot questionSection : dataSnapshot.child("questions").getChildren()) {
            long type = (long) questionSection.child("type").getValue();
            ArrayList<Question> questionList = new ArrayList<>();
            for (DataSnapshot question : questionSection.child("questions").getChildren()) {
                String content = (String) question.child("content").getValue();
                if (type == 0) {
                    questionList.add(new TFNGQuestion(content));
                } else if (type == 1) {
                    ArrayList<String> options = new ArrayList<>();
                    for (DataSnapshot option : question.child("choices").getChildren()) {
                        String optionContent = (String) option.getValue();
                        options.add(optionContent);
                    }
                    questionList.add(new MCQQuestion(content, options));
                }
            }
            questions.add(new QuestionSection(type, questionList));
        }
        return questions;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<String> getContent() {
        return content;
    }

    public ArrayList<QuestionSection> getQuestions() {
        return questions;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }
}
