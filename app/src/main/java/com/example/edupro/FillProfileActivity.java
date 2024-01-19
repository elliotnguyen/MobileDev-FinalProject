package com.example.edupro;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.edupro.model.UserDto;
import com.google.android.material.textfield.TextInputEditText;

public class FillProfileActivity extends AppCompatActivity {
    private TextInputEditText fullName, phoneNumber;
    private UserDto userDto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_profile);

        // binding
        userDto = (UserDto) getIntent().getSerializableExtra("user");
        fullName = findViewById(R.id.edtFullName);
        phoneNumber = findViewById(R.id.edtPhoneNumber);
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
                intent.putExtra("user", userDto);
                startActivity(intent);
            }
        }, 3000); // delay
    }

    private void saveUserInfo() {
        userDto.setName(fullName.getText().toString());
        userDto.setPhoneNumber(phoneNumber.getText().toString());
    }
}