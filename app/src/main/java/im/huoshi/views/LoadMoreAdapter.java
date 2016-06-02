package im.huoshi.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import im.huoshi.R;

/**
 * Created by Lyson on 16/4/14.
 */
public abstract class LoadMoreAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected boolean mNomoreData;
    protected LayoutInflater mLayoutInflater;
    protected List<T> mItemList;
    private static final int ITEM_VIEW_TYPE_NORMAL = 1000;
    private static final int ITEM_VIEW_TYPE_LOADING = 1001;

    public LoadMoreAdapter(Context context, List<T> list) {
        this.mItemList = list;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public void setNoMoreData(boolean noMoreData) {
        this.mNomoreData = noMoreData;
        if (this.mNomoreData && getItemCount() > 0) {
            notifyItemChanged(getItemCount() - 1);
        }
//        notifyDataSetChanged();
    }

    public void resetData(List<T> list) {
        mItemList = list;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_LOADING) {
            return new LoadingViewHolder(mLayoutInflater.inflate(R.layout.widget_loading, parent, false));
        }
        return getNewItemViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            if (mNomoreData) {
                loadingViewHolder.mProgressBar.setVisibility(View.GONE);
                loadingViewHolder.mLoadingTextView.setText("已经加载完毕");
            } else {
                loadingViewHolder.mProgressBar.setVisibility(View.VISIBLE);
                loadingViewHolder.mLoadingTextView.setText("正在加载...");
            }
            return;
        }
        bindNewViewHolder(holder, position);
    }

//    @Override
//    public int getItemCount() {
//        if (mItemList == null || mItemList.size() == 0) {
//            return 0;
//        }
//        return mItemList.size() + 1;
//    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (position != 0 && position == getItemCount() - 1) {
            return ITEM_VIEW_TYPE_LOADING;
        }
        return getNewItemViewtype(position);
    }

    public abstract long getNewItemId(int position);

    public int getNewItemViewtype(int position) {
        return -1;
    }

    public abstract RecyclerView.ViewHolder getNewItemViewHolder(ViewGroup parent, int viewType);

    public abstract void bindNewViewHolder(RecyclerView.ViewHolder holder, int position);
}
