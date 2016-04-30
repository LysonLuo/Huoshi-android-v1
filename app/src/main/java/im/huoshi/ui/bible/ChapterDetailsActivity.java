package im.huoshi.ui.bible;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import im.huoshi.R;
import im.huoshi.asynapi.callback.RestApiCallback;
import im.huoshi.asynapi.request.ReadRequest;
import im.huoshi.base.BaseActivity;
import im.huoshi.model.Chapter;
import im.huoshi.model.LastHistory;
import im.huoshi.model.ReadStat;
import im.huoshi.model.event.RefreshEvent;
import im.huoshi.utils.DateUtils;
import im.huoshi.utils.LogUtils;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;

/**
 * Created by Lyson on 16/1/1.
 * 在某个页面超过300s，结束统计，如果在300s之内有切换页面，记录下最后一次切换的时间，再重新倒计时
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
    private int mLastPosition;
    private ChapterPagerAdapter mChapterAdapter;
    private boolean mIsShow = false;
    private SectionFragment mFragment;
    private String mBookName;
    private int mBookId;
    private String mKeyWord;
    private long mStartTime;
    private long mStopTime;
    private int mCurrentMinutes;
    private boolean mIsFirstShow = true;//是否初次打开页面

    private CountDownTimer mCountDownTimer;//300s的计时器，如果没有页面切换，统计结束
    private boolean mHasChangePage = false;//是否有做页面切换的操作
    private boolean mHasSavedData = false;
    private boolean mIsFinalChapter = false;//是否最后一章，用于提示
    private boolean mHasShowToast = false;//是否显示最后一章提示，只显示一次


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_details);
        ViewUtils.inject(this);

        setupViews();
        asynReadData();
        startTimer();
    }

    private void startTimer() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
        mCountDownTimer = new CountDownTimer(300 * 1000, 30 * 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                //到了三百秒，没有切换页面操作，结束计时，结束倒计时
                if (!mHasChangePage) {
                    updateReadStat();
                    if (mCountDownTimer != null) {
                        mCountDownTimer.cancel();
                        mCountDownTimer = null;
                    }
                    return;
                }
                //到了三百秒，有切换页面操作，继续倒计时
                startTimer();
            }
        }.start();
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
                public void onFailure() {
                }
            });
        }
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        if (mIsFirstShow) {
            mToolbarUtils.setTitleText(mBookName + "\t\t" + mChapters.get(mCurrentPosition).getChapterNo() + "章");
            mIsFirstShow = !mIsFirstShow;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        recordHistory();
        updateReadStat();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
    }

    /**
     * 记录阅读历史
     */
    private void recordHistory() {
        LastHistory lastHistory = new LastHistory();
        lastHistory.setTime(System.currentTimeMillis());
        lastHistory.setChapterNo(mChapters.get(mCurrentPosition).getChapterNo());
        lastHistory.setSectionNo(((SectionFragment) mChapterAdapter.getItem(mCurrentPosition)).getLastSectionNo());
        lastHistory.setLastPosition(((SectionFragment) mChapterAdapter.getItem(mCurrentPosition)).getLastPosition());
        lastHistory.setBookId(mBookId);
        lastHistory.setBookName(mBookName);
        mLocalRead.saveLastHistory(lastHistory);
        EventBus.getDefault().post(new RefreshEvent());
    }

    private void updateReadStat() {
        if (isLogin() && !mHasSavedData) {
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
                if (dayBetweenLast == 1) {
                    mLocalRead.updateContinuousDays();
                } else if (dayBetweenLast == 2 && dayBetweenCurrent == 1) {
                    mLocalRead.updateContinuousDays();
                    mLocalRead.setTodayShouldAdd(true);
                } else if (dayBetweenLast == 0) {
                    //今天读过了，不应该做任何事情，但是这种情况呢：从昨天十二点多，读到凌晨，那应该＋1。。。
                    if (mLocalRead.todayShouldAdd()) {
                        mLocalRead.updateContinuousDays();
                        mLocalRead.setTodayShouldAdd(false);
                    }
                } else {
                    mLocalRead.updateContinuousDays(1);
                }
                mLocalRead.updateLastReadLong(mStopTime);
                reloadLocalData();
            }
            mStartTime = System.currentTimeMillis();
            mHasSavedData = true;
            mHasChangePage = false;
        }
    }

    private void setupViews() {
        mStartTime = System.currentTimeMillis();
        mChapters = getIntent().getParcelableArrayListExtra("chapters");
        mCurrentPosition = getIntent().getIntExtra("position", 0);
        mLastPosition = getIntent().getIntExtra("last_position", 0);
        mBookName = getIntent().getStringExtra("bookName");
        mBookId = getIntent().getIntExtra("bookId", 0);
        mKeyWord = getIntent().getStringExtra("keyWord");
        mAnimationUp = AnimationUtils.loadAnimation(this, R.anim.anim_translate_up);
        mAnimationDown = AnimationUtils.loadAnimation(this, R.anim.anim_translate_down);
        mAnnotationTextView.setMovementMethod(new ScrollingMovementMethod());
        mChapterAdapter = new ChapterPagerAdapter(getSupportFragmentManager(), mChapters, mKeyWord, mCurrentPosition + 1, mLastPosition);
        mViewPager.setAdapter(mChapterAdapter);
        mViewPager.setCurrentItem(mCurrentPosition);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (mIsFinalChapter && !mHasShowToast) {
                    showLongToast("当前为最后一章");
                    mHasShowToast = true;
                    return;
                }
                if (position == mChapters.size() - 1) {
                    mIsFinalChapter = true;
                } else {
                    mHasShowToast = false;
                    mIsFinalChapter = false;
                }
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
                mToolbarUtils.setTitleText(mBookName + "\t\t" + mChapters.get(mCurrentPosition).getChapterNo() + "章");
                if (mIsShow) {
                    mFragment.changeIndexColor();
                    mFragment.setIsChecked(false);
                    hideLayout();
                }
                mHasChangePage = true;
                //已经保存过数据，说明超过了300s，或者执行了onstop
                if (mHasSavedData) {
                    mStartTime = System.currentTimeMillis();
                    startTimer();
                    mHasSavedData = false;
                    return;
                }
                //没保存过数据，直接重新倒计时即可
                startTimer();
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

    public static void launch(BaseActivity activity, String bookName, int bookId, String keyWord, ArrayList<Chapter> chapters, int position, int lastPosition) {
        Intent intent = new Intent(activity, ChapterDetailsActivity.class);
        intent.putParcelableArrayListExtra("chapters", chapters);
        intent.putExtra("position", position);
        intent.putExtra("bookName", bookName);
        intent.putExtra("bookId", bookId);
        intent.putExtra("keyWord", keyWord);
        intent.putExtra("last_position", lastPosition);
        activity.startActivity(intent);
    }
}
