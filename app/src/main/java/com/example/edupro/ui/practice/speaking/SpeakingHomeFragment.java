package com.example.edupro.ui.practice.speaking;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.edupro.R;
import com.example.edupro.databinding.FragmentSpeakingHomeBinding;
import com.example.edupro.model.AnswerDto;
import com.example.edupro.model.reading.ReadingDto;
import com.example.edupro.model.speaking.SpeakingDto;
import com.example.edupro.ui.RecyclerViewClickInterface;
import com.example.edupro.ui.practice.listening.ListeningHomeViewModel;
import com.example.edupro.ui.practice.listening.ListeningListAdapter;
import com.example.edupro.ui.practice.reading.ReadingHomeFragment;
import com.example.edupro.ui.practice.reading.ReadingHomeViewModel;
import com.example.edupro.viewmodel.UserViewModel;

import java.util.ArrayList;

public class SpeakingHomeFragment extends Fragment {

    private SpeakingHomeViewModel mViewModel;
    private FragmentSpeakingHomeBinding binding;
    private final ArrayList<SpeakingDto> speakings = new ArrayList<>();
    private SpeakingListAdapter speakingListAdapter;
    private UserViewModel userViewModel;
    private final ArrayList<AnswerDto> answers = new ArrayList<>();
    public static SpeakingHomeFragment newInstance() {
        return new SpeakingHomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSpeakingHomeBinding.inflate(inflater, container, false);

        binding.speakingHomeBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.navigation_practice);
            }
        });

        mViewModel = new SpeakingHomeViewModel();

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        mViewModel.init();
        observerAnyChange();

        observeAnyChangeOfAnswer();

        configureSpeakingListRecyclerView();

        return binding.getRoot();
    }

    private void observeAnyChangeOfAnswer() {
        if (userViewModel.getUser().getValue() == null) {
            return;
        }
        mViewModel.getAllAnswerBySkillIdOfUserId(userViewModel.getUser().getValue().getId(), "speaking");
        mViewModel.getAllAnswersBySkillIdOfUserIdList().observe(getViewLifecycleOwner(), answerDtos -> {
            if (answerDtos == null) {
                return;
            }
            answers.clear();
            answers.addAll(answerDtos);
            speakingListAdapter.notifyDataSetChanged();
        });
    }

    private void observerAnyChange() {
        if (userViewModel.getUser().getValue() == null) {
            return;
        }
        mViewModel.init();
        mViewModel.getAllSpeakingList().observe(getViewLifecycleOwner(), speakingDtos -> {
            if (speakingDtos == null) {
                return;
            }
            speakings.clear();
            speakings.addAll(speakingDtos);
            speakingListAdapter.notifyDataSetChanged();
        });
    }

    private void configureSpeakingListRecyclerView() {
        binding.speakingHomeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.speakingHomeRecyclerView.setHasFixedSize(true);
        speakingListAdapter = new SpeakingListAdapter(speakings,answers ,new RecyclerViewClickInterface() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putString("speakingId", speakings.get(position).getId());
                if (answers.size() > position) {
                    bundle.putString("answers", answers.get(position).getAnswer());
                }
                NavController navController = Navigation.findNavController(binding.getRoot());
                navController.navigate(R.id.navigation_practice_speaking_practice, bundle);
            }

            @Override
            public void onLongItemClick(int position) {
                // TODO: Long click
            }
        },R.layout.speaking_list_viewholder, R.id.speaking_list_viewholder_title, R.id.speaking_list_viewholder_progressbar_text, R.id.speaking_list_viewholder_action_btn, R.id.speaking_list_viewholder_progressbar);

        binding.speakingHomeRecyclerView.setAdapter(speakingListAdapter);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SpeakingHomeViewModel.class);
        // TODO: Use the ViewModel
    }

}