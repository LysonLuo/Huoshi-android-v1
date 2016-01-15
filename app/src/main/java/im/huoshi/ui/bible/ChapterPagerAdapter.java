package im.huoshi.ui.bible;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import im.huoshi.model.Chapter;

/**
 * Created by Lyson on 16/1/11.
 */
public class ChapterPagerAdapter extends FragmentStatePagerAdapter {
    private Chapter[] mChapters = null;

    public ChapterPagerAdapter(FragmentManager fm, Chapter[] chapters) {
        super(fm);
        this.mChapters = chapters;
    }

    @Override
    public int getCount() {
        return mChapters == null ? 0 : mChapters.length;
    }

    @Override
    public Fragment getItem(int position) {
        SectionFragment fragment = SectionFragment.getInstance(mChapters[position]);
        return fragment;
    }
}
