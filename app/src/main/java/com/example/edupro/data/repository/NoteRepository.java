package com.example.edupro.data.repository;

import android.util.Log;

import com.example.edupro.model.Note;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NoteRepository {
    private DatabaseReference databaseReference;

    public NoteRepository() {
        this.databaseReference = FirebaseDatabase.getInstance().getReference().child("notes");
    }

    public void addNote(Note note) {
        String noteId = databaseReference.push().getKey();

        // Set the generated key as the note's ID
        note.setId(noteId);
        databaseReference.child(note.getId()).setValue(note);
    }

    public void updateNote(Note note) {
        // Update the note in the database based on note's ID
        databaseReference.child(note.getId()).setValue(note);
    }
    public void getNotesByUserId(String userId, final OnNotesFetchedListener listener) {
        Query query = databaseReference.orderByChild("user_id").equalTo(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Note> notes = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Note note = snapshot.getValue(Note.class);
                    if(note.getWordList() == null) note.setWordList(new HashMap<>());

                    if (note != null) {
                        notes.add(note);
                    }
                }
                listener.onNotesFetched(notes);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
                listener.onError(databaseError.toException());
            }
        });
    }
    public interface OnNotesFetchedListener {
        void onNotesFetched(List<Note> notes);

        void onError(Exception e);
    }
    public void deleteNote(String noteId) {
        // Delete the note from the database based on note's ID
        databaseReference.child(noteId).removeValue();
    }

    public void getNote(String noteId, final OnNoteFetchedListener listener) {
        // Retrieve a note from the database based on note's ID
        databaseReference.child(noteId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Note note = dataSnapshot.getValue(Note.class);
                if(note.getWordList() == null) note.setWordList(new HashMap<>());
                if (note != null) {
                    listener.onNoteFetched(note);
                } else {
                    // Handle the case where the note with the specified ID is not found
                    listener.onError(new Exception("Note not found"));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
                listener.onError(databaseError.toException());
            }
        });
    }

    // Interface for callback when note is fetched
    public interface OnNoteFetchedListener {
        void onNoteFetched(Note note);

        void onError(Exception e);
    }
}
