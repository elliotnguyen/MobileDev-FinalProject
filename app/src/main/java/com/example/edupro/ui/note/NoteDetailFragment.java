package com.example.edupro.ui.note;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.edupro.R;
import com.example.edupro.model.Note;

public class NoteDetailFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_note, container, false);

        // Retrieve the data from the arguments Bundle
        Bundle args = getArguments();
        if (args != null) {
            Note selectedNote = args.getParcelable("selected_note");
            Log.d("a",selectedNote.getWordList().toString());
            // Now you have the selected note, and you can use its data as needed
        }
        else Log.e("err","null");

        return view;
    }
}