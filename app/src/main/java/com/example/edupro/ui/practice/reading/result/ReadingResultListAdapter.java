package com.example.edupro.ui.practice.reading.result;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.edupro.R;

import java.util.ArrayList;

public class ReadingResultListAdapter extends RecyclerView.Adapter<ReadingResultListAdapter.ViewHolder>{
    private final ArrayList<String> answers;
    private final ArrayList<String> correctAnswers;
    private final int part;

    public ReadingResultListAdapter(ArrayList<String> answers, ArrayList<String> correctAnswers, int part) {
        this.answers = answers;
        this.correctAnswers = correctAnswers;
        this.part = part;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View readingResultItem = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.reading_result_viewholder, parent, false);
        return new ViewHolder(readingResultItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String idxAnswer = String.valueOf(position + 1);
        String answer = idxAnswer + ". " + AnswerMapping.mapAnswer(part, answers.get(position));
        String correctAnswer = AnswerMapping.mapAnswer(part, correctAnswers.get(position));

        holder.answer.setText(answer);
        holder.correctAnswer.setText(correctAnswer);
        if (answers.get(position).equals(correctAnswers.get(position))) {
            holder.resultIcon.setImageResource(R.drawable.tick_icon);
            holder.correctAnswer.setTextColor(holder.itemView.getResources().getColor(R.color.material_deep_teal_20));
        } else {
            holder.resultIcon.setImageResource(R.drawable.x_icon);
            holder.correctAnswer.setTextColor(holder.itemView.getResources().getColor(R.color.red_btn_bg_color));
        }
    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView answer;
        TextView correctAnswer;
        ImageView resultIcon;
        public ViewHolder(View itemView) {
            super(itemView);

            answer = itemView.findViewById(R.id.reading_result_answer);
            correctAnswer = itemView.findViewById(R.id.reading_result_correct_answer);
            resultIcon = itemView.findViewById(R.id.reading_result_correct_answer_icon);
        }
    }
}
