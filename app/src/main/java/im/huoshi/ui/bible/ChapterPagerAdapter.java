package im.huoshi.ui.bible;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import im.huoshi.model.Chapter;

/**
 * Created by Lyson on 16/1/11.
 */
public class ChapterPagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Chapter> mChapters = null;

    public ChapterPagerAdapter(FragmentManager fm, ArrayList<Chapter> chapters) {
        super(fm);
        this.mChapters = chapters;
    }

    @Override
    public int getCount() {
        return mChapters == null ? 0 : mChapters.size();
    }

    @Override
    public Fragment getItem(int position) {
        SectionFragment fragment = SectionFragment.getInstance(mChapters.get(position));
        return fragment;
    }
}
