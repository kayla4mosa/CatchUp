package com.example.catch_up.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.catch_up.Models.Post;
import com.example.catch_up.Models.User;
import com.example.catch_up.R;
import com.example.catch_up.databinding.PostRvBinding;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyHolder> {

    private static final String TAG = "PostAdapter";
    private Context context;
    private ArrayList<Post> postList;

    public PostAdapter(Context context, ArrayList<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        PostRvBinding binding;

        public MyHolder(PostRvBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PostRvBinding binding = PostRvBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyHolder(binding);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Post post = postList.get(position);

        if (post == null) {
            Log.e(TAG, "Post is null for position: " + position);
            return;
        }

        String uid = post.getUid() != null ? post.getUid() : "unknown_uid";
        String postTime = post.getTime() != null ? post.getTime() : "0";

        // Bind post data to views
        Glide.with(context)
                .load(post.getPostUrl())
                .placeholder(R.drawable.loading)
                .into(holder.binding.postImage);

        holder.binding.caption.setText(post.getCaption());

        // Format and display the time using TimeAgo utility
        try {
            String text = TimeAgo.using(Long.parseLong(postTime));
            holder.binding.time.setText(text);
        } catch (Exception e) {
            holder.binding.time.setText("");
            Log.e(TAG, "Failed to format time: ", e);
        }

        // Fetch user data from Firestore
        FirebaseFirestore.getInstance().collection("User").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    User user = documentSnapshot.toObject(User.class);
                    if (user != null) {
                        Log.d(TAG, "User data: " + user.getName());
                        Glide.with(context)
                                .load(user.getImage())
                                .placeholder(R.drawable.profileimage)
                                .into(holder.binding.profilePicView);
                        holder.binding.name.setText(user.getName());
                    } else {
                        Log.e(TAG, "User data is null for UID: " + uid);
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Failed to fetch user data: ", e));

        // Handle like button click
        holder.binding.like.setOnClickListener(v -> holder.binding.like.setImageResource(R.drawable.likeclick));
    }
}
