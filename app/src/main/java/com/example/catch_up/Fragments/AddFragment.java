package com.example.catch_up.Fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.catch_up.Post.PostActivity;
import com.example.catch_up.R;

public class AddFragment extends Fragment {
    public AddFragment() {
        // Required empty public constructor
    }

    // Inflates the layout for this fragment and starts PostActivity immediately
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Intent intent = new Intent(getActivity(), PostActivity.class);
        startActivity(intent);
        if (getActivity() != null) {
            getActivity().finish();
        }
        return inflater.inflate(R.layout.fragment_add, container, false);
    }
}
