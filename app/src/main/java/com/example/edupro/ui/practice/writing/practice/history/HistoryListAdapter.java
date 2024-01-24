package com.example.edupro.ui.practice.writing.practice.history;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edupro.R;
import com.example.edupro.model.AnswerDto;
import com.example.edupro.ui.RecyclerViewClickInterface;

import java.util.ArrayList;

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.HistoryViewHolder> {
    private static final String TAG = "HistoryListAdapter";
    private final ArrayList<AnswerDto> answerDtos;
    private final RecyclerViewClickInterface recyclerViewClickInterface;
    private final String username;
    public HistoryListAdapter(String username, ArrayList<AnswerDto> answerDtos, RecyclerViewClickInterface recyclerViewClickInterface) {
        this.answerDtos = answerDtos;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
        this.username = username;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View historySubmissionView = LayoutInflater.from(parent.getContext()).inflate(R.layout.writing_history_viewholder, parent, false);
        return new HistoryViewHolder(historySubmissionView);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        holder.historySubmissionContent.setText(answerDtos.get(position).getAnswer());
        holder.historySubmissionDate.setText(answerDtos.get(position).getTimeStamp());
        holder.historySubmissionName.setText(username);
        String score = "Grade: " + answerDtos.get(position).getScore();
        holder.historySubmissionGrade.setText(answerDtos.get(position).getScore());
    }

    @Override
    public int getItemCount() {
        return answerDtos.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView historySubmissionGrade;
        TextView historySubmissionName;
        TextView historySubmissionContent;
        TextView historySubmissionDate;
        public HistoryViewHolder(View itemView) {
            super(itemView);

            historySubmissionGrade = itemView.findViewById(R.id.writing_grade);
            historySubmissionName = itemView.findViewById(R.id.writing_author_name);
            historySubmissionContent = itemView.findViewById(R.id.writing_content);
            historySubmissionDate = itemView.findViewById(R.id.writing_date);

            itemView.setOnClickListener(v -> {
                recyclerViewClickInterface.onItemClick(getAdapterPosition());
            });
        }
    }
}
