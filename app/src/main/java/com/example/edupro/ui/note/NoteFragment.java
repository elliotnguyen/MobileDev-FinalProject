package com.example.edupro.ui.note;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.edupro.R;
import com.example.edupro.model.Note;
import com.example.edupro.model.User;
import com.example.edupro.viewmodel.NoteViewModel;
import com.example.edupro.viewmodel.UserViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoteFragment extends Fragment {

    private NoteViewModel mViewModel;
    private NoteAdapter myNoteAdapter;
    public static NoteFragment newInstance() {
        return new NoteFragment();
    }
    RecyclerView noteRecyclerView;
    User user;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        noteRecyclerView = view.findViewById(R.id.note_recycler_view_my_words);
        noteRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false));
        myNoteAdapter = new NoteAdapter();
        noteRecyclerView.setAdapter(myNoteAdapter);

        mViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        UserViewModel userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
         user = userViewModel.getUser().getValue();
        // Observe the list of notes
//        Map<String,String> dict = new HashMap<>();
//        dict.put("pollution","O nhiem : ellitot is a person pollution");
//        Note note = new Note("2",user.getId(),"Enviroment",user.getNickName(),dict);
        mViewModel.fetchNotesByUserId(user.getId());
        mViewModel.getMutableNotes().observe(getViewLifecycleOwner(), new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {

                myNoteAdapter.setNoteList(notes);
                myNoteAdapter.notifyDataSetChanged();
            }
        });

        ImageView add_note = view.findViewById(R.id.note_add_note);
        add_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddNoteDialog();
            }
        });
        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(noteRecyclerView);

    }

    private void showAddNoteDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.add_note_dialog);

        // Set dialog size and properties
        RelativeLayout overlay = dialog.findViewById(R.id.add_note_overlay);
        overlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // Close the dialog when the overlay is clicked
            }
        });

        EditText editTextNote = dialog.findViewById(R.id.add_note_name_edit);
        Button btnAddNote = dialog.findViewById(R.id.add_note_btnSave);

        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle adding a new note here
                String noteText = editTextNote.getText().toString();
                Note note = new Note("2",user.getId(),noteText,user.getNickName(),new HashMap<>());

                mViewModel.addNote(note);

                dialog.dismiss(); // Close the dialog after adding the note
            }
        });

        // Show the dialog
        dialog.show();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        // TODO: Use the ViewModel
    }

}