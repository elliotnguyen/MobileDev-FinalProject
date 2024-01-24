package com.example.edupro.ui.practice.writing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edupro.R;
import com.example.edupro.databinding.FragmentReadingHomeBinding;
import com.example.edupro.databinding.FragmentWritingHomeBinding;
import com.example.edupro.model.AnswerDto;
import com.example.edupro.model.reading.ReadingDto;
import com.example.edupro.model.writing.WritingDto;
import com.example.edupro.ui.RecyclerViewClickInterface;
import com.example.edupro.ui.practice.reading.ReadingHomeFragment;
import com.example.edupro.ui.practice.reading.ReadingHomeViewModel;
import com.example.edupro.viewmodel.UserViewModel;

import java.util.ArrayList;

public class WritingHomeFragment extends Fragment {
    private FragmentWritingHomeBinding binding;
    private WritingHomeViewModel writingHomeViewModel;
    private UserViewModel userViewModel;
    private final ArrayList<WritingDto> writings = new ArrayList<>();
    private final ArrayList<AnswerDto> answers = new ArrayList<>();
    public static WritingHomeFragment newInstance() {
        return new WritingHomeFragment();
    }
    private RecyclerView.Adapter writingListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWritingHomeBinding.inflate(inflater, container, false);
        binding.writingHomeBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.navigation_practice);
            }
        });

        onfigureWritingListRecyclerView();

        writingHomeViewModel = new WritingHomeViewModel();

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        ObserverAnyChangeOfWritingList();

        ObserverAnyChangeOfAnswerList();

//        handleReadingTopicFilter(binding.getRoot());
//        handleReadingQuestionTypeFilter(binding.getRoot());
        return binding.getRoot();
    }

    private void ObserverAnyChangeOfAnswerList() {
        if (userViewModel.getUser().getValue() == null) {
            return;
        }
        writingHomeViewModel.getAllAnswerBySkillIdOfUserId(userViewModel.getUser().getValue().getId(), "writing");
        writingHomeViewModel.getAllAnswersBySkillIdOfUserIdList().observe(getViewLifecycleOwner(), answerDtos -> {
            if (answerDtos == null) {
                return;
            }
            answers.clear();
            answers.addAll(answerDtos);
            writingListAdapter.notifyDataSetChanged();
        });
    }

    private void ObserverAnyChangeOfWritingList() {
        writingHomeViewModel.getAllWriting();
        writingHomeViewModel.getAllWritingList().observe(getViewLifecycleOwner(), readingDtos -> {
            if (readingDtos == null) {
                return;
            }
            writings.clear();
            writings.addAll(readingDtos);
            writingListAdapter.notifyDataSetChanged();
        });
    }

    private void onfigureWritingListRecyclerView() {
        binding.writingHomeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        writingListAdapter = new WritingListAdapter(writings, answers, new RecyclerViewClickInterface() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putString("writingId", writings.get(position).getId());
                if (answers.size() > position) {
                    bundle.putString("answers", answers.get(position).getAnswer());
                }
                Navigation.findNavController(binding.getRoot()).navigate(R.id.navigation_practice_writing, bundle);
            }

            @Override
            public void onLongItemClick(int position) {

            }
        }, R.layout.writing_list_viewholder, R.id.writing_list_viewholder_title, R.id.writing_list_viewholder_progressbar_text, R.id.writing_list_viewholder_action_btn, R.id.writing_list_viewholder_progressbar);
        binding.writingHomeRecyclerView.setAdapter(writingListAdapter);
    }

}
