package im.huoshi.ui.bible;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import im.huoshi.R;
import im.huoshi.base.BaseFragment;
import im.huoshi.common.OnRecClickListener;
import im.huoshi.database.dao.SectionDao;
import im.huoshi.model.Chapter;
import im.huoshi.model.Section;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;

/**
 * Created by Lyson on 16/1/12.
 */
public class SectionFragment extends BaseFragment {
    @ViewInject(R.id.recyclerview_section)
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private Chapter mChapter = null;
    private SectionDao mSectionDao;
    private SectionAdapter mSectionAdapter;
    private ChapterDetailsActivity mActivity;
    private boolean mIsChecked = false;
    private String mKeyWord;
    private int mFirstVisibleItem;
    private List<Section> mSectionList;
    private int mCurrentPosition;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_section, container, false);
        ViewUtils.inject(this, contentView);
        setupViews();
        return contentView;
    }


    public void setIsChecked(boolean isChecked) {
        this.mIsChecked = isChecked;
    }

    public void changeIndexColor() {
        mSectionAdapter.changeIndexColor();
    }

    /**
     * 获取阅读历史
     *
     * @return
     */
    public int getLastSectionNo() {
        return mSectionList.get(mFirstVisibleItem).getSectionNo() == 0 ? mSectionList.get(mFirstVisibleItem + 1).getSectionNo() : mSectionList.get(mFirstVisibleItem).getSectionNo();
    }

    public int getLastPosition() {
        return mFirstVisibleItem;
    }

    private void setupViews() {
        this.mSectionDao = new SectionDao();
        mChapter = getArguments().getParcelable("chapter");
        mKeyWord = getArguments().getString("keyWord");
        mCurrentPosition = getArguments().getInt("position", 0);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mSectionList = mSectionDao.getList(mChapter.getBookId(), mChapter.getChapterNo());
        mSectionAdapter = new SectionAdapter(getActivity(), mSectionList, mKeyWord);
        mRecyclerView.setAdapter(mSectionAdapter);
        mActivity = (ChapterDetailsActivity) getActivity();
        if (mCurrentPosition != 0) {
            mRecyclerView.scrollToPosition(mCurrentPosition);
        }
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mFirstVisibleItem = mLayoutManager.findFirstCompletelyVisibleItemPosition();
            }
        });
        mSectionAdapter.setItemClickListener(new OnRecClickListener<Section>() {
            @Override
            public void OnClick(Section section) {
                if (!mIsChecked) {
                    mIsChecked = true;
                    mActivity.showLayout(section.getNoteText(), SectionFragment.this);
                } else {
                    mIsChecked = false;
                    mActivity.hideLayout();
                }
            }
        });
    }

    public static SectionFragment getInstance(Chapter chapter, String keyWord, int position) {
        SectionFragment fragment = new SectionFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("chapter", chapter);
        bundle.putString("keyWord", keyWord);
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        return fragment;
    }
}
