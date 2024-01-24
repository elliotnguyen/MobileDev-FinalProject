package com.example.edupro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.edupro.constant.NoBottomNavFragment;
import com.example.edupro.data.repository.UserRepository;
import com.example.edupro.databinding.ActivityMainBinding;
import com.example.edupro.model.User;
import com.example.edupro.viewmodel.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_READ = 910;
    private static final int REQUEST_PERMISSION_WRITE = 911;
    private ActivityMainBinding binding;
    private UserViewModel userViewModel;

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
            if (NoBottomNavFragment.shouldHideBottomBar(destination.getId())) {
                hideBottomNavigationBar();
            } else {
                showBottomNavigationBar();
            }
        });

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

//        if(userViewModel.getUser().getValue() == null)
//            disableBottomNavigationClick(navView);
        userViewModel.initUser(new UserRepository.OnUserFetchedListener() {
            @Override
            public void onUserFetched(User user) {
                enableBottomNavigationClick(navView);
            }

            @Override
            public void onError(Exception e) {

            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            Log.e("err", " Dont have permission");
            ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.READ_MEDIA_AUDIO}, REQUEST_PERMISSION_READ);
        }


//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            Log.e("err", " Dont have permission W");
//            ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_READ);
//        }
    }


    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_READ) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Permission READ"," Enabled");
            } else {
                Toast.makeText(this,"Neu ban khong cho, thi dung xai app oke?",Toast.LENGTH_LONG).show();
                Log.d("Permission READ"," Disabled");

            }
        }

        if (requestCode == REQUEST_PERMISSION_WRITE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Permission WRITe"," Enabled");
            } else {
                Toast.makeText(this,"Neu ban khong cho, thi dung xai app oke?",Toast.LENGTH_LONG).show();

                Log.d("Permission WRITe"," Disabled");

            }
        }
    }
    private void showBottomNavigationBar() {
        binding.navView.setVisibility(View.VISIBLE);
    }
    private void hideBottomNavigationBar() {
        binding.navView.setVisibility(View.GONE);
    }

    private void disableBottomNavigationClick(BottomNavigationView bottomNavigationView) {
        for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
            bottomNavigationView.getMenu().getItem(i).setEnabled(false);
        }
    }

    private void enableBottomNavigationClick(BottomNavigationView bottomNavigationView) {
        for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
            bottomNavigationView.getMenu().getItem(i).setEnabled(true);
        }
    }
}