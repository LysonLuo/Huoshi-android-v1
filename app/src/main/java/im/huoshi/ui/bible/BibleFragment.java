package im.huoshi.ui.bible;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import im.huoshi.R;
import im.huoshi.base.BaseFragment;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;

/**
 * Created by Lyson on 15/12/24.
 */
public class BibleFragment extends BaseFragment {
    @ViewInject(R.id.tablayout)
    private TabLayout mTablayout;
    @ViewInject(R.id.viewpager)
    private ViewPager mViewPager;
    private BiblePagerAdapter mAdapter;
    private List<Fragment> mFragments = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_bible, container, false);
        ViewUtils.inject(this, contentView);

        setupViews();
        return contentView;
    }

    private void setupViews() {
        initFragments();
        mAdapter = new BiblePagerAdapter(getChildFragmentManager(), mFragments);
        mViewPager.setAdapter(mAdapter);
        mTablayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(1);
    }

    private void initFragments() {
        mFragments.add(BibleContentFragment.getInstance(false));
        mFragments.add(BibleContentFragment.getInstance(true));
    }
}
