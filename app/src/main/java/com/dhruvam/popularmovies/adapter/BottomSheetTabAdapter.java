package com.dhruvam.popularmovies.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dhruvam.popularmovies.fragments.TabFragmentOne;
import com.dhruvam.popularmovies.fragments.TabFragmentTwo;

public class BottomSheetTabAdapter extends FragmentStatePagerAdapter {

    private int mTabCount;

    public BottomSheetTabAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.mTabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0 :
                return new TabFragmentOne();

            case 1 :
                return new TabFragmentTwo();


            default:
                return null;

        }

    }

    @Override
    public int getCount() {
        return mTabCount;
    }


}
