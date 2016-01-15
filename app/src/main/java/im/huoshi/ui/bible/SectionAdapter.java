package im.huoshi.ui.bible;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import im.huoshi.R;
import im.huoshi.common.OnRecClickListener;
import im.huoshi.model.Section;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;

/**
 * Created by Lyson on 16/1/1.
 */
public class SectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_VIEW_TYPE_HEADER = 1;
    private static final int ITEM_VIEW_TYPE_NORMAL = 2;
    private Context mContext;
    private OnRecClickListener mItemClickListener;
    private List<Section> mSectionList = new ArrayList<>();

    public SectionAdapter(Context context, List<Section> sectionList) {
        this.mContext = context;
        this.mSectionList = sectionList;
    }

    @Override
    public int getItemViewType(int position) {
        if (mSectionList.get(position).getSectionNo() == 0) {
            return ITEM_VIEW_TYPE_HEADER;
        }
        return ITEM_VIEW_TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_HEADER) {
            View contentView = LayoutInflater.from(mContext).inflate(R.layout.widget_chapter_header, parent, false);
            ChapterTitleHolder holder = new ChapterTitleHolder(contentView);
            return holder;
        }
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.widget_chapter_details_item, parent, false);
        ChapterDetailHolder holder = new ChapterDetailHolder(contentView);
        return holder;
    }

    public <Section> void setItemClickListener(OnRecClickListener<Section> clickListener) {
        this.mItemClickListener = clickListener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == ITEM_VIEW_TYPE_HEADER) {
            ChapterTitleHolder chapterTitleHolder = (ChapterTitleHolder) holder;
            chapterTitleHolder.mTitleTextView.setText(mSectionList.get(position).getSectionText());
            return;
        }
        ChapterDetailHolder detailHolder = (ChapterDetailHolder) holder;
        detailHolder.mIndexTextView.setText(mSectionList.get(position).getSectionNo() + "");
        detailHolder.mContentTextView.setText(mSectionList.get(position).getSectionText());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.OnClick(mSectionList.get(position));
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSectionList.size();
    }

    class ChapterTitleHolder extends RecyclerView.ViewHolder {
        @ViewInject(R.id.textview_chapter_title)
        private TextView mTitleTextView;

        public ChapterTitleHolder(View itemView) {
            super(itemView);
            ViewUtils.inject(this, itemView);
        }
    }

    class ChapterDetailHolder extends RecyclerView.ViewHolder {
        @ViewInject(R.id.textview_index)
        private TextView mIndexTextView;
        @ViewInject(R.id.textview_content)
        private TextView mContentTextView;

        public ChapterDetailHolder(View itemView) {
            super(itemView);
            ViewUtils.inject(this, itemView);
        }
    }
}
