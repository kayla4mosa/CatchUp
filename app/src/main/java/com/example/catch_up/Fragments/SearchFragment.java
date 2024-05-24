package com.example.catch_up.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.catch_up.Adapters.SearchAdapter;
import com.example.catch_up.Models.User;
import com.example.catch_up.databinding.FragmentSearchBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    private FragmentSearchBinding binding;
    private SearchAdapter adapter;
    private ArrayList<User> userList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        binding.rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new SearchAdapter(requireContext(), userList);
        binding.rv.setAdapter(adapter);

        FirebaseFirestore.getInstance().collection("User").get().addOnSuccessListener(queryDocumentSnapshots -> {
            ArrayList<User> tempList = new ArrayList<>();
            userList.clear();
            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                if (!document.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    User user = document.toObject(User.class);
                    if (user != null) {
                        tempList.add(user);
                    }
                }
            }
            userList.addAll(tempList);
            adapter.notifyDataSetChanged();
        });

        binding.searchView.setOnClickListener(v -> {
            String text = binding.searchView.getText().toString();
            FirebaseFirestore.getInstance().collection("User").whereEqualTo("name", text).get().addOnSuccessListener(queryDocumentSnapshots -> {
                ArrayList<User> tempList = new ArrayList<>();
                userList.clear();
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        if (!document.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            User user = document.toObject(User.class);
                            if (user != null) {
                                tempList.add(user);
                            }
                        }
                    }
                    userList.addAll(tempList);
                    adapter.notifyDataSetChanged();
                }
            });
        });

        return binding.getRoot();
    }
}