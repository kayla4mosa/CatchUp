package com.example.catch_up.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.catch_up.Adapters.PostAdapter;
import com.example.catch_up.Models.Post;
import com.example.catch_up.Models.User;
import com.example.catch_up.databinding.FragmentHomeBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ArrayList<Post> postList = new ArrayList<>();
    private PostAdapter adapter;
    private ArrayList<User> followList = new ArrayList<>();

    // Inflates the layout for this fragment and fetches posts from Firestore to display
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        // Initialize adapter and RecyclerView
        adapter = new PostAdapter(requireContext(), postList);
        binding.postRv.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.postRv.setAdapter(adapter);

        // Fetch posts from Firestore
        FirebaseFirestore.getInstance().collection("Post").get().addOnSuccessListener(queryDocumentSnapshots -> {
            ArrayList<Post> tempList = new ArrayList<>();
            postList.clear();
            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                Post post = document.toObject(Post.class);
                if (post != null) {
                    tempList.add(post);
                }
            }
            postList.addAll(tempList);
            adapter.notifyDataSetChanged();
        });

        return binding.getRoot();
    }
}
