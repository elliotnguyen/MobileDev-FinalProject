package com.example.edupro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.edupro.data.repository.UserRepository;
import com.example.edupro.model.User;

import com.example.edupro.ui.dialog.SweetAlertDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    private TextInputEditText email, password, confirmPassword;
    private TextView signIn;
    private Button signUp;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        handleSignUpWithFirebase();
        handleSignInWhenSignUp();
    }

    private void handleSignUpWithFirebase() {
        email = findViewById(R.id.edtEmail);
        password = findViewById(R.id.edtPassword);
        confirmPassword = findViewById(R.id.edtConfirmPassword);

        signUp = findViewById(R.id.btnSignUp);
        signUp.setOnClickListener(view -> {
            String email = this.email.getText().toString();
            String password = this.password.getText().toString();
            String confirmPassword = this.confirmPassword.getText().toString();

            if (validate(email, password, confirmPassword)) {
                performFirebaseRegistration(email, password);
            }
        });
    }

    private void performFirebaseRegistration(String email, String password) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText("Loading");
        sweetAlertDialog.show();

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

                            sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            sweetAlertDialog.setTitleText("Sign Up Success");
                            sweetAlertDialog.setContentText("Please fill your profile");
                            sweetAlertDialog.setConfirmText("OK");
                            sweetAlertDialog.setConfirmClickListener(SweetAlertDialog::dismissWithAnimation);

                            Intent intent = new Intent(SignUpActivity.this, FillProfileActivity.class);
                            startActivity(intent);
                        } else {
                            sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            sweetAlertDialog.setTitleText("Sign Up Failed");
                            sweetAlertDialog.setContentText("Please check your email and password");
                            sweetAlertDialog.setConfirmText("OK");
                            sweetAlertDialog.setConfirmClickListener(SweetAlertDialog::dismissWithAnimation);
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

    private void handleSignInWhenSignUp() {
        signIn = findViewById(R.id.tvSignIn);
        signIn.setOnClickListener(view -> {
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intent);
        });
    }
}