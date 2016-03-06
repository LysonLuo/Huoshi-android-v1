package im.huoshi.ui.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import im.huoshi.R;
import im.huoshi.common.OnRecClickListener;
import im.huoshi.model.History;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;

/**
 * Created by Lyson on 16/2/28.
 */
public class HistoryRecAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<History> mHistories;
    private OnRecClickListener<History> mItemClickListener;

    public HistoryRecAdapter(Context context, List<History> histories, OnRecClickListener<History> listener) {
        this.mContext = context;
        this.mHistories = histories;
        this.mItemClickListener = listener;
    }

    public void resetData(List<History> histories) {
        this.mHistories = histories;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.widget_search_history_item, parent, false);
        return new HistoryViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        HistoryViewHolder viewHolder = (HistoryViewHolder) holder;
        viewHolder.mHistoryTextView.setText(mHistories.get(position).getKeyWord());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.OnClick(mHistories.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mHistories.size();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {
        @ViewInject(R.id.textview_history)
        private TextView mHistoryTextView;
        @ViewInject(R.id.view_divider)
        private View mDividerView;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            ViewUtils.inject(this, itemView);
        }
    }
}
