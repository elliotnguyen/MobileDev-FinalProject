package com.example.edupro.ui.note;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edupro.R;
import com.example.edupro.data.repository.NoteRepository;
import com.example.edupro.model.Note;

import java.util.List;

public class SearchNoteFragment extends Fragment {

    private SearchView searchView;
    private NoteRepository noteRepository;
    private SearchNoteAdapter noteAdapter;
    private RecyclerView recyclerView;

    public SearchNoteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_note, container, false);

        searchView = view.findViewById(R.id.search_view_all);

        noteRepository = new NoteRepository();
        noteAdapter = new SearchNoteAdapter();
        recyclerView = view.findViewById(R.id.note_recycler_view_my_words);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(noteAdapter);


        View backBtn = view.findViewById(R.id.note_search_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().onBackPressed();
            }
        });


        Bundle args = getArguments();
        if (args != null) {
            String search = args.getString("search_term");
           if(search != null && !search.isEmpty())
               performSearch(search);
        } else {
            Log.e("err", "null");
        }


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle search when the user submits the query
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle search as the user types
//                performSearch(newText);
                return true;
            }
        });
        return view;
    }

    private void performSearch(String term) {
Log.d("search now", term);
        if (!term.isEmpty()) {
            noteRepository.searchNotesByCategory(term, new NoteRepository.OnNotesFetchedListener() {
                @Override
                public void onNotesFetched(List<Note> notes) {
                    // Update the RecyclerView with the search results
                    Log.d("detached", notes.size()+term);

                    noteAdapter.setNoteList(notes);
                }

                @Override
                public void onError(Exception e) {
                    // Handle search error
                }
            });
        }
    }
}

