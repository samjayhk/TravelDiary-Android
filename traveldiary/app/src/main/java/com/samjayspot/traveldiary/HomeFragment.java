package com.samjayspot.traveldiary;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends Fragment {

    View rootView;
    TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(
                R.layout.fragment_home,
                container,
                false);

        tabLayout = (TabLayout) rootView.findViewById(R.id.tabLayout);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.main_blue));
        tabLayout.setTabTextColors(getResources().getColor(R.color.main_font_color_2), getResources().getColor(R.color.main_font_content));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_popular));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_list));

        final ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewpagerHome);
        tabLayout.setupWithViewPager(viewPager);

        HomePagerAdapter pagerAdapter = new HomePagerAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount(), getContext());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return rootView;
    }
}
