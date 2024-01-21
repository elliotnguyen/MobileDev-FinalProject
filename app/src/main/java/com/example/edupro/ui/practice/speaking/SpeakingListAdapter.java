package com.example.edupro.ui.practice.speaking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edupro.R;
import com.example.edupro.model.reading.ReadingDto;
import com.example.edupro.model.speaking.SpeakingDto;
import com.example.edupro.ui.RecyclerViewClickInterface;

import java.util.ArrayList;

public class SpeakingListAdapter extends RecyclerView.Adapter<SpeakingListAdapter.SpeakingListViewHolder> {
    ArrayList<SpeakingDto> speakings;
    RecyclerViewClickInterface recyclerViewClickInterface;
    public SpeakingListAdapter(ArrayList<SpeakingDto> speakings, RecyclerViewClickInterface recyclerViewClickInterface) {
        this.speakings = speakings;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }
    @NonNull
    @Override
    public SpeakingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reading_list_viewholder, parent, false);
        return new SpeakingListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpeakingListViewHolder holder, int position) {
        holder.title.setText("Topic");
        holder.progress.setText("0%");
        holder.action.setText("Start");
    }

    @Override
    public int getItemCount() {
        return speakings.size();
    }

    public class SpeakingListViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView progress;
        TextView action;

        public SpeakingListViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.reading_list_viewholder_title);
            progress = itemView.findViewById(R.id.reading_list_viewholder_progressbar_text);
            action = itemView.findViewById(R.id.reading_list_viewholder_action_btn);

            action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerViewClickInterface.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
