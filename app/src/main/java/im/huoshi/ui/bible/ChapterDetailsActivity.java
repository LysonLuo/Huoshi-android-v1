package im.huoshi.ui.bible;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
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
    private boolean mIsChecked = false;
    private Chapter[] mChapters;
    private ChapterPagerAdapter mChapterAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_details);
        ViewUtils.inject(this);

        setupViews();
    }

    private void setupViews() {
        mChapters = (Chapter[]) getIntent().getSerializableExtra("chapters");
        mAnimationUp = AnimationUtils.loadAnimation(this, R.anim.anim_translate_up);
        mAnimationDown = AnimationUtils.loadAnimation(this, R.anim.anim_translate_down);
        mChapterAdapter = new ChapterPagerAdapter(getSupportFragmentManager(), mChapters);
        mViewPager.setAdapter(mChapterAdapter);

//        mLayoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mAdapter = new SectionAdapter(this);
//        mRecyclerView.setAdapter(mAdapter);
//        mAdapter.setItemClickListener(new OnRecClickListener() {
//            @Override
//            public void OnClick(Object o) {
//                if (!mIsChecked) {
//                    mIsChecked = true;
//                    showLayout();
//                } else {
//                    mIsChecked = false;
//                    hideLayout();
//                }
//            }
//        });
    }

//    private void hideLayout() {
//        mAnimationDown.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                mAnnotationTextView.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        mAnnotationTextView.startAnimation(mAnimationDown);
//    }

//    private void showLayout() {
//        mAnimationUp.setAnimationListener(null);
//        mAnnotationTextView.setVisibility(View.VISIBLE);
//        mAnnotationTextView.startAnimation(mAnimationUp);
//    }

    public static void launch(MainActivity activity, Chapter[] chapters) {
        Intent intent = new Intent(activity, ChapterDetailsActivity.class);
        intent.putExtra("chapters", chapters);
        activity.startActivity(intent);
    }
}
