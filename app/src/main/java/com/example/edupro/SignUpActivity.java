package com.example.edupro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edupro.data.repository.UserRepository;
import com.example.edupro.model.User;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class SignUpActivity extends AppCompatActivity {
    private TextInputEditText email, password, confirmPassword;
    private TextView signIn;
    private Button signUp;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email = findViewById(R.id.edtEmail);
        password = findViewById(R.id.edtPassword);
        confirmPassword = findViewById(R.id.edtConfirmPassword);
        signUp = findViewById(R.id.btnSignUp);
        signIn = findViewById(R.id.tvSignIn);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        signUp.setOnClickListener(view -> {
            String email = this.email.getText().toString();
            String password = this.password.getText().toString();
            String confirmPassword = this.confirmPassword.getText().toString();

            if (validate(email, password, confirmPassword)) {
                performFirebaseRegistration(email, password);
            }
        });

        signIn.setOnClickListener(view -> {
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intent);
        });
    }

    private void performFirebaseRegistration(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid = user.getUid();
                            User newUser = new User(uid,email,"No name");
                            UserRepository userRepository = new UserRepository();
                            userRepository.addUser(newUser);
                            Toast.makeText(SignUpActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this, FillProfileActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(SignUpActivity.this, "Sign up failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validate(String email, String password, String confirmPassword) {
        if (email.isEmpty()) {
            this.email.setError("Email is required");
            this.email.requestFocus();
            return false;
        }
        if (password.isEmpty()) {
            this.password.setError("Password is required");
            this.password.requestFocus();
            return false;
        }
        if (confirmPassword.isEmpty()) {
            this.confirmPassword.setError("Confirm Password is required");
            this.confirmPassword.requestFocus();
            return false;
        }
        if (!password.equals(confirmPassword)) {
            this.confirmPassword.setError("Password and Confirm Password must be same");
            this.confirmPassword.requestFocus();
            return false;
        }
        return true;
    }
}