package com.example.edupro.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.edupro.data.repository.NoteRepository;
import com.example.edupro.model.Note;

public class NoteDetailViewModel extends ViewModel {

    private MutableLiveData<Note> note = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private NoteRepository noteRepository;

    public NoteDetailViewModel() {
        this.noteRepository = new NoteRepository();
    }

    // Method to observe the list of notes
    public MutableLiveData<Note> getMutableNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note.setValue(note);
    }

    // Method to add a note to the list
    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    public void deleteNote(Note note, final NoteRepository.OnNoteDeletedListener listener) {
        isLoading.setValue(true);
        noteRepository.deleteNote(note.getId(), new NoteRepository.OnNoteDeletedListener() {
            @Override
            public void onNoteDeleted() {
                // Handle the completion of the delete operation if needed
                isLoading.setValue(false);
                listener.onNoteDeleted();
            }

            @Override
            public void onError(Exception e) {
                // Handle error if needed
                isLoading.setValue(false);
                listener.onError(e);
            }
        });
    }
    public void updateWord(String word, String description, final NoteRepository.OnNoteUpdatedListener listener) {


        isLoading.setValue(true); // Set loading state to true before updating

        Note currentNote = note.getValue();
        currentNote.getWordList().put(word, description);


        noteRepository.updateNote(currentNote, new NoteRepository.OnNoteUpdatedListener() {
            @Override
            public void onNoteUpdated() {
                isLoading.setValue(false); // Set loading state to false after updating
                listener.onNoteUpdated();
                note.setValue(currentNote);
            }

            @Override
            public void onError(Exception e) {
                isLoading.setValue(false); // Set loading state to false on error
                listener.onError(e);
            }
        });
    }

    public void deleteWord(String word, final NoteRepository.OnNoteUpdatedListener listener) {
        isLoading.setValue(true); // Set loading state to true before deleting

        Note currentNote = note.getValue();
        currentNote.getWordList().remove(word);
        note.setValue(currentNote);

        noteRepository.updateNote(currentNote, new NoteRepository.OnNoteUpdatedListener() {
            @Override
            public void onNoteUpdated() {
                isLoading.setValue(false); // Set loading state to false after deleting
                listener.onNoteUpdated();
            }

            @Override
            public void onError(Exception e) {
                isLoading.setValue(false); // Set loading state to false on error
                listener.onError(e);
            }
        });
    }


}
