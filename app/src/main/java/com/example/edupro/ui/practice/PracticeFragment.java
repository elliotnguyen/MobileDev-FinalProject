package com.example.edupro.ui.practice;

import androidx.appcompat.app.ActionBar;
import androidx.core.widget.PopupMenuCompat;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

public class PracticeFragment extends Fragment {

    private PracticeViewModel mViewModel;

    public static PracticeFragment newInstance() {
        return new PracticeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View readingHome = inflater.inflate(R.layout.fragment_practice, container, false);
        if (getActivity() != null) {
            ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle("Practice");
            }
        }
        if (container != null) {
            handleReadingTopicFilter(readingHome);
            handleReadingQuestionTypeFilter(readingHome);
        }
        return readingHome;
    }
    private void handleReadingTopicFilter(View view) {
        ImageView readingTopicFilter = view.findViewById(R.id.reading_topic_filter);
        readingTopicFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterMenu(view);
            }
            private void showFilterMenu(View view) {
                //PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                //popupMenu.getMenuInflater().inflate(R.menu.reading_topic_menu, popupMenu.getMenu());
                ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(view.getContext(), R.style.PopupMenuStyle);
                //PopupMenuCompat.setGravity(popupMenu, Gravity.END);
                PopupMenu customizePopupMenu = new PopupMenu(contextThemeWrapper, view, Gravity.END);
                customizePopupMenu.getMenuInflater().inflate(R.menu.reading_topic_menu, customizePopupMenu.getMenu());
                customizePopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return true;
                    }
                });
                customizePopupMenu.show();
            }
        });
    }
    private void handleReadingQuestionTypeFilter(View view) {
        ImageView readingQuestionTypeFilter = view.findViewById(R.id.reading_question_type_filter);
        readingQuestionTypeFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterMenu(view);
            }

            private void showFilterMenu(View view) {
                PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                popupMenu.getMenuInflater().inflate(R.menu.reading_question_type_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return true;
                    }
                });
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