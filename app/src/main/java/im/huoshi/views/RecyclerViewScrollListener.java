package im.huoshi.views;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * http://www.jayrambhia.com/blog/footer-loader/
 * Created by Lyson on 16/4/14.
 */
public abstract class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    private int mPreviousTotal = 0;
    private boolean mLoading = true;
    private int mFirstVisibleItem;
    private int mVisibleItemCount;
    private int mTotalItemCount;

    public abstract void onLoadMore();

    public abstract void onNoMore();

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        mVisibleItemCount = recyclerView.getChildCount();
        if (manager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            mFirstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();
            mTotalItemCount = gridLayoutManager.getItemCount();
        } else if (manager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) manager;
            mFirstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
            mTotalItemCount = linearLayoutManager.getItemCount();
        }
        if (mLoading) {
            if (mTotalItemCount > mPreviousTotal) {
                mLoading = false;
                mPreviousTotal = mTotalItemCount;
            }
        }

        if (mTotalItemCount - mVisibleItemCount == 0) {
            onNoMore();
            return;
        }
        if (!mLoading && (mTotalItemCount - mVisibleItemCount <= mFirstVisibleItem + 1)) {
            onLoadMore();
            mLoading = true;
        }
    }

    public void reSetPreviousTotal() {
        mPreviousTotal = 0;
    }
}
