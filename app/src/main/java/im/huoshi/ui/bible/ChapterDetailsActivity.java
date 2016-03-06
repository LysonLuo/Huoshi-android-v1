package im.huoshi.ui.bible;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import im.huoshi.R;
import im.huoshi.asynapi.callback.RestApiCallback;
import im.huoshi.asynapi.request.ReadRequest;
import im.huoshi.base.BaseActivity;
import im.huoshi.model.ApiError;
import im.huoshi.model.Chapter;
import im.huoshi.model.ReadStat;
import im.huoshi.utils.DateUtils;
import im.huoshi.utils.LogUtils;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;

/**
 * Created by Lyson on 16/1/1.
 */
public class ChapterDetailsActivity extends BaseActivity {
    private static final String LOG_TAG = ChapterDetailsActivity.class.getSimpleName();
    @ViewInject(R.id.viewpager_section)
    private ViewPager mViewPager;
    @ViewInject(R.id.textview_annotation)
    private TextView mAnnotationTextView;
    private Animation mAnimationUp;
    private Animation mAnimationDown;
    private ArrayList<Chapter> mChapters;
    private int mCurrentPosition;
    private ChapterPagerAdapter mChapterAdapter;
    private boolean mIsShow = false;
    private SectionFragment mFragment;
    private String mBookName;
    private long mStartTime;
    private long mStopTime;
    private int mCurrentMinutes;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_details);
        ViewUtils.inject(this);

        setupViews();
        asynReadData();
    }

    private void asynReadData() {
        if (isLogin()) {
            ReadRequest.readStat(ChapterDetailsActivity.this, mUser.getUserId(), mReadStat.getLastMinutes(), mReadStat.getTotalMinutes(), mReadStat.getContinuousDays(), mLocalRead.getAddStat(), new RestApiCallback() {
                @Override
                public void onSuccess(String responseString) {
                    ReadStat readStat = new Gson().fromJson(responseString, new TypeToken<ReadStat>() {
                    }.getType());
                    mLocalRead.saveReadStat(readStat);
                    mLocalRead.updateAddStat(false);
                    LogUtils.d(LOG_TAG, "同步成功~");
                }

                @Override
                public void onFailure(ApiError apiError) {
                    LogUtils.d(LOG_TAG, apiError.errorMessage);
                }
            });
        }
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        mToolbarUtils.setTitleText(mBookName + "\t\t" + mChapters.get(mCurrentPosition).getChapterNo() + "章");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isLogin()) {
            mStopTime = System.currentTimeMillis();
            //本次阅读结束距离上次阅读结束的时间间隔
            int dayBetweenLast = DateUtils.getDayBetween(mLocalRead.getLastReadLong());
            //本次阅读结束距离本次阅读开始的时间间隔
            int dayBetweenCurrent = DateUtils.getDayBetween(mStartTime);
            mCurrentMinutes = (int) (mStopTime - mStartTime) / (60 * 1000);

            if (mCurrentMinutes >= 1) {
                mLocalRead.updateAddStat(true);
                mLocalRead.updateLastMinutes(mCurrentMinutes);
                mLocalRead.updateTotalMinutes();
                //从上次阅读结束到这次阅读结束,时间间隔为1,或者从上次阅读结束到这次阅读结束,时间间隔为2,但是从开始阅读到结束阅读,时间间隔为1,也就是过了24点还在读~
                if (dayBetweenLast == 1 || (dayBetweenLast == 2 && dayBetweenCurrent == 1)) {
                    mLocalRead.updateContinuousDays();
                } else {
                    mLocalRead.updateContinuousDays(1);
                }
                mLocalRead.updateLastReadLong(mStopTime);
                reloadLocalData();
            }
        }
    }

    private void setupViews() {
        mStartTime = System.currentTimeMillis();
        mChapters = getIntent().getParcelableArrayListExtra("chapters");
        mCurrentPosition = getIntent().getIntExtra("position", 0);
        mBookName = getIntent().getStringExtra("bookName");
        mAnimationUp = AnimationUtils.loadAnimation(this, R.anim.anim_translate_up);
        mAnimationDown = AnimationUtils.loadAnimation(this, R.anim.anim_translate_down);
        mChapterAdapter = new ChapterPagerAdapter(getSupportFragmentManager(), mChapters);
        mViewPager.setAdapter(mChapterAdapter);
        mViewPager.setCurrentItem(mCurrentPosition);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mToolbarUtils.setTitleText(mBookName + "\t\t第" + mChapters.get(position).getChapterNo() + "章");
                if (mIsShow) {
                    mFragment.changeIndexColor(false);
                    mFragment.setIsChecked(false);
                    hideLayout();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void hideLayout() {
        mIsShow = false;
        mAnimationDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mAnnotationTextView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mAnnotationTextView.startAnimation(mAnimationDown);
    }

    public void showLayout(String noteText, SectionFragment sectionFragment) {
        mIsShow = true;
        mFragment = sectionFragment;
        mAnimationUp.setAnimationListener(null);
        mAnnotationTextView.setText(TextUtils.isEmpty(noteText) ? "暂无注释" : noteText);
        mAnnotationTextView.setVisibility(View.VISIBLE);
        mAnnotationTextView.startAnimation(mAnimationUp);
    }

    public static void launch(BaseActivity activity, String bookName, ArrayList<Chapter> chapters, int position) {
        Intent intent = new Intent(activity, ChapterDetailsActivity.class);
        intent.putParcelableArrayListExtra("chapters", chapters);
        intent.putExtra("position", position);
        intent.putExtra("bookName", bookName);
        activity.startActivity(intent);
    }
}
