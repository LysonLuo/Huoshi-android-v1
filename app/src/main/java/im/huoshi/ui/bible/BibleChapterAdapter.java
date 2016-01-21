package im.huoshi.ui.bible;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.List;

import im.huoshi.R;
import im.huoshi.model.Chapter;
import im.huoshi.ui.main.MainActivity;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;

/**
 * Created by Lyson on 15/12/29.
 */
public class BibleChapterAdapter extends RecyclerView.Adapter<BibleChapterAdapter.ChapterViewHolder> {
    private Context mContext;
    private String mBookName;
    private BibleAdapter mBibleAdapter;
    private List<Chapter> mChapterList = new ArrayList<>();

    public BibleChapterAdapter(Context mContext, String bookName, List<Chapter> chapterList) {
        this.mContext = mContext;
        this.mBookName = bookName;
        this.mChapterList = chapterList;
    }

    @Override
    public ChapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.widget_chapter_item, parent, false);
        ChapterViewHolder holder = new ChapterViewHolder(contentView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ChapterViewHolder holder, final int position) {
        holder.mChapterCheckBox.setText(mChapterList.get(position).getChapterNo() + "");
        holder.mChapterCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ChapterDetailsActivity.launch((MainActivity) mContext, mBookName, mChapterList.toArray(new Chapter[mChapterList.size()]), position);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBibleAdapter.hideLayout(null);
                    }
                }, 500);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mChapterList.size();
    }

    public void setParentAdapter(BibleAdapter bibleAdapter) {
        this.mBibleAdapter = bibleAdapter;
    }

    class ChapterViewHolder extends RecyclerView.ViewHolder {
        @ViewInject(R.id.checkbox_chapter)
        private CheckBox mChapterCheckBox;

        public ChapterViewHolder(View itemView) {
            super(itemView);
            ViewUtils.inject(this, itemView);
        }
    }
}
