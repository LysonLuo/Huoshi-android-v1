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

import im.huoshi.R;
import im.huoshi.base.BaseActivity;
import im.huoshi.model.Chapter;
import im.huoshi.ui.main.MainActivity;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;

/**
 * Created by Lyson on 16/1/1.
 */
public class ChapterDetailsActivity extends BaseActivity {
    @ViewInject(R.id.viewpager_section)
    private ViewPager mViewPager;
    @ViewInject(R.id.textview_annotation)
    private TextView mAnnotationTextView;
    private Animation mAnimationUp;
    private Animation mAnimationDown;
    private Chapter[] mChapters;
    private int mCurrentPosition;
    private ChapterPagerAdapter mChapterAdapter;
    private boolean mIsShow = false;
    private SectionFragment mFragment;
    private String mBookName;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_details);
        ViewUtils.inject(this);

        setupViews();
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        mToolbarUtils.setTitleText(mBookName + "\t\t" + mChapters[mCurrentPosition].getChapterNo() + "章");
    }

    private void setupViews() {
        mChapters = (Chapter[]) getIntent().getSerializableExtra("chapters");
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
                mToolbarUtils.setTitleText(mBookName + "\t\t" + mChapters[position].getChapterNo() + "章");
                if (mIsShow) {
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

    public static void launch(MainActivity activity, String bookName, Chapter[] chapters, int position) {
        Intent intent = new Intent(activity, ChapterDetailsActivity.class);
        intent.putExtra("chapters", chapters);
        intent.putExtra("position", position);
        intent.putExtra("bookName", bookName);
        activity.startActivity(intent);
    }
}
