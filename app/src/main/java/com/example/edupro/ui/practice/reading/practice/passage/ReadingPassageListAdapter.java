package com.example.edupro.ui.practice.reading.practice.passage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edupro.R;

import java.util.ArrayList;

public class ReadingPassageListAdapter extends RecyclerView.Adapter<ReadingPassageListAdapter.ReadingPassageListViewHolder> {
    ArrayList<String> passages;
    public ReadingPassageListAdapter(ArrayList<String> passages) {
        this.passages = passages;
    }
    @NonNull
    @Override
    public ReadingPassageListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reading_passage_viewholder, parent, false);
        return new ReadingPassageListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReadingPassageListViewHolder holder, int position) {
        holder.passage.setText(passages.get(position));
    }

    @Override
    public int getItemCount() {
        return passages.size();
    }

    public static class ReadingPassageListViewHolder extends RecyclerView.ViewHolder {
        TextView passage;
        public ReadingPassageListViewHolder(@NonNull View itemView) {
            super(itemView);
            passage = itemView.findViewById(R.id.reading_practice_passage_content);
        }
    }
}
