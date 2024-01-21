package com.example.edupro.ui.practice.listening.practice.question;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edupro.R;
import com.example.edupro.model.reading.MCQQuestion;
import com.example.edupro.ui.practice.listening.practice.ListeningPracticeViewModel;
import com.example.edupro.ui.practice.reading.practice.ReadingPracticeViewModel;
import com.example.edupro.ui.practice.reading.practice.question.MCQClickInterface;

import java.util.ArrayList;

public class MCQListAdapter extends RecyclerView.Adapter<MCQListAdapter.ViewHolder> {
    private int index = 0;
    private ArrayList<MCQQuestion> mcqQuestions = new ArrayList<>();
    private final MCQClickInterface mcqClickInterface;
    private final ListeningPracticeViewModel listeningPracticeViewModel;
    public MCQListAdapter(ArrayList<MCQQuestion> mcqQuestions, int index, ListeningPracticeViewModel listeningPracticeViewModel, MCQClickInterface mcqClickInterface) {
        this.mcqQuestions = mcqQuestions;
        this.index = index;
        this.mcqClickInterface = mcqClickInterface;
        this.listeningPracticeViewModel = listeningPracticeViewModel;
    }
    @NonNull
    @Override
    public MCQListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(com.example.edupro.R.layout.reading_mcq_question_layout, parent, false);
        return new MCQListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MCQListAdapter.ViewHolder holder, int position) {
        String questionNumber = "# Question " + (this.index + position + 1);
        holder.questionNumber.setText(questionNumber);
        String questionContent = mcqQuestions.get(position).getContent();
        holder.questionContent.setText(questionContent);
        holder.optionA.setText(mcqQuestions.get(position).getOptions().get(0));
        holder.optionB.setText(mcqQuestions.get(position).getOptions().get(1));
        holder.optionC.setText(mcqQuestions.get(position).getOptions().get(2));
        holder.optionD.setText(mcqQuestions.get(position).getOptions().get(3));

        String answer = listeningPracticeViewModel.getAnswerAtIndex(position + index).getValue();
        if (answer != null) {
            switch (answer) {
                case "0":
                    holder.optionA.setChecked(true);
                    break;
                case "1":
                    holder.optionB.setChecked(true);
                    break;
                case "2":
                    holder.optionC.setChecked(true);
                    break;
                case "3":
                    holder.optionD.setChecked(true);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mcqQuestions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView questionNumber;
        TextView questionContent;
        RadioButton optionA;
        RadioButton optionB;
        RadioButton optionC;
        RadioButton optionD;
        RadioGroup radioGroup;

        public ViewHolder(View itemView) {
            super(itemView);

            questionNumber = itemView.findViewById(com.example.edupro.R.id.reading_mcq_question_number);
            questionContent = itemView.findViewById(com.example.edupro.R.id.reading_mcq_question_content);

            optionA = itemView.findViewById(R.id.radioButton_reading_a);
            optionB = itemView.findViewById(R.id.radioButton_reading_b);
            optionC = itemView.findViewById(R.id.radioButton_reading_c);
            optionD = itemView.findViewById(R.id.radioButton_reading_d);

            radioGroup = itemView.findViewById(com.example.edupro.R.id.radioGroup_mcq_question);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    String option = "";
                    if (checkedId == R.id.radioButton_reading_a) {
                        option = "0";
                    } else if (checkedId == R.id.radioButton_reading_b) {
                        option = "1";
                    } else if (checkedId == R.id.radioButton_reading_c) {
                        option = "2";
                    } else if (checkedId == R.id.radioButton_reading_d) {
                        option = "3";
                    }
                    mcqClickInterface.onItemClick(getAdapterPosition(), option);
                }
            });
        }
    }
}

