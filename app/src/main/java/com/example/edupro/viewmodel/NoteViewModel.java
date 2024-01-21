package com.example.edupro.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.edupro.data.repository.NoteRepository;
import com.example.edupro.model.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteViewModel extends ViewModel {
    private MutableLiveData<List<Note>> noteList = new MutableLiveData<>();
    private NoteRepository noteRepository;

    public NoteViewModel() {
        this.noteRepository = new NoteRepository();
    }

    // Method to observe the list of notes
    public MutableLiveData<List<Note>> getMutableNotes() {
        return noteList;
    }

    // Method to add a note to the list
    public void addNote(Note note) {
        List<Note> currentNotes = noteList.getValue();
        if (currentNotes == null) {
            currentNotes = new ArrayList<>();
        }

        currentNotes.add(note);
        noteList.setValue(currentNotes);

        noteRepository.addNote(note);
    }

    // Method to fetch notes from the repository based on user_id
    public void fetchNotesByUserId(String userId) {
        noteRepository.getNotesByUserId(userId, new NoteRepository.OnNotesFetchedListener() {
            @Override
            public void onNotesFetched(List<Note> notes) {
                // Update the MutableLiveData with the new list of notes
                noteList.setValue(notes);
            }

            @Override
            public void onError(Exception e) {
                // Handle errors, if any
            }
        });
    }
}
