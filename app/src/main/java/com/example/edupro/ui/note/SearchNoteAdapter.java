package com.example.edupro.ui.note;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edupro.R;
import com.example.edupro.model.Note;

import java.util.ArrayList;
import java.util.List;

public class SearchNoteAdapter extends RecyclerView.Adapter<SearchNoteAdapter.NoteViewHolder> {

    private List<Note> noteList;

    public SearchNoteAdapter() {
        noteList = new ArrayList<>();
    }
    public void setNoteList(List<Note> noteList) {
        this.noteList.clear();
        this.noteList.addAll(noteList);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_note_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.cateTextView.setText(note.getCategory());
        holder.authorTextView.setText(note.getUser_name());
        holder.countWordTextView.setText(String.valueOf(note.getWordList() != null ? note.getWordList() .size():0) + " terms");

        holder.detailNote.setOnClickListener( v -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("selected_note", (Parcelable) noteList.get(position));
            Navigation.findNavController(holder.itemView).navigate(R.id.navigation_note_detail,bundle);
        });
    }

    @Override
    public int getItemCount() {
        return noteList != null? noteList.size():0;
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView cateTextView;
        TextView countWordTextView;
        TextView authorTextView;
        View detailNote;

        public NoteViewHolder(@NonNull View itemView) {

            super(itemView);
            cateTextView = itemView.findViewById(R.id.note_item_cate_name);
            countWordTextView = itemView.findViewById(R.id.note_item_term_count);
            authorTextView = itemView.findViewById(R.id.note_item_author_name);
            detailNote = itemView.findViewById(R.id.btnContinue);


        }
    }
}
