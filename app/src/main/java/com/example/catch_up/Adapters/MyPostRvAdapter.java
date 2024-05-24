package com.example.catch_up.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.catch_up.Models.Post;
import com.example.catch_up.databinding.MyPostRvDesignBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyPostRvAdapter extends RecyclerView.Adapter<MyPostRvAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Post> postList;

    // Constructor: Initializes the adapter with context and post list
    public MyPostRvAdapter(Context context, ArrayList<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    // Creates ViewHolder and inflates the layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyPostRvDesignBinding binding = MyPostRvDesignBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    // Binds data to views (image loading)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = postList.get(position);
        // Bind post data to views
        Picasso.get().load(post.getPostUrl()).into(holder.binding.postImage);
    }

    // Returns the total number of items in the data set
    @Override
    public int getItemCount() {
        return postList.size();
    }

    // ViewHolder: Holds references to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        MyPostRvDesignBinding binding;

        // Constructor: Initializes the binding
        public ViewHolder(@NonNull MyPostRvDesignBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
