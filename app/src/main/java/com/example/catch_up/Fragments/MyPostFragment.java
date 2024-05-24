package com.example.catch_up.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.example.catch_up.Adapters.MyPostRvAdapter;
import com.example.catch_up.Models.Post;
import com.example.catch_up.databinding.FragmentMyPostBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Objects;

public class MyPostFragment extends Fragment {
    private FragmentMyPostBinding binding;

    // Inflates the layout for this fragment and fetches user's posts from Firestore to display
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyPostBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        ArrayList<Post> postList = new ArrayList<>();
        MyPostRvAdapter adapter = new MyPostRvAdapter(requireContext(), postList);
        binding.rv.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        binding.rv.setAdapter(adapter);

        // Fetch user's posts from Firestore
        FirebaseFirestore.getInstance().collection(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    postList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Post post = document.toObject(Post.class);
                        if (post != null) {
                            postList.add(post);
                        }
                    }
                    adapter.notifyDataSetChanged();
                });
        return rootView;
    }
}
