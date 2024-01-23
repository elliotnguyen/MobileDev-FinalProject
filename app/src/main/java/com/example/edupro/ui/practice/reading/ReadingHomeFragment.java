package com.example.edupro.ui.practice.reading;

import android.os.Bundle;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.edupro.databinding.FragmentReadingHomeBinding;
import com.example.edupro.model.AnswerDto;
import com.example.edupro.model.reading.ReadingDto;
import com.example.edupro.ui.RecyclerViewClickInterface;
import com.example.edupro.viewmodel.UserViewModel;

import java.util.ArrayList;

public class ReadingHomeFragment extends Fragment {
    private static final String TAG = "ReadingFragment";
    private ReadingHomeViewModel readingViewModel;
    private UserViewModel userViewModel;
    private FragmentReadingHomeBinding binding;
    private final ArrayList<ReadingDto> readings = new ArrayList<>();
    private final ArrayList<AnswerDto> answers = new ArrayList<>();
    public static ReadingHomeFragment newInstance() {
        return new ReadingHomeFragment();
    }
    private RecyclerView.Adapter readingListAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentReadingHomeBinding.inflate(inflater, container, false);

        binding.readingHomeBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(view);
                navController.navigateUp();
            }
        });

        configureReadingListRecyclerView(binding.getRoot());

        readingViewModel = new ReadingHomeViewModel();

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        ObserverAnyChangeOfReadingList();

        ObserverAnyChangeOfAnswerList();

        handleReadingTopicFilter(binding.getRoot());
        handleReadingQuestionTypeFilter(binding.getRoot());

        return binding.getRoot();
    }

    private void ObserverAnyChangeOfReadingList() {
        readingViewModel.getAllReading();
        readingViewModel.getAllReadingList().observe(getViewLifecycleOwner(), readingDtos -> {
            if (readingDtos == null) {
                return;
            }
            readings.clear();
            readings.addAll(readingDtos);
            readingListAdapter.notifyDataSetChanged();
        });
    }

    private void ObserverAnyChangeOfAnswerList() {
        if (userViewModel.getUser().getValue() == null) {
            return;
        }
        readingViewModel.getAllAnswerBySkillIdOfUserId(userViewModel.getUser().getValue().getId(), "reading");
        readingViewModel.getAllAnswersBySkillIdOfUserIdList().observe(getViewLifecycleOwner(), answerDtos -> {
            if (answerDtos == null) {
                return;
            }
            answers.clear();
            answers.addAll(answerDtos);
            readingListAdapter.notifyDataSetChanged();
        });
    }

    private void configureReadingListRecyclerView(View view) {
        binding.readingHomeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        readingListAdapter = new ReadingListAdapter(readings, answers, new RecyclerViewClickInterface() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                String readingId = readings.get(position).getId();

                bundle.putString("readingId", readingId);
                bundle.putString("answers", answers.get(position).getAnswer());

                Navigation.findNavController(view).navigate(R.id.navigation_practice_reading_practice, bundle);
            }
            @Override
            public void onLongItemClick(int position) {

            }
        }, R.layout.reading_list_viewholder, R.id.reading_list_viewholder_title, R.id.reading_list_viewholder_progressbar_text, R.id.reading_list_viewholder_action_btn, R.id.reading_list_viewholder_progressbar);
        binding.readingHomeRecyclerView.setAdapter(readingListAdapter);
    }

    private void handleReadingTopicFilter(View view) {
        binding.readingTopicFilter.setOnClickListener(new View.OnClickListener() {
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
                        if (item.getItemId() == R.id.reading_topic_all) {
                            binding.readingTopicText.setText("All");
                            if (userViewModel.getUser().getValue() == null) {
                                return true;
                            }
                            readingViewModel.getAllReading();
                            readingViewModel.getAllAnswerBySkillIdOfUserId(userViewModel.getUser().getValue().getId(), "reading");

                            readingListAdapter.notifyDataSetChanged();
                            return true;
                        } else {
                            binding.readingTopicText.setText(item.getTitle());
                        }

                        int topicId = 0;
                        if (item.getItemId() == R.id.reading_topic_eco) {
                            topicId = 0;
                        } else if (item.getItemId() == R.id.reading_topic_edu) {
                            topicId = 1;
                        } else if (item.getItemId() == R.id.reading_topic_lei) {
                            topicId = 2;
                        } else if (item.getItemId() == R.id.reading_topic_env) {
                            topicId = 3;
                        } else if (item.getItemId() == R.id.reading_topic_sci) {
                            topicId = 4;
                        }

                        Pair<ArrayList<ReadingDto>, ArrayList<AnswerDto>> readingsOfTopic = ReadingHomeViewModel.getReadingsOfTopic(readings, answers, topicId);
                        if (answers.size() > 0) {
                            answers.clear();
                        }
                        answers.addAll(readingsOfTopic.second);

                        if (readings.size() > 0) {
                            readings.clear();
                        }

                        readings.addAll(readingsOfTopic.first);

                        readingListAdapter.notifyDataSetChanged();

                        return true;
                    }
                });

                popupMenu.setGravity(Gravity.END);
                popupMenu.show();
            }
        });
    }

    private void handleReadingQuestionTypeFilter(View view) {
        binding.readingQuestionTypeFilter.setOnClickListener(new View.OnClickListener() {
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
                popupMenu.setGravity(Gravity.END);
                popupMenu.show();
            }
        });
    }
}
