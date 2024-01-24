package com.example.edupro.ui.practice.speaking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edupro.R;
import com.example.edupro.model.AnswerDto;
import com.example.edupro.model.listening.ListeningDto;
import com.example.edupro.model.reading.ReadingDto;
import com.example.edupro.model.speaking.SpeakingDto;
import com.example.edupro.ui.RecyclerViewClickInterface;
import com.example.edupro.ui.practice.SkillListAdapterBase;
import com.example.edupro.ui.practice.SkillListViewHolder;

import java.util.ArrayList;

public class SpeakingListAdapter extends RecyclerView.Adapter{
    ArrayList<SpeakingDto> speakings;
    RecyclerViewClickInterface recyclerViewClickInterface;
    private final SkillListAdapterBase baseListAdapter;
    private final ArrayList<AnswerDto> answers;


    public SpeakingListAdapter(ArrayList<SpeakingDto> speakings, ArrayList<AnswerDto> answers, RecyclerViewClickInterface recyclerViewClickInterface, int resourceId, int titleId, int progressId, int actionId, int progressBarId) {
        this.speakings = speakings;
        this.answers = answers;
        baseListAdapter = new SkillListAdapterBase(recyclerViewClickInterface, resourceId, titleId, progressId, actionId, progressBarId);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(baseListAdapter.resourceId, parent, false);
        return new SkillListViewHolder(view, baseListAdapter.titleId, baseListAdapter.progressId, baseListAdapter.actionId, baseListAdapter.progressBarId, baseListAdapter.recyclerViewClickInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String title = speakings.get(position).getTitle();
        String progress = "0%";
        String action = "Start";

        if (answers.size() > 0 && answers.size() > position) {
            if (answers.get(position).getSubmitted() != null && answers.get(position).getSubmitted()) {
                progress = "100%";
                action = "Retry: " + answers.get(position).getScore();
            } else {
                progress = String.valueOf(answers.get(position).getProgress()) + "%";
                action = "Continue";
            }
        }

        ((SkillListViewHolder) holder).bind(title,
                progress,
                action);
    }

    @Override
    public int getItemCount() {
        return speakings.size();
    }
}
