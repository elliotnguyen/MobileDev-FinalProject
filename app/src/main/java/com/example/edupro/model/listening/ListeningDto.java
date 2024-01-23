package com.example.edupro.model.listening;

import android.util.Log;

import com.example.edupro.model.SkillDto;
import com.example.edupro.model.question.QuestionSection;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

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

    public static ListeningDto fromFirebaseData(DataSnapshot listening) {
        String id = (String) listening.child("id").getValue();
        Log.d("ListeningDto", "fromFirebaseData: " + id);
        ArrayList<Long> type = new ArrayList<>();
        for (DataSnapshot typeSnapshot : listening.child("type").getChildren()) {
            type.add((Long) typeSnapshot.getValue());
        }
        long topic = (long) listening.child("topic").getValue();

        String audio = (String) listening.child("content").getValue();
        Log.d("ListeningDto", "fromFirebaseData: " + audio);

//        ArrayList<QuestionSection> questions = new ArrayList<>();
//        questions = handleQuestions(listening);

        ArrayList<QuestionSection> questions = QuestionSection.handleQuestions(listening);

        ArrayList<String> answers = new ArrayList<>();
        for (DataSnapshot answerSnapshot : listening.child("answers").getChildren()) {
            answers.add((String) answerSnapshot.getValue());
        }

        String title = (String) listening.child("title").getValue();
        Log.d("ListeningDto", "fromFirebaseData: " + title);
        return new ListeningDto(id, type, topic, audio, questions, answers, title);
    }

//    private static ArrayList<QuestionSection> handleQuestions(DataSnapshot dataSnapshot) {
//        ArrayList<QuestionSection> questions = new ArrayList<>();
//        for (DataSnapshot questionSection : dataSnapshot.child("questions").getChildren()) {
//            long type = (long) questionSection.child("type").getValue();
//            ArrayList<Question> questionList = new ArrayList<>();
//            for (DataSnapshot question : questionSection.child("questions").getChildren()) {
//                String content = (String) question.child("content").getValue();
//                if (type == 0) {
//                    questionList.add(new TFNGQuestion(content));
//                } else if (type == 1) {
//                    ArrayList<String> options = new ArrayList<>();
//                    for (DataSnapshot option : question.child("choices").getChildren()) {
//                        String optionContent = (String) option.getValue();
//                        options.add(optionContent);
//                    }
//                    questionList.add(new MCQQuestion(content, options));
//                }
//            }
//            questions.add(new QuestionSection(type, questionList));
//        }
//        return questions;
//    }

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