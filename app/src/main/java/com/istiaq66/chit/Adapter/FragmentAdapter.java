package com.istiaq66.chit.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.istiaq66.chit.Fragments.Calls;
import com.istiaq66.chit.Fragments.Chats;
import com.istiaq66.chit.Fragments.Status;

public class FragmentAdapter extends FragmentPagerAdapter {
    String[] text = {"Chat","Status","Calls"};
    public FragmentAdapter(@NonNull  FragmentManager fm) {
        super(fm);
    }



    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0:
                return  new Chats();
            case 1:
                return new Status();
            case 2:
                return  new Calls();
            default:
                return  new Chats();
        }

    }

    @Override
    public int getCount() {
        return 3;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return text[position];
    }
}


