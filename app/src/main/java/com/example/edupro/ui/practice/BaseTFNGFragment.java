//// BaseTFNGFragment.java
//package com.example.edupro.ui.practice;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.ViewModel;
//import androidx.lifecycle.ViewModelProvider;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.edupro.model.reading.Question;
//import com.example.edupro.ui.practice.reading.practice.question.MCQClickInterface;
//
//import java.util.ArrayList;
//
//public abstract class BaseTFNGFragment<VM extends ViewModel, Q extends Question> extends Fragment {
//    private final int index;
//    private final ArrayList<Q> tfngQuestions = new ArrayList<>();
//    protected VM practiceViewModel;
//
//    public BaseTFNGFragment(ArrayList<Q> tfngQuestions, int index) {
//        this.tfngQuestions.addAll(tfngQuestions);
//        this.index = index;
//    }
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        View tfngFragment = inflater.inflate(getLayoutResourceId(), container, false);
//
//        setupViewModel();
//
//        RecyclerView tfngRecyclerView = tfngFragment.findViewById(getRecyclerViewId());
//        tfngRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//        TFNGListAdapter<Q> tfngListAdapter = new TFNGListAdapter<>(tfngQuestions, this.index, practiceViewModel, new MCQClickInterface() {
//            @Override
//            public void onItemClick(int position, String option) {
//                // Assuming there is a setAnswerAtIndex method in your ViewModel
//                practiceViewModel.setAnswerAtIndex(position + index, option);
//            }
//        });
//        tfngRecyclerView.setAdapter(tfngListAdapter);
//        return tfngFragment;
//    }
//
//    protected abstract int getLayoutResourceId();
//
//    protected abstract int getRecyclerViewId();
//
//    protected abstract void setupViewModel();
//}
