package im.huoshi.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import im.huoshi.R;
import im.huoshi.base.BaseActivity;
import im.huoshi.ui.find.interces.InterCesFragment;
import im.huoshi.ui.find.interces.InterCesPagerAdapter;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;

/**
 * Created by Lyson on 15/12/26.
 */
public class MyPrayerActivity extends BaseActivity {
    @ViewInject(R.id.tablayout)
    private TabLayout mTablayout;
    @ViewInject(R.id.viewpager)
    private ViewPager mViewPager;
    private InterCesPagerAdapter mAdapter;
    private List<InterCesFragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_prayer);
        ViewUtils.inject(this);

        setupViews();
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        mToolbarUtils.setTitleText("我的祷告箱");
    }

    private void setupViews() {
        mFragments.add(InterCesFragment.getInstance(InterCesFragment.INTERCES_TYPE_PRAYER));
        mFragments.add(InterCesFragment.getInstance(InterCesFragment.INTERCES_TYPE_PRAYER));
        mAdapter = new InterCesPagerAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(mAdapter);
        mTablayout.setupWithViewPager(mViewPager);
    }

    public static void launch(BaseActivity activity) {
        activity.startActivity(new Intent(activity, MyPrayerActivity.class));
    }
}
