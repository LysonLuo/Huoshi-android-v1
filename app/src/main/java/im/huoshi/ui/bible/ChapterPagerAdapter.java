package im.huoshi.ui.bible;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import im.huoshi.model.Chapter;

/**
 * Created by Lyson on 16/1/11.
 */
public class ChapterPagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Chapter> mChapters = null;
    private List<SectionFragment> mFragmentList = new ArrayList<>();
    private String mKeyWord;

    public ChapterPagerAdapter(FragmentManager fm, ArrayList<Chapter> chapters, String keyWord,int chapterNo, int sectionNo) {
        super(fm);
        this.mChapters = chapters;
        this.mKeyWord = keyWord;
        for (Chapter chapter : mChapters) {
            mFragmentList.add(SectionFragment.getInstance(chapter, mKeyWord, (chapter.getChapterNo() == chapterNo ? sectionNo : 0)));
        }
    }

    @Override
    public int getCount() {
        return mChapters == null ? 0 : mChapters.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }
}
