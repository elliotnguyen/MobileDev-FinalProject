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

        handleAnimation(practiceHome);

        handleReading(practiceHome);
        handleWriting(practiceHome);
        handleListening(practiceHome);
        handleSpeaking(practiceHome);

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
                Navigation.findNavController(view).navigate(R.id.navigation_practice_writing);
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

    private void handleAnimation(View practiceHome) {
        View skillCards = practiceHome.findViewById(R.id.linear_layout_1);
        skillCards.setVisibility(View.GONE);

        skillCards.setAlpha(0f);
        skillCards.setVisibility(View.VISIBLE);

        skillCards.animate()
                .alpha(1f)
                .setDuration(500)
                .setListener(null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PracticeViewModel.class);
        // TODO: Use the ViewModel
    }
}