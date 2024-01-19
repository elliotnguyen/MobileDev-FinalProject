package com.example.edupro.ui.practice.listening;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.edupro.R;
import com.example.edupro.databinding.FragmentListeningHomeBinding;

public class ListeningHomeFragment extends Fragment {

    private ListeningHomeViewModel mViewModel;
    private FragmentListeningHomeBinding binding;

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
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ListeningHomeViewModel.class);
        // TODO: Use the ViewModel
    }

}