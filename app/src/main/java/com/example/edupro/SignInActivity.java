package com.example.edupro;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edupro.data.repository.UserRepository;
import com.example.edupro.model.User;
import com.example.edupro.ui.dialog.SweetAlertDialog;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class SignInActivity extends AppCompatActivity {
    private TextInputEditText email, password;
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        mAuth = FirebaseAuth.getInstance();
        redirectWhenLoggedIn();

        handleSignInWithFirebase();
        handleSignInWithGoogle();
        handleSignUpWhenLogInFailed();
    }

    private void redirectWhenLoggedIn() {
        if(mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void handleSignInWithFirebase() {
        email = findViewById(R.id.edtEmail);
        password = findViewById(R.id.edtPassword);
        Button signIn = findViewById(R.id.btnSignIn);
        signIn.setOnClickListener(view -> {
            String email = this.email.getText().toString();
            String password = this.password.getText().toString();
            if (validate(email, password)) {
                performFirebaseLogin(email, password);
            }
        });
    }

    private void performFirebaseLogin(String email, String password) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText("Loading");
        sweetAlertDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        sweetAlertDialog.dismissWithAnimation();

                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        sweetAlertDialog.setTitleText("Login failed");
                        sweetAlertDialog.setContentText("Please check your email and password");
                        sweetAlertDialog.setConfirmText("OK");
                        sweetAlertDialog.setConfirmClickListener(SweetAlertDialog::dismissWithAnimation);
                    }
                });
    }

    private boolean validate(String email, String password) {
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
        return true;
    }

    private void handleSignUpWhenLogInFailed() {
        TextView signUp = findViewById(R.id.tvSignUp);
        signUp.setOnClickListener(view -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInWithGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        ImageView signInWithGoogle = findViewById(R.id.signin_google);

        signInWithGoogle.setOnClickListener(view -> {

            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            if (account != null) {
                // If already signed in, sign out first
                mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                });
            } else {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }

        });
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            Toast.makeText(this, "Google Sign-In failed" + e, Toast.LENGTH_LONG).show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText("Loading");
        sweetAlertDialog.show();

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        sweetAlertDialog.dismissWithAnimation();
                        // Sign in success, update UI or navigate to the next screen
                        // Example: navigate to MainActivity
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        FirebaseUser userAuth = mAuth.getCurrentUser();
                        User user = new User(userAuth.getUid(),userAuth.getEmail(),userAuth.getDisplayName());
                        UserRepository userRepository = new UserRepository();
                        userRepository.addUser(user);
                        startActivity(intent);
                    } else {
                        // If sign in fails, display a message to the user.
                        //Toast.makeText(this, "Google Sign-In failed", Toast.LENGTH_SHORT).show();
                        sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        sweetAlertDialog.setTitleText("Login failed");
                        sweetAlertDialog.setContentText("Please check your email and password");
                        sweetAlertDialog.setConfirmText("OK");
                        sweetAlertDialog.setConfirmClickListener(SweetAlertDialog::dismissWithAnimation);
                    }
                });
    }
}