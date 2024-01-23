package com.example.edupro.ui.practice.listening;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.edupro.R;
import com.example.edupro.databinding.FragmentListeningHomeBinding;
import com.example.edupro.model.listening.ListeningDto;
import com.example.edupro.ui.RecyclerViewClickInterface;

import java.util.ArrayList;

public class ListeningHomeFragment extends Fragment {

    private ListeningHomeViewModel mViewModel;
    private FragmentListeningHomeBinding binding;
    private ListeningListAdapter listeningListAdapter;
    private final ArrayList<ListeningDto> listenings = new ArrayList<>();

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
                navController.navigateUp();
            }
        });

        mViewModel = new ListeningHomeViewModel();
        mViewModel.init(); // get all listening

        observeAnyChange();

        configureListeningListRecyclerView();
        return binding.getRoot();
    }

    private void observeAnyChange() {
        mViewModel.getAllListeningList().observe(getViewLifecycleOwner(), listeningList -> {
            if (listeningList.size() > 0) {
                binding.listeningHomeRecyclerView.setVisibility(View.VISIBLE);
                listenings.clear();
                listenings.addAll(listeningList);
                binding.listeningHomeRecyclerView.setAdapter(listeningListAdapter);
            } else {
                Toast.makeText(getContext(), "No listening found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void configureListeningListRecyclerView() {
        binding.listeningHomeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.listeningHomeRecyclerView.setHasFixedSize(true);
        listeningListAdapter = new ListeningListAdapter(listenings, new RecyclerViewClickInterface() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putString("listeningId", listenings.get(position).getId());
                NavController navController = Navigation.findNavController(binding.getRoot());
                navController.navigate(R.id.navigation_practice_listening_practice, bundle);
            }

            @Override
            public void onLongItemClick(int position) {
                // TODO: Long click
            }
        });
    }
}