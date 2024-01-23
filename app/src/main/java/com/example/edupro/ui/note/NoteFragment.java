package com.example.edupro.ui.note;

import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edupro.R;
import com.example.edupro.api.TestAPI;
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
    private NoteAdapter poplarNoteAdapter;
    public static NoteFragment newInstance() {
        return new NoteFragment();
    }
    RecyclerView noteRecyclerView;
    RecyclerView popularNoteRecyclerView;
    User user;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        UserViewModel userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        user = userViewModel.getUser().getValue();
        mViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        mViewModel.fetchNotesByUserId(user.getId());
        mViewModel.fetchNotesRandomly(10);



        return inflater.inflate(R.layout.fragment_note, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ((TextView)view.findViewById(R.id.note_user_name)).setText("Hello, "+ user.getNickName());
        noteRecyclerView = view.findViewById(R.id.note_recycler_view_my_words);
        noteRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false));

        popularNoteRecyclerView = view.findViewById(R.id.note_recycler_view_popular_words);
        popularNoteRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false));


        myNoteAdapter = new NoteAdapter();
        poplarNoteAdapter = new NoteAdapter();

        noteRecyclerView.setAdapter(myNoteAdapter);
        popularNoteRecyclerView.setAdapter(poplarNoteAdapter);



        SearchView searchView = view.findViewById(R.id.search_view_all );

        // Set up a listener for the search button in the SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle search query submission here
                Bundle bundle = new Bundle();
                bundle.putString("search_term", query);
                Navigation.findNavController(view).navigate(R.id.navigation_search_note,bundle);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle search query change (if needed)
                return false;
            }
        });


        mViewModel.getMutableNotes().observe(getViewLifecycleOwner(), new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {

                myNoteAdapter.setNoteList(notes);
                myNoteAdapter.notifyDataSetChanged();
            }
        });

        mViewModel.getPopularNoteList().observe(getViewLifecycleOwner(), new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {

                poplarNoteAdapter.setNoteList(notes);
                poplarNoteAdapter.notifyDataSetChanged();
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
                if(noteText.isEmpty()) {
                    Toast.makeText(getContext(),"Invalid category",Toast.LENGTH_SHORT).show();
                    return;
                }
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