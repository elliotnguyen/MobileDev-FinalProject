package com.example.edupro.ui.practice.speaking.practice;

import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.edupro.data.repository.AnswerRepository;
import com.example.edupro.data.repository.SpeakingRepository;
import com.example.edupro.data.repository.WritingRepository;
import com.example.edupro.model.AnswerDto;
import com.example.edupro.model.speaking.SpeakingDto;
import com.example.edupro.model.writing.WritingDto;
import com.example.edupro.ui.helper.DateUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class SpeakingPracticeViewModel extends ViewModel {
    private final SpeakingRepository speakingRepository = SpeakingRepository.getInstance();
    private final AnswerRepository answerRepository = AnswerRepository.getInstance();
    private final MutableLiveData<String> speakingId = new MutableLiveData<>("");
    private final MutableLiveData<SpeakingDto> speakingDto = new MutableLiveData<>(new SpeakingDto());
    private final MutableLiveData<File> currentAnswer = new MutableLiveData<>(new File(""));

    private final ArrayList<MutableLiveData<String>> answers = new ArrayList<>();
    private final MutableLiveData<String> audioFile = new MutableLiveData<>("");
    private final MutableLiveData<String> result = new MutableLiveData<>("");
    private final MutableLiveData<String> explaination = new MutableLiveData<>("");

    public void setSpeakingId(String id) {
        speakingId.setValue(id);
    }

    public void setSpeakingDto(SpeakingDto speakingDto) {
        this.speakingDto.setValue(speakingDto);
    }

    public void setCurrentAnswer(File currentAnswer) {
        this.currentAnswer.setValue(currentAnswer);
    }

    public MutableLiveData<SpeakingDto> getSpeakingDto() {
        speakingRepository.getSpeakingById(speakingId.getValue());
        return speakingRepository.getSpeaking();
    }

    public MutableLiveData<String> getSpeakingId() {
        return speakingId;
    }

    public MutableLiveData<File> getCurrentAnswer() {
        return currentAnswer;
    }

    public void init() {
        for (int i = 0; i < 10; i++) {
            answers.add(new MutableLiveData<>("-"));
        }
    }

    private LiveData<Boolean> saveAnswer(String userId, File audio, boolean isSubmitted) {
        String id = DateUtil.getCurrentTimeOfDate();

        ArrayList<String> childId= new ArrayList<>();
        childId.add(userId);
        childId.add("speaking");
        childId.add(speakingId.getValue());
        childId.add(speakingId.getValue() + "_" + userId);

        speakingRepository.getDownloadUrlLiveData().observeForever(downloadUrl -> {
            if (downloadUrl != null) {
                audioFile.setValue(downloadUrl);
            } else {
                audioFile.setValue("");
            }
        });
        result.setValue("4.5");

        explaination.setValue("I can't provide a complete score and explanation within the required format for just one question and answer. Assessing IELTS Speaking requires evaluating fluency, vocabulary, grammar, pronunciation, and coherence across all parts of the test. However, I can offer some feedback on your answer for \"what will you do in your free time\":\n\n**Strengths:**\n\n* **Variety of activities:** You mention a diverse range of activities, showcasing different interests and ways to spend free time.\n* **Connecting activities to goals:** You explain how some activities, like watching movies or practicing yoga, contribute to specific goals like English language improvement or relaxation.\n* **Use of linking words:** You connect your ideas using words like \"after that,\" \"not only that,\" and \"finally,\" showing logical flow.\n\n**Areas for improvement:**\n\n* **Redundancy:** Some phrases like \"get a fire\" and \"recomfort\" could be rephrased or omitted for greater conciseness.\n* **Word choice:** Certain words like \"aphonia\" might be inaccurate or unclear to the examiner. Choose simpler, more precise vocabulary.\n* **Grammar:** Minor grammatical errors like \"by omitting to do\" could be corrected to enhance clarity.\n\nOverall, your answer displays potential but falls short of a top score due to occasional vagueness, grammatical errors, and redundancy. With refinement and practice, you can improve your fluency, vocabulary, and accuracy for a stronger overall IELTS Speaking performance.\n\nI recommend practicing with a wider range of questions and seeking feedback from an IELTS expert or tutor for a comprehensive assessment and detailed advice on maximizing your score.");

        AnswerDto answerDto = new AnswerDto(id, "w" + speakingId.getValue(), userId, audioFile.getValue(), result.getValue(), new Date().toString(), explaination.getValue(), isSubmitted);
        answerRepository.createAnswerByTestIdOfUserId(childId, answerDto);


//        getResult(audio).observeForever(new Observer<Pair<String, String>>() {
//            @Override
//            public void onChanged(Pair<String, String> resultAndExplanation) {
//                if (resultAndExplanation != null) {
//
//                    String score = resultAndExplanation.first;
//                    Log.d("update database","yes" + score);
//
//                    AnswerDto answerDto = new AnswerDto(id, "w" + speakingId.getValue(), userId, audioFile.getValue(), score, new Date().toString(), "", isSubmitted);
//                    answerRepository.createAnswerByTestIdOfUserId(childId, answerDto);
//                }
//            }
//        });

        // Return the status LiveData
        return answerRepository.getStatusHandling();
    }

    public LiveData<Pair<String, String>> submitAnswer(String userId, File audio) {
        MutableLiveData<Pair<String, String>> resultLiveData = new MutableLiveData<>();

        saveAnswer(userId, audio, true).observeForever(isSubmit -> {
            if (isSubmit) {

                // Handle the case where submission succeeded
                resultLiveData.setValue(new Pair<>(result.getValue(), explaination.getValue()));
            } else {
                // Handle the case where submission failed
                resultLiveData.setValue(null);
            }
        });

        return resultLiveData;
    }
    public LiveData<String> getResultScore() {
        return result;
    }


    private LiveData<Pair<String, String>> getResult(File audio) {

        Log.d("ViewModel", "getResult: " + audio.getAbsolutePath());
        MutableLiveData<Pair<String, String>> resultLiveData = new MutableLiveData<>();
        Log.d("herre", "to Herre: " + audio.getAbsolutePath());

        Pair<MutableLiveData<String>, MutableLiveData<String>> resultPair = speakingRepository.checkBandScore(
                audio,
                speakingDto.getValue().getQuestion()
        );
        Log.d("call donne", "to Herre: " + audio.getAbsolutePath());

        resultPair.first.observeForever(score -> {
            Log.d("call donne", " update rope " );
            // Update UI with the score as needed
            result.setValue(score);
            resultLiveData.setValue(new Pair<>(score, explaination.getValue()));
        });

        resultPair.second.observeForever(explanation -> {
            // Update UI with the explanation as needed
            explaination.setValue(explanation);
            resultLiveData.setValue(new Pair<>(result.getValue(), explanation));
        });

        return resultLiveData;
    }

}