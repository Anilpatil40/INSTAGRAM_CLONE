package com.swayam.instagramclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAdapter extends FragmentPagerAdapter {
    private FragmentManager fm ;

    public TabAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new UserDetailsFragment();
            case 1:
                return new PostFragment();
            case 2:
                return new AddPostFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "User";
            case 1:
                return "Posts";
            case 2:
                return "Add Post";
        }
        return super.getPageTitle(position);
    }
}
