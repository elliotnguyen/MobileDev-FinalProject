package com.example.edupro.ui.note;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edupro.R;
import com.example.edupro.data.repository.NoteRepository;
import com.example.edupro.model.Note;
import com.example.edupro.ui.helper.LoadingDialogFragment;
import com.example.edupro.viewmodel.NoteDetailViewModel;
import com.example.edupro.viewmodel.NoteViewModel;
import com.example.edupro.viewmodel.UserViewModel;

import java.util.List;

public class NoteDetailFragment extends Fragment {
    RecyclerView wordRecyclerView;
    NoteDetailAdapter noteDetailAdapter;
    NoteDetailViewModel noteDetailViewModel;
    Note note;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            note = args.getParcelable("selected_note");
            Log.d("dictionary: ", note.getWordList().toString());
        } else {
            Log.e("err", "null");
            return;
        }
        wordRecyclerView = view.findViewById(R.id.note_recycler_view_my_words);
        View addWordBtn = view.findViewById(R.id.note_add_word);
        View backBtn = view.findViewById(R.id.note_detail_back);
        wordRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        noteDetailViewModel = new ViewModelProvider(this).get(NoteDetailViewModel.class);
        noteDetailViewModel.setNote(note);
        UserViewModel userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        boolean isAllowEdit = (userViewModel.getUser().getValue().getId()).equals(note.getUser_id());

        noteDetailAdapter = new NoteDetailAdapter(note.getWordList(), isAllowEdit);

        ((TextView)view.findViewById(R.id.note_detail_category)).setText(note.getCategory());
        ((TextView)view.findViewById(R.id.note_detail_auth)).setText(note.getUser_name());
        wordRecyclerView.setAdapter(noteDetailAdapter);

        if(isAllowEdit){
            view.findViewById(R.id.note_delete).setOnClickListener(v->{
                showDeleteNoteConfirmationDialog();
            });
            addWordBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showEditDialog(null,false);
                }
            });
        }
        else {
            view.findViewById(R.id.note_delete).setVisibility(View.GONE);
            view.findViewById(R.id.note_add_word).setVisibility(View.GONE);
        }

        noteDetailViewModel.getMutableNote().observe(getViewLifecycleOwner(), new Observer<Note>() {
            @Override
            public void onChanged(Note note) {

                noteDetailAdapter.setWordMap(note.getWordList());
                noteDetailAdapter.notifyDataSetChanged();
            }
        });


        noteDetailViewModel.getIsLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading) {
                    showLoadingDialog();
                } else {
                    hideLoadingDialog();
                }
            }
        });


        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(wordRecyclerView);


        noteDetailAdapter.setOnEditClickListener(new NoteDetailAdapter.OnEditClickListener() {
            @Override
            public void onEditClick(String word) {
                // Handle item click here
                showEditDialog(word,true);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                NavController navController = Navigation.findNavController(view);
//                navController.navigateUp();

                getActivity().onBackPressed();
            }
        });




        noteDetailAdapter.setOnDeleteClickListener(new NoteDetailAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(String word) {
                // Handle the delete action here
                showDeleteConfirmationDialog(word);
            }
        });
    }
    private void showDeleteConfirmationDialog(String word) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete this word?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the delete action, e.g., call a method in your ViewModel
                noteDetailViewModel.deleteWord(word, new NoteRepository.OnNoteUpdatedListener() {
                    @Override
                    public void onNoteUpdated() {

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showDeleteNoteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete this note and all its contents?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the delete action, e.g., call a method in your ViewModel
                noteDetailViewModel.deleteNote(note, new NoteRepository.OnNoteDeletedListener() {
                    @Override
                    public void onNoteDeleted() {
                        getActivity().onBackPressed();
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showEditDialog(String word, boolean disableTerm) {

        // Create and show the dialog using the selectedWord
        EditWordDialogFragment editDialog = EditWordDialogFragment.newInstance(
                word,
                note.getWordList().get(word),
                disableTerm
        );


        editDialog.setOnSaveClickListener(new EditWordDialogFragment.OnSaveClickListener() {
            @Override
            public void onSaveClick(String term, String description) {
                // Handle the save action, update your data or perform any necessary tasks
                // For example, update the note or call a method in your ViewModel

                if(term.isEmpty()){
                    Toast.makeText(getContext(),"In valid term ",Toast.LENGTH_SHORT).show();
                    return;
                }
                noteDetailViewModel.updateWord(term, description, new NoteRepository.OnNoteUpdatedListener() {
                    @Override
                    public void onNoteUpdated() {

                    }

                    @Override
                    public void onError(Exception e) {

                        Log.e("NoteDetailFragment", "Error updating note: " + e.getMessage());
                    }
                });
            }
        });

        editDialog.show(getChildFragmentManager(), "EditWordDialogFragment");
    }
    private void showLoadingDialog() {

        LoadingDialogFragment loadingDialog = new LoadingDialogFragment();
        loadingDialog.show(getChildFragmentManager(), "LoadingDialogFragment");
    }

    private void hideLoadingDialog() {
        Fragment loadingDialog = getChildFragmentManager().findFragmentByTag("LoadingDialogFragment");
        if (loadingDialog != null) {
            ((DialogFragment) loadingDialog).dismiss();
        }
    }

}