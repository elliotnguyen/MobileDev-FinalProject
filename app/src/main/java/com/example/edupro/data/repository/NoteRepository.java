package com.example.edupro.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.edupro.model.Note;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

public class NoteRepository {
    private final DatabaseReference databaseReference;

    public NoteRepository() {
        this.databaseReference = FirebaseDatabase.getInstance().getReference().child("notes");
    }

    public void addNote(Note note) {
        String noteId = databaseReference.push().getKey();

        // Set the generated key as the note's ID
        note.setId(noteId);
        databaseReference.child(note.getId()).setValue(note);
    }

    public void updateNote(Note note, final OnNoteUpdatedListener listener) {
        // Update the note in the database based on note's ID
        try {
            databaseReference.child(note.getId()).setValue(note)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("udpated","Herre");

                            listener.onNoteUpdated();
                        } else {
                            Log.d("Loi1","Herre");

                            listener.onError(task.getException());
                        }
                    });
        }catch (Exception e){
            Log.e("Loi2","Herre");
            listener.onError(e);
        }

    }
    public void getNotesByUserId(String userId, final OnNotesFetchedListener listener) {
        Query query = databaseReference.orderByChild("user_id").equalTo(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Note> notes = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Note note = snapshot.getValue(Note.class);

                    if (note != null) {
                        if(note.getWordList() == null) note.setWordList(new HashMap<>());

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

    public void getRandomNotes(int numberOfNotes, final OnNotesFetchedListener listener) {

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Note> notes = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Note note = snapshot.getValue(Note.class);

                    if (note != null) {
                        if(note.getWordList() == null) note.setWordList(new LinkedHashMap<>());
                        notes.add(note);
                    }
                }

                if (notes.isEmpty()) {
                    listener.onError(new Exception("No notes found"));
                    return;
                }

                // Ensure the number of requested notes does not exceed the available notes
                int currentNumberOfNotes = Math.min(numberOfNotes, notes.size());

                // Randomly select the specified number of notes
                Random random = new Random();
                List<Note> randomNotes = new ArrayList<>();
                for (int i = 0; i < currentNumberOfNotes; i++) {
                    int randomIndex = random.nextInt(notes.size());
                    randomNotes.add(notes.get(randomIndex));
                    notes.remove(randomIndex);
                }

                listener.onNotesFetched(randomNotes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                listener.onError(databaseError.toException());
            }
        });
    }

    public interface OnNotesFetchedListener {
        void onNotesFetched(List<Note> notes);

        void onError(Exception e);
    }

    public interface OnNoteUpdatedListener {
        void onNoteUpdated();
        void onError(Exception e);
    }
    public void deleteNote(String noteId, final OnNoteDeletedListener listener) {
        databaseReference.child(noteId).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listener.onNoteDeleted();
            } else {
                listener.onError(task.getException());
            }
        });
    }

    public interface OnNoteDeletedListener {
        void onNoteDeleted();
        void onError(Exception e);
    }

    public void getNote(String noteId, final OnNoteFetchedListener listener) {
        // Retrieve a note from the database based on note's ID
        databaseReference.child(noteId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Note note = dataSnapshot.getValue(Note.class);
                if (note != null) {
                    if(note.getWordList() == null) note.setWordList(new HashMap<>());
                    listener.onNoteFetched(note);
                } else {
                    // Handle the case where the note with the specified ID is not found
                    listener.onError(new Exception("Note not found"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
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


    public void searchNotesByCategory(String searchTerm, final OnNotesFetchedListener listener) {
        Query query = databaseReference.orderByChild("category");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Note> notes = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Note note = snapshot.getValue(Note.class);
                    if (note != null && note.getCategory() != null && note.getCategory().toLowerCase().contains(searchTerm.toLowerCase())) {
                        if(note.getWordList() == null) note.setWordList(new LinkedHashMap<>());
                        notes.add(note);
                        Log.d("yes",note.getCategory());

                    }
                }

                listener.onNotesFetched(notes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                listener.onError(databaseError.toException());
            }
        });
    }
}
