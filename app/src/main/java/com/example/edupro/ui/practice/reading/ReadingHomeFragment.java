package com.example.edupro.ui.practice.reading;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edupro.R;
import com.example.edupro.model.ReadingDto;
import com.example.edupro.ui.RecyclerViewClickInterface;
import com.example.edupro.ui.practice.reading.practice.ReadingPracticeViewModel;

import java.util.ArrayList;

public class ReadingHomeFragment extends Fragment {
    private static final String TAG = "ReadingFragment";
    private ReadingHomeViewModel readingViewModel;
    //private ReadingPracticeViewModel readingPracticeViewModel;
    private final ArrayList<ReadingDto> readings = new ArrayList<>();
    public static ReadingHomeFragment newInstance() {
        return new ReadingHomeFragment();
    }
    private RecyclerView readingListRecyclerView;
    private RecyclerView.Adapter readingListAdapter;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View readingHome = inflater.inflate(R.layout.fragment_reading_home, container, false);

        ImageView backButton = readingHome.findViewById(R.id.reading_home_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(view);
                navController.navigateUp();
            }
        });

        readingListRecyclerView = readingHome.findViewById(R.id.reading_home_recycler_view);
        configureReadingListRecyclerView(readingHome);

        readingViewModel = new ReadingHomeViewModel();
        readingViewModel.getAllReading();
        ObserverAnyChange();

        //readingPracticeViewModel = new ViewModelProvider(this).get(ReadingPracticeViewModel.class);
        return readingHome;
    }

    private void ObserverAnyChange() {
        readingViewModel.getAllReadingList().observe(getViewLifecycleOwner(), readingDtos -> {
            readings.clear();
            readings.addAll(readingDtos);
            readingListAdapter.notifyDataSetChanged();
        });
    }

    private void configureReadingListRecyclerView(View view) {
        readingListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        readingListAdapter = new ReadingListAdapter(readings, new RecyclerViewClickInterface() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                String readingId = readings.get(position).getId();
                //readingPracticeViewModel.setReadingId(readingId);
                bundle.putString("readingId", readingId);
                Navigation.findNavController(view).navigate(R.id.navigation_practice_reading_practice, bundle);
                //NavController navController = Navigation.findNavController(view);
                //navController.navigate(R.id.navigation_practice_reading_practice, bundle);
            }
            @Override
            public void onLongItemClick(int position) {

            }
        });
        readingListRecyclerView.setAdapter(readingListAdapter);
    }

    private void handleReadingTopicFilter(View view) {
        ImageView readingTopicFilter = view.findViewById(R.id.reading_topic_filter);
        readingTopicFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterMenu(view);
            }
            private void showFilterMenu(View view) {
                PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                popupMenu.getMenuInflater().inflate(R.menu.reading_topic_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return true;
                    }
                });
                popupMenu.setGravity(Gravity.END);
                popupMenu.show();
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
}
