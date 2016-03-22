package im.huoshi.ui.find.interces;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lyson on 15/12/27.
 */
public class InterCesPagerAdapter extends FragmentStatePagerAdapter {
    private List<InterCesFragment> mFragments = new ArrayList<>();
    private String[] mTitles = new String[]{"我的祷告", "我的代祷"};

    public InterCesPagerAdapter(FragmentManager fm, List<InterCesFragment> mFragments) {
        super(fm);
        this.mFragments = mFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public int getCount() {
        return 2;
    }
}
