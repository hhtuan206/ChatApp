package com.hht.chatapp.Apdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerApdapter extends FragmentPagerAdapter {
    private final ArrayList<Fragment> fragments;
    private final ArrayList<String> titles;

    public ViewPagerApdapter(FragmentManager fm){
        super(fm);
        this.fragments = new ArrayList<>();
        titles = new ArrayList<>();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addFragment(Fragment fragment,String title){
        fragments.add(fragment);
        titles.add(title);
    }

    public CharSequence getPageTitle(int position){
        return titles.get(position);
    }
}