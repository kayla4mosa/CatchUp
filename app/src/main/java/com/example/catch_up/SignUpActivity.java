package com.example.catch_up;

import static com.example.catch_up.Utils.Utils.uploadImage;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.catch_up.Models.User;
import com.example.catch_up.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private User user;
    private User existingUser;
    private final ActivityResultLauncher<String> launcher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    // Launches the image selection and upload process
                    uploadImage(uri, "Profile", imageUrl -> {
                        if (imageUrl != null) {
                            user.setImage(imageUrl);
                            binding.profilePicView.setImageURI(uri);
                        } else {
                            // Handle case when imageUrl is null
                        }
                    });
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Customize the sign-up text with HTML
        String text = "<font color=#000000>Already have an account?</font> <font color=#1e88e5>Login</font>";
        binding.login.setText(Html.fromHtml(text));

        user = new User();

        // Check if the activity was started in update mode
        if (getIntent().hasExtra("MODE")) {
            if (getIntent().getIntExtra("MODE", -1) == 1) {
                binding.signUpBtn.setText("Update Profile");

                // Fetch existing user data from Firestore for update
                FirebaseFirestore.getInstance().collection("User")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                existingUser = documentSnapshot.toObject(User.class);
                                if (existingUser != null) {
                                    if (existingUser.getImage() != null && !existingUser.getImage().isEmpty()) {
                                        Picasso.get().load(existingUser.getImage()).into(binding.profilePicView);
                                    }
                                    binding.name.getEditText().setText(existingUser.getName());
                                    binding.email.getEditText().setText(existingUser.getEmail());
                                    binding.password.getEditText().setText(existingUser.getPassword());
                                }
                            }
                        });
            }
        }

        // OnClickListener for sign-up button
        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent currentIntent = getIntent();

                // Check if the activity was started in update mode
                if (currentIntent.hasExtra("MODE")) {
                    if (currentIntent.getIntExtra("MODE", -1) == 1) {
                        // Update user profile
                        updateUserProfile();
                    }
                } else {
                    // Sign up a new user
                    signUp();
                }
            }
        });

        // OnClickListener for profile image selection
        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the image selection
                launcher.launch("image/*");
            }
        });

        // OnClickListener for login text
        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start LoginActivity when login text is clicked
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // Method to sign up a new user
    private void signUp() {
        // Check if all required fields are filled
        if (areFieldsFilled()) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    binding.email.getEditText().getText().toString(),
                    binding.password.getEditText().getText().toString()
            ).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // If sign-up is successful, save user data to Firestore
                    user.setName(binding.name.getEditText().getText().toString());
                    user.setPassword(binding.password.getEditText().getText().toString());
                    user.setEmail(binding.email.getEditText().getText().toString());

                    FirebaseFirestore.getInstance().collection("User")
                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .set(user)
                            .addOnSuccessListener(aVoid -> {
                                Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(SignUpActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.e("SignUpActivity", "Error writing document",
                                        e);
                            });
                } else {
                    // If sign-up fails, display error message
                    Toast.makeText(SignUpActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            // If any required field is empty, display a toast message
            Toast.makeText(SignUpActivity.this, "Please fill in all the fields!", Toast.LENGTH_LONG).show();
        }
    }

    // Method to update user profile
    private void updateUserProfile() {
        if (existingUser != null) {
            // Use existing data if fields are empty
            String name = binding.name.getEditText().getText().toString().isEmpty() ?
                    existingUser.getName() : binding.name.getEditText().getText().toString();
            String email = binding.email.getEditText().getText().toString().isEmpty() ?
                    existingUser.getEmail() : binding.email.getEditText().getText().toString();
            String password = binding.password.getEditText().getText().toString().isEmpty() ?
                    existingUser.getPassword() : binding.password.getEditText().getText().toString();
            String image = user.getImage() == null || user.getImage().isEmpty() ?
                    existingUser.getImage() : user.getImage();

            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            user.setImage(image);

            // Update user data in Firestore
            FirebaseFirestore.getInstance().collection("User")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .set(user)
                    .addOnSuccessListener(aVoid -> {
                        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(SignUpActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("SignUpActivity", "Error writing document", e);
                    });
        }
    }

    // Method to check if all required fields are filled
    private boolean areFieldsFilled() {
        return !binding.name.getEditText().getText().toString().isEmpty() &&
                !binding.email.getEditText().getText().toString().isEmpty() &&
                !binding.password.getEditText().getText().toString().isEmpty();
    }
}
