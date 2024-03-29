package com.example.edupro.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.edupro.model.User;
import com.example.edupro.data.repository.UserRepository;
import com.example.edupro.data.repository.UserRepository.OnUserFetchedListener;
import com.google.firebase.auth.FirebaseAuth;

public class UserViewModel extends ViewModel {
    private MutableLiveData<User> user;
    private UserRepository userRepository;
    private FirebaseAuth mAuth;
    public void initUser(OnUserFetchedListener callback) {

        userRepository = new UserRepository();
        mAuth = FirebaseAuth.getInstance();
        // Fetch user using mAuth Firebase ID
        String userId = mAuth.getCurrentUser().getUid();
        userRepository.getUser(userId, new OnUserFetchedListener() {
            @Override
            public void onUserFetched(User user) {
                setUser(user);
                callback.onUserFetched(user);
                updateStreakAutomatically();
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
    public UserViewModel(){
        user = new MutableLiveData<>();
    }
    public MutableLiveData<User> getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user.setValue(user);
    }

    public void updateStreakAutomatically(){
        User user = this.user.getValue();
        if(user == null) return;
        user.updateStreakIfLastAccessYesterday();
        userRepository.updateUser(user);
        this.user.setValue(user);
    }
}
