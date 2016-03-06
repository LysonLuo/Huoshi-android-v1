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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.widget_section_container, container, false);
        ViewUtils.inject(this, contentView);

        setupViews();
        return contentView;
    }

    public void setIsChecked(boolean isChecked) {
        this.mIsChecked = isChecked;
    }

    private void setupViews() {
        this.mSectionDao = new SectionDao();
        mChapter = getArguments().getParcelable("chapter");
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        List<Section> sectionList = mSectionDao.getList(mChapter.getBookId(), mChapter.getChapterNo());
        mSectionAdapter = new SectionAdapter(getActivity(), sectionList);
        mRecyclerView.setAdapter(mSectionAdapter);
        mActivity = (ChapterDetailsActivity) getActivity();
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

    public static SectionFragment getInstance(Chapter chapter) {
        SectionFragment fragment = new SectionFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("chapter", chapter);
        fragment.setArguments(bundle);
        return fragment;
    }
}
