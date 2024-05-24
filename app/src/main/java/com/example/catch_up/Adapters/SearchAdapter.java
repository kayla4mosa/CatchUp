package com.example.catch_up.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.catch_up.Models.User;
import com.example.catch_up.R;
import com.example.catch_up.databinding.SearchRvBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private Context context;
    private ArrayList<User> userList;

    public SearchAdapter(Context context, ArrayList<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private SearchRvBinding binding;

        public ViewHolder(SearchRvBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SearchRvBinding binding = SearchRvBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        boolean[] isFollow = {false};
        User user = userList.get(position);

        Glide.with(context).load(user.getImage()).placeholder(R.drawable.user).into(holder.binding.profilePicView);
        holder.binding.name.setText(user.getName());

        FirebaseFirestore.getInstance().collection(FirebaseAuth.getInstance().getCurrentUser().getUid() + "Follow")
                .whereEqualTo("email", user.getEmail()).get().addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.getDocuments().isEmpty()) {
                        isFollow[0] = false;
                    } else {
                        holder.binding.follow.setText("Unfollow");
                    }
                });

        holder.binding.follow.setOnClickListener(v -> {
            if (isFollow[0]) {
                FirebaseFirestore.getInstance().collection(FirebaseAuth.getInstance().getCurrentUser().getUid() + "Follow")
                        .whereEqualTo("email", user.getEmail()).get().addOnSuccessListener(queryDocumentSnapshots -> {
                            FirebaseFirestore.getInstance().collection(FirebaseAuth.getInstance().getCurrentUser().getUid() + "Follow")
                                    .document(queryDocumentSnapshots.getDocuments().get(0).getId()).delete();
                            holder.binding.follow.setText("Follow");
                            isFollow[0] = false;
                        });
            } else {
                FirebaseFirestore.getInstance().collection(FirebaseAuth.getInstance().getCurrentUser().getUid() + "Follow")
                        .document().set(user);
                holder.binding.follow.setText("Unfollow");
                isFollow[0] = true;
            }
        });
    }
}