package com.example.catch_up;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.catch_up.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using ViewBinding
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Find the BottomNavigationView from the layout
        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Get the NavController associated with the navigation host fragment
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home);

        // Set up navigation with the BottomNavigationView
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

}
