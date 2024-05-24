package com.example.catch_up.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> titleList = new ArrayList<>();

    // Constructor: Initializes the fragment lists
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    // Returns the total number of fragments
    @Override
    public int getCount() {
        return fragmentList.size();
    }

    // Returns the fragment at the specified position
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    // Returns the title of the fragment at the specified position
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

    // Adds a fragment and its title to the lists
    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        titleList.add(title);
    }
}