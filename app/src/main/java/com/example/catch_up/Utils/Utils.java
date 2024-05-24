package com.example.catch_up.Utils;

import android.net.Uri;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.UUID;

public class Utils {

    // Method to upload an image to Firebase Storage
    public static void uploadImage(Uri uri, String folderName, final Callback<String> callback) {
        final String[] imageUrl = {null}; // Array to hold the image URL

        // Reference to Firebase Storage location with a unique filename
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(folderName)
                .child(UUID.randomUUID().toString());

        // Upload the image file to Firebase Storage
        storageReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {
            // Get the download URL of the uploaded image
            storageReference.getDownloadUrl().addOnSuccessListener(uri1 -> {
                // Store the download URL in imageUrl array and invoke the callback
                imageUrl[0] = uri1.toString();
                callback.onComplete(imageUrl[0]);
            });
        });
    }

    // Interface for callback when image upload is complete
    public interface Callback<T> {
        void onComplete(T result);
    }
}
