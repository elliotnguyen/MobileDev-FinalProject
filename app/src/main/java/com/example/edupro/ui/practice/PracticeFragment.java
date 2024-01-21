package com.example.edupro.ui.practice;

import androidx.appcompat.app.ActionBar;
import androidx.cardview.widget.CardView;
import androidx.core.widget.PopupMenuCompat;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.example.edupro.MainActivity;
import com.example.edupro.R;
import com.example.edupro.ui.practice.reading.ReadingHomeFragment;

public class PracticeFragment extends Fragment {

    private PracticeViewModel mViewModel;

    public static PracticeFragment newInstance() {
        return new PracticeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View practiceHome = inflater.inflate(R.layout.fragment_practice, container, false);
        handleReading(practiceHome);
        handleWriting(practiceHome);
        handleListening(practiceHome);
        handleSpeaking(practiceHome);
//        CardView readingCard = practiceHome.findViewById(R.id.card_view_reading);
//        readingCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Navigation.findNavController(view).navigate(R.id.navigation_practice_reading);
//            }
//        });
        return practiceHome;
    }

    private void handleSpeaking(View practiceHome) {
        CardView speakingCard = practiceHome.findViewById(R.id.card_view_speaking);
        speakingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.navigation_practice_speaking);
            }
        });
    }

    private void handleReading(View practiceHome) {
        CardView readingCard = practiceHome.findViewById(R.id.card_view_reading);
        readingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.navigation_practice_reading);
            }
        });
    }

    private void handleWriting(View practiceHome) {
        CardView writingCard = practiceHome.findViewById(R.id.card_view_writing);
        writingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.navigation_practice_writing_practice);
            }
        });
    }
    private void handleListening(View practiceHome) {
        CardView listeningCard = practiceHome.findViewById(R.id.card_view_listening);
        listeningCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.navigation_practice_listening);
            }
        });
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PracticeViewModel.class);
        // TODO: Use the ViewModel
    }
}