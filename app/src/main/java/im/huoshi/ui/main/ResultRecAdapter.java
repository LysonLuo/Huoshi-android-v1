package im.huoshi.ui.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import im.huoshi.R;
import im.huoshi.common.OnRecClickListener;
import im.huoshi.model.SearchResult;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;

/**
 * Created by Lyson on 16/2/28.
 */
public class ResultRecAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SearchResult> searchResults = new ArrayList<>();
    private Context mContext;
    private OnRecClickListener<SearchResult> mOnItemClickListener;
    private String mKeyWord;

    public ResultRecAdapter(Context context, String keyWord, List<SearchResult> searchResults, OnRecClickListener<SearchResult> listener) {
        this.mKeyWord = keyWord;
        this.searchResults = searchResults;
        this.mContext = context;
        this.mOnItemClickListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.widget_search_result_item, parent, false);
        ResultViewHolder viewHolder = new ResultViewHolder(contentView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ResultViewHolder viewHolder = (ResultViewHolder) holder;
        final SearchResult searchResult = searchResults.get(position);
        viewHolder.mBookNameTextView.setText(searchResult.getBookName());
        viewHolder.mChapterSectionTextView.setText(searchResult.getChapter() + ":" + searchResult.getSection());
        String sectionText = searchResult.getSectionText();
        SpannableString spannableString = new SpannableString(sectionText);
        spannableString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.text_color_blue_light)), sectionText.indexOf(mKeyWord), sectionText.indexOf(mKeyWord) + mKeyWord.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.mContentTextView.setText(spannableString);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.OnClick(searchResult);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    class ResultViewHolder extends RecyclerView.ViewHolder {
        @ViewInject(R.id.textview_result_book_name)
        private TextView mBookNameTextView;
        @ViewInject(R.id.textview_result_chapter_section)
        private TextView mChapterSectionTextView;
        @ViewInject(R.id.textview_result_content)
        private TextView mContentTextView;

        public ResultViewHolder(View itemView) {
            super(itemView);
            ViewUtils.inject(this, itemView);
        }
    }
}
