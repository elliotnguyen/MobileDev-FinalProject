package com.example.edupro.ui.practice.reading.practice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edupro.R;
import com.example.edupro.model.reading.ReadingDto;
import com.example.edupro.ui.RecyclerViewClickInterface;

import java.util.ArrayList;

public class ReadingQuestionListAdapter extends RecyclerView.Adapter<ReadingQuestionListAdapter.ViewHolder>{
    private static final String TAG = "QuestionListAdaptter";
    private final ArrayList<String> questionIdx;
    private final RecyclerViewClickInterface recyclerViewClickInterface;

    public ReadingQuestionListAdapter(ArrayList<String> questionIdx, RecyclerViewClickInterface recyclerViewClickInterface) {
        this.questionIdx = questionIdx;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    @NonNull
    @Override
    public ReadingQuestionListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.practice_question_viewholder, parent, false);
        return new ReadingQuestionListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReadingQuestionListAdapter.ViewHolder holder, int position) {
        String questionNumber = String.valueOf(position + 1);
        holder.questionIdx.setText(questionNumber);
        if (!questionIdx.get(position).equals("")) {
            holder.questionIdx.setBackgroundResource(R.drawable.question_finish_background);
        }
        else {
            holder.questionIdx.setBackgroundResource(R.drawable.question_left_background);
        }
    }

    @Override
    public int getItemCount() {
        return questionIdx.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView questionIdx;
        public ViewHolder(View itemView) {
            super(itemView);
            questionIdx = itemView.findViewById(R.id.question_number_text_view);
            itemView.setOnClickListener(v -> recyclerViewClickInterface.onItemClick(getAdapterPosition()));
        }
    }
}
