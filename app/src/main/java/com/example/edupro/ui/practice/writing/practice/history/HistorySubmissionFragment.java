package com.example.edupro.ui.practice.writing.practice.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edupro.R;
import com.example.edupro.databinding.FragmentWritingPracticeHistoryBinding;
import com.example.edupro.model.AnswerDto;
import com.example.edupro.ui.RecyclerViewClickInterface;
import com.example.edupro.ui.practice.writing.practice.WritingPracticeViewModel;
import com.example.edupro.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.Objects;

public class HistorySubmissionFragment extends Fragment {
    private static final String TAG = "HistorySubmissionFragment";
    private FragmentWritingPracticeHistoryBinding binding;
    private WritingPracticeViewModel writingPracticeViewModel;
    private UserViewModel userViewModel;
    private ArrayList<AnswerDto> answerDtos = new ArrayList<>();
    private RecyclerView.Adapter historyListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWritingPracticeHistoryBinding.inflate(inflater, container, false);
        writingPracticeViewModel = new ViewModelProvider(requireParentFragment()).get(WritingPracticeViewModel.class);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        configureRecyclerView();
        observeAnyChange();

        return binding.getRoot();
    }

    private void configureRecyclerView() {
        binding.writingPracticeHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        historyListAdapter = new HistoryListAdapter(Objects.requireNonNull(userViewModel.getUser().getValue()).getNickName(), answerDtos , new RecyclerViewClickInterface() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putString("your_answer", answerDtos.get(position).getAnswer());
                bundle.putString("grade", answerDtos.get(position).getScore());
                bundle.putString("explanation", answerDtos.get(position).getNote());

                Navigation.findNavController(requireView()).navigate(R.id.navigation_practice_writing_result_detail, bundle);
            }

            @Override
            public void onLongItemClick(int position) {

            }
        });
        binding.writingPracticeHistoryRecyclerView.setAdapter(historyListAdapter);
    }

    private void observeAnyChange() {
        writingPracticeViewModel.getAnswerDtos(Objects.requireNonNull(userViewModel.getUser().getValue()).getId()).observe(getViewLifecycleOwner(), answerDtos -> {
            if (answerDtos != null) {
                this.answerDtos.clear();
                for (AnswerDto answerDto : answerDtos) {
                    if (answerDto.getSubmitted()) {
                        this.answerDtos.add(answerDto);
                    }
                }
                //this.answerDtos.addAll(answerDtos);
                historyListAdapter.notifyDataSetChanged();
            }
        });
    }
}
