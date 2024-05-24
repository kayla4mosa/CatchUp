package com.example.catch_up.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.catch_up.Adapters.ViewPagerAdapter;
import com.example.catch_up.Models.User;
import com.example.catch_up.SignUpActivity;
import com.example.catch_up.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ViewPagerAdapter viewPagerAdapter;

    // Inflates the layout for this fragment, sets up ViewPager with fragments, and fetches user data from Firestore
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        // Navigate to Edit Profile Activity
        binding.editProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SignUpActivity.class);
            intent.putExtra("MODE", 1);
            if (getActivity() != null) {
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });

        // Set up ViewPager with fragments
        viewPagerAdapter = new ViewPagerAdapter(requireActivity().getSupportFragmentManager());
        viewPagerAdapter.addFragment(new MyPostFragment(), "My Posts");
        binding.viewPager.setAdapter(viewPagerAdapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        return binding.getRoot();
    }

    // Fetches and displays user data from Firestore
    @Override
    public void onStart() {
        super.onStart();
        FirebaseFirestore.getInstance().collection("User")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    User user = documentSnapshot.toObject(User.class);
                    if (user != null) {
                        binding.name.setText(user.getName());
                        binding.bio.setText(user.getEmail());
                        if (user.getImage() != null && !user.getImage().isEmpty()) {
                            Picasso.get().load(user.getImage()).into(binding.profilePicView);
                        }
                    }
                });
    }
}
