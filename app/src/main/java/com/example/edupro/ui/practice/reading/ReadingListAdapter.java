package com.example.edupro.ui.practice.reading;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edupro.R;
import com.example.edupro.model.ReadingDto;
import com.example.edupro.ui.RecyclerViewClickInterface;

import java.util.ArrayList;

public class ReadingListAdapter extends RecyclerView.Adapter<ReadingListAdapter.ReadingListViewHolder> {
    ArrayList<ReadingDto> readings;
    RecyclerViewClickInterface recyclerViewClickInterface;
    public ReadingListAdapter(ArrayList<ReadingDto> readings, RecyclerViewClickInterface recyclerViewClickInterface) {
        this.readings = readings;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }
    @NonNull
    @Override
    public ReadingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reading_list_viewholder, parent, false);
        return new ReadingListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReadingListViewHolder holder, int position) {
        holder.readingTitle.setText(readings.get(position).getTitle());
        holder.readingProgress.setText("0%");
        holder.readingAction.setText("Start");
    }

    @Override
    public int getItemCount() {
        return readings.size();
    }

    public class ReadingListViewHolder extends RecyclerView.ViewHolder {
        TextView readingTitle;
        TextView readingProgress;
        TextView readingAction;

        public ReadingListViewHolder(@NonNull View itemView) {
            super(itemView);
            readingTitle = itemView.findViewById(R.id.reading_list_viewholder_title);
            readingProgress = itemView.findViewById(R.id.reading_list_viewholder_progressbar_text);
            readingAction = itemView.findViewById(R.id.reading_list_viewholder_action_btn);

            readingAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerViewClickInterface.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
