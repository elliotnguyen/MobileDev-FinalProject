package com.example.edupro.ui.note;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private OnEditClickListener onEditClickListener;
    private OnDeleteClickListener onDeleteClickListener;

    public NoteDetailAdapter(Map<String, String> wordMap, boolean isAllowEdit) {
        this.wordMap = wordMap;
        this.isAllowEdit = isAllowEdit;
        wordList = new ArrayList<>(wordMap.keySet());

    }



    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView wordNameTextView;
        TextView descriptionTextView;
        Button editBtn;
        View deleteBtn;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            wordNameTextView = itemView.findViewById(R.id.note_detail_item_word_name);
            descriptionTextView = itemView.findViewById(R.id.note_detail_item_description);
            editBtn = itemView.findViewById(R.id.note_detail_edit);
            deleteBtn = itemView.findViewById(R.id.delete_btn);


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

        if (isAllowEdit) {
            holder.editBtn.setVisibility(View.VISIBLE);
            holder.deleteBtn.setVisibility(View.VISIBLE);
            holder.editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onEditClickListener != null) {
                        onEditClickListener.onEditClick(wordList.get(holder.getAdapterPosition()));
                    }
                }
            });
            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onDeleteClickListener != null) {
                        onDeleteClickListener.onDeleteClick(wordList.get(holder.getAdapterPosition()));
                    }
                }
            });
        } else {
            Log.d("an btn","an");
            holder.editBtn.setVisibility(View.GONE);
            holder.deleteBtn.setVisibility(View.GONE);
        }
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

    // Method to enable or disable editing
    public void setAllowEdit(boolean isAllowEdit) {
        this.isAllowEdit = isAllowEdit;
        notifyDataSetChanged();
    }


    public interface OnEditClickListener {
        void onEditClick(String word);
    }

    // Set the click listener from the outside
    public void setOnEditClickListener(OnEditClickListener listener) {
        this.onEditClickListener = listener;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(String word);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }
}
