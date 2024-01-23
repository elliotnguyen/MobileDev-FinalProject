package com.example.edupro.ui.note;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edupro.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NoteDetailAdapter extends RecyclerView.Adapter<NoteDetailAdapter.NoteViewHolder> {

    private Map<String, String> wordMap;
    private boolean isAllowEdit;
    private boolean isAllowEditTerm;
    private boolean isAllowDelete;
    List<String> wordList;

    public NoteDetailAdapter(Map<String, String> wordMap, boolean isAllowEdit) {
        this.wordMap = wordMap;
        this.isAllowEdit = isAllowEdit;
        wordList = new ArrayList<>(wordMap.keySet());
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView wordNameTextView;
        TextView descriptionTextView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            wordNameTextView = itemView.findViewById(R.id.note_detail_item_word_name);
            descriptionTextView = itemView.findViewById(R.id.note_detail_item_description);
        }
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_note_detail_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        wordList = new ArrayList<>(wordMap.keySet());
        String word = wordList.get(position);
        String description = wordMap.get(word);

        holder.wordNameTextView.setText(word);
        holder.descriptionTextView.setText(description);
    }

    @Override
    public int getItemCount() {
        return wordMap.size();
    }

    // Method to update the map data
    public void setWordMap(Map<String, String> wordMap) {
        this.wordMap = wordMap;
        notifyDataSetChanged();
    }
}
