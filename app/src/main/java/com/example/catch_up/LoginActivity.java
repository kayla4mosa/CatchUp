package com.example.catch_up;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.catch_up.Models.User;
import com.example.catch_up.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using ViewBinding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // OnClickListener for login button
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.email.getEditText().getText().toString().equals("") ||
                        binding.password.getEditText().getText().toString().equals("")) {
                    // Display a toast message if any field is empty
                    Toast.makeText(LoginActivity.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Get email and password from EditText fields
                    String email = binding.email.getEditText().getText().toString();
                    String password = binding.password.getEditText().getText().toString();
                    // Create a new User object
                    User user = new User(email, password);
                    // Sign in user with email and password using FirebaseAuth
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // If sign-in is successful, navigate to HomeActivity
                                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                        finish();
                                    } else {
                                        // If sign-in fails, display error message
                                        Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });

        // OnClickListener for create account button
        binding.createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start SignUpActivity when create account button is clicked
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                finish();
            }
        });
    }
}
