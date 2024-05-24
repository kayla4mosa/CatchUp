package com.example.catch_up.Post;

import static com.example.catch_up.Utils.Utils.uploadImage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.catch_up.HomeActivity;
import com.example.catch_up.Models.Post;
import com.example.catch_up.Models.User;
import com.example.catch_up.databinding.ActivityPostBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class PostActivity extends AppCompatActivity {

    private static final String TAG = "PostActivity";
    private ActivityPostBinding binding;
    private String imageUrl = null;

    // ActivityResultLauncher to handle selecting an image from the gallery
    private final ActivityResultLauncher<String> launcher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    uploadImage(uri, "PostImages", url -> {
                        if (url != null) {
                            binding.selectImage.setImageURI(uri);
                            imageUrl = url;
                        } else {
                            Log.e(TAG, "Failed to upload image");
                        }
                    });
                } else {
                    Log.e(TAG, "Image URI is null");
                }
            }
    );

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up Toolbar with back button
        setSupportActionBar(binding.materialToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Navigate back to the previous activity when back button is clicked
        binding.materialToolbar.setNavigationOnClickListener(view -> finish());

        // Launch image picker when selectImage button is clicked
        binding.selectImage.setOnClickListener(view -> launcher.launch("image/*"));

        // Cancel post and return to HomeActivity
        binding.cancelButton.setOnClickListener(view -> {
            startActivity(new Intent(PostActivity.this, HomeActivity.class));
            finish();
        });

        // Post the image and caption to Firestore when postButton is clicked
        binding.postButton.setOnClickListener(view -> {
            if (imageUrl == null) {
                Log.e(TAG, "Image URL is null");
                return;
            }
            String caption = binding.caption.getEditText().getText().toString();
            if (caption.isEmpty()) {
                Log.e(TAG, "Caption is empty");
                return;
            }
            // Fetch current user's data from Firestore
            FirebaseFirestore.getInstance().collection("User")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            // Create a new post object
                            Post post = new Post(imageUrl, caption, FirebaseAuth.getInstance().getCurrentUser().getUid(), String.valueOf(System.currentTimeMillis()));
                            // Save post to "Post" collection in Firestore
                            FirebaseFirestore.getInstance().collection("Post").document().set(post).addOnSuccessListener(unused -> {
                                // Also save post to user's specific collection in Firestore
                                FirebaseFirestore.getInstance().collection(FirebaseAuth.getInstance().getCurrentUser().getUid()).document().set(post).addOnSuccessListener(unused1 -> {
                                    // Navigate back to HomeActivity after posting
                                    startActivity(new Intent(PostActivity.this, HomeActivity.class));
                                    finish();
                                }).addOnFailureListener(e -> Log.e(TAG, "Failed to save post to user's collection", e));
                            }).addOnFailureListener(e -> Log.e(TAG, "Failed to save post to main collection", e));
                        } else {
                            Log.e(TAG, "User data is null");
                        }
                    })
                    .addOnFailureListener(e -> Log.e(TAG, "Failed to fetch user data", e));
        });
    }
}
