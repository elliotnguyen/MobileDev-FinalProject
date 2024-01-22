package com.example.edupro.ui.note;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.edupro.R;

public class EditWordDialogFragment extends DialogFragment {

    private EditText termEditText;
    private EditText descriptionEditText;
    private Button saveButton;

    private String currentTerm;
    private String currentDescription;
    private OnSaveClickListener onSaveClickListener;
    boolean isDisableTerm;
    public EditWordDialogFragment() {
        // Required empty public constructor
    }

    public static EditWordDialogFragment newInstance(String term, String description, boolean isDisableTerm) {
        EditWordDialogFragment fragment = new EditWordDialogFragment();
        Bundle args = new Bundle();
        args.putString("term", term);
        args.putString("description", description);
        args.putBoolean("isDisableTerm", isDisableTerm);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentTerm = getArguments().getString("term", "");
            currentDescription = getArguments().getString("description", "");
            isDisableTerm = getArguments().getBoolean("isDisableTerm", false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.word_edit_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        termEditText = view.findViewById(R.id.add_word_term_edit);
        descriptionEditText = view.findViewById(R.id.add_word_description_edit);
        saveButton = view.findViewById(R.id.add_note_btnSave);

        termEditText.setText(currentTerm);
        if(isDisableTerm) termEditText.setEnabled(false);
        descriptionEditText.setText(currentDescription);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String term = termEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                if (onSaveClickListener != null) {
                    onSaveClickListener.onSaveClick(term, description);
                }
                dismiss();
            }
        });
    }

    public void setOnSaveClickListener(OnSaveClickListener onSaveClickListener) {
        this.onSaveClickListener = onSaveClickListener;
    }

    public interface OnSaveClickListener {
        void onSaveClick(String term, String description);
    }
}
