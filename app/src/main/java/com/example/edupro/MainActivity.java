package com.example.edupro;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.example.edupro.data.repository.UserRepository;
import com.example.edupro.databinding.ActivityMainBinding;
import com.example.edupro.model.User;
import com.example.edupro.viewmodel.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private UserViewModel userViewModel;
    private final ArrayList<Integer> hidingBottomBarFragmentIds = new ArrayList<>(
            Arrays.asList(
                    R.id.navigation_practice_reading,
                    R.id.navigation_practice_reading_practice,
                    R.id.navigation_practice_writing_practice,
                    R.id.navigation_practice_reading_result
            )
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_practice, R.id.navigation_note, R.id.navigation_dashboard)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {

//            if (destination.getId() == R.id.navigation_practice_reading || destination.getId() == R.id.navigation_practice_reading_practice) {
//                hideBottomNavigationBar();
//            } else {
//                showBottomNavigationBar();
//            }

            if (shouldHideBottomBar(destination.getId())) {
                hideBottomNavigationBar();
            } else {
                showBottomNavigationBar();
            }
        });

        hideBottomNavigationBar();


        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userViewModel.initUser(new UserRepository.OnUserFetchedListener() {
            @Override
            public void onUserFetched(User user) {
                showBottomNavigationBar();
            }

            @Override
            public void onError(Exception e) {

            }
        });


    }
    private boolean shouldHideBottomBar(int id) {
        return hidingBottomBarFragmentIds.contains(id);
    }
    private void showBottomNavigationBar() {
        binding.navView.setVisibility(View.VISIBLE);
    }
    private void hideBottomNavigationBar() {
        binding.navView.setVisibility(View.GONE);
    }
}