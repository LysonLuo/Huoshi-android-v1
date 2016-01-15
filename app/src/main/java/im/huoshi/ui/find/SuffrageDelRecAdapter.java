package im.huoshi.ui.find;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import im.huoshi.R;

/**
 * Created by Lyson on 15/12/26.
 */
public class SuffrageDelRecAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private static final int ITEM_TYPE_TOP = 1;//头部
    private static final int ITEM_TYPE_NORMAL = 2;//祝福

    public SuffrageDelRecAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_TOP) {
            View contentView = LayoutInflater.from(mContext).inflate(R.layout.widget_suffrage_details_top_item, parent, false);
            TopViewHolder topViewHolder = new TopViewHolder(contentView);
            return topViewHolder;
        } else {
            View contentView = LayoutInflater.from(mContext).inflate(R.layout.widget_suffrage_details_normal_item, parent, false);
            DetailViewHolder detailViewHolder = new DetailViewHolder(contentView);
            return detailViewHolder;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_TOP;
        } else {
            return ITEM_TYPE_NORMAL;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }


    class TopViewHolder extends RecyclerView.ViewHolder {
        public TopViewHolder(View itemView) {
            super(itemView);
        }
    }

    class DetailViewHolder extends RecyclerView.ViewHolder {
        public DetailViewHolder(View itemView) {
            super(itemView);
        }
    }
}
