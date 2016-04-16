package im.huoshi.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import im.huoshi.R;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;

/**
 * Created by Lyson on 16/4/14.
 */
public class LoadingViewHolder extends RecyclerView.ViewHolder {
    @ViewInject(R.id.progressBar)
    public ProgressBar mProgressBar;
    @ViewInject(R.id.textview_loading)
    public TextView mLoadingTextView;

    public LoadingViewHolder(View itemView) {
        super(itemView);
        ViewUtils.inject(this, itemView);
    }
}
