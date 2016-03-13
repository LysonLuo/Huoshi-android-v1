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
    private String mKeyWord;

    public ChapterPagerAdapter(FragmentManager fm, ArrayList<Chapter> chapters, String keyWord) {
        super(fm);
        this.mChapters = chapters;
        this.mKeyWord = keyWord;
    }

    @Override
    public int getCount() {
        return mChapters == null ? 0 : mChapters.size();
    }

    @Override
    public Fragment getItem(int position) {
        SectionFragment fragment = SectionFragment.getInstance(mChapters.get(position), mKeyWord);
        return fragment;
    }
}
