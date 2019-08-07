package com.samjayspot.traveldiary;

/**
 * PLEASE BE MIND THAT YOU COULD NOT COPY, MODIFY OR SHARE THIS PROJECT IF YOU ARE NOT GET PERMISSION.
 * @samjayhk
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class HomePagerAdapter extends FragmentStatePagerAdapter {

    int nNumOfTabs;
    private Context _context;

    public HomePagerAdapter(FragmentManager fm, int nNumOfTabs, Context c) {
        super(fm);
        this.nNumOfTabs = nNumOfTabs;
        this._context = c;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                PopularTabFragment tab1 = new PopularTabFragment();
                return tab1;
            case 1:
                ListTabFragment tab2 = new ListTabFragment();
                return tab2;
        }
        return null;
    }

    @Override
    public int getCount() {
        return nNumOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String[] title = {_context.getResources().getString(R.string.tab_popular), _context.getResources().getString(R.string.tab_list)};
        return title[position];
    }
}