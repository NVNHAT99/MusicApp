package com.example.musicplayermyown;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class adapterFragment extends FragmentStatePagerAdapter {


    private String ListTab[] = {"All Song","Album","Play List"};

    private All_songFragment all_songFragment;
    private Album_Fragment album_fragment;
    private List_ListPlayFragment list_listPlayFragment;


    public adapterFragment(FragmentManager fm) {
        super(fm);
        all_songFragment = new All_songFragment();
        album_fragment = new Album_Fragment();
        list_listPlayFragment = new List_ListPlayFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return all_songFragment;
            case 1:
                return  album_fragment;
            case 2:
                return list_listPlayFragment;
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return ListTab[position];
    }

    @Override
    public int getCount() {

        return ListTab.length;
    }

}
