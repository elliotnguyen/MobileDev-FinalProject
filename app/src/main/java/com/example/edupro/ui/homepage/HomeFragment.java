package com.example.edupro.ui.homepage;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.edupro.R;
import com.example.edupro.data.repository.UserRepository;
import com.example.edupro.databinding.FragmentHomeBinding;
import com.example.edupro.model.User;
import com.example.edupro.viewmodel.UserViewModel;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        handleUserInfoDisplay();

        return root;
    }

    private void handleUserInfoDisplay() {
        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.initUser(new UserRepository.OnUserFetchedListener() {
            @Override
            public void onUserFetched(User user) {
                TextView userNickName = binding.userNameHome;
                userNickName.setText(user.getNickName());
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}