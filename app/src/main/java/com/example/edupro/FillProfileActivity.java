package com.example.edupro;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.edupro.data.repository.UserRepository;
import com.example.edupro.model.User;
import com.example.edupro.viewmodel.UserViewModel;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class FillProfileActivity extends AppCompatActivity {
    private TextInputEditText fullName, phoneNumber,nickName;
    private User user;
    FirebaseUser userAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_profile);
        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.initUser(new UserRepository.OnUserFetchedListener() {
            @Override
            public void onUserFetched(User currentUser) {
                user = currentUser;
                init();
            }
            @Override
            public void onError(Exception e) {

            }
        });
    }

    private  void init(){

        userAuth =  FirebaseAuth.getInstance().getCurrentUser();

        fullName = findViewById(R.id.edtFullName);
        phoneNumber = findViewById(R.id.edtPhoneNumber);
        nickName = findViewById(R.id.edtNickName);
        TextInputEditText edtEmailProfile = findViewById(R.id.edtEmailProfile);

        // Disable editing
        edtEmailProfile.setEnabled(false);
        edtEmailProfile.setText(user.getEmail());

        //
        fullName.setText(user.getName());
        phoneNumber.setText(user.getPhoneNumber());
        nickName.setText(user.getNickName());

        ImageButton btnBack = findViewById(R.id.btnBack);
        Button btnContinue = findViewById(R.id.btnContinue);

        btnBack.setOnClickListener(view -> {
            finish();
        });

        btnContinue.setOnClickListener(view -> {
            performDialog();
            saveUserInfo();
        });
    }

    private void performDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.signup_dialog, null);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        alertDialog.getWindow().setDimAmount(0.5f);

        // Set up a delay to simulate the loading process
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Dismiss the dialog after the delay
                alertDialog.dismiss();
                Intent intent = new Intent(FillProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000); // delay
    }

    private void saveUserInfo() {
        user.setName(fullName.getText().toString());
        user.setNickName(nickName.getText().toString());
        user.setPhoneNumber(phoneNumber.getText().toString());

        (new UserRepository()).updateUser(user);
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(fullName.getText().toString())
                .build();

        userAuth.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Profile update successful
                        Toast.makeText(FillProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        // If the profile update fails, display a message to the user
                        Toast.makeText(FillProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}