package com.example.edupro.ui.practice.listening;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.edupro.R;
import com.example.edupro.databinding.FragmentListeningHomeBinding;
import com.example.edupro.model.AnswerDto;
import com.example.edupro.model.listening.ListeningDto;
import com.example.edupro.ui.RecyclerViewClickInterface;
import com.example.edupro.viewmodel.UserViewModel;

import java.util.ArrayList;

public class ListeningHomeFragment extends Fragment {

    private ListeningHomeViewModel mViewModel;
    private FragmentListeningHomeBinding binding;
    private RecyclerView.Adapter listeningListAdapter;
    private final ArrayList<AnswerDto> answers = new ArrayList<>();
    private final ArrayList<ListeningDto> listenings = new ArrayList<>();

    private UserViewModel userViewModel;

    public static ListeningHomeFragment newInstance() {
        return new ListeningHomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentListeningHomeBinding.inflate(inflater, container, false);

        binding.listeningHomeBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.navigation_practice);
            }
        });

        configureListeningListRecyclerView();

        mViewModel = new ListeningHomeViewModel();

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        observeAnyChange();

        observeAnyChangeOfAnswer();

        return binding.getRoot();
    }

    private void observeAnyChangeOfAnswer() {
        if (userViewModel.getUser().getValue() == null) {
            return;
        }
        mViewModel.getAllAnswerBySkillIdOfUserId(userViewModel.getUser().getValue().getId(), "listening");
        mViewModel.getAllAnswersBySkillIdOfUserIdList().observe(getViewLifecycleOwner(), answerDtos -> {
            if (answerDtos == null) {
                return;
            }
            answers.clear();
            answers.addAll(answerDtos);
            listeningListAdapter.notifyDataSetChanged();
        });

    }

    private void observeAnyChange() {
        if (userViewModel.getUser().getValue() == null) {
            return;
        }
        mViewModel.init();
        mViewModel.getAllListeningList().observe(getViewLifecycleOwner(), readingDtos -> {
            if (readingDtos == null) {
                return;
            }
            listenings.clear();
            listenings.addAll(readingDtos);
            listeningListAdapter.notifyDataSetChanged();
        });
    }

    private void configureListeningListRecyclerView() {
        binding.listeningHomeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.listeningHomeRecyclerView.setHasFixedSize(true);
        listeningListAdapter = new ListeningListAdapter(listenings, answers,new RecyclerViewClickInterface() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putString("listeningId", listenings.get(position).getId());
                if (answers.size() > position) {
                    bundle.putString("answers", answers.get(position).getAnswer());
                }
                NavController navController = Navigation.findNavController(binding.getRoot());
                navController.navigate(R.id.navigation_practice_listening_practice, bundle);
            }

            @Override
            public void onLongItemClick(int position) {
                // TODO: Long click
            }
        },R.layout.listening_list_viewholder, R.id.listening_list_viewholder_title, R.id.listening_list_viewholder_progressbar_text, R.id.listening_list_viewholder_action_btn, R.id.listening_list_viewholder_progressbar);

        binding.listeningHomeRecyclerView.setAdapter(listeningListAdapter);
    }
}