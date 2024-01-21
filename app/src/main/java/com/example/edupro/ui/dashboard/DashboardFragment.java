package com.example.edupro.ui.dashboard;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.edupro.FillProfileActivity;
import com.example.edupro.R;
import com.example.edupro.SignInActivity;
import com.example.edupro.SignUpActivity;
import com.example.edupro.model.User;
import com.example.edupro.viewmodel.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;

public class DashboardFragment extends Fragment {

    private DashboardViewModel mViewModel;

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }
    private FirebaseAuth mAuth;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View profile = inflater.inflate(R.layout.fragment_dashboard, container, false);
        View profileDetail = profile.findViewById(R.id.profile_detail);
        UserViewModel userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        MutableLiveData<User> user =  userViewModel.getUser();
        user.observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                // Update UI elements based on the new user data
                if (user != null) {
                    TextView userNameTV = profile.findViewById(R.id.profile_user_name);
                    TextView streak_count = profile.findViewById(R.id.profile_streak_count_txt);
                    streak_count.setText(getString(R.string.your_current_streak_is)+String.valueOf(user.getStreakCount()) + " days");
                    userNameTV.setText(user.getNickName());
                }
            }
        });
        View logoutBtn= profile.findViewById(R.id.profile_logout);

        mAuth = FirebaseAuth.getInstance();
        profileDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FillProfileActivity.class);
                startActivity(intent);
            }
        });
        FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    //Do anything here which needs to be done after signout is complete
                    Intent intent = new Intent(getContext(), SignInActivity.class);
                    startActivity(intent);
                    getActivity().finish();// Close the current activity to prevent the user from navigating back
                }
                else {
                }
            }
        };
        mAuth.addAuthStateListener(authStateListener);

        logoutBtn.setOnClickListener(view -> {
            // Sign out the user
            FirebaseAuth.getInstance().signOut();



        });
        return profile;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        // TODO: Use the ViewModel
    }

}