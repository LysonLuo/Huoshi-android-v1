package im.huoshi.ui.find;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import im.huoshi.R;
import im.huoshi.common.OnRecClickListener;

/**
 * Created by Lyson on 15/12/26.
 */
public class SuffrageRecAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private OnRecClickListener<List<String>> mRecClickListener;

    public SuffrageRecAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void  setmRecClickListener(OnRecClickListener<List<String>> recClickListener) {
        this.mRecClickListener = recClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.widget_suffrage_item, parent, false);
        IntercesViewHolder holder = new IntercesViewHolder(contentView);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRecClickListener != null) {
                    mRecClickListener.OnClick(new ArrayList<String>());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class IntercesViewHolder extends RecyclerView.ViewHolder {
        View itemView;

        public IntercesViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }
}
