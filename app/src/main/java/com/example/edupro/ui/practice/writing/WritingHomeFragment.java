package com.example.edupro.ui.practice.writing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.edupro.R;
import com.example.edupro.databinding.FragmentWritingHomeBinding;

public class WritingHomeFragment extends Fragment {
    private FragmentWritingHomeBinding binding;

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
        return binding.getRoot();
    }

}
