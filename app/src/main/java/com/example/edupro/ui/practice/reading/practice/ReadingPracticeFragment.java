package com.example.edupro.ui.practice.reading.practice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.edupro.MainActivity;
import com.example.edupro.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ReadingPracticeFragment extends Fragment {
    private static final String TAG = "ReadingPracticeFragment";
    private ReadingPracticeViewModel readingViewModel;
    public static ReadingPracticeFragment newInstance() {
        return new ReadingPracticeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View readingPractice = inflater.inflate(R.layout.fragment_reading_practice, container, false);
        readingViewModel = new ViewModelProvider(this).get(ReadingPracticeViewModel.class);
        if (getArguments() != null) {
            String readingId = getArguments().getString("readingId");
            readingViewModel.setReadingId(readingId);

            BottomNavigationView navView = readingPractice.findViewById(R.id.reading_practice_nav_view);
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_practice_reading_passage, R.id.navigation_practice_reading_question)
                    .build();

            NavController navController = Navigation.findNavController(requireActivity(), R.id.reading_practice_content);
            NavigationUI.setupActionBarWithNavController((AppCompatActivity) requireActivity(), navController, appBarConfiguration);
            NavigationUI.setupWithNavController(navView, navController);
        }
        return readingPractice;
    }
}
