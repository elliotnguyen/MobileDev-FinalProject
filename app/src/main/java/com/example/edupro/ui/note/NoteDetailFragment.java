package com.example.edupro.ui.note;

import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edupro.R;
import com.example.edupro.data.repository.NoteRepository;
import com.example.edupro.databinding.FragmentNoteDetailBinding;
import com.example.edupro.model.Note;
import com.example.edupro.ui.dialog.SweetAlertDialog;
import com.example.edupro.viewmodel.UserViewModel;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class NoteDetailFragment extends Fragment {
    NoteDetailAdapter noteDetailAdapter;
    NoteDetailViewModel noteDetailViewModel;
    Note note;
    FragmentNoteDetailBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNoteDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            note = args.getParcelable("selected_note");
        } else {
            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
            sweetAlertDialog.setTitleText("Error");
            sweetAlertDialog.setContentText("Error loading note");
            sweetAlertDialog.show();
            return;
        }

        noteDetailViewModel = new ViewModelProvider(this).get(NoteDetailViewModel.class);
        noteDetailViewModel.setNote(note);

        handleEditNote();

        binding.noteDetailName.setText(note.getCategory());
        binding.noteDetailUserName.setText(note.getUser_name());
        String numOfWords = "Num of words: " + note.getWordList().size() + " words";
        binding.noteDetailNumberOfWords.setText(numOfWords);

        configurationRecyclerView();

        observeAnyChange();

        handleBackButton();
    }

    private void handleBackButton() {
        binding.noteDetailBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.navigation_note);
            }
        });
    }

    private void observeAnyChange() {
        noteDetailViewModel.getMutableNote().observe(getViewLifecycleOwner(), new Observer<Note>() {
            @Override
            public void onChanged(Note note) {
                noteDetailAdapter.setWordMap(note.getWordList());
                noteDetailAdapter.notifyDataSetChanged();

                String numOfWords = "Num of words: " + note.getWordList().size() + " words";
                binding.noteDetailNumberOfWords.setText(numOfWords);
            }
        });
    }

    private void configurationRecyclerView(){
        binding.noteRecyclerViewMyWords.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        noteDetailAdapter = new NoteDetailAdapter(note.getWordList(), noteDetailViewModel.isAllowedEdit());
        binding.noteRecyclerViewMyWords.setAdapter(noteDetailAdapter);

        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(binding.noteRecyclerViewMyWords);

        if (noteDetailViewModel.isAllowedEdit()) {
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(removeItemCallback);
            itemTouchHelper.attachToRecyclerView(binding.noteRecyclerViewMyWords);
        }
    }

    public ItemTouchHelper.SimpleCallback removeItemCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView noteRecyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) {
                return;
            }

            String choosenWord = getWordAtPosition(position);

            if (choosenWord.equals("")) {
                return;
            }

            switch (direction) {
                case ItemTouchHelper.LEFT:
                    showDeleteConfirmationDialog(choosenWord, position);
                    break;
                case ItemTouchHelper.RIGHT:
                    showEditDialog(choosenWord, true);
                    break;
            }
        }

        private String getWordAtPosition(int position) {
            String choosenWord = "";
            int idx = 0;
            for (String word : note.getWordList().keySet()) {
                if (idx == position) {
                    choosenWord = word;
                    break;
                }
                idx++;
            }
            return choosenWord;
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(requireContext(), c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue))
                    .addSwipeLeftActionIcon(R.drawable.baseline_auto_delete_24)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue))
                    .addSwipeRightActionIcon(R.drawable.edit_icon_blue)
                    .setActionIconTint(ContextCompat.getColor(recyclerView.getContext(), android.R.color.white))
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private void handleEditNote() {
        UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        boolean isAllowEdit = userViewModel.getUser().getValue().getId().equals(note.getUser_id());
        noteDetailViewModel.setAllowedEdit(isAllowEdit);

        if(isAllowEdit){
            binding.fragmentNoteDetailAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showEditDialog(null,false);
                }
            });
        }
        else {
            binding.fragmentNoteDetailAdd.setVisibility(View.GONE);
        }
    }

    private void showDeleteConfirmationDialog(String word, int position) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText("Confirm Deletion");
        sweetAlertDialog.setContentText("Are you sure you want to delete this word?");
        sweetAlertDialog.setConfirmText("Delete");
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
                sweetAlertDialog.setTitleText("Loading");
                noteDetailViewModel.deleteWord(word, new NoteRepository.OnNoteUpdatedListener() {
                    @Override
                    public void onNoteUpdated() {
                        sweetAlertDialog.dismissWithAnimation();
                    }

                    @Override
                    public void onError(Exception e) {
                        sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        sweetAlertDialog.setTitleText("Error");
                        sweetAlertDialog.setContentText("Error deleting word");
                        sweetAlertDialog.show();
                    }
                });
            }
        });
        sweetAlertDialog.setCancelText("Cancel");
        sweetAlertDialog.setCancelClickListener(
                new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        noteDetailAdapter.notifyItemChanged(position);
                        sweetAlertDialog.dismissWithAnimation();
                    }
                }
        );
        sweetAlertDialog.show();
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
                if(term.isEmpty()){
                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setTitleText("Error");
                    sweetAlertDialog.setContentText("Term cannot be empty");
                    sweetAlertDialog.show();
                }
                noteDetailViewModel.updateWord(term, description, new NoteRepository.OnNoteUpdatedListener() {
                    @Override
                    public void onNoteUpdated() {

                    }
                    @Override
                    public void onError(Exception e) {
                        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
                        sweetAlertDialog.setTitleText("Error");
                        sweetAlertDialog.setContentText("Error updating word");
                        sweetAlertDialog.show();
                    }
                });
            }
        });

        editDialog.show(getChildFragmentManager(), "EditWordDialogFragment");
    }
}