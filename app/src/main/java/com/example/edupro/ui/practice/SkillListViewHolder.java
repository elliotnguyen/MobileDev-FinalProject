package com.example.edupro.ui.practice;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edupro.ui.RecyclerViewClickInterface;

public class SkillListViewHolder extends RecyclerView.ViewHolder{
    private final TextView title;
    private final TextView progress;
    private final TextView action;
    private final ProgressBar progressBar;

    public SkillListViewHolder(@NonNull View itemView, int titleId, int progressId, int actionId, int progressBarId, RecyclerViewClickInterface recyclerViewClickInterface) {
        super(itemView);

        title = itemView.findViewById(titleId);
        progress = itemView.findViewById(progressId);
        action = itemView.findViewById(actionId);
        progressBar = itemView.findViewById(progressBarId);

        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewClickInterface.onItemClick(getAdapterPosition());
            }
        });
    }

    public void bind(String title, String progress, String action) {
        this.title.setText(title);
        this.progress.setText(progress);
        this.action.setText(action);

        int progressValue = progress.split("%")[0].equals("") ? 0 : Integer.parseInt(progress.split("%")[0]);
        progressBar.setProgress(progressValue);
    }
}
