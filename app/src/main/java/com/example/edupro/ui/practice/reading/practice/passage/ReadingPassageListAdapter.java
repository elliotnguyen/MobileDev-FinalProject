package com.example.edupro.ui.practice.reading.practice.passage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edupro.R;
import com.example.edupro.model.reading.ReadingDto;

public class ReadingPassageListAdapter extends RecyclerView.Adapter<ReadingPassageListAdapter.ReadingPassageListViewHolder> {
    //ArrayList<String> passages;
    ReadingDto readingDto;
//    public ReadingPassageListAdapter(ArrayList<String> passages) {
//        this.passages = passages;
//    }
    public ReadingPassageListAdapter(ReadingDto readingDto) {
        this.readingDto = readingDto;
    }
    @NonNull
    @Override
    public ReadingPassageListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reading_passage_viewholder, parent, false);
        return new ReadingPassageListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReadingPassageListViewHolder holder, int position) {
        //holder.passage.setText(passages.get(position));
        holder.passage.setText(readingDto.getContent().get(position));
    }

    @Override
    public int getItemCount() {
        return readingDto.getContent().size();
    }

    public static class ReadingPassageListViewHolder extends RecyclerView.ViewHolder {
        TextView passage;
        public ReadingPassageListViewHolder(@NonNull View itemView) {
            super(itemView);
            passage = itemView.findViewById(R.id.reading_practice_passage_content);
        }
    }
}
