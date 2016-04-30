package im.huoshi.ui.bible;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.List;

import im.huoshi.R;
import im.huoshi.database.dao.ChapterDao;
import im.huoshi.model.Book;
import im.huoshi.model.Chapter;
import im.huoshi.utils.DeviceUtils;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;
import im.huoshi.utils.ViewWrapperUtils;

/**
 * Created by Lyson on 15/12/24.
 */
public class BibleAdapter extends RecyclerView.Adapter<BibleAdapter.BibleViewHolder> {
    private Context mContext;
    private ViewWrapperUtils mViewWrap;
    private ViewWrapperUtils mDividerWrap;
    private int mSelectedIndex = -1;
    private int mSelectedColumnIndex = -1;
    private CheckBox mSelectCheckBox;
    private List<Book> mBookList = new ArrayList<>();
    private ChapterDao mChapterDao;

    public BibleAdapter(Context mContext, List<Book> bookList) {
        this.mContext = mContext;
        this.mBookList = bookList;
        mChapterDao = new ChapterDao();
    }

    @Override
    public BibleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.widget_bible_item, parent, false);
        BibleViewHolder holder = new BibleViewHolder(contentView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final BibleViewHolder holder, final int position) {
        final int basePosition = 3 * position;
        SpannableString spannableString = new SpannableString(mBookList.get(basePosition).getBookName());
        int contentLenth = spannableString.toString().length();
        spannableString.setSpan(new RelativeSizeSpan(contentLenth == 7 ? 0.61f : 0.72f), 1, contentLenth, 0);
        spannableString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.text_color_black)), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.mFirstCheckBox.setText(spannableString);

        SpannableString spannableString1 = new SpannableString(mBookList.get(basePosition + 1).getBookName());
        int contentLenth1 = spannableString1.toString().length();
        spannableString1.setSpan(new RelativeSizeSpan(contentLenth1 == 7 ? 0.61f : 0.72f), 1, contentLenth1, 0);
        spannableString1.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.text_color_black)), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString1.setSpan(new StyleSpan(Typeface.BOLD), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.mSecondCheckBox.setText(spannableString1);

        SpannableString spannableString2 = new SpannableString(mBookList.get(basePosition + 2).getBookName());
        int contentLenth2 = spannableString2.toString().length();
        spannableString2.setSpan(new RelativeSizeSpan(contentLenth2 == 7 ? 0.61f : 0.72f), 1, contentLenth2, 0);
        spannableString2.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.text_color_black)), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString2.setSpan(new StyleSpan(Typeface.BOLD), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.mThirdCheckBox.setText(spannableString2);

        holder.mFirstCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                hideOrShowChapter(holder, isChecked, 0, position, basePosition, holder.mFirstCheckBox);
            }
        });
        holder.mSecondCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                hideOrShowChapter(holder, isChecked, 1, position, basePosition, holder.mSecondCheckBox);
            }
        });
        holder.mThirdCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                hideOrShowChapter(holder, isChecked, 2, position, basePosition, holder.mThirdCheckBox);
            }
        });
    }

    private void hideOrShowChapter(final BibleViewHolder holder, boolean isChecked, int index, int position, int basePostion, CheckBox selectCheckBox) {
        if ((mSelectedIndex != -1 && mSelectedIndex != position) || (mSelectedColumnIndex != -1 && mSelectedColumnIndex != index)) {
            switch (index) {
                case 0:
                    holder.mFirstCheckBox.setChecked(false);
                    break;
                case 1:
                    holder.mSecondCheckBox.setChecked(false);
                    break;
                case 2:
                    holder.mThirdCheckBox.setChecked(false);
                    break;
            }
            if (mSelectCheckBox != null) {
                mSelectCheckBox.setChecked(false);
            }
            mSelectedIndex = -1;
            mSelectedColumnIndex = -1;
            ObjectAnimator.ofInt(mViewWrap, "height", DeviceUtils.dip2px(mContext, 130), 0).setDuration(500).start();
            ObjectAnimator.ofInt(mViewWrap, "width", DeviceUtils.dip2px(mContext, 330), 0).setDuration(500).start();
            return;
        }
        if (isChecked) {
            mSelectedIndex = position;
            mSelectCheckBox = selectCheckBox;
            mSelectedColumnIndex = index;
            holder.mLayoutManager = new GridLayoutManager(mContext, 8);
            holder.mRecyclerView.setLayoutManager(holder.mLayoutManager);
            holder.mChapterList = loadChapter(basePostion + index);
            holder.mAdapter = new BibleChapterAdapter(mContext, mSelectCheckBox.getText().toString(), mBookList.get(basePostion + index).getBookNo(), holder.mChapterList);
            holder.mRecyclerView.setAdapter(holder.mAdapter);
            holder.mAdapter.setParentAdapter(this);
            mViewWrap = new ViewWrapperUtils(holder.mRecyclerView);
            mDividerWrap = new ViewWrapperUtils(holder.mDividerView);
            holder.lineCount = (int) Math.ceil(holder.mChapterList.size() / 8.0);
            ObjectAnimator.ofInt(mViewWrap, "height", 0, DeviceUtils.dip2px(mContext, holder.lineCount * (40) + 10)).setDuration(500).start();
            ObjectAnimator.ofInt(mViewWrap, "width", 0, DeviceUtils.dip2px(mContext, 330)).setDuration(500).start();
            ObjectAnimator.ofInt(mDividerWrap, "height", 0, DeviceUtils.dip2px(mContext, 10)).setDuration(200).start();
        } else {
            hideLayout(holder);
        }
    }

    private ArrayList<Chapter> loadChapter(int index) {
        return mChapterDao.getList(mBookList.get(index).getBookNo());
    }

    public void hideLayout(BibleViewHolder holder) {
        if (mSelectCheckBox != null) {
            mSelectCheckBox.setChecked(false);
        }
        mSelectedIndex = -1;
        mSelectedColumnIndex = -1;
        ObjectAnimator.ofInt(mViewWrap, "height", DeviceUtils.dip2px(mContext, holder == null ? 100 : holder.lineCount * (40) + 10), 0).setDuration(500).start();
        ObjectAnimator.ofInt(mViewWrap, "width", DeviceUtils.dip2px(mContext, 330), 0).setDuration(500).start();
        ObjectAnimator.ofInt(mDividerWrap, "height", DeviceUtils.dip2px(mContext, 10), 0).setDuration(500).start();
    }


    @Override
    public int getItemCount() {
        return (int) Math.ceil(mBookList.size() / 3.0);
    }

    public void resetData(List<Book> bookList) {
        this.mBookList = bookList;
        notifyDataSetChanged();
    }

    class BibleViewHolder extends RecyclerView.ViewHolder {
        @ViewInject(R.id.checkbox_book_first)
        private CheckBox mFirstCheckBox;
        @ViewInject(R.id.checkbox_book_second)
        private CheckBox mSecondCheckBox;
        @ViewInject(R.id.checkbox_book_third)
        private CheckBox mThirdCheckBox;
        @ViewInject(R.id.view_divider)
        private View mDividerView;
        @ViewInject(R.id.recyclerview)
        private RecyclerView mRecyclerView;
        private GridLayoutManager mLayoutManager;
        private BibleChapterAdapter mAdapter;
        private int lineCount;
        private ArrayList<Chapter> mChapterList = new ArrayList<>();

        public BibleViewHolder(View itemView) {
            super(itemView);
            ViewUtils.inject(this, itemView);
        }
    }
}
