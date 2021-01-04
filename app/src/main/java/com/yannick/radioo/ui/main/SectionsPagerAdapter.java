package com.yannick.radioo.ui.main;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.yannick.radioo.R;
import com.yannick.radioo.radios.FavouriteFragment;
import com.yannick.radioo.radios.PlayerFragment;
import com.yannick.radioo.radios.PodcastFragment;
import com.yannick.radioo.radios.RadioFragment;
import com.yannick.radioo.radios.VocalUIFragment;


public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
//            case 0:
//                return VocalUIFragment.newInstance();
            case 0: return RadioFragment.newInstance(1);
            case 1: return PodcastFragment.newInstance(1);
            case 2: return PlayerFragment.newInstance(null);
            case 3: return FavouriteFragment.newInstance(1);
            default: return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: //Page number 1
                return "Search";
            case 1: //Page number 2
                return "Podcasts";
            case 2: //Page number 3
                return "Player";
            case 3: //Page number 4
                return "Favourites";
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        // Show 4 total pages.
        return 4;
    }
}