package im.huoshi.ui.bible;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import im.huoshi.R;
import im.huoshi.base.BaseActivity;
import im.huoshi.common.OnRecClickListener;
import im.huoshi.model.Section;
import im.huoshi.utils.PopupListAdapter;
import im.huoshi.utils.PopupUtils;
import im.huoshi.utils.ShareUtils;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;

/**
 * Created by Lyson on 16/1/1.
 */
public class SectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_VIEW_TYPE_HEADER = 1;
    private static final int ITEM_VIEW_TYPE_NORMAL = 2;
    private Context mContext;
    private ChapterDetailHolder mSelectedViewHolder;
    private OnRecClickListener mItemClickListener;
    private List<Section> mSectionList = new ArrayList<>();
    private Set<Integer> mViewShowIndex = new HashSet<>();
    private int mSelectedIndex;
    private String mKeyWord;

    public SectionAdapter(Context context, List<Section> sectionList, String keyWord) {
        this.mContext = context;
        this.mSectionList = sectionList;
        this.mKeyWord = keyWord;
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == ITEM_VIEW_TYPE_HEADER) {
            ChapterTitleHolder chapterTitleHolder = (ChapterTitleHolder) holder;
            chapterTitleHolder.mTitleTextView.setText(mSectionList.get(position).getSectionText());
            return;
        }
        final ChapterDetailHolder detailHolder = (ChapterDetailHolder) holder;

        detailHolder.mIndexTextView.setText(mSectionList.get(position).getSectionNo() + "");
        String content = mSectionList.get(position).getSectionText();

        if (!TextUtils.isEmpty(mKeyWord) && content.indexOf(mKeyWord) != -1) {
            SpannableString spannableString = new SpannableString(content);
            spannableString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.text_color_blue_light)), content.indexOf(mKeyWord), content.indexOf(mKeyWord) + mKeyWord.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            detailHolder.mContentTextView.setText(spannableString);
        } else {
            detailHolder.mContentTextView.setText(content);
        }
        if (mViewShowIndex.contains(position)) {
            detailHolder.mIndexTextView.setTextColor(mContext.getResources().getColor(R.color.text_color_black));
        } else {
            detailHolder.mIndexTextView.setTextColor(mContext.getResources().getColor(R.color.text_section_index));
        }
        detailHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailHolder.mIndexTextView.requestFocus();
                if (mSelectedViewHolder != null) {
                    mSelectedViewHolder.mIndexTextView.setTextColor(mContext.getResources().getColor(R.color.text_section_index));
                    mSectionList.get(position).isChecked = false;
                    mSectionList.get(mSelectedIndex).isChecked = false;
                    mViewShowIndex.remove(position);
                    mViewShowIndex.remove(mSelectedIndex);
                    mSelectedViewHolder = null;
                    mItemClickListener.OnClick(mSectionList.get(position));
                    return;
                }
                mSelectedViewHolder = detailHolder;
                if (mItemClickListener != null) {
                    mSelectedIndex = position;
                    mSectionList.get(position).isChecked = true;
                    mViewShowIndex.add(position);
                    detailHolder.mIndexTextView.setTextColor(mContext.getResources().getColor(R.color.text_color_black));
                    mItemClickListener.OnClick(mSectionList.get(position));
                }
            }
        });
        detailHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                detailHolder.mContentTextView.setTextIsSelectable(true);
                detailHolder.mContentTextView.setSelectAllOnFocus(true);
                detailHolder.mContentTextView.requestFocus();
                final PopupUtils popupUtils = new PopupUtils();
                final List<String> popupList = new ArrayList<>();
                popupList.add("复制");
                popupList.add("分享");
                popupUtils.setOnItemClickListener(new PopupListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View contextView, int contextPosition, View view, int position) {
                        //复制
                        if (position == 0) {
                            ClipboardManager clipboardManager = (ClipboardManager) mContext.getSystemService(Service.CLIPBOARD_SERVICE);
                            ClipData clipData = ClipData.newPlainText("data", detailHolder.mContentTextView.getText());
                            clipboardManager.setPrimaryClip(clipData);
                            Toast.makeText(mContext, "复制成功", Toast.LENGTH_SHORT).show();
                            popupUtils.dismiss();
                            detailHolder.mContentTextView.setSelected(false);
                            detailHolder.mContentTextView.clearFocus();
                            return;
                        }
                        //分享
                        ShareUtils.initOnlyText((BaseActivity) mContext, detailHolder.mContentTextView.getText().toString());
                        popupUtils.dismiss();
                    }
                });
                popupUtils.showPopupWindow(mContext, detailHolder.itemView, position, popupList);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSectionList.size();
    }

    public void changeIndexColor() {
        if (mSelectedViewHolder != null) {
            mSectionList.get(mSelectedIndex).isChecked = false;
            mViewShowIndex.remove(mSelectedIndex);
            mSelectedViewHolder.mIndexTextView.setTextColor(mContext.getResources().getColor(R.color.text_section_index));
            mSelectedViewHolder = null;
        }
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
