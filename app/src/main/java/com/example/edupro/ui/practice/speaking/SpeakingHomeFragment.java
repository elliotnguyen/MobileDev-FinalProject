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
import com.example.edupro.model.reading.ReadingDto;
import com.example.edupro.model.speaking.SpeakingDto;
import com.example.edupro.ui.RecyclerViewClickInterface;
import com.example.edupro.ui.practice.reading.ReadingHomeFragment;
import com.example.edupro.ui.practice.reading.ReadingHomeViewModel;

import java.util.ArrayList;

public class SpeakingHomeFragment extends Fragment {

    private SpeakingHomeViewModel mViewModel;
    private FragmentSpeakingHomeBinding binding;
    private SpeakingHomeViewModel speakingViewModel;
    private final ArrayList<SpeakingDto> speakings = new ArrayList<>();
    private SpeakingListAdapter speakingListAdapter;

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
                navController.navigateUp();
            }
        });

        configureSpeakingListRecyclerView();

        speakingViewModel = new SpeakingHomeViewModel();
        speakingViewModel.init();

        observerAnyChange();

        return binding.getRoot();
    }

    private void observerAnyChange() {
        speakingViewModel.getAllSpeakingList().observe(getViewLifecycleOwner(), speakingList -> {
            speakings.clear();
            speakings.addAll(speakingList);
            speakingListAdapter.notifyDataSetChanged();
        });
    }

    private void configureSpeakingListRecyclerView() {
        binding.speakingHomeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.speakingHomeRecyclerView.setHasFixedSize(true);
        speakingListAdapter = new SpeakingListAdapter(speakings, new RecyclerViewClickInterface() {
            @Override
            public void onItemClick(int position) {
                NavController navController = Navigation.findNavController(getView());
                Bundle bundle = new Bundle();
                bundle.putString("speakingId", speakings.get(position).getId());
                navController.navigate(R.id.navigation_practice_speaking_practice,bundle);
            }

            @Override
            public void onLongItemClick(int position) {
                // do nothing
            }
        });
        binding.speakingHomeRecyclerView.setAdapter(speakingListAdapter);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SpeakingHomeViewModel.class);
        // TODO: Use the ViewModel
    }

}